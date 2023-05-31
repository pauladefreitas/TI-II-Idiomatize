package dao;

import model.Postagem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PostagemDAO extends DAO {	
	public PostagemDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert (Postagem postagem) {
		boolean status = false;
		try {
			String sql = "INSERT INTO postagem (titulo, subtitulo, descricao, idioma, texto, publicacao, imagem, id_usuario) "
		               + "VALUES ('" + postagem.getTitulo() + "', '"
		               + postagem.getSubtitulo() + "', '" + postagem.getDescricao() + "', '" + postagem.getIdioma() + "', '" + postagem.getTexto() + "', '" + postagem.getPublicacao() + "', '" + postagem.getImagem() + "', " + postagem.getAutor() + ");";
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

	
	public Postagem get (int id) {
		Postagem postagem = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM postagem WHERE id = " + id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 postagem = new Postagem(rs.getInt("id"), rs.getString("titulo"), rs.getString("subtitulo"), 
	        			 			   	 rs.getString("descricao"), 
	        			 			   	 rs.getString("idioma"),
	        			 			   	 rs.getString("texto"),
	        			 			   	 rs.getDate("publicacao").toLocalDate(),
	        			 			   	 rs.getString("imagem"),
	        			 			   	 rs.getInt("id_usuario"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return postagem;
	}
	
	
	public List<Postagem> get() {
		return get("");
	}

	
	public List<Postagem> getOrderById() {
		return get("id");
	}
	
	
	public List<Postagem> getOrderByTitulo() {
		return get("titulo");		
	}
	
	
	public List<Postagem> getOrderByPublicacao() {
		return get("publicacao");		
	}
	
	
	private List<Postagem> get (String orderBy) {
		List<Postagem> postagens = new ArrayList<Postagem>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM postagem" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Postagem u = new Postagem(rs.getInt("id"), rs.getString("titulo"), rs.getString("subtitulo"), 
							 			  rs.getString("descricao"), 
							 			  rs.getString("idioma"),
							 			  rs.getString("texto"),
							 			  rs.getDate("publicacao").toLocalDate(),
							 			  rs.getString("imagem"),
		        			 			  rs.getInt("id_usuario"));
	            postagens.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return postagens;
	}
	
	
	// Função para filtrar o idioma da POSTAGEM
	public List<Postagem> getIdioma (String idioma) {
		List<Postagem> postagens = new ArrayList<Postagem>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM postagem" + ((idioma.trim().length() == 0) ? "" : (" WHERE idioma = '" + idioma + "'")) + " ORDER BY id DESC";
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Postagem u = new Postagem(rs.getInt("id"), rs.getString("titulo"), rs.getString("subtitulo"), 
							 			  rs.getString("descricao"), 
							 			  rs.getString("idioma"),
							 			  rs.getString("texto"),
							 			  rs.getDate("publicacao").toLocalDate(),
							 			  rs.getString("imagem"),
		        			 			  rs.getInt("id_usuario"));
	            postagens.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return postagens;
	}
	
	
	
	// Função para filtrar a postagem de acordo com o usuário que a criou
	public List<Postagem> getAutorPostagem (int id_usuario) {
		List<Postagem> postagens = new ArrayList<Postagem>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM postagem WHERE id_usuario = " + id_usuario + " ORDER BY id DESC";
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Postagem u = new Postagem(rs.getInt("id"), rs.getString("titulo"), rs.getString("subtitulo"), 
							 			  rs.getString("descricao"), 
							 			  rs.getString("idioma"),
							 			  rs.getString("texto"),
							 			  rs.getDate("publicacao").toLocalDate(),
							 			  rs.getString("imagem"),
		        			 			  rs.getInt("id_usuario"));
	            postagens.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return postagens;
	}
	
	
	
	public boolean update (Postagem postagem) {
		boolean status = false;
		try {
			String sql = "UPDATE postagem SET titulo = '" + postagem.getTitulo() + "', "
					   + "subtitulo = '" + postagem.getSubtitulo() + "', " 
					   + "descricao = '" + postagem.getDescricao() + "',"
					   + "idioma = '" + postagem.getIdioma() + "'," 
					   + "texto = '" + postagem.getTexto() + "' WHERE id = " + postagem.getId();
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
			st.executeUpdate("DELETE FROM postagem WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		
		return status;
	}	
}