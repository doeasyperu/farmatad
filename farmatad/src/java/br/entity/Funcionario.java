package br.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;

@Entity
public class Funcionario extends PessoaFisica implements Serializable{

    private String endereco;
    private String senha;
    private boolean adminstrador;
    private List<Venda> venda;
    private List<Entrada> entrada;
    private int idFuncionario;

    public Funcionario() {
    }

    public Funcionario(int idPessoa) {
        setIdPessoa(idPessoa);
    }
    
    public boolean validaFuncionario(String email, String senha) {
        return false;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    

    public boolean isAdminstrador() {
        return adminstrador;
    }

    public void setAdminstrador(boolean adminstrador) {
        this.adminstrador = adminstrador;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Entrada> getEntrada() {
        return entrada;
    }

    public void setEntrada(List<Entrada> entrada) {
        this.entrada = entrada;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Venda> getVenda() {
        return venda;
    }

    public void setVenda(List<Venda> venda) {
        this.venda = venda;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Funcionario other = (Funcionario) obj;
        if ((this.senha == null) ? (other.senha != null) : !this.senha.equals(other.senha)) {
            return false;
        }
        if (this.idFuncionario != other.idFuncionario) {
            return false;
        }
        if (this.getIdPessoa() != other.getIdPessoa()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.senha != null ? this.senha.hashCode() : 0);
        hash = 79 * hash + this.idFuncionario + getIdPessoa();
        return hash;
    }


}
 
