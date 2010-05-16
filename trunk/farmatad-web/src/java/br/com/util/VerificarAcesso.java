
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.util;

import br.entity.Funcionario;
import java.io.Serializable;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author marcio
 */
public class VerificarAcesso implements PhaseListener, Serializable {

    public void afterPhase(PhaseEvent event) {
//        System.out.println(" after Fase " + event.getPhaseId());
        FacesContext fc = FacesContext.getCurrentInstance();
//        System.out.println("Invocado before" + fc.getCurrentPhaseId());
//        System.out.println("Página é " + fc.getViewRoot().getViewId());
        final boolean restrito = fc.getViewRoot().getViewId().contains("/restrito/");
//        System.out.println("é restrito " + restrito);
        if (restrito) {
            final boolean acesso = fc.getViewRoot().getViewId().contains("/restrito/funcionario/");
            Funcionario f = null;
            HttpSession hs = (HttpSession) fc.getExternalContext().getSession(true);
            f = (Funcionario) hs.getAttribute("funcionario");
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            if (f == null) {
//                System.out.println("Redirecionar");
                nh.handleNavigation(fc, null, "/login.xhtml");
            } else if ((acesso) && (f != null)&&(!f.isAdminstrador())) {
                nh.handleNavigation(fc, null, "/restrito/index.xhtml");
            }
        }
    }

    public void beforePhase(PhaseEvent event) {
//        System.out.println(" before Fase " + event.getPhaseId());
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
