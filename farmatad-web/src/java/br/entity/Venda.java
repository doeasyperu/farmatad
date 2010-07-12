package br.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venda implements Serializable {

    private int idVenda;
    private Date data;
    private String formPagto;
    private double desconto;
    private Cliente cliente;
    private Funcionario funcionario;
    private List<ItemVenda> listaItensVenda = null;
    private Double subtotal;

    public void adicionaItem(Produto produto, int qtd, String pPagto) {
        ItemVenda iv = new ItemVenda();
        iv.setProduto(produto);
        iv.setQuantidade(qtd);
    }

    public void removeItem(ItemVenda pItem) {
    }

    public void calculaDescont(int pPntos) {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public String getFormPagto() {
        return formPagto;
    }

    public void setFormPagto(String formPagto) {
        this.formPagto = formPagto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<ItemVenda> getListaItensVenda() {
        if (listaItensVenda == null) {
            listaItensVenda = new ArrayList<ItemVenda>();
        }
        return listaItensVenda;
    }

    public void setListaItensVenda(List<ItemVenda> listaItensVenda) {
        this.listaItensVenda = listaItensVenda;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public double getSubTotal() {
        if (subtotal == null) {
            double total = 0d;
            for (ItemVenda itemVenda : listaItensVenda) {
                total += itemVenda.getValorVenda()*itemVenda.getQuantidade();
            }
            subtotal = total;
        }
        return subtotal;
    }

    public double getTotal() {
        return getSubTotal() - getDesconto();
    }
}
 
