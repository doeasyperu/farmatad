package br.com.controller;

import br.entity.Fornecedor;
import br.com.controller.util.JsfUtil;
import br.com.session.FornecedorFacade;
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

@ManagedBean(name = "fornecedorController")
@SessionScoped
public class FornecedorController implements Serializable {

    private Fornecedor current;
    @EJB
    private FornecedorFacade ejbFacade;
    private List<Fornecedor> listaFornecedor = null;
    private List<Fornecedor> listaPesquisa = null;

    public FornecedorController() {
    }

    public List<Fornecedor> getListaPesquisa() {
        return listaPesquisa;
    }

    public void setListaPesquisa(List<Fornecedor> listaPesquisa) {
        this.listaPesquisa = listaPesquisa;
    }
    

    public List<Fornecedor> getListaFornecedor() {
        if (listaFornecedor == null){
            listaFornecedor = ejbFacade.findAll();
        }
        return listaFornecedor;
    }

    public void setListaFornecedor(List<Fornecedor> listaFornecedor) {
        this.listaFornecedor = listaFornecedor;
    }

    public Fornecedor getSelected() {
        if (current == null) {
            current = new Fornecedor();
        }
        return current;
    }
    public void setSelected(Fornecedor fornecedor){
        current = fornecedor;
    }

    private FornecedorFacade getFacade() {
        return ejbFacade;
    }

    public String preparaPesquisa(){
        current = new Fornecedor();
        listaPesquisa = null;
        return "fornecedor/Search.xhtml";
    }

    public String pesquisar(){
        listaPesquisa = ejbFacade.findByNome(current);
        return null;
    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        current = new Fornecedor();
        return "Create";
    }

    public String create() {
        try {
            final int resposta = getFacade().create(current);
            if (resposta == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFornecedor").getString("FornecedorCreated"));
            } else if(resposta == -1){
                JsfUtil.addErrorMessage("Existe uma pessoa jurídica com este CNPJ cadastrado no sistema.");
            } else if (resposta == -2){
                JsfUtil.addErrorMessage("Erro não esperado, contate a equipe técnica");
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFornecedor").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFornecedor").getString("FornecedorUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFornecedor").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
//        current = (Fornecedor) getItems().getRowData();
        performDestroy();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
            return "View";
    }

    private void performDestroy() {
        try {
            final int resposta = getFacade().remove(current);
            if (resposta == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFornecedor").getString("FornecedorDeleted"));
            } else if (resposta == -1) {
                JsfUtil.addSuccessMessage("Fornecedor não pode ser excluído, foi marcado como inativo");
            } else if (resposta == -2) {
                JsfUtil.addErrorMessage("Houve um erro, contate o desenvolvedor e descreva suas operações");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFornecedor").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        recreateModel();
    }

    private void recreateModel() {
        listaFornecedor =null;
    }

    public List<SelectItem> getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Fornecedor.class)
    public static class FornecedorControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            System.out.println("invocado FornecedorControllerConverter getAsObject value=" + value);
            if (value == null || value.length() == 0) {
                System.out.println("fornecedor nulo em getAsObject");
                return null;
            }
            FornecedorController controller = (FornecedorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fornecedorController");
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
            System.out.println("invocado FornecedorControllerConverter getAsString object=" + object.toString());
            if (object == null) {
                System.out.println("fornecedor  nulo em getAsString");
                return null;
            }
            if (object instanceof Fornecedor) {
                Fornecedor o = (Fornecedor) object;
                return getStringKey(o.getIdPessoa());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: "
                        + Fornecedor.class.getName());
            }
        }
    }
}
