/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Fornecedor;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolationException;

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
        } catch (MySQLIntegrityConstraintViolationException mix) {
            System.out.println("codigo do erro do mysql " + mix.getErrorCode());
            try {

                setSql("delete from pessoa where idpessoa = ?");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
                getPreparedStatement().executeUpdate();
                return -1;
            } catch (SQLException ex) {
                Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
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
        } catch (MySQLIntegrityConstraintViolationException mix) {
            System.out.println(mix.getCause());
            setSql("update pessoa set ativo = 0 where idpessoa = ?");
            try {
                setPreparedStatement(Conexao.getInstance().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
                getPreparedStatement().executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exceção ao alterar no mysql ");
            }
            return -1;
        } catch (ConstraintViolationException cv) {
            System.out.println(cv.getCause());
            setSql("update pessoa set ativo = 0 where idpessoa = ?");
            try {
                setPreparedStatement(Conexao.getInstance().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getFornecedor().getIdPessoa());
                getPreparedStatement().executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exceção ao alterar");
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedorDao.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Constraint ? " + ex.getCause());
            return -2;
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
            getPreparedStatement().setString(1, getFornecedor().getCnpj());
            getPreparedStatement().setString(2, getFornecedor().getRazaoSocial());
            getPreparedStatement().setInt(3, getFornecedor().getIdPessoa());
            Logger.getLogger(FornecedorDao.class.getName()).info(getPreparedStatement().toString());
            getPreparedStatement().executeUpdate();

            setSql("update fornecedor set contato = ?, pessoa_juridica_pessoa_idpessoa = ? "
                    + "where idFornecedor = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFornecedor().getContato());
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

    public List<Fornecedor> selectByNome() {
        try {
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            setSql("select p.idpessoa f1, p.nome f2, p.telefone f3, p.email f4, "
                    + "pj.cnpj f5, pj.razaoSocial f6, f.contato f7, f.idFornecedor f8 "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "where p.ativo = 1 and (lower(p.nome) like ? or lower(p.nome) like ?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, "%" + getFornecedor().getNome().toLowerCase() + "%");
            getPreparedStatement().setString(2, "%" + getFornecedor().getNome().toUpperCase() + "%");
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Fornecedor f = new Fornecedor();
                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2")); //f2
                    f.setTelefone(getResultSet().getString("f3")); //f3
                    f.setEmail(getResultSet().getString("f4")); //f4

                    f.setCnpj(getResultSet().getString("f5"));//f5
                    f.setRazaoSocial(getResultSet().getString("f6")); //f6
                    f.setContato(getResultSet().getString("f7")); //f7
                    f.setIdFornecedor(getResultSet().getInt("f8")); //f8
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
    public Fornecedor select() {
        try {
            setSql("select p.idpessoa f1, p.nome f2, p.telefone f3, p.email f4, "
                    + "pj.cnpj f5, pj.razaoSocial f6, f.contato f7, f.idFornecedor f8"
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "where f.idfornecedor = ? and p.ativo = 1");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFornecedor().getIdFornecedor());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    getFornecedor().setIdPessoa(getResultSet().getInt("f1"));// f1
                    getFornecedor().setNome(getResultSet().getString("f2")); //f2
                    getFornecedor().setTelefone(getResultSet().getString("f3")); //f3
                    getFornecedor().setEmail(getResultSet().getString("f4")); //f4

                    getFornecedor().setCnpj(getResultSet().getString("f5")); //f5
                    getFornecedor().setRazaoSocial(getResultSet().getString("f6")); //f6
                    getFornecedor().setContato(getResultSet().getString("f7")); //f7
                    getFornecedor().setIdFornecedor(getResultSet().getInt("f8")); //f8

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
    public List<Fornecedor> findAll() {
        try {
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            setSql("select p.idpessoa f1, p.nome f2, p.telefone f3, p.email f4, "
                    + "pj.cnpj f5, pj.razaoSocial f6, f.contato f7, f.idFornecedor f8 "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "where p.ativo = 1");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Fornecedor f = new Fornecedor();
                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2")); //f2
                    f.setTelefone(getResultSet().getString("f3")); //f3
                    f.setEmail(getResultSet().getString("f4")); //f4

                    f.setCnpj(getResultSet().getString("f5"));//f5
                    f.setRazaoSocial(getResultSet().getString("f6")); //f6
                    f.setContato(getResultSet().getString("f7")); //f7
                    f.setIdFornecedor(getResultSet().getInt("f8")); //f8
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
            setSql("select p.idpessoa f1, p.nome f2, p.telefone f3, p.email f4, "
                    + "pj.cnpj f5, pj.razaoSocial f6, f.contato f7, f.idFornecedor f8 "
                    + "from pessoa p join pessoa_juridica pj on "
                    + "p.idpessoa = pj.pessoa_idpessoa join fornecedor f on "
                    + "f.pessoa_juridica_pessoa_idpessoa = pj.pessoa_idpessoa "
                    + "where p.ativo = 1 "
                    + "limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            Logger.getLogger(FornecedorDao.class.getName()).info(getPreparedStatement().toString());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Fornecedor f = new Fornecedor();
                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2"));//f2
                    f.setTelefone(getResultSet().getString("f3"));//f3
                    f.setEmail(getResultSet().getString("f4"));//f4

                    f.setCnpj(getResultSet().getString("f5")); //f5
                    f.setRazaoSocial(getResultSet().getString("f6"));//f6
                    f.setContato(getResultSet().getString("f7"));//f7
                    f.setIdFornecedor(getResultSet().getInt("f8"));//f8
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
            setSql("select count(*) from fornecedor f join pessoa_juridica pj on "
                    + "pj.pessoa_idpessoa = f.pessoa_juridica_pessoa_idpessoa join pessoa p"
                    + " on p.idpessoa = pj.pessoa_idpessoa where ativo = 1");
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
