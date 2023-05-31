package dao;

import model.Curso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CursoDAO extends DAO {	
	public CursoDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert (Curso curso) {
		boolean status = false;
		try {
			String sql = "INSERT INTO curso (titulo, descricao, idioma, imagem, quiz, dur_horas, dur_minutos, id_usuario) "
		               + "VALUES ('" + curso.getTitulo() + "', '"
		               + curso.getDescricao() + "', '" + curso.getIdioma() + "', '" + curso.getImagem() + "', '" + curso.getQuiz() + "', " + curso.getHoras() + ", " + curso.getMinutos() + ", " + curso.getAutor() + ");";
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

	
	public Curso get (int id) {
		Curso curso = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM curso WHERE id = " + id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 curso = new Curso(rs.getInt("id"),
	        			 				 rs.getString("titulo"), 
	        			 			   	 rs.getString("descricao"), 
	        			 			   	 rs.getString("idioma"),
	        			 			   	 rs.getString("imagem"),
	        			 			   	 rs.getString("quiz"),
	        			 			   	 rs.getInt("dur_horas"),
	        			 			   	 rs.getInt("dur_minutos"),
	        			 			   	 rs.getInt("id_usuario"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return curso;
	}
	
	
	public List<Curso> get() {
		return get("");
	}

	
	public List<Curso> getOrderById() {
		return get("id");
	}
	
	
	public List<Curso> getOrderByTitulo() {
		return get("titulo");		
	}
		
	
	private List<Curso> get (String orderBy) {
		List<Curso> cursos = new ArrayList<Curso>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM curso" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Curso u = new Curso(rs.getInt("id"),
					 				 rs.getString("titulo"), 
					 			   	 rs.getString("descricao"), 
					 			   	 rs.getString("idioma"),
					 			   	 rs.getString("imagem"),
					 			   	 rs.getString("quiz"),
					 			   	 rs.getInt("dur_horas"),
					 			   	 rs.getInt("dur_minutos"),
					 			   	 rs.getInt("id_usuario"));
	            cursos.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return cursos;
	}
	
	
	// Função para filtrar o idioma do CURSO
	public List<Curso> getIdioma (String idioma) {
		List<Curso> cursos = new ArrayList<Curso>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM curso" + ((idioma.trim().length() == 0) ? "" : (" WHERE idioma = '" + idioma + "'")) + " ORDER BY id DESC";
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Curso u = new Curso(rs.getInt("id"),
					 				 rs.getString("titulo"), 
					 			   	 rs.getString("descricao"), 
					 			   	 rs.getString("idioma"),
					 			   	 rs.getString("imagem"),
					 			   	 rs.getString("quiz"),
					 			   	 rs.getInt("dur_horas"),
					 			   	 rs.getInt("dur_minutos"),
					 			   	 rs.getInt("id_usuario"));
	            cursos.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return cursos;
	}
	
	
	
	// Função para filtrar o CURSO de acordo com o usuário que o criou
	public List<Curso> getAutorCurso (int id_usuario) {
		List<Curso> cursos = new ArrayList<Curso>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM curso WHERE id_usuario = " + id_usuario + " ORDER BY id DESC";
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Curso u = new Curso(rs.getInt("id"),
					 				 rs.getString("titulo"), 
					 			   	 rs.getString("descricao"), 
					 			   	 rs.getString("idioma"),
					 			   	 rs.getString("imagem"),
					 			   	 rs.getString("quiz"),
					 			   	 rs.getInt("dur_horas"),
					 			   	 rs.getInt("dur_minutos"),
					 			   	 rs.getInt("id_usuario"));
	            cursos.add(u);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return cursos;
	}
	
	
	
	public boolean update (Curso curso) {
		boolean status = false;
		try {
			String sql = "UPDATE curso SET titulo = '" + curso.getTitulo() + "', "
					   + "descricao = '" + curso.getDescricao() + "', "
					   + "idioma = '" + curso.getIdioma() + "', " 
					   + "quiz = '" + curso.getQuiz() + "', "
					   + "dur_horas = " + curso.getHoras() + ", "
					   + "dur_minutos = " + curso.getMinutos() + " WHERE id = " + curso.getId();
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
			st.executeUpdate("DELETE FROM curso WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		
		return status;
	}	
}