package br.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class Entrada implements Serializable{

    @Id
    private int idEntrada;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;
    private Funcionario funcionario;
    private Fornecedor fonecedor;
    private ItemEntrada itemEntrada;

    public void adicionaItem(Produto produto, double valor) {
        itemEntrada = new ItemEntrada();
        itemEntrada.setProduto(produto);
        itemEntrada.setEntrada(this);
        itemEntrada.setValorCompra(valor);

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
        return itemEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemEntrada = itemEntrada;
    }
}
 
