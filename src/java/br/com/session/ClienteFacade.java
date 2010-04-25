/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.session;

import br.com.dao.ClienteDao;
import br.entity.Cliente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author marcio
 */
@Stateless
public class ClienteFacade {

    private ClienteDao cd = new ClienteDao();

    public void create(Cliente cliente) {
        cd.setCliente(cliente);
        cd.insert();
    }

    public void edit(Cliente cliente) {
        cd.setCliente(cliente);
        cd.update();
    }

    public void remove(Cliente cliente) {
        cd.setCliente(cliente);
        cd.delete();
    }

    public Cliente find(Integer id) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(id);
        cd.setCliente(cliente);
        return cd.select();
    }

    public List<Cliente> findAll() {
        return cd.findAll();
    }

    public List<Cliente> findRange(int[] range) {
        
        return cd.findRange(range);
    }

    public int count() {
        return cd.count();
    }
}
