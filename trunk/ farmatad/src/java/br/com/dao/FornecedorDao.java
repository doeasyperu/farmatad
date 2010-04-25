/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Fornecedor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class FornecedorDao extends Dao {

    private Fornecedor fornecedor;
    private PessoaDao pessoaDao;

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public FornecedorDao() {
        try {
            getConexao().conectar();
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int insert() {
        try {
            setSql("insert into Pessoa(nome, telefone, email) values"
                    + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getNome());
            getPreparedStatement().setString(2, getFornecedor().getTelefone());
            getPreparedStatement().setString(3, getFornecedor().getEmail());
            getPreparedStatement().executeUpdate();

            /*
             * o select max serve para pegar a ultima tupla inserida
             * poderia usar o método do jdbc, mas se no futuro houver necessidade
             * de alterar o banco, poderá haver problemas, principalmente com oracle e
             * postgres que o método é diferente, como ambos suportam o select max
             * é mais conveniente o uso dessa query para evitar incompatibilidade e
             * problemas futuros
             */
            setSql("select max(idpessoa) as last from pessoa");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            while (getResultSet().next()) {
                getFornecedor().setIdPessoa(getResultSet().getInt("last"));
            }

            setSql("insert into Pessoa_Juridica(pessoa_idpessoa, cnpj, razaoSocial) values"
                    + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
            getPreparedStatement().setString(2, getFornecedor().getCnpj());
            getPreparedStatement().setString(3, getFornecedor().getRazaoSocial());
            getPreparedStatement().executeUpdate();

            setSql("insert into Fornecedor (contato, Pessoa_Juridica_Pessoa_idPessoa)"
                    + " values(?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getContato());
            getPreparedStatement().setInt(2, getFornecedor().getIdPessoa());
            getPreparedStatement().executeUpdate();


            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public Fornecedor select() {
        try {
            setSql("select p.idpessoa, p.nome, p.telefone, p.email, "
                    + "pj.cnpj, pj.razaoSocial, f.contato, f.idFornecedor "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "where idfornecedor = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdFornecedor());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    getFornecedor().setIdPessoa(getResultSet().getInt(1));
                    getFornecedor().setNome(getResultSet().getString(2));
                    getFornecedor().setTelefone(getResultSet().getString(3));
                    getFornecedor().setEmail(getResultSet().getString(4));

                    getFornecedor().setCnpj(getResultSet().getString(5));
                    getFornecedor().setRazaoSocial(getResultSet().getString(6));
                    getFornecedor().setContato(getResultSet().getString(7));
                    getFornecedor().setIdFornecedor(getResultSet().getInt(8));

                }
                return getFornecedor();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int delete() {
        try {
            setSql("delete from fornecedor where idfornecedor = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdFornecedor());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa_juridica where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
            getPreparedStatement().executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int update() {
        try {
            setSql("update pessoa set nome = ?, email = ?, telefone = ? where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getNome());
            getPreparedStatement().setString(2, getFornecedor().getEmail());
            getPreparedStatement().setString(3, getFornecedor().getTelefone());
            getPreparedStatement().setInt(4, getFornecedor().getIdPessoa());
            Logger.getLogger(FornecedorDao.class.getName()).info(getPreparedStatement().toString());
            getPreparedStatement().executeUpdate();

            setSql("update pessoa_juridica set cnpj = ?, razaoSocial = ? where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getNome());
            getPreparedStatement().setString(2, getFornecedor().getEmail());
            getPreparedStatement().setInt(3, getFornecedor().getIdPessoa());
            Logger.getLogger(FornecedorDao.class.getName()).info(getPreparedStatement().toString());
            getPreparedStatement().executeUpdate();

            setSql("update fornecedor set contato = ?, pessoa_juridica_pessoa_idpessoa = ? "
                    + "where idFornecedor = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getNome());
            getPreparedStatement().setInt(2, getFornecedor().getIdPessoa());
            getPreparedStatement().setInt(3, getFornecedor().getIdFornecedor());
            Logger.getLogger(FornecedorDao.class.getName()).info(getPreparedStatement().toString());
            getPreparedStatement().executeUpdate();

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }


    }

    @Override
    public List<Fornecedor> findAll() {
        try {
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            setSql("select p.idpessoa, p.nome, p.telefone, p.email, "
                    + "pj.cnpj, pj.razaoSocial, f.contato, f.idFornecedor "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa ");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Fornecedor f = new Fornecedor();
                    f.setIdPessoa(getResultSet().getInt(1));
                    f.setNome(getResultSet().getString(2));
                    f.setTelefone(getResultSet().getString(3));
                    f.setEmail(getResultSet().getString(4));

                    f.setCnpj(getResultSet().getString(5));
                    f.setRazaoSocial(getResultSet().getString(6));
                    f.setContato(getResultSet().getString(7));
                    f.setIdFornecedor(getResultSet().getInt(8));
                    lista.add(f);

                }
                return lista;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object find() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Fornecedor> findRange(int[] range) {
        try {
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            setSql("select p.idpessoa, p.nome, p.email, p.telefone, "
                    + "pj.cnpj, pj.razaoSocial, f.contato, f.idFornecedor "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Fornecedor f = new Fornecedor();
                    f.setIdPessoa(getResultSet().getInt(1));
                    f.setNome(getResultSet().getString(2));
                    f.setTelefone(getResultSet().getString(3));
                    f.setEmail(getResultSet().getString(4));

                    f.setCnpj(getResultSet().getString(5));
                    f.setRazaoSocial(getResultSet().getString(6));
                    f.setContato(getResultSet().getString(7));
                    f.setIdFornecedor(getResultSet().getInt(8));
                    lista.add(f);

                }
                return lista;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int count() {
        try {
            setSql("select count (*) total from fornecedor");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            getResultSet().next();
            return getResultSet().getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
