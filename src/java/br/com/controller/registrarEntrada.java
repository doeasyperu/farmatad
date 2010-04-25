/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.controller.util.JsfUtil;
import br.com.session.FornecedorFacade;
import br.com.session.ProdutoFacade;
import br.entity.Entrada;
import br.entity.ItemEntrada;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author marcio
 */
@ManagedBean(name = "registrarEntrada")
@RequestScoped
public class registrarEntrada {

    /** Creates a new instance of registrarEntrada */
    @EJB
    private ProdutoFacade ejbProduto;
    @EJB
    private FornecedorFacade ejbFornecedor;
    private Entrada entrada = new Entrada();
    private ItemEntrada itemEntrada= new ItemEntrada();

    public registrarEntrada() {
        entrada.setData(new Date());
        
       
    }

    public ItemEntrada getItemEntrada() {
        return itemEntrada;
    }

    public void setItemEntrada(ItemEntrada itemEntrada) {
        this.itemEntrada = itemEntrada;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public SelectItem[] getFornecedores() {
        return JsfUtil.getSelectItems(ejbFornecedor.findAll(), true);
    }

    public SelectItem[] getProdutos() {
        return JsfUtil.getSelectItems(ejbProduto.findAll(), true);
    }

    

}
