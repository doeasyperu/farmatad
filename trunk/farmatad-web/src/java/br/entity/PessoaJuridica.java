package br.entity;

import javax.persistence.Entity;


public class PessoaJuridica extends Pessoa {

    private String cnpj;
    private String razaoSocial;

    public PessoaJuridica() {
        
    }
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}
 
