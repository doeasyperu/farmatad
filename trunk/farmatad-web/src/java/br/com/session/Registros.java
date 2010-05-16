/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.session;

import br.com.dao.RegistroDao;
import br.entity.ItemEntrada;
import javax.ejb.Stateless;

/**
 *
 * @author marcio
 */
@Stateless
public class Registros {

    private RegistroDao dao;
    private ItemEntrada itemEntrada;

    public RegistroDao getDao() {
        if (dao == null){
            dao = new RegistroDao();
        }
        return dao;
    }

    public ItemEntrada getItemEntrada() {
        return itemEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemEntrada = itemEntrada;
    }
    public void registrarEntrada(){
        getDao().setItemEntrada(itemEntrada);
        getDao().registrarEntrada();
    }
}
