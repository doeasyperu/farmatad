package br.entity;

import java.io.Serializable;
import java.util.Date;

public class Entrada implements Serializable{

    private Integer idEntrada;
    private Date data;
    private Funcionario funcionario;
    private Fornecedor fornecedor;

    public void adicionaItem(Produto produto, double valor) {
       

    }

    public void removeItem(ItemEntrada itemEntrada) {
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Fornecedor getFornecedor() {
        if (fornecedor ==null){
            fornecedor = new Fornecedor();
        }
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Funcionario getFuncionario() {
        if (funcionario == null){
            funcionario = new Funcionario();
        }
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }   
}
 
