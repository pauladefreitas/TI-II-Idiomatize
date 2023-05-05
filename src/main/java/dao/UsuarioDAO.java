package dao;

import model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO extends DAO {	
	public UsuarioDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert (Usuario usuario) {
		boolean status = false;
		try {
			String sql = "INSERT INTO usuario (nome, login, email, senha, fluencia, imagem) "
		               + "VALUES ('" + usuario.getNome() + "', '"
		               + usuario.getLogin() + "', '" + usuario.getEmail() + "', '" + usuario.getSenha() + "', '" + usuario.getFluencia() + "', '" + usuario.getImagem() + "');";
			PreparedStatement st = conexao.prepareStatement(sql);
			System.out.println(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Usuario get (int id) {
		Usuario usuario = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario WHERE id = " + id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 usuario = new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), 
	        			 			   rs.getString("email"), 
	        			 			   rs.getString("senha"),
	        			 			   rs.getString("fluencia"),
	        			 			   rs.getString("imagem"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return usuario;
	}
	
	
	// Recuperar o usuário autenticado corretamente
	public Usuario get (String login, String senha) {
		Usuario usuario = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario WHERE login LIKE '" + login + "' AND senha LIKE '" + senha  + "'";
			ResultSet rs = st.executeQuery(sql);	
			if(rs.next()){            
				usuario = new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), 
						rs.getString("email"), 
						rs.getString("senha"),
						rs.getString("fluencia"),
						rs.getString("imagem"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return usuario;
	}
	
	
	public List<Usuario> get() {
		return get("");
	}

	
	public List<Usuario> getOrderById() {
		return get("id");
	}
	
	
	public List<Usuario> getOrderByNome() {
		return get("nome");		
	}
	
	
	public List<Usuario> getOrderByLogin() {
		return get("login");		
	}
	
	
	private List<Usuario> get (String orderBy) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Usuario u = new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("login"), 
						 			   	rs.getString("email"), 
						 			   	rs.getString("senha"),
						 			   	rs.getString("fluencia"),
						 			   	rs.getString("imagem"));
	            usuarios.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return usuarios;
	}
	
	
	public boolean update (Usuario usuario) {
		boolean status = false;
		try {
			String sql = "UPDATE usuario SET nome = '" + usuario.getNome() + "', "
					   + "login = '" + usuario.getLogin() + "', " 
					   + "email = '" + usuario.getEmail() + "',"
					   + "senha = '" + usuario.getSenha() + "'," 
					   + "fluencia = '" + usuario.getFluencia() + "' WHERE id = " + usuario.getId();
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		
		return status;
	}
	
	
	public boolean delete (int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM usuario WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		
		return status;
	}
	
	
	public boolean autenticar(String login, String senha) {
		boolean resp = false;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM usuario WHERE login LIKE '" + login + "' AND senha LIKE '" + senha  + "'";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			resp = rs.next();
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return resp;
	}	
}