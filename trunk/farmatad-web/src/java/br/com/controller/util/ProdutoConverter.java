/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller.util;

import br.com.controller.ProdutoController;
import br.com.session.ProdutoFacade;
import br.entity.Produto;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author marcio
 */
//@FacesConverter(forClass = Produto.class, value="produtoConverter")
public class ProdutoConverter  {

    @EJB
    private ProdutoFacade ejbFacade;

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        System.out.println("key p " +value);
        return getEjbFacade().find(getKey(value));
    }

    int getKey(String value) {
        int key;
        key = Integer.parseInt(value);
        return key;
    }

    String getStringKey(int value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        System.out.println("Object p " + object);
        if (object == null) {
            return null;
        }
       
//            Produto o = (Produto) object;
            return object.toString();
      
    }

    private ProdutoFacade getEjbFacade() {
        return ejbFacade;
    }

}
