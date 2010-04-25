package br.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Produto implements Serializable {

    @Id
    private int idProduto;
    private String nome;
    private String descricao;
    private String laboratorio;
    private int quantidade;
    private double valorVenda;

    public Produto(int idProduto) {
        this.idProduto = idProduto;
    }

    public Produto() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        if (this.idProduto != other.idProduto) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if (this.quantidade != other.quantidade) {
            return false;
        }
        if (this.valorVenda != other.valorVenda) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.idProduto;
        hash = 23 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 23 * hash + this.quantidade;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.valorVenda) ^ (Double.doubleToLongBits(this.valorVenda) >>> 32));
        return hash;
    }
}
 
