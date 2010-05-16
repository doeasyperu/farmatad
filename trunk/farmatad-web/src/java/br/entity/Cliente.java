package br.entity;

import java.io.Serializable;

public class Cliente extends PessoaFisica implements Serializable{

    private int idCliente = (-1);
    private int pontos;

    public void atualizaPontos(int pontos) {
        this.pontos = pontos;
    }

    public void zeraPontos() {
        pontos = 0;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (this.idCliente != other.idCliente) {
            return false;
        }
        if (this.getIdPessoa() != other.getIdPessoa()) {
            return false;
        }
        if (this.pontos != other.pontos) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.idCliente + getIdPessoa();
        hash = 59 * hash + this.pontos + getIdPessoa();
        return hash;
    }

    
    
}
 
