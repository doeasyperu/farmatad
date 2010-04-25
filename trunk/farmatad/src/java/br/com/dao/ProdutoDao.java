/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Produto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class ProdutoDao extends Dao {

    private Produto produto;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public ProdutoDao() {
        try {
            getConexao().conectar();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ProdutoDao(Produto produto) {
        this();
        this.produto = produto;
    }

    @Override
    public int insert() {
        try {
            setSql("insert into produto (nome, descricao, laboratorio, quantidade, valor_venda) "
                    + "values(?,?,?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setString(1, getProduto().getNome());
            getPreparedStatement().setString(2, getProduto().getDescricao());
            getPreparedStatement().setString(3, getProduto().getLaboratorio());
            getPreparedStatement().setInt(4, getProduto().getQuantidade());
            getPreparedStatement().setDouble(5, getProduto().getValorVenda());
            getPreparedStatement().executeUpdate();



            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public Produto select() {
        try {
            setSql("select p.idproduto, p.nome, p.descricao, p.laboratorio, p.quantidade, "
                    + "p.valor_venda from produto p where idproduto = ?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getProduto().getIdProduto());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    if (getProduto() == null) {
                        setProduto(new Produto());
                    }
                    getProduto().setIdProduto(getResultSet().getInt(1));
                    getProduto().setNome(getResultSet().getString(2));
                    getProduto().setDescricao(getResultSet().getString(3));
                    getProduto().setLaboratorio(getResultSet().getString(4));
                    getProduto().setQuantidade(getResultSet().getInt(5));
                }
            }

            return getProduto();
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public int delete() {
        try {
            setSql("delete from produto where idproduto = ?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getProduto().getIdProduto());
            getPreparedStatement().executeUpdate();

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int update() {
        try {
            setSql("update produto set nome = ?, descricao = ?, laboratorio = ?,"
                    + " quantidade = ?, valor_venda = ? where idproduto = ?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getProduto().getIdProduto());
            getPreparedStatement().setString(1, produto.getNome());
            getPreparedStatement().setString(2, produto.getDescricao());
            getPreparedStatement().setString(3, produto.getLaboratorio());
            getPreparedStatement().setInt(4, produto.getQuantidade());
            getPreparedStatement().setDouble(5, produto.getValorVenda());
            getPreparedStatement().setInt(6, produto.getIdProduto());
            getPreparedStatement().executeUpdate();

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<Produto> findAll() {
        List<Produto> lista = new ArrayList<Produto>();
        try {
            setSql("select p.idproduto, p.nome, p.descricao, p.laboratorio, p.quantidade, "
                    + "p.valor_venda from produto p where idproduto = ?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getProduto().getIdProduto());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Produto p = new Produto();
                    p.setIdProduto(getResultSet().getInt(1));
                    p.setNome(getResultSet().getString(2));
                    p.setDescricao(getResultSet().getString(3));
                    p.setLaboratorio(getResultSet().getString(4));
                    p.setQuantidade(getResultSet().getInt(5));
                    lista.add(p);
                }
            }

            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
