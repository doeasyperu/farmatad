/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Pessoa;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class PessoaDao extends Dao {

    private Pessoa pessoa;

    @Override
    public int insert() {
        try {
            setSql("insert into Pessoa(nome, telefone, email) values"
                    + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getPessoa().getNome());
            getPreparedStatement().setString(2, getPessoa().getTelefone());
            getPreparedStatement().setString(3, getPessoa().getEmail());
            return getPreparedStatement().executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public Object select() {
        return 0;
    }

    @Override
    public int delete() {
        try {
            setSql("delete from pessoa where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getPessoa().getIdPessoa());
            getPreparedStatement().executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int update() {
        try {
            setSql("update pessoa set nome = ?, email = ?, telefone = ? where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getPessoa().getNome());
            getPreparedStatement().setString(2, getPessoa().getEmail());
            getPreparedStatement().setString(3, getPessoa().getTelefone());
            getPreparedStatement().setInt(4, getPessoa().getIdPessoa());
            return getPreparedStatement().executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<?> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object find() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public List<?> findRange(int[] range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int count() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
