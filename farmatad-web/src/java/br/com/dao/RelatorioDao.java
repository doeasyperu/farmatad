/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Cliente;
import br.entity.ItemVenda;
import br.entity.Produto;
import br.entity.Venda;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class RelatorioDao {

    private Conexao conexao = Conexao.getInstance();
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String sql;

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

    public void setConexao(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Venda> pesquisarVenda(Venda venda) {
        try {
            Cliente cliente = venda.getCliente();
            setSql("select p.idpessoa as f1, p.nome as f2, p.telefone as f3, "
                    + " p.email as f4, p.ativo as f5, pf.pessoa_idpessoa as f6, "
                    + " pf.cpf as f7, pf.rg as f8, c.id_cliente as f9, c.pontos as f10, "
                    + " c.pessoa_fisica_pessoa_idpessoa as f11, "
                    + " v.idvenda as f12, v.data as f13, v.formapagto as f14, v.desconto as f15 "
                    + " from pessoa p "
                    + " join pessoa_fisica pf "
                    + " on p.idpessoa = pf.pessoa_idpessoa join cliente c"
                    + " on pf.pessoa_idpessoa = c.pessoa_fisica_pessoa_idpessoa "
                    + " join venda v on c.id_cliente = v.cliente_id_cliente "
                    + "where ativo = 1 ");
            if (cliente.getCpf() != null && !cliente.getCpf().trim().equals("")) {
                setSql(getSql() + " and pf.cpf = '" + cliente.getCpf() + "' ");
            }
            if (venda.getData() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                setSql(getSql() + " and v.data = '" + sdf.format(venda.getData()) + "' ");
            }
            if (venda.getIdVenda() != 0) {
                setSql(getSql() + " and v.idvenda = " + venda.getIdVenda());
            }
            System.out.println("SQL\n" + getSql());
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            venda.setCliente(new Cliente());
            List<Venda> listaVenda = new ArrayList<Venda>();
            while (getResultSet().next()) {
                Cliente c = new Cliente();
                Venda v = new Venda();
                c.setIdPessoa(getResultSet().getInt("f1")); //f1
                c.setNome(getResultSet().getString("f2")); //f2
                c.setTelefone(getResultSet().getString("f3")); //f3
                c.setEmail(getResultSet().getString("f4")); //f4
                c.setAtivo(getResultSet().getBoolean("f5")); //f5
                c.setCpf(getResultSet().getString("f7")); //f7
                c.setRg(getResultSet().getString("f8")); //f8
                c.setIdCliente(getResultSet().getInt("f9"));//f9
                c.setPontos(getResultSet().getInt("f10")); //f10
                v.setIdVenda(getResultSet().getInt("f12"));
                v.setData(getResultSet().getDate("f13"));
                v.setFormPagto(getResultSet().getString("f14"));
                v.setDesconto(getResultSet().getDouble("f15"));
                v.setCliente(c);
                listaVenda.add(v);
                System.out.println("idcliente em select " + cliente.getIdCliente());
            }
            return listaVenda;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ItemVenda> listarItems(Venda venda) {
        setSql("select it.idproduto as f1, it.idvenda as f2, it.quantidade as f3, "
                + "it.valor_venda as f4, "
                + "p.idproduto as f5, p.nome as f6, p.laboratorio as f7, p.descricao as f8 "
                + "from item_venda as it "
                + "join produto p on it.idproduto = p.idproduto "
                + "where it.idvenda = " + venda.getIdVenda());
        System.out.println("SQL \n" + getSql());
        try {
            setPreparedStatement(getConexao().getConnection().prepareStatement(sql));
            setResultSet(getPreparedStatement().executeQuery(getSql()));
            List<ItemVenda> listaVenda = new ArrayList<ItemVenda>();
            while (getResultSet().next()) {
                ItemVenda iv = new ItemVenda();
                Produto p = new Produto();
                iv.setIdVenda(getResultSet().getInt("f2"));
                iv.setQuantidade(getResultSet().getInt("f3"));
                iv.setValorVenda(getResultSet().getDouble("f4"));

                p.setIdProduto(getResultSet().getInt("f1"));
                p.setNome(getResultSet().getString("f6"));
                p.setLaboratorio(getResultSet().getString("f7"));
                p.setDescricao(getResultSet().getString("f8"));
                iv.setProduto(p);
                listaVenda.add(iv);
            }
            return listaVenda;
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioDao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        return null;
    }
}
