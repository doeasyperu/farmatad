/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.entity.Entrada;
import br.entity.ItemEntrada;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcio
 */
public class Negocios {

    private String sql;
    private PreparedStatement pstm;
    private Conexao conexao = Conexao.getInstance();

    public int inserirEntrada(Entrada entrada, ItemEntrada itemEntrada) {
        try {
            sql = "insert into entrada (data, fornecedor_idfornecedor, funcionario_idfuncionario)"
                    + "values (?,?,?)";
            pstm = conexao.getConnection().prepareStatement(sql);
            java.sql.Date data = new java.sql.Date(entrada.getData().getTime());
            pstm.setDate(1, data);
            pstm.setInt(2, entrada.getFonecedor().getIdFornecedor());
            pstm.setInt(3, entrada.getFuncionario().getIdFuncionario());
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(Negocios.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
