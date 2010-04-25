/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.ItemEntrada;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class EntradaDao extends Dao {

    private ItemEntrada entrada;

    public ItemEntrada getEntrada() {
        return entrada;
    }

    public void setEntrada(ItemEntrada entrada) {
        this.entrada = entrada;
    }

    @Override
    public int insert() {
        setSql("insert into entrada (fornecedor_fornecedorid, funcionario_funcionarioid, data "
                + "values(?,?, now())");
        try {
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getEntrada().getEntrada().getFuncionario().getIdFuncionario());
            getPreparedStatement().setInt(1, getEntrada().getEntrada().getFuncionario().getIdFuncionario());

        } catch (SQLException ex) {
            Logger.getLogger(EntradaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public Object select() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<?> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object find() {
        throw new UnsupportedOperationException("Not supported yet.");
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
