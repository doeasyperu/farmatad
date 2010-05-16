/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.controller.util.JsfUtil;
import br.com.session.FornecedorFacade;
import br.com.session.ProdutoFacade;
import br.com.session.Registros;
import br.entity.Entrada;
import br.entity.Fornecedor;
import br.entity.Funcionario;
import br.entity.ItemEntrada;
import br.entity.Produto;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author marcio
 */
@ManagedBean(name = "registrarEntrada")
@SessionScoped
public class RegistrarEntrada implements Serializable {

    /** Creates a new instance of RegistrarEntrada */
    @EJB
    private ProdutoFacade ejbProduto;
    @EJB
    private FornecedorFacade ejbFornecedor;
    @EJB
    private Registros registros;
    private Fornecedor fornecedor;
    private Produto produto;
    private Entrada entrada;
    private ItemEntrada itemEntrada;
    private boolean busca = false;
    private List<Produto> listaProdutos = null;
    private List<Fornecedor> listaFornecedores = null;

    public List<Fornecedor> getListaFornecedores() {
        if(listaFornecedores == null){
            listaFornecedores = ejbFornecedor.findAll();
        }
        return listaFornecedores;
    }

    public void setListaFornecedores(List<Fornecedor> listaFornecedores) {
        this.listaFornecedores = listaFornecedores;
    }

    public List<Produto> getListaProdutos() {
        if(listaProdutos == null){
            listaProdutos = ejbProduto.findAll();
        }
        return listaProdutos;
    }

    public void setListaProdutos(List<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
    

    public boolean isBusca() {
        return busca;
    }

    public void setBusca(boolean busca) {
        this.busca = busca;
    }   

    public Entrada getEntrada() {
        if (entrada == null) {
            entrada = new Entrada();
        }
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public ItemEntrada getItemEntrada() {
        if (itemEntrada == null) {
            itemEntrada = new ItemEntrada();
        }
        return itemEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemEntrada = itemEntrada;
    }

    public Fornecedor getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new Fornecedor();
            fornecedor.setIdPessoa(-1);
        }
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        System.out.println("fornecedor set * " + fornecedor);
        this.fornecedor = fornecedor;
    }

    public Produto getProduto() {
        if (produto == null) {
            produto = new Produto();
            produto.setIdProduto(-1);
        }
        return produto;
    }

    public void setProduto(Produto produto) {
        System.out.println("produto set  * " + produto);
        this.produto = produto;
    }

   

    public String registrar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession h = (HttpSession) fc.getExternalContext().getSession(true);
        Funcionario f = new Funcionario();
        f = (Funcionario) h.getAttribute("funcionario");
        System.out.println("produto " + produto.getIdProduto());
        System.out.println("fornecedor - " + fornecedor.getIdPessoa());
        if (produto.getIdProduto() == -1) {
            JsfUtil.addErrorMessage("Escolha o produto");
        } else if (fornecedor.getIdPessoa() == -1) {
            JsfUtil.addErrorMessage("Escolha o fornecedor");
        } else {
            entrada = new Entrada();
            entrada.setFornecedor(fornecedor);
            entrada.setFuncionario(f);
            itemEntrada.setEntrada(entrada);
            itemEntrada.setProduto(produto);
            registros.setItemEntrada(itemEntrada);
            registros.registrarEntrada();
            produto = new Produto(-1);
            fornecedor = new Fornecedor(-1);
            itemEntrada = new ItemEntrada();
            JsfUtil.addSuccessMessage("Entrada registrada com sucesso");
        }
        return null;
    }

    public String acao() {
        return null;
    }

    public String pesquisarProduto() {
        listaProdutos = null;
        listaProdutos = ejbProduto.findByNome(produto);
        System.out.println("Chamada a pesquisa");
        return null;
    }
    public String pesquisarFornecedor(){
        listaFornecedores = null;
        listaFornecedores = ejbFornecedor.findByNome(fornecedor);
        return null;
    }
}
