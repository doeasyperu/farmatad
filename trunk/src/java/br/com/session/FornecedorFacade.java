/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.session;

import br.com.dao.FornecedorDao;
import br.entity.Fornecedor;
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
public class FornecedorFacade {

    private FornecedorDao em = new FornecedorDao();

    public void create(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        em.insert();
    }

    public void edit(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        em.update();
        ;
    }

    public void remove(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        em.delete();
    }

    public Fornecedor find(Object id) {
        em.setFornecedor(new Fornecedor());
        return em.select();
    }

    public List<Fornecedor> findAll() {
        return em.findAll();
    }

    public List<Fornecedor> findRange(int[] range) {

        return em.findRange(range);
    }

    public int count() {

        return em.count();
    }
}
