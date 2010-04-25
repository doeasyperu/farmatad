/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.session;

import br.com.dao.ProdutoDao;
import br.entity.Fornecedor;
import br.entity.Produto;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
public class ProdutoFacade {
    
    private ProdutoDao em = new ProdutoDao();

    public void create(Produto produto) {
        em.setProduto(produto);
        em.insert();
    }

    public void edit(Produto produto) {
        em.setProduto(produto);
        em.update();
    }

    public void remove(Produto produto) {
        em.setProduto(produto);
        em.delete();
    }

    public Produto find(Object id) {
        em.setProduto(new Produto(Integer.parseInt(id.toString())));
        return em.select();

    }

    public List<Produto> findAll() {
        return em.findAll();
    }

    public List<Produto> findRange(int[] range) {
        return em.findRange(range);
    }

    public int count() {
        return em.count();
    }
//    public List<Produto> findByFornecedor(Fornecedor f){
//
//    }

}
