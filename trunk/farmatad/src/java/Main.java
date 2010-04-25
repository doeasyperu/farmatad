
import br.com.dao.ClienteDao;
import br.com.dao.Conexao;
import br.com.dao.FornecedorDao;
import br.com.dao.FuncionarioDao;
import br.com.dao.ProdutoDao;
import br.entity.Cliente;
import br.entity.Fornecedor;
import br.entity.Funcionario;
import br.entity.Produto;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marcio
 */
public class Main {

    public static void main(String[] args) {
       
        ClienteDao cd = new ClienteDao();
        
        
        Logger.getLogger(Produto.class.getName()).info("nome " +cd.findRange(new int[]{0,15}));
        System.exit(0);
    }
}
