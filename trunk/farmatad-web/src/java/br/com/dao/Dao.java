/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author marcio
 */
public abstract class Dao {

    private String sql;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    
    private Conexao conexao = Conexao.getInstance();

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Conexao getConexao() {
        return conexao;
    }
    

    public abstract int insert();
    public abstract Object select();
    public abstract int delete();
    public abstract int update();
    public abstract List<?> findAll();
    public abstract List<?> findRange(int[] range);
    public abstract int count();
    public abstract Object find();

}
