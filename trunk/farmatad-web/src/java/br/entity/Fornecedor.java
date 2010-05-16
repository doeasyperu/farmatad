package br.entity;


public class Fornecedor extends PessoaJuridica {

    private int idFornecedor;
    private String contato;

    public Fornecedor(int idPessoa) {
        this.setIdPessoa(idPessoa);
    }

    public Fornecedor() {
        super();
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fornecedor other = (Fornecedor) obj;
        if (this.idFornecedor != other.idFornecedor) {
            return false;
        }
        if (this.getIdPessoa() != other.getIdPessoa()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.idFornecedor + this.getIdPessoa();
        return hash;
    }

    public String toString() {
        return getNome();
    }

}
 
