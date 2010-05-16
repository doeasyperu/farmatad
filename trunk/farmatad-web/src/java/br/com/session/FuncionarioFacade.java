/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.session;

import br.com.dao.FuncionarioDao;
import br.entity.Funcionario;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author marcio
 */
@Stateless
public class FuncionarioFacade {
    private FuncionarioDao em = new FuncionarioDao();

    public int create(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        return em.insert();
    }

    public void edit(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        em.update();
    }

    public int remove(Funcionario funcionario) {
        em.setFuncionario(funcionario);
        return em.delete();
    }

    public Funcionario find(Object id) {
        em.setFuncionario(new Funcionario(Integer.parseInt(id.toString())));
        return em.select();
    }

    public List<Funcionario> findByNome(Funcionario funcionario){
        em.setFuncionario(funcionario);
        return em.selectByNome();
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
    public Funcionario validate(String email, String senha){
        return em.validate(email, senha);
    }
}
