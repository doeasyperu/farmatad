/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.session;

import br.com.dao.FornecedorDao;
import br.entity.Fornecedor;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author marcio
 */
@Stateless
public class FornecedorFacade {

    private FornecedorDao em = new FornecedorDao();

    public int create(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        return em.insert();
    }

    public void edit(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        em.update();
        ;
    }

    public int remove(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        return em.delete();
    }
    public List<Fornecedor> findByNome(Fornecedor fornecedor) {
        em.setFornecedor(fornecedor);
        return em.selectByNome();
    }

    public Fornecedor find(Object id) {
        final int chave = Integer.parseInt(id.toString());
        em.setFornecedor(new Fornecedor(chave));
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
