package br.com.controller;

import br.entity.Funcionario;
import br.com.controller.util.JsfUtil;
import br.com.session.FuncionarioFacade;
import java.io.Serializable;
import java.util.List;

import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "funcionarioController")
@SessionScoped
public class FuncionarioController implements Serializable {

    private Funcionario current;
    @EJB
    private FuncionarioFacade ejbFacade;
    private List<Funcionario> listaFunionario = null;
    private List<Funcionario> listaPesquisa = null;
    private boolean senha = false;

    public String acao() {
        return null;
    }

    public List<Funcionario> getListaPesquisa() {
        return listaPesquisa;
    }

    public void setListaPesquisa(List<Funcionario> listaPesquisa) {
        this.listaPesquisa = listaPesquisa;
    }

    public String preparaPesquisa() {
        current = new Funcionario();
        listaPesquisa = null;
        return "funcionario/Search";
    }

    public String pesquisar() {
        listaPesquisa = ejbFacade.findByNome(current);
        return null;
    }

    public List<Funcionario> getListaFunionario() {
        if (listaFunionario == null) {
            listaFunionario = ejbFacade.findAll();
        }
        return listaFunionario;
    }

    public void setListaFunionario(List<Funcionario> listaFunionario) {
        this.listaFunionario = listaFunionario;
    }

    public boolean getSenha() {
        return senha;
    }

    public void setSenha(boolean senha) {
        this.senha = senha;
    }

    public FuncionarioController() {
    }

    public Funcionario getSelected() {
        if (current == null) {
            current = new Funcionario();
            listaFunionario = null;
        }
        return current;
    }

    public void setSelected(Funcionario funcionario) {
        current = funcionario;
    }

    private FuncionarioFacade getFacade() {
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
        current = new Funcionario();
        return "Create";
    }

    public String create() {
        try {
            final int resultado = getFacade().create(current);
            if (resultado == 1) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFuncionario").getString("FuncionarioCreated"));
            } else if (resultado == -2) {
                JsfUtil.addErrorMessage("Já existe funcionario com esse e-mail e senha, tente outro");
            } else if (resultado == -3) {
                JsfUtil.addErrorMessage("Já existe funcionario com cpf, tente outro");
            } else if (resultado == -4) {
                JsfUtil.addErrorMessage("Esse erro não foi previsto, contate a equipe técnica");
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFuncionario").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFuncionario").getString("FuncionarioUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFuncionario").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
//        current = (Funcionario) getItems().getRowData();
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
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleFuncionario").getString("FuncionarioDeleted"));
            } else if (resposta == -1) {
                JsfUtil.addSuccessMessage("Funcionario não pode ser excluído, foi marcado como inativo");
            } else if (resposta == -2) {
                JsfUtil.addErrorMessage("Erro não previsto, por favor contate a equipe técnica e relate exatemente o acontecido");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleFuncionario").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();

    }

    private void recreateModel() {
        listaFunionario = null;
    }

    public List<SelectItem> getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public boolean isAdministrador() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession hs = (HttpSession) fc.getExternalContext().getSession(true);
        Funcionario f = (Funcionario) hs.getAttribute("funcionario");
        if (f != null) {
            final boolean resp = f.isAdminstrador();
            return resp;
        } else {
            return false;
        }
    }

    public String validaFuncionario() {
        String email = getSelected().getEmail();
        String senha = getSelected().getSenha();
        System.out.println("email e senha " + email + ":" + senha);
        System.out.println("current is null " + current != null);
        current = ejbFacade.validate(email, senha);
        if (current != null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession sessao = (HttpSession) fc.getExternalContext().getSession(true);
            sessao.setAttribute("funcionario", current);
            System.out.println("Autenticado " + current.getIdFuncionario());
            current = new Funcionario();
            return "/restrito/index";
        } else {
            JsfUtil.addErrorMessage("Nome do usuário ou senha errados, tente novamente");
            return null;
        }
    }

    public String terminaSessao() {
        System.out.println("Sessão terminada");
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession s = (HttpSession) externalContext.getSession(true);
        s.setAttribute("funcionario", null);
        s.invalidate();
        externalContext.getSession(false);
        return "../login.xhtml";
    }

    @FacesConverter(forClass = Funcionario.class)
    public static class FuncionarioControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FuncionarioController controller = (FuncionarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "funcionarioController");
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
            if (object instanceof Funcionario) {
                Funcionario o = (Funcionario) object;
                return getStringKey(o.getIdPessoa());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FuncionarioController.class.getName());
            }
        }
    }
}
