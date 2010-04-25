/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Funcionario;
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

            return getPreparedStatement().executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            return -1;
        }
    }

    @Override
    public Funcionario select() {
        try {
            setSql("select p.idpessoa, p.nome, p.email, p.telefone,"
                    + "pf.cpf, pf.rg, f.idfuncionario, f.endereco, f.senha,"
                    + " f.administrador from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa "
                    + " where f.pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, getFuncionario().getIdPessoa());
            setResultSet(getPreparedStatement().executeQuery());
            Logger.getLogger(FuncionarioDao.class.getName()).info(getPreparedStatement().toString());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    if (getFuncionario() == null) {
                        setFuncionario(new Funcionario());
                    }
                    getFuncionario().setIdPessoa(getResultSet().getInt(1));
                    getFuncionario().setNome(getResultSet().getString(2));
                    getFuncionario().setEmail(getResultSet().getString(3));
                    getFuncionario().setTelefone(getResultSet().getString(4));
                    getFuncionario().setCpf(getResultSet().getString(5));
                    getFuncionario().setRg(getResultSet().getString(6));
                    getFuncionario().setIdFuncionario(getResultSet().getInt(7));
                    getFuncionario().setEndereco(getResultSet().getString(8));
                    getFuncionario().setSenha(getResultSet().getString(9));
                    getFuncionario().setAdminstrador(getResultSet().getBoolean(10));
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
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
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
    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<Funcionario>();
        try {
            getConexao().conectar();
            setSql("select p.idpessoa, p.nome, p.email, p.telefone,"
                    + "pf.cpf, pf.rg, f.idfuncionario, f.endereco, f.senha,"
                    + " f.administrador from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa ");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Funcionario funcionario = new Funcionario();

                    funcionario.setIdPessoa(getResultSet().getInt(1));
                    funcionario.setNome(getResultSet().getString(2));
                    funcionario.setEmail(getResultSet().getString(3));
                    funcionario.setTelefone(getResultSet().getString(4));
                    funcionario.setCpf(getResultSet().getString(5));
                    funcionario.setRg(getResultSet().getString(6));
                    funcionario.setIdFuncionario(getResultSet().getInt(7));
                    funcionario.setEndereco(getResultSet().getString(8));
                    funcionario.setSenha(getResultSet().getString(9));
                    funcionario.setAdminstrador(getResultSet().getBoolean(10));
                    lista.add(funcionario);
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
            setSql("select p.idpessoa, p.nome, p.email, p.telefone,"
                    + "pf.cpf, pf.rg, f.idfuncionario, f.endereco, f.senha,"
                    + " f.administrador from "
                    + "funcionario f join pessoa_fisica pf "
                    + "on f.pessoa_fisica_pessoa_idpessoa = pf.pessoa_idpessoa "
                    + "join pessoa p "
                    + "on p.idpessoa = pf.pessoa_idpessoa limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            setResultSet(getPreparedStatement().executeQuery());
            if (getResultSet() != null) {
                while (getResultSet().next()) {
                    Funcionario funcionario = new Funcionario();

                    funcionario.setIdPessoa(getResultSet().getInt(1));
                    funcionario.setNome(getResultSet().getString(2));
                    funcionario.setEmail(getResultSet().getString(3));
                    funcionario.setTelefone(getResultSet().getString(4));
                    funcionario.setCpf(getResultSet().getString(5));
                    funcionario.setRg(getResultSet().getString(6));
                    funcionario.setIdFuncionario(getResultSet().getInt(7));
                    funcionario.setEndereco(getResultSet().getString(8));
                    funcionario.setSenha(getResultSet().getString(9));
                    funcionario.setAdminstrador(getResultSet().getBoolean(10));
                    lista.add(funcionario);
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
            setPreparedStatement(getConexao().getConnection().prepareCall(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            getResultSet().next();
            return getResultSet().getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }


    }
}
