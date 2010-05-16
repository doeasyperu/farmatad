package br.com.controller;

import br.entity.Cliente;
import br.com.controller.util.JsfUtil;
import br.com.session.ClienteFacade;
import java.io.Serializable;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "clienteController")
@SessionScoped
public class ClienteController implements Serializable {

    private Cliente current;
    @EJB
    private ClienteFacade ejbFacade;
    private List<Cliente> listaCliente = null;

    ;
    private List<Cliente> listaProcura = null;

    public List<Cliente> getListaProcura() {
        return listaProcura;
    }

    public void setListaProcura(List<Cliente> listaProcura) {
        this.listaProcura = listaProcura;
    }

    public List<Cliente> getListaCliente() {
        listaCliente = ejbFacade.findAll();
        return listaCliente;
    }

    public ClienteController() {
    }

    public Cliente getCurrent() {
        if (current == null) {
            current = new Cliente();
        }
        //System.out.println("get selected " + current.getIdCliente());
        return current;
    }

    public Cliente getSelected() {
        return getCurrent();
    }

    public void setCurrent(Cliente current) {
        this.current = current;
    }

    private ClienteFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
//        recreateModel();
        return "List";
    }

    public String prepareView() {
//        current = (Cliente) getItems().getRowData();
        //System.out.println("controller id cliente "+current.getIdCliente());
        return "View";
    }

    public String prepareCreate() {
        current = new Cliente();
        return "Create";
    }

    public String create() {
        try {
            final int resposta = getFacade().create(current);
            if (resposta == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleCliente").getString("ClienteCreated"));
            } else if (resposta == -1) {
                JsfUtil.addErrorMessage("Não foi possível inserir este usuário, pois já existe CPF cadastrado com esse número");
            } else if (resposta == -2) {
                JsfUtil.addErrorMessage("Houve erro não previsto, contate a equipe técnica");
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
//        current = (Cliente) getItems().getRowData();
        //System.out.println("controller id cliente "+current.getIdCliente());
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleCliente").getString("ClienteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleCliente").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
//        current = (Cliente) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        listaCliente = ejbFacade.findAll();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
//        recreateModel();
//        updateCurrentItem();
        // all items were removed - go back to list
        listaCliente = ejbFacade.findAll();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleCliente").getString("ClienteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleCliente").getString("PersistenceErrorOccured"));
        }
    }

    public String preparaPesquisa(){
        current = new Cliente();
        listaProcura = null;
        return "cliente/Search.xhtml";
    }

    public String pesquisar() {
        listaProcura = ejbFacade.findByNome(current);
        return null;
    }

    public List<Cliente> getClientes() {
        return getFacade().findAll();
    }

    public List<SelectItem> getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Cliente.class)
    public static class ClienteControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clienteController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object == null) {
                return null;
            }
            if (object instanceof Cliente) {
                Cliente o = (Cliente) object;
                return getStringKey(o.getIdPessoa());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ClienteController.class.getName());
            }
        }
    }
}
