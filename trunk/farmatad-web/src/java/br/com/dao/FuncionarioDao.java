/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Funcionario;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class FuncionarioDao extends Dao {

    private Funcionario funcionario;

    public FuncionarioDao() {
        try {
            getConexao().conectar();
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FuncionarioDao(Funcionario funcionario) {
        this();
        setFuncionario(funcionario);
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public int insert() {
        try {

            setSql("select p.idpessoa, p.email, f.senha from pessoa p join funcionario f "
                    + "on f.pessoa_fisica_pessoa_idpessoa = p.idpessoa where "
                    + "p.email = ? and f.senha = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getNome());
            getPreparedStatement().setString(2, getFuncionario().getSenha());
            System.out.println("sql insert funcionario "+getPreparedStatement());
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet().next()) {
                int id = 0;
                id = getResultSet().getInt("p.idpessoa");
                System.out.println("d " + id);
                if (id != 0) {
                    return -2;
                }
            }
            setSql("insert into Pessoa(nome, telefone, email) values"
                    + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getNome());
            getPreparedStatement().setString(2, getFuncionario().getTelefone());
            getPreparedStatement().setString(3, getFuncionario().getEmail());
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
                getFuncionario().setIdPessoa(getResultSet().getInt("last"));
            }
            setSql("insert into Pessoa_Fisica(pessoa_idpessoa, cpf, rg) values"
                    + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            getPreparedStatement().setString(2, getFuncionario().getCpf());
            getPreparedStatement().setString(3, getFuncionario().getRg());
            getPreparedStatement().executeUpdate();

            setSql("insert into funcionario (endereco, senha, administrador, "
                    + "Pessoa_Fisica_Pessoa_idPessoa) values (?,?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getEndereco());
            getPreparedStatement().setString(2, getFuncionario().getSenha());
            getPreparedStatement().setBoolean(3, getFuncionario().isAdminstrador());
            getPreparedStatement().setInt(4, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();
            return 1;
        } catch (MySQLIntegrityConstraintViolationException mix) {
            try {
                setSql("delete from pessoa where idpessoa = ?");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
                getPreparedStatement().executeUpdate();
                return -3;
            } catch (SQLException ex) {
                Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                return -4;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            return -4;
        }
    }

    @Override
    public int delete() {
        try {
            setSql("delete from funcionario where pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa_fisica where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            return 1;
        } catch (MySQLIntegrityConstraintViolationException mix) {
            try {
                setSql("update pessoa set ativo = 0 where idpessoa = ?");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
                getPreparedStatement().executeUpdate();
                return -1;
            } catch (SQLException ex) {
                Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
    }

    @Override
    public int update() {
        try {
            setSql("update pessoa set nome = ?, email = ?, telefone = ? where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getNome());
            getPreparedStatement().setString(2, getFuncionario().getEmail());
            getPreparedStatement().setString(3, getFuncionario().getTelefone());
            getPreparedStatement().setInt(4, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            Logger.getLogger(this.getClass().toString()).info(getPreparedStatement().toString());

            setSql("update pessoa_fisica set rg = ?, cpf = ? where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getRg());
            getPreparedStatement().setString(2, getFuncionario().getCpf());
            getPreparedStatement().setInt(3, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            Logger.getLogger(this.getClass().toString()).info(getPreparedStatement().toString());

            setSql("update funcionario set endereco = ?, senha = ?, administrador = ? "
                    + " where pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getFuncionario().getEndereco());
            getPreparedStatement().setString(2, getFuncionario().getSenha());
            getPreparedStatement().setBoolean(3, getFuncionario().isAdminstrador());
            getPreparedStatement().setInt(4, getFuncionario().getIdPessoa());
            getPreparedStatement().executeUpdate();

            Logger.getLogger(this.getClass().toString()).info(getPreparedStatement().toString());

            return 1;

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public Funcionario select() {
        try {
            setSql("select p.idpessoa f1, p.nome f2, p.email f3, p.telefone f4,"
                    + "pf.cpf f5, pf.rg f6, f.idfuncionario f7, f.endereco f8, f.senha f9,"
                    + " f.administrador f10 from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa "
                    + " where f.pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            setResultSet(getPreparedStatement().executeQuery());
            Logger.getLogger(FuncionarioDao.class.getName()).info(getPreparedStatement().toString());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    if (getFuncionario() == null) {
                        setFuncionario(new Funcionario());
                    }
                    getFuncionario().setIdPessoa(getResultSet().getInt("f1")); //f1
                    getFuncionario().setNome(getResultSet().getString("f2"));//f2
                    getFuncionario().setEmail(getResultSet().getString("f3"));//f3
                    getFuncionario().setTelefone(getResultSet().getString("f4"));//f4
                    getFuncionario().setCpf(getResultSet().getString("f5"));//f5
                    getFuncionario().setRg(getResultSet().getString("f6"));//f6
                    getFuncionario().setIdFuncionario(getResultSet().getInt("f7")); //f7
                    getFuncionario().setEndereco(getResultSet().getString("f8")); //f8
                    getFuncionario().setSenha(getResultSet().getString("f9")); //f9
                    getFuncionario().setAdminstrador(getResultSet().getBoolean("f10")); //f10
                }
                return getFuncionario();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public List<Funcionario> selectByNome() {
        List<Funcionario> lista = new ArrayList<Funcionario>();
        try {
            getConexao().conectar();
            setSql("select p.idpessoa f1, p.nome f2, p.email f3, p.telefone f4,"
                    + "pf.cpf f5, pf.rg f6, f.idfuncionario f7, f.endereco f8, f.senha f9,"
                    + " f.administrador f10 from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa where p.ativo = 1 and (lower(p.nome) like ? or lower(p.nome) like ?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, "%" + getFuncionario().getNome().toLowerCase() + "%");
            getPreparedStatement().setString(2, "%" + getFuncionario().getNome().toUpperCase() + "%");
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Funcionario f = new Funcionario();

                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2")); //f2
                    f.setEmail(getResultSet().getString("f3")); //f3
                    f.setTelefone(getResultSet().getString("f4")); //f4
                    f.setCpf(getResultSet().getString("f5")); //f5
                    f.setRg(getResultSet().getString("f6")); //f6
                    f.setIdFuncionario(getResultSet().getInt("f7")); //f7
                    f.setEndereco(getResultSet().getString("f8")); //f8
                    f.setSenha(getResultSet().getString("f9")); //f9
                    f.setAdminstrador(getResultSet().getBoolean("f10")); //f10
                    lista.add(f);
                }
                return lista;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<Funcionario>();
        try {
            getConexao().conectar();
            setSql("select p.idpessoa f1, p.nome f2, p.email f3, p.telefone f4,"
                    + "pf.cpf f5, pf.rg f6, f.idfuncionario f7, f.endereco f8, f.senha f9,"
                    + " f.administrador f10 from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa where p.ativo = 1");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Funcionario f = new Funcionario();

                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2")); //f2
                    f.setEmail(getResultSet().getString("f3")); //f3
                    f.setTelefone(getResultSet().getString("f4")); //f4
                    f.setCpf(getResultSet().getString("f5")); //f5
                    f.setRg(getResultSet().getString("f6")); //f6
                    f.setIdFuncionario(getResultSet().getInt("f7")); //f7
                    f.setEndereco(getResultSet().getString("f8")); //f8
                    f.setSenha(getResultSet().getString("f9")); //f9
                    f.setAdminstrador(getResultSet().getBoolean("f10")); //f10
                    lista.add(f);
                }
                return lista;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Funcionario find() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public List<Funcionario> findRange(int[] range) {
        List<Funcionario> lista = new ArrayList<Funcionario>();
        try {
            getConexao().conectar();
            setSql("select p.idpessoa f1, p.nome f2, p.email f3, p.telefone f4,"
                    + "pf.cpf f5, pf.rg f6, f.idfuncionario f7, f.endereco f8, f.senha f9,"
                    + " f.administrador f10 from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Funcionario f = new Funcionario();

                    f.setIdPessoa(getResultSet().getInt("f1")); //f1
                    f.setNome(getResultSet().getString("f2")); //f2
                    f.setEmail(getResultSet().getString("f3")); //f3
                    f.setTelefone(getResultSet().getString("f4")); //f4
                    f.setCpf(getResultSet().getString("f5")); //f5
                    f.setRg(getResultSet().getString("f6")); //f6
                    f.setIdFuncionario(getResultSet().getInt("f7")); //f7
                    f.setEndereco(getResultSet().getString("f8")); //f8
                    f.setSenha(getResultSet().getString("f9")); //f9
                    f.setAdminstrador(getResultSet().getBoolean("f10")); //f10
                    lista.add(f);
                }
                return lista;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public int count() {
        try {
            setSql("select count(*) from funcionario");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            getResultSet().next();
            return getResultSet().getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public Funcionario validate(String email, String senha) {
        try {
            setSql("select p.nome, f.idfuncionario, p.email, f.administrador from funcionario f join "
                    + "pessoa p on p.idpessoa = f.pessoa_fisica_pessoa_idpessoa where p.email = ?"
                    + " and f.senha = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, email);
            getPreparedStatement().setString(2, senha);
            System.out.println("Query " + getPreparedStatement());
            setResultSet(getPreparedStatement().executeQuery());
            funcionario = null;
            while (getResultSet().next()) {
                funcionario = new Funcionario();
                funcionario.setEmail(getResultSet().getString(1));
                funcionario.setIdFuncionario(getResultSet().getInt(2));
                funcionario.setNome(getResultSet().getString(3));
                funcionario.setAdminstrador(getResultSet().getBoolean(4));
                System.out.println("funcionario id " + funcionario.getIdFuncionario());
                return funcionario;
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
