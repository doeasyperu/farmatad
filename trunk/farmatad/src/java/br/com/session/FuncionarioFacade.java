/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.session;

import br.com.dao.FornecedorDao;
import br.com.dao.FuncionarioDao;
import br.entity.Funcionario;
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
public class FuncionarioFacade {
    private FuncionarioDao em = new FuncionarioDao();

    public void create(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        em.insert();
    }

    public void edit(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        em.update();
    }

    public void remove(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        em.update();
    }

    public Funcionario find(Object id) {
        em.setFuncionario(new Funcionario(Integer.parseInt(id.toString())));
        return em.select();
    }

    public List<Funcionario> findAll() {
        return em.findAll();
    }

    public List<Funcionario> findRange(int[] range) {
        return em.findRange(range);
    }

    public int count() {
        return em.count();
    }

}
