package br.entity;

import java.util.Date;

public class Venda {
 
	private Date data;
	 
	private String formPagto;
	 
	private double desconto;
	 
	private Cliente cliente;
	 
	private Funcionario funcionario;
	 
	private ItemVenda[] itemVenda;
	 
	public void adicionaItem(Produto produto, int qtd, String pPagto) {
	 
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
        
	 
}
 
