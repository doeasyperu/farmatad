/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controller;

import br.com.dao.Conexao;
import br.com.dao.RelatorioDao;
import br.entity.Cliente;
import br.entity.Venda;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.application.StateManager;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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

    private List<Venda> listaVenda;
    private Venda venda;
    private String idvenda;

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
                stateManager.saveView(fc);
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

    public List<Venda> getListaVenda() {
        if (listaVenda == null) {
            listaVenda = new ArrayList<Venda>();
        }
        return listaVenda;
    }

    public void setListaVenda(List<Venda> listaVenda) {
        this.listaVenda = listaVenda;
    }

    public Venda getVenda() {
        if (venda == null) {
            venda = new Venda();
            venda.setCliente(new Cliente());

        }
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public String getIdvenda() {

        return idvenda;
    }

    public void setIdvenda(String idvenda) {
        this.idvenda = idvenda;
    }

    public String pesquisarNotas() {
        boolean cpf = venda.getCliente() == null
                || venda.getCliente().getCpf().equals("");
        boolean data = venda.getData() == null;
        boolean codigo = idvenda == null || idvenda.equals("");
        if (cpf && data && codigo) {
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            String msg = "Preencha algum critério de pesquisa";
            fm.setSummary(msg);
            fm.setDetail(msg);
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return null;
        } else {
            if (!idvenda.equals("")) {
                venda.setIdVenda(new Integer(idvenda));
            }
            RelatorioDao dao = new RelatorioDao();
            listaVenda = dao.pesquisarVenda(venda);
        }
        return null;
    }

    public String mostrarNotaFiscal() {

        RelatorioDao dao = new RelatorioDao();
        venda.setListaItensVenda(dao.listarItems(venda));

        return "notaFiscal";
    }

    public String cancelar() {

        venda = null;
        listaVenda = null;
        idvenda = "";
        return "/restrito/index";
    }

    public String emitirNota() {
        try {
            Conexao c = Conexao.getInstance();
            String path = FacesContext.getCurrentInstance().
                    getExternalContext().getRealPath("/resources/nota_fiscal.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(path);
            Map<String, Integer> attr = new HashMap<String, Integer>();
            attr.put("IDVENDA", new Integer(venda.getIdVenda()));
            JasperPrint rel = JasperFillManager.fillReport(jr, attr, c.getConnection());
            byte[] pdf = JasperExportManager.exportReportToPdf(rel);
            final FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setContentLength(pdf.length);
            response.setHeader("Content-disposition", "attachment;filename=notaFiscal.pdf");

            response.setHeader("Cache-Control", "cache, must-revalidate");
            response.setHeader("Pragma", "public");
            ServletOutputStream out;
            try {
                out = response.getOutputStream();
                out.write(pdf);
                StateManager stateManager = (StateManager) fc.getApplication().getStateManager();
                stateManager.saveView(fc);
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
