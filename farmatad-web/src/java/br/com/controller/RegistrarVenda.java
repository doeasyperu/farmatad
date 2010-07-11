/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.dao.RegistroDao;
import br.com.session.ClienteFacade;
import br.com.session.ProdutoFacade;
import br.entity.Cliente;
import br.entity.Funcionario;
import br.entity.ItemVenda;
import br.entity.Produto;
import br.entity.Venda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author marcio
 */
@ManagedBean(name = "registrarVenda")
@SessionScoped
public class RegistrarVenda implements Serializable {

    private List<Produto> listaProdutos = null;
    private List<Cliente> listaCliente = null;
    private List<Produto> listaCompras = new ArrayList<Produto>();
    @EJB
    private ClienteFacade ejbCliente;
    @EJB
    private ProdutoFacade ejbProduto;
    @EJB
    private RegistroDao registroDao;
    private Produto produto;
    private Cliente cliente;
    private Venda venda = null;
    private boolean pesquisaCliente = false;
    private boolean pesquisaProduto = false;
    private double total = 0d;

    private RegistroDao getRegistroDao() {
        return registroDao;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Venda getVenda() {
        if (venda == null) {
            venda = new Venda();
        }
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public boolean isPesquisaCliente() {
        return pesquisaCliente;
    }

    public void setPesquisaCliente(boolean pesquisaCliente) {
        this.pesquisaCliente = pesquisaCliente;
    }

    public boolean isPesquisaProduto() {
        return pesquisaProduto;
    }

    public void setPesquisaProduto(boolean pesquisaProduto) {
        this.pesquisaProduto = pesquisaProduto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Produto getProduto() {
        if (produto == null) {
            produto = new Produto(-1);
        }
        return produto;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setIdCliente(-1);
        }
        return cliente;
    }

    private ClienteFacade getEjbCliente() {
        return ejbCliente;
    }

    private ProdutoFacade getEjbProduto() {
        return ejbProduto;
    }

    public List<Cliente> getListaCliente() {
        if (listaCliente == null) {
            if (pesquisaCliente) {
                listaCliente = getEjbCliente().findByNome(cliente);
            } else {
                listaCliente = getEjbCliente().findAll();
            }
        }
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public List<Produto> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Produto> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public List<Produto> getListaProdutos() {
        if (listaProdutos == null) {
            if (pesquisaProduto) {
                listaProdutos = getEjbProduto().findByNome(produto);
            } else {
                listaProdutos = getEjbProduto().findAll();
            }
        }
        return listaProdutos;
    }

    public void setListaProdutos(List<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public String adicionarProduto() {
        System.out.println("quantidade " + produto.getQuantidade());
        Produto p1 = getEjbProduto().find(produto.getIdProduto());
        FacesContext fc = FacesContext.getCurrentInstance();
        String sumario = "Quantidade insuficiente em estoque";
        String mensagem = "Quantidade insuficiente em estoque";
        if (p1.getQuantidade() == 0) {
            mensagem = "Não tem o produto em estoque";

        } else {
            mensagem = "Só tem " + p1.getQuantidade() + " em estoque.";
        }
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, sumario, mensagem);
        if (p1.getQuantidade() < produto.getQuantidade() || p1.getQuantidade() == 0) {
            fc.addMessage("formVendas:produtosel", fm);
            return null;
        } else if (p1.getQuantidade() >= produto.getQuantidade() && p1.getQuantidade() > 0) {
            for (int index = 0; index < listaCompras.size(); index++) {
                Produto p = new Produto();
                p = listaCompras.get(index);
                if (p.getIdProduto() == produto.getIdProduto()) {
                    if (p1.getQuantidade() < (p.getQuantidade() + produto.getQuantidade())) {
                        fc.addMessage("formVendas:produtosel", fm);
                    } else if (p1.getQuantidade() >= (p.getQuantidade() + produto.getQuantidade())) {
                        produto.setQuantidade(p.getQuantidade() + produto.getQuantidade());
                        total -= p.getQuantidade() * p.getValorVenda();

                        listaCompras.set(index, produto);
                        total += produto.getQuantidade() * produto.getValorVenda();
                        produto = new Produto(-1);
                        listaProdutos = null;
                    }
                    return null;
                }
            }
            listaCompras.add(produto);
            total += produto.getValorVenda() * produto.getQuantidade();
            produto = null;
            listaProdutos = null;

        }
        return null;
    }

    public String removerProduto() {
        listaCompras.remove(produto);
        total = total - (produto.getValorVenda() * produto.getQuantidade());
        produto = new Produto(-1);
        return null;
    }

    public String pesquisarProdutos() {
        listaProdutos = null;
        listaProdutos = getEjbProduto().findByNome(produto);
        produto = new Produto(-1);
        return null;
    }

    public String pesquisaClientes() {
        listaCliente = null;
        listaCliente = getEjbCliente().findByNome(cliente);
        cliente = new Cliente();
        return null;
    }

    public String cadastrarCliente() {
        getEjbCliente().create(cliente);
        listaCliente = null;
        return null;
    }

    public String registrarVenda() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession hs = (HttpSession) fc.getExternalContext().getSession(true);
        final Funcionario f = (Funcionario) hs.getAttribute("funcionario");
        if (cliente.getIdCliente() == -1) {
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Escolha cliente", "Escolha cliente");
            fc.addMessage("formVendas:clientessel", m);
            return null;
        } else if (listaCompras.size() <= 0) {
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Escolha produto", "Escolha produto");
            fc.addMessage("formVendas:clientessel", m);
            return null;
        } else {
            Double pontos = Math.ceil(total/ 10);
            List<ItemVenda> listaItens = new ArrayList<ItemVenda>();
            for (Produto p : listaCompras) {
                ItemVenda iv = new ItemVenda();
                iv.setProduto(p);
                listaItens.add(iv);
            }
            venda.setListaItensVenda(listaItens);
            venda.setFuncionario(f);
            if (cliente.getPontos() > 10) {
                venda.setDesconto(0.10);
                cliente.setPontos( cliente.getPontos() -10);
            }
            cliente.setPontos(cliente.getPontos() + pontos.intValue());
            venda.setCliente(cliente);
            getRegistroDao().registrarVenda(venda);

            produto = null;
            cliente = null;
            listaItens = null;
            listaCliente = null;
            listaCompras = new ArrayList<Produto>();
            listaProdutos = null;
            venda = null;
            total = 0d;
            venda = new Venda();
        }
        return null;
    }
}
