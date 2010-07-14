package br.entity;

import java.io.Serializable;

public class Produto implements Serializable {

    private int idProduto;
    private String nome;
    private String descricao;
    private String laboratorio;
    private Integer quantidade = new Integer(0);
    private Integer quantidade2 = new Integer(0);
    private Double valorVenda = new Double(0d);

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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade2() {
        return quantidade2;
    }

    public void setQuantidade2(Integer quantidade2) {
        this.quantidade2 = quantidade2;
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

    @Override
    public String toString() {
        return getNome();
    }

}
 
