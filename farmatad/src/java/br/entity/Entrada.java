package br.entity;

import java.util.Date;
import java.util.List;

public class Entrada {
 
	private Date data;
	 
	private Funcionario funcionario;
	 
	private Fornecedor fonecedor;
	 
	private ItemEntrada itemsEntrada;
	 
	public void adicionaItem(Produto produto, double valor) {
            itemsEntrada = new ItemEntrada();
            itemsEntrada.setProduto(produto);
            itemsEntrada.setEntrada(this);
            itemsEntrada.setValorCompra(valor);
            
	}
	 
	public void removeItem(ItemEntrada itemEntrada) {
	 
	}

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Fornecedor getFonecedor() {
        return fonecedor;
    }

    public void setFonecedor(Fornecedor fonecedor) {
        this.fonecedor = fonecedor;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public ItemEntrada getItemsEntrada() {
        return itemsEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemsEntrada = itemEntrada;
    }
	 
}
 
