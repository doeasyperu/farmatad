/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.dao.Conexao;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.StateManager;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author marcio
 */
@ManagedBean(name = "RelatorioController")
@SessionScoped
public class RelatorioController implements Serializable {

    /** Creates a new instance of RelatorioController */
    public RelatorioController() {
    }

    public String gerarRelatorio() {
        try {
            Conexao c = Conexao.getInstance();
            String path = FacesContext.getCurrentInstance().
                    getExternalContext().getRealPath("/resources/relatorio_produtos.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(path);
            JasperPrint rel = JasperFillManager.fillReport(jr, null, c.getConnection());
            byte[] pdf = JasperExportManager.exportReportToPdf(rel);
            final FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(pdf.length);
            response.setHeader("Content-disposition", "attachment;filename=relatorio.pdf");

            response.setHeader("Cache-Control", "cache, must-revalidate");
            response.setHeader("Pragma", "public");
            ServletOutputStream out;
            try {
                out = response.getOutputStream();
                out.write(pdf);
                StateManager stateManager = (StateManager) fc.getApplication().getStateManager();
                stateManager.saveSerializedView(fc);
            } catch (IOException ex) {
                Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
            }


            fc.responseComplete();
        } catch (Exception ex) {
//            Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return null;
    }
}
