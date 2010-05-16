package br.com.controller;

import br.entity.Produto;
import br.com.controller.util.JsfUtil;
import br.com.session.ProdutoFacade;
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

@ManagedBean(name = "produtoController")
@SessionScoped
public class ProdutoController implements Serializable {

    private Produto current;
    @EJB
    ProdutoFacade ejbFacade;
    List<Produto> listaProduto = null;
    List<Produto> listaPesquisa = null;

    public ProdutoController() {
    }

    public List<Produto> getListaPesquisa() {
        return listaPesquisa;
    }

    public void setListaPesquisa(List<Produto> listaPesquisa) {
        this.listaPesquisa = listaPesquisa;
    }
    public String preparePesquisa(){
        current = new Produto();
        listaPesquisa = null;
        return "produto/Search.xhtml";
    }

    public String pesquisar(){
        listaPesquisa = ejbFacade.findByNome(current);
        return null;
    }
    public List<Produto> getListaProduto() {
        if (listaProduto == null) {
            listaProduto = ejbFacade.findAll();
        }
        return listaProduto;
    }

    public void setListaProduto(List<Produto> listaProduto) {
        this.listaProduto = listaProduto;
    }

    public Produto getSelected() {
        if (current == null) {
            current = new Produto();
        }
        return current;
    }

    public Produto getCurrent() {
        getSelected();
        return current;
    }

    public void setCurrent(Produto current) {
        System.out.println("Selecionado " + current.getIdProduto());
        this.current = current;
    }

    public void setSelected(Produto produto) {
        current = produto;
    }

    private ProdutoFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        current = new Produto();
        return "Create";
    }

    public String create() {
        try {
            final int resposta = getFacade().create(current);
            if (resposta == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleProduto").getString("ProdutoCreated"));
            } else if (resposta == 0) {
                JsfUtil.addErrorMessage("Já existe um produto com este fornecedor cadastrado");
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleProduto").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleProduto").getString("ProdutoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleProduto").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
//        current = (Produto) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        System.out.println("produto " + current.getIdProduto());
        performDestroy();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            final int resposta = getFacade().remove(current);
            if (resposta == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleProduto").getString("ProdutoDeleted"));
            } else if (resposta == -1) {
                JsfUtil.addSuccessMessage("Não foi possível excluir o produto, foi marcado comoinativo");
            } else if (resposta == -2) {
                JsfUtil.addErrorMessage("Erro não previsto, favor contatar a equipe técnica");
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleProduto").getString("PersistenceErrorOccured"));
        }
    }

    private void recreateModel() {
        listaProduto = null;
    }

    public List<SelectItem> getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public String paginaPrincipal(){
        recreateModel();
        return "../index.xhtml";
    }
    @FacesConverter(forClass = Produto.class)
    public static class ProdutoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            System.out.println("invocado ProdutoControllerConverter getAsObject value=" + value);
            if (value == null || value.length() == 0) {
                return null;
            }
            ProdutoController controller = (ProdutoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "produtoController");
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
            System.out.println("invocado ProdutoControllerConverter getAsString object=" + object.toString());
            if (object == null) {
                return null;
            }
            if (object instanceof Produto) {
                Produto o = (Produto) object;
                return getStringKey(o.getIdProduto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ProdutoController.class.getName());
            }
        }
    }
}
