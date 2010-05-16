/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.ItemEntrada;
import br.entity.ItemVenda;
import br.entity.Produto;
import br.entity.Venda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author marcio
 */
@Stateless
public class RegistroDao {

    private Conexao conexao = Conexao.getInstance();
    private ItemEntrada itemEntrada;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;
    private ProdutoDao produtoDao;

    private ProdutoDao getProdutoDao() {
        if (produtoDao == null) {
            produtoDao = new ProdutoDao();
        }
        return produtoDao;
    }

    private Conexao getConexao() {
        return conexao;
    }

    public void registrarEntrada() {
        try {
            setSql("insert into entrada (data, fornecedor_idfornecedor, funcionario_idfuncionario) "
                    + "values (now(),?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, itemEntrada.getEntrada().getFornecedor().getIdFornecedor());
            getPreparedStatement().setInt(2, itemEntrada.getEntrada().getFuncionario().getIdFuncionario());
            System.out.println("sql " + getPreparedStatement());
            getPreparedStatement().executeUpdate();
            setSql("select max(identrada) from entrada");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            getResultSet().next();
            itemEntrada.getEntrada().setIdEntrada(getResultSet().getInt(1));
            setSql("insert into item_Entrada (entrada_identrada, produto_idproduto, quantidade, valor_compra) "
                    + "values(?,?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, itemEntrada.getEntrada().getIdEntrada());
            getPreparedStatement().setInt(2, itemEntrada.getProduto().getIdProduto());
            getPreparedStatement().setInt(3, itemEntrada.getQuantidade());
            getPreparedStatement().setDouble(4, itemEntrada.getValorCompra());
            System.out.println("sql " + getPreparedStatement());
            getPreparedStatement().executeUpdate();

            setSql("update produto set valor_venda = ?, quantidade = ? where idproduto = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setDouble(1, getItemEntrada().getProduto().getValorVenda());
            System.out.println("intens entrando " + getItemEntrada().getQuantidade());
            System.out.println("itens agora " + getItemEntrada().getProduto().getQuantidade());
            getPreparedStatement().setInt(2, (getItemEntrada().getProduto().getQuantidade() + getItemEntrada().getQuantidade()));
            getPreparedStatement().setInt(3, getItemEntrada().getProduto().getIdProduto());
            System.out.println("update produto " + getPreparedStatement());
            getPreparedStatement().executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(RegistroDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int registrarVenda(Venda venda) {
        try {
//            for (ItemVenda iv : venda.getListaItensVenda()) {
//                setSql("insert into item_venda");
//            }
            setSql("insert into venda (data, formaPagto, desconto, Cliente_id_cliente, Funcionario_id_funcionario) "
                    + "values (now(),?,?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, venda.getFormPagto());
            getPreparedStatement().setDouble(2, venda.getDesconto());
            getPreparedStatement().setInt(3, venda.getCliente().getIdCliente());
            getPreparedStatement().setInt(4, venda.getFuncionario().getIdFuncionario());
            getPreparedStatement().executeUpdate();

            setSql("select max(idvenda) from venda");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            while (getResultSet().next()) {
                venda.setIdVenda(getResultSet().getInt(1));
            }
            for (ItemVenda iv : venda.getListaItensVenda()) {
                setSql("insert into item_venda (idvenda, idproduto, quantidade, valor_venda) "
                        + "values(?,?,?,?)");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, venda.getIdVenda());
                getPreparedStatement().setInt(2, iv.getProduto().getIdProduto());
                getPreparedStatement().setInt(3, iv.getProduto().getQuantidade());
                getPreparedStatement().setDouble(4, iv.getProduto().getValorVenda());
                getPreparedStatement().executeUpdate();
                Produto p = new Produto(iv.getProduto().getIdProduto());
                getProdutoDao().setProduto(p);
                p = getProdutoDao().select();
                p.setQuantidade(p.getQuantidade() - iv.getProduto().getQuantidade());
                getProdutoDao().setProduto(p);
                getProdutoDao().update();
            }

        } catch (SQLException ex) {
            Logger.getLogger(RegistroDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return -3;
    }

    public ItemEntrada getItemEntrada() {
        return itemEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemEntrada = itemEntrada;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
