package br.entity;

import java.io.Serializable;

public class PessoaFisica extends Pessoa implements Serializable{
 
	private String rg;
	 
	private String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
	 
}
 
