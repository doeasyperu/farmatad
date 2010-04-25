/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class ClienteDao extends Dao {

    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ClienteDao() {
        try {
            getConexao().conectar();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int insert() {
        try {
            setSql("insert into Pessoa(nome, telefone, email) values" + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getCliente().getNome());
            getPreparedStatement().setString(2, getCliente().getTelefone());
            getPreparedStatement().setString(3, getCliente().getEmail());
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
                getCliente().setIdPessoa(getResultSet().getInt("last"));
            }
            setSql("insert into Pessoa_Fisica(pessoa_idpessoa, cpf, rg) values" + "(?,?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            getPreparedStatement().setString(2, getCliente().getCpf());
            getPreparedStatement().setString(3, getCliente().getRg());
            getPreparedStatement().executeUpdate();


            setSql("select max(idpessoa) as last from pessoa");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            while (getResultSet().next()) {
                getCliente().setIdPessoa(getResultSet().getInt("last"));
            }
            setSql("insert into cliente (pontos, Pessoa_Fisica_Pessoa_idPessoa) "
                    + "values(?,?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getPontos());
            getPreparedStatement().setInt(2, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);

            return -1;
        }
    }

    @Override
    public Cliente select() {
        try {
            setSql("select p.idpessoa, p.nome, p.telefone, p.email, p.ativo, "
                    + "pf.cpf, pf.rg, "
                    + "c.pontos, c.id_cliente "
                    + "from pessoa p "
                    + "join pessoa_fisica pf on p.idpessoa = pf.pessoa_idpessoa "
                    + "join cliente c on c.pessoa_fisica_pessoa_idpessoa  = pf.pessoa_idpessoa "
                    + "where ativo = 1 and pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            setResultSet(getPreparedStatement().executeQuery());

//            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            setCliente(new Cliente());

            while (getResultSet().next()) {
                getCliente().setIdPessoa(getResultSet().getInt(1));
                getCliente().setNome(getResultSet().getString(2));
                getCliente().setTelefone(getResultSet().getString(3));
                getCliente().setEmail(getResultSet().getString(4));
                getCliente().setAtivo(getResultSet().getBoolean(5));

                getCliente().setCpf(getResultSet().getString(5));
                getCliente().setRg(getResultSet().getString(6));

                getCliente().setPontos(getResultSet().getInt(6));
                getCliente().setIdCliente(getResultSet().getInt(7));
            }
            return getCliente();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public int delete() {
        try {
            setSql("delete from cliente where pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa_fisica where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();

            setSql("delete from pessoa where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();


            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int update() {
        try {
            setSql("update pessoa set nome = ?, email = ?, telefone = ?, ativo = ? "
                    + "where idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getCliente().getNome());
            getPreparedStatement().setString(2, getCliente().getEmail());
            getPreparedStatement().setString(3, getCliente().getTelefone());
            getPreparedStatement().setBoolean(4, getCliente().isAtivo());
            getPreparedStatement().setInt(5, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();
//            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            setSql("update pessoa_fisica set cpf = ?, rg = ? "
                    + "where pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, getCliente().getCpf());
            getPreparedStatement().setString(2, getCliente().getRg());
            getPreparedStatement().setString(2, getCliente().getRg());
            getPreparedStatement().setInt(3, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();
//            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            setSql("update cliente set pontos = ? "
                    + "where pessoa_fisica_pessoa_idpessoa = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getPontos());
            getPreparedStatement().setInt(2, getCliente().getIdPessoa());
            getPreparedStatement().executeUpdate();
//            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<Cliente> findAll() {
        try {
            List<Cliente> lista = new ArrayList<Cliente>();
            setSql("select p.idpessoa, p.nome, p.telefone, p.email, p.ativo, "
                    + "pf.cpf, pf.rg, " + "c.pontos, c.id_cliente "
                    + "from pessoa p "
                    + "join pessoa_fisica pf on p.idpessoa = pf.pessoa_idpessoa "
                    + "join cliente c on c.pessoa_fisica_pessoa_idpessoa  = pf.pessoa_idpessoa "
                    + "where ativo = 1");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdPessoa());
            setResultSet(getPreparedStatement().executeQuery());
            //            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            while (getResultSet().next()) {
                Cliente c = new Cliente();
                c.setIdPessoa(getResultSet().getInt(1));
                c.setNome(getResultSet().getString(2));
                c.setTelefone(getResultSet().getString(3));
                c.setEmail(getResultSet().getString(4));
                c.setAtivo(getResultSet().getBoolean(5));
                c.setCpf(getResultSet().getString(5));
                c.setRg(getResultSet().getString(6));
                c.setPontos(getResultSet().getInt(6));
                c.setIdCliente(getResultSet().getInt(7));
                lista.add(c);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public List<Cliente> findRange(int[] range) {
        try {
            List<Cliente> lista = new ArrayList<Cliente>();
            setSql("select p.idpessoa, p.nome, p.telefone, p.email, p.ativo, "
                    + "pf.cpf, pf.rg, " + "c.pontos, c.id_cliente "
                    + "from pessoa p "
                    + "join pessoa_fisica pf on p.idpessoa = pf.pessoa_idpessoa "
                    + "join cliente c on c.pessoa_fisica_pessoa_idpessoa  = pf.pessoa_idpessoa "
                    + "where ativo = 1 limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            setResultSet(getPreparedStatement().executeQuery());
                        Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            while (getResultSet().next()) {
                Cliente c = new Cliente();
                c.setIdPessoa(getResultSet().getInt(1));
                c.setNome(getResultSet().getString(2));
                c.setTelefone(getResultSet().getString(3));
                c.setEmail(getResultSet().getString(4));
                c.setAtivo(getResultSet().getBoolean(5));
                c.setCpf(getResultSet().getString(5));
                c.setRg(getResultSet().getString(6));
                c.setPontos(getResultSet().getInt(6));
                c.setIdCliente(getResultSet().getInt(7));
                lista.add(c);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Object find() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public int count(){
        try {
            setSql("select count(*) tuplas from Cliente");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            getResultSet().next();
            return getResultSet().getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

}
