/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author marcio
 */
//@javax.inject.Singleton
public class Conexao {

    private Connection connection;
    private String banco;
    private String login;
    private String senha;
    private String porta;
    private String url;
    private static Conexao conexao;

    private Conexao() {
        setBanco("farmatads");
        setUrl("jdbc:mysql://localhost");
        setPorta("3306");
        setLogin("root");
        setSenha("");
    }
    public void conectar ()throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url +":"+porta+"/"+banco, login, senha);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe não encontrada");
        } 
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public static Conexao getInstance(){
        if (conexao == null){
            conexao = new Conexao();
        }
        return conexao;
    }
}
