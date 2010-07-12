/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Cliente;
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
        } catch (MySQLIntegrityConstraintViolationException mix) {
            try {
                System.out.println("codigo do mysql " + mix.getErrorCode());
                setSql("select max(idpessoa) as last from pessoa");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                setResultSet(getPreparedStatement().executeQuery());
                while (getResultSet().next()) {
                    getCliente().setIdPessoa(getResultSet().getInt("last"));
                }
                setSql("delete from pessoa where idpessoa = ?");
                setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
                getPreparedStatement().setInt(1, getCliente().getIdPessoa());
                getPreparedStatement().executeUpdate();
                return -1;
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);

            return -2;
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
    public Cliente select() {
        try {
            setSql("select p.idpessoa as f1, p.nome as f2, p.telefone as f3, "
                    + "p.email as f4, p.ativo as f5, pf.pessoa_idpessoa as f6, "
                    + "pf.cpf as f7, pf.rg as f8, c.id_cliente as f9, c.pontos as f10, "
                    + "c.pessoa_fisica_pessoa_idpessoa as f11 from pessoa p "
                    + "join pessoa_fisica pf "
                    + " on p.idpessoa = pf.pessoa_idpessoa join cliente c"
                    + " on pf.pessoa_idpessoa = c.pessoa_fisica_pessoa_idpessoa "
                    + "where ativo = 1 and c.id_cliente = ?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, getCliente().getIdCliente());
            setResultSet(getPreparedStatement().executeQuery());
            setCliente(new Cliente());
            while (getResultSet().next()) {
                getCliente().setIdPessoa(getResultSet().getInt("f1")); //f1
                getCliente().setNome(getResultSet().getString("f2")); //f2
                getCliente().setTelefone(getResultSet().getString("f3")); //f3
                getCliente().setEmail(getResultSet().getString("f4")); //f4
                getCliente().setAtivo(getResultSet().getBoolean("f5")); //f5
                getCliente().setCpf(getResultSet().getString("f7")); //f7
                getCliente().setRg(getResultSet().getString("f8")); //f8
                getCliente().setIdCliente(getResultSet().getInt("f9"));//f9
                getCliente().setPontos(getResultSet().getInt("f10")); //f10
                System.out.println("idcliente em select " + getCliente().getIdCliente());
            }
            return getCliente();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Cliente> findAll() {
        try {
            List<Cliente> lista = new ArrayList<Cliente>();
            setSql("select p.idpessoa as f1, p.nome as f2, p.telefone as f3, "
                    + "p.email as f4, p.ativo as f5, pf.pessoa_idpessoa as f6, "
                    + "pf.cpf as f7, pf.rg as f8, c.id_cliente as f9, c.pontos as f10, "
                    + "c.pessoa_fisica_pessoa_idpessoa as f11 from pessoa p "
                    + "join pessoa_fisica pf "
                    + " on p.idpessoa = pf.pessoa_idpessoa join cliente c"
                    + " on pf.pessoa_idpessoa = c.pessoa_fisica_pessoa_idpessoa where p.ativo = 1");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            setResultSet(getPreparedStatement().executeQuery());
            while (getResultSet().next()) {
                Cliente c = new Cliente();
                c.setIdPessoa(getResultSet().getInt("f1")); //f1
                c.setNome(getResultSet().getString("f2")); //f2
                c.setTelefone(getResultSet().getString("f3")); //f3
                c.setEmail(getResultSet().getString("f4")); //f4
                c.setAtivo(getResultSet().getBoolean("f5")); //f5
                c.setCpf(getResultSet().getString("f7")); //f7
                c.setRg(getResultSet().getString("f8")); //f8
                c.setIdCliente(getResultSet().getInt("f9"));//f9
                c.setPontos(getResultSet().getInt("f10")); //f10
                System.out.println("\nidcliente em find all " + c.getIdCliente());
                System.out.println("idpessoa em find all " + c.getIdPessoa() + "\n");
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
            setSql("select p.idpessoa as f1, p.nome as f2, p.telefone as f3, "
                    + "p.email as f4, p.ativo as f5, pf.pessoa_idpessoa as f6, "
                    + "pf.cpf as f7, pf.rg as f8, c.id_cliente as f9, c.pontos as f10, "
                    + "c.pessoa_fisica_pessoa_idpessoa as f11 from pessoa p "
                    + "join pessoa_fisica pf "
                    + " on p.idpessoa = pf.pessoa_idpessoa join cliente c"
                    + " on pf.pessoa_idpessoa = c.pessoa_fisica_pessoa_idpessoa where p.ativo = 1 limit ?,?");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setInt(1, range[0]);
            getPreparedStatement().setInt(2, range[1]);
            setResultSet(getPreparedStatement().executeQuery());
            Logger.getLogger(ClienteDao.class.getName()).info(getPreparedStatement().toString());

            while (getResultSet().next()) {
                Cliente c = new Cliente();
                c.setIdPessoa(getResultSet().getInt("f1")); //f1
                c.setNome(getResultSet().getString("f2")); //f2
                c.setTelefone(getResultSet().getString("f3")); //f3
                c.setEmail(getResultSet().getString("f4")); //f4
                c.setAtivo(getResultSet().getBoolean("f5")); //f5
                c.setCpf(getResultSet().getString("f7")); //f7
                c.setRg(getResultSet().getString("f8")); //f8
                c.setIdCliente(getResultSet().getInt("f9"));//f9
                c.setPontos(getResultSet().getInt("f10")); //f10
                System.out.println("\nidcliente em findRange " + c.getIdCliente());
                System.out.println("\nidpessoa em findRange " + c.getIdPessoa() + "\n");
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
        return null;
    }

    public List<Cliente> findByNome() {
        try {
            List<Cliente> lista = new ArrayList<Cliente>();
            setSql("select p.idpessoa as f1, p.nome as f2, p.telefone as f3, "
                    + "p.email as f4, p.ativo as f5, pf.pessoa_idpessoa as f6, "
                    + "pf.cpf as f7, pf.rg as f8, c.id_cliente as f9, c.pontos as f10, "
                    + "c.pessoa_fisica_pessoa_idpessoa as f11 from pessoa p "
                    + "join pessoa_fisica pf "
                    + " on p.idpessoa = pf.pessoa_idpessoa join cliente c"
                    + " on pf.pessoa_idpessoa = c.pessoa_fisica_pessoa_idpessoa "
                    + "where ativo = 1 and (lower(p.nome) like ? or lower(p.nome) like ?)");
            setPreparedStatement(getConexao().getConnection().prepareStatement(getSql()));
            getPreparedStatement().setString(1, "%"+getCliente().getNome().toLowerCase()+"%");
            getPreparedStatement().setString(2, "%"+getCliente().getNome().toUpperCase()+"%");
            System.out.println(getPreparedStatement());
            setResultSet(getPreparedStatement().executeQuery());
            setCliente(new Cliente());
            while (getResultSet().next()) {
                Cliente c = new Cliente();
                c.setIdPessoa(getResultSet().getInt("f1")); //f1
                c.setNome(getResultSet().getString("f2")); //f2
                c.setTelefone(getResultSet().getString("f3")); //f3
                c.setEmail(getResultSet().getString("f4")); //f4
                c.setAtivo(getResultSet().getBoolean("f5")); //f5
                c.setCpf(getResultSet().getString("f7")); //f7
                c.setRg(getResultSet().getString("f8")); //f8
                c.setIdCliente(getResultSet().getInt("f9"));//f9
                c.setPontos(getResultSet().getInt("f10")); //f10
                System.out.println("\nidcliente em findRange " + c.getIdCliente());
                System.out.println("\nidpessoa em findRange " + c.getIdPessoa() + "\n");
                lista.add(c);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int count() {
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
