/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller.util;

import br.com.controller.FornecedorController;
import br.com.session.FornecedorFacade;
import br.entity.Fornecedor;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author marcio
 */
//@FacesConverter(forClass=Fornecedor.class, value="fornecedorConverter")
public class FornecedorConverter  {

    @EJB
    private FornecedorFacade ejbFacade;

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        System.out.println("string f "+value);
        if (value == null || value.length() == 0) {
            return null;
        }        
        return getEjbFacade().find(Integer.parseInt(value));
    }

   
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        System.out.println("object f "+object);
        if (object == null) {
            return null;
        }
            return object.toString();
       
    }

    private FornecedorFacade getEjbFacade() {
        return ejbFacade;
    }

}
   
