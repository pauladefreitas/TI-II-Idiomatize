package service;

import java.util.Scanner;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import dao.DAO;
import dao.UsuarioDAO;
import dao.PostagemDAO;
import dao.CursoDAO;
import model.Usuario;
import model.Postagem;
import model.Curso;
import spark.Request;
import spark.Response;


public class UsuarioService {

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private String usuarioForm;
	private String usuarioPerfil;
	
	
	
	public Object insert(Request request, Response response) throws Exception {
		String nome = request.queryParams("txt_nome");
		String login = request.queryParams("txt_login");
		String email = request.queryParams("txt_email");
		String senha = DAO.toMD5(request.queryParams("txt_senha"));
		String fluencia = request.queryParams("txt_idioma");
		String imagem = request.queryParams("img_perfil");
		boolean premium = false;
		
		String resp = "";
		
		Usuario usuario = new Usuario(0, nome, login, email, senha, fluencia, imagem, premium);
		
		if(usuarioDAO.insert(usuario) == true) {
            resp = "Usuário (" + login + ") cadastrado!";
            response.status(302);
            response.header("Location", "http://localhost:6789/login.html");
		} else {
			resp = "Usuário (" + login + ") não cadastrado!";
			response.status(404); // 404 Not found
		}
			
		return resp;
	}

	
	
	// Solicitar o perfil
	public Object get (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Usuario usuario = (Usuario) usuarioDAO.get(id);
		
		if (usuario != null) {
			response.status(200); // Success
			exibirPerfil(usuario);
        } else {
            response.status(404); // 404 Not Found   
        }

		return usuarioPerfil;
	}
	
	
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Usuario usuario = (Usuario) usuarioDAO.get(id);
		
		if (usuario != null) {
			response.status(200); // Success
			// makeForm(FORM_UPDATE, usuario, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
    		// makeForm();
    		// form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return usuarioForm; // RETORNO TESTE
	}
	
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		// makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
	    
		return usuarioForm;
	}
			
	
	
	public Object update(Request request, Response response) throws Exception {
        int id = Integer.parseInt(request.params(":id"));
		Usuario usuario = usuarioDAO.get(id);
        String resp = "";       

        if (usuario != null) {
        	usuario.setNome(request.queryParams("txt_nome"));
        	usuario.setLogin(request.queryParams("txt_login"));
        	usuario.setEmail(request.queryParams("txt_email"));
        	usuario.setSenha(DAO.toMD5(request.queryParams("txt_senha")));
        	usuario.setFluencia(request.queryParams("txt_idioma"));
        	
        	usuarioDAO.update(usuario);

        	response.status(302);
            response.header("Location", "http://localhost:6789/index.html");
        } else {
            response.status(404); // 404 Not found
        }
		
		return resp;
	}
	
	
	public Object updatePremium(Request request, Response response) throws Exception {
        int id = Integer.parseInt(request.params(":id"));
		Usuario usuario = usuarioDAO.get(id);
        String resp = "";       

        if (usuario != null) {
        	
        	usuarioDAO.updatePremium(usuario);

        	response.status(302);
            response.header("Location", "http://localhost:6789/index.html");
        } else {
            response.status(404); // 404 Not found
        }
		
		return resp;
	}
	
	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);
        String resp = "";       

        if (usuario != null) {
            usuarioDAO.delete(id);
            resp = "Usuário (" + id + ") excluído!";
            response.status(302);
            response.header("Location", "http://localhost:6789/index.html");
        } else {
            response.status(404); // 404 Not found
            resp = "Usuário (" + id + ") não encontrado!";
        }
		return resp;
	}
	
	
	public String autenticar(Request request, Response response) throws Exception {
		String username = request.queryParams("username");
		String senha = DAO.toMD5(request.queryParams("senha"));
		
		if(usuarioDAO.autenticar(username, senha) == true) {
			Usuario usuario = usuarioDAO.get(username, senha);
			
			// Retorna o JSON que será usado para inserir o USUÁRIO na Session Storage
			return "{\"id\": " + usuario.getId() + ", \"login\": \"" + usuario.getLogin() + "\", \"imagem\": \"" + usuario.getImagem() + "\", \"nome\": \"" + usuario.getNome() + "\", \"email\": \"" + usuario.getEmail() + "\", \"premium\": " + usuario.getPremium() + "}";
		} else {
			return "";
		}
	}
	
	
	
	// FIM DO CRUD
	
	
	
	// Exibir o perfil do USUÁRIO
	public void exibirPerfil(Usuario usuario) {
		String nomeArquivo = "./src/main/resources/public/perfil.html";
		usuarioPerfil = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while(entrada.hasNext()){
				usuarioPerfil += (entrada.nextLine() + "\n");
			}
			entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		
		String list = new String();
		String statusUsuario = new String();
		String opcaoPremium = new String();
		
		if(usuario.getPremium()) {
			statusUsuario = "<button type=\"button\" class=\"btn btn-warning btn-sm\"><i class=\"fa-solid fa-user-graduate\"></i> Usuário Premium</button>";
			opcaoPremium = "";
		} else {
			statusUsuario = "<button type=\"button\" class=\"btn btn-secondary btn-sm\"><i class=\"fa-solid fa-user\"></i> Usuário Gratuito</button>";
			opcaoPremium = "<li><a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalPremium\"><i class=\"fa-solid fa-coins\"></i> Virar Premium</a></li>\n";
		}
		
		// Construir a página de perfil
		list += "<div class=\"row principal__informacoes-usuario\">\n" +
				"<div class=\"col-3 principal__informacoes-usuario--foto-perfil\">\n" +
				"<img src=\"http://localhost:6789/imgs/perfil/" + usuario.getImagem() + "\" alt=\"Foto de perfil do usuário\">\n" +
				"</div>\n" +
				"<div class=\"col-9 principal__informacoes-usuario--descricao\">\n" +
				"<div class=\"principal__informacoes-usuario--descricao-identidade\">\n" +
				"<h1 id=\"nomeAtualizadoUsuario\">" + usuario.getNome() + "</h1>\n" +
				"<p>@" + usuario.getLogin() + "</p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-estatisticas\">\n" +
				"<p>\n" +
				"<i class=\"fa-solid fa-file-pen fa-lg\"></i> <!-- CONTADOR DE POSTS -->\n" +
				"<i class=\"fa-solid fa-graduation-cap fa-lg\"></i> <!-- CONTADOR DE CURSOS -->\n" +
				"</p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-premium\" id=\"statusUsuario\">\n" +
				"" + statusUsuario + "\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-fluencia\">\n" +
				"<h6>Fluente em:</h6>\n" +
				"<p><img src=\"http://localhost:6789/imgs/idioma-" + usuario.getFluencia() + ".png\" alt=\"Bandeira idioma\"></p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-contato\">\n" +
				"<p><a href=\"mailto:" + usuario.getEmail() + "\"><i class=\"fa-solid fa-envelope fa-lg\"></i> Contato</a></p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-configuracoes\" id=\"configuracoesUsuario\">\n" +
				
				
				//Dropdown
				"<div class=\"dropdown\">\n" +
				"<button class=\"btn btn-sm btn-info dropdown-toggle\" type=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\n" +
				"<i class=\"fa-solid fa-gear\"></i>\n" +
				"</button>\n" +
				"<ul class=\"dropdown-menu dropdown-menu-end\">" +
				"<li><a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalUpdate\"><i class=\"fa-solid fa-pencil\"></i> Editar Perfil</a></li>\n" +
				"" + opcaoPremium + "\n" +
				"<li><a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalDelete\"><i class=\"fa-solid fa-trash-can\"></i> Deletar Perfil</a></li>\n" +
				"</ul>\n" +
				"</div>\n" +
				
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				
				
				// Modal USUÁRIO Deletar
				"<div class=\"modal fade\" id=\"modalDelete\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
				"<div class=\"modal-dialog modal-dialog-centered\">\n" +
				"<div class=\"modal-content text-center\">\n" +
				"<div class=\"modal-header\">\n" +
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
				"</div>\n" +
				"<div class=\"modal-body\">\n" +
				"<p>Você está prestes a deletar o seu perfil permanentemente.</p>\n" +
				"<p>Você tem certeza mesmo disso?</p>\n" +
				"</div>\n" +
				"<div class=\"modal-footer\">\n" +
				"<button type=\"button\" class=\"btn btn-secondary btn-sm\" data-bs-dismiss=\"modal\">Cancelar</button>\n" +
				"<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"location.href='http://localhost:6789/usuario/delete/" + usuario.getId() + "';\">Deletar</button>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				
				
				// Modal USUÁRIO Update
				"<div class=\"modal fade\" id=\"modalUpdate\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
				"<div class=\"modal-dialog modal-dialog-centered\">\n" +
				"<div class=\"modal-content\">\n" +
				"<div class=\"modal-header\">\n" +
				"<h1 class=\"modal-title fs-5\" id=\"exampleModalLabel\">Editar Perfil</h1>\n" +
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
				"</div>\n" +
				"<div class=\"modal-body\">\n" +
				"<form id=\"update-user-form\" class=\"form\" action=\"/usuario/update/" + usuario.getId() + "\" method=\"post\">\n" +
				"<div class=\"text-center\">\n" +
				"<i class=\"fa-solid fa-user-pen fa-lg\"></i>\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"nome\">Nome:</label><br>\n" +
				"<input type=\"text\" name=\"txt_nome\" id=\"txt_nome\" class=\"form-control\" value=\"" + usuario.getNome() + "\">\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"login\">Usuário:</label><br>\n" +
				"<input type=\"text\" name=\"txt_login\" id=\"txt_login\" class=\"form-control\" value=\"" + usuario.getLogin() + "\">\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"email\">Email:</label><br>\n" +
				"<input type=\"text\" name=\"txt_email\" id=\"txt_email\" class=\"form-control\" value=\"" + usuario.getEmail() + "\">\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"senha\">Senha:</label>\n" +
				"<div>\n" +
				"<input type=\"password\" name=\"txt_senha\" id=\"txt_senha\" class=\"form-control\" value=\"\">\n" +
				"<i class=\"fa-solid fa-eye fa-lg\" onclick=\"mostrarSenha()\"></i>" +
				"</div>\n" +
				"</div>\n" +
				"<div class=\"form-group form-group-idioma\">\n" +
				"<label for=\"idioma\">Fluente em:</label>\n" +
				"<select id=\"txt_idioma\" name=\"txt_idioma\">\n" +
				"<option value=\"portugues\" selected>🇧🇷 Português</option>\n" +
				"<option value=\"ingles\">🇺🇸 Inglês</option>\n" +
				"<option value=\"espanhol\">🇪🇸 Espanhol</option>\n" +
				"<option value=\"frances\">🇫🇷 Francês</option>\n" +
				"<option value=\"alemao\">🇩🇪 Alemão</option>\n" +
				"<option value=\"mandarim\">🇨🇳 Mandarim</option>\n" +
				"</select>\n" +
				"</div>\n" +
				"<div class=\"modal-footer\">\n" +
				"<button type=\"submit\" id=\"btn_salvar\" class=\"btn btn-primary btn-sm\" disabled=\"disabled\">Atualizar Perfil</button>\n" +
				"</div>\n" +
				"</form>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				
				
				// Modal USUÁRIO Premium
				"<div class=\"modal fade\" id=\"modalPremium\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
				"<div class=\"modal-dialog modal-dialog-centered\">\n" +
				"<div class=\"modal-content\">\n" +
				"<div class=\"modal-header\">\n" +
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
				"</div>\n" +
				"<div class=\"modal-body\">\n" +
				"<form id=\"cad-user-form\" class=\"form\" action=\"/usuario/premium/" + usuario.getId() + "\" method=\"post\" onsubmit=\"myFunction(event)\">\n" +
				"<div class=\"text-center\">\n" +
				"<i class=\"fa-solid fa-lock fa-lg\"></i>\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"nome\">Número do Cartão:</label><br>\n" +
				"<input type=\"text\" name=\"credit_numero\" id=\"credit_numero\" class=\"form-control creditCardNumero\" placeholder=\"0000 0000 0000 0000\" maxlength=\"19\">\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"login\">Nome:</label><br>\n" +
				"<input type=\"text\" name=\"credit_nome\" id=\"credit_nome\" class=\"form-control\" placeholder=\"Nome impresso no cartão\">\n" +
				"</div>\n" +
				"<div class=\"form-credit-infos\">\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"email\">Validade:</label><br>\n" +
				"<input type=\"text\" name=\"credit_validade\" id=\"credit_validade\" class=\"form-control creditCardValidade\" placeholder=\"00/00\" maxlength=\"5\">\n" +
				"</div>\n" +
				"<div class=\"form-group\">\n" +
				"<label for=\"senha\">CVV:</label>\n" +
				"<input type=\"text\" name=\"credit_cvv\" id=\"credit_cvv\" class=\"form-control\" placeholder=\"000\" maxlength=\"3\">\n" +
				"</div>\n" +
				"</div>\n" +
				"<div class=\"modal-footer\">\n" +
				"<button type=\"submit\" class=\"btn btn-warning btn-sm\">Virar Premium</button>\n" +
				"</div>\n" +
				"</form>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n";
		
			
		usuarioPerfil = usuarioPerfil.replaceFirst("<!-- PERFIL -->", list);
		
		
		// Construir o feed de POSTAGENS do usuário
		String postagensUsuario = new String();
		PostagemDAO postagemDAO = new PostagemDAO();
		int contadorPostagens = 0;
		
		List<Postagem> postagens = postagemDAO.getAutorPostagem(usuario.getId()); // Pegar a postagem pelo id do usuário e arrumar em ordem decrescente
		
		for (Postagem u : postagens) {
			contadorPostagens++;
			
			postagensUsuario += "<div class=\"row principal__postagens--item\">\n" + 
								"<div class=\"col-5 col-sm-2 principal__postagens--imagem\">\n" +
								"<img src=\"http://localhost:6789/imgs/postagem/" + u.getImagem() + "\" alt=\"Imagem ilustrando a postagem\">\n" +
								"<img src=\"http://localhost:6789/imgs/postagem/" + u.getImagem() + "\" alt=\"Imagem ilustrando a postagem\">\n" +
								"</div>\n" +
								"<div class=\"col-7 col-sm-10 principal__postagens--descricao\">\n" +
								"<div class=\"principal__postagens--descricao-autoria\">\n" +
								"<img src=\"http://localhost:6789/imgs/perfil/" + usuario.getImagem() + "\" alt=\"Foto de perfil\">\n" +
								"<a href=\"http://localhost:6789/usuario/" + usuario.getId() + "\">\n" +
								"<p>" + usuario.getNome() + "</p>\n" +
								"</a>\n" +
								"</div>\n" +
								"<div class=\"principal__postagens--descricao-titulos\">\n" +
								"<a href=\"http://localhost:6789/postagem/" + u.getId() + "\">\n" +
								"<h4>" + u.getTitulo() + "</h4>\n" +
								"</a>\n" +
								"<p>" + u.getSubtitulo() + "</p>\n" +
								"</div>\n" +
								"<div class=\"principal__postagens--descricao-estatisticas\">\n" +
								"<p>" + u.getPublicacao() + " • <img src=\"http://localhost:6789/imgs/idioma-" + u.getIdioma() + ".png\" alt=\"Bandeira idioma\"></p>\n" +
								"</div>\n" +
								
								"<div class=\"principal__postagens--descricao-configuracoes\" id=\"configuracoesPostagem\">\n" +
								
								// Dropdown
								"<div class=\"dropdown\">\n" +
								"<button class=\"btn btn-sm btn-secondary dropdown-toggle\" type=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\n" +
								"<i class=\"fa-solid fa-gear\"></i>\n" +
								"</button>\n" +
								"<ul class=\"dropdown-menu dropdown-menu-end\">\n" +
								"<li><a href=\"http://localhost:6789/postagem/update/" + u.getId() + "\"><i class=\"fa-solid fa-pencil\"></i> Editar Post</a></li>\n" +
								"<li><a href=\"http://localhost:6789/postagem/" + u.getId() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i> Ver Post</a></li>\n" +
								"<li><a href=\"http://localhost:6789/postagem/delete/" + u.getId() + "\"><i class=\"fa-solid fa-trash-can\"></i> Excluir Post</a></li>\n" +
								"</ul>\n" +
								"</div>\n" +
								
								"</div>\n" +
								"</div>\n" +
								"</div>\n";
		}
		
		if(contadorPostagens > 0) {
			usuarioPerfil = usuarioPerfil.replaceFirst("<!-- POSTAGENS USUÁRIO -->", postagensUsuario);
		} else {
			String nenhumaPostagem = new String();
			
			nenhumaPostagem += "<div class=\"principal__postagens--erro text-center\">\n" +
							   "<img src=\"http://localhost:6789/imgs/error-404.png\" alt=\"Não encontrado\">\n" +
							   "<p>" + usuario.getNome() + " ainda não criou nenhuma postagem!</p>\n" +
							   "</div>";
			
			usuarioPerfil = usuarioPerfil.replaceFirst("<!-- POSTAGENS USUÁRIO -->", nenhumaPostagem);
		}
		
		usuarioPerfil = usuarioPerfil.replaceFirst("<!-- CONTADOR DE POSTS -->", "" + contadorPostagens);
		
		
		// Construir o feed de CURSOS do usuário
		String cursosUsuario = new String();
		CursoDAO cursoDAO = new CursoDAO();
		int contadorCursos = 0;
		
		List<Curso> cursos = cursoDAO.getAutorCurso(usuario.getId()); // Pegar o curso pelo id do usuário e arrumar em ordem decrescente
		
		for (Curso u : cursos) {
			contadorCursos++;
			
			cursosUsuario += "<div class=\"col-12 col-sm-6 col-md-4 col-lg-3\">\n" + 
								"<div class=\"principal__cursos-item\">\n" +
								"<div class=\"principal__cursos-item--imagem\">\n" +
								"<img src=\"http://localhost:6789/imgs/curso/" + u.getImagem() + "\" alt=\"Imagem do curso\">\n" +
								"</div>\n" +
								"<div class=\"principal__cursos-item--corpo\">\n" +
								"<div class=\"principal__cursos-item--corpo-autoria\">\n" +
								"<img src=\"http://localhost:6789/imgs/perfil/" + usuario.getImagem() + "\" alt=\"Imagem do curso\">\n" +
								"<a href=\"http://localhost:6789/usuario/" + usuario.getId() + "\">\n" +
								"<p>" + usuario.getNome() + "</p>\n" +
								"</a>\n" +
								"</div>\n" +
								"<h6>" + u.getTitulo() + "</h6>\n" +
								"</div>\n" +
								
								"<div class=\"principal__cursos--descricao-configuracoes\" id=\"configuracoesPostagem\">\n" +
								
								// Dropdown
								"<div class=\"dropdown\">\n" +
								"<button class=\"btn btn-sm btn-light dropdown-toggle\" type=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\n" +
								"<i class=\"fa-solid fa-gear\"></i>\n" +
								"</button>\n" +
								"<ul class=\"dropdown-menu dropdown-menu-end\">\n" +
								"<li><a href=\"http://localhost:6789/curso/update/" + u.getId() + "\"><i class=\"fa-solid fa-pencil\"></i> Editar Curso</a></li>\n" +
								"<li><a href=\"http://localhost:6789/curso/" + u.getId() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i> Ver Curso</a></li>\n" +
								"<li><a href=\"http://localhost:6789/curso/delete/" + u.getId() + "\"><i class=\"fa-solid fa-trash-can\"></i> Excluir Curso</a></li>\n" +
								"</ul>\n" +
								"</div>\n" +
								
								"</div>\n" +
								"</div>\n" +
								"</div>\n";
		}
		
		if(contadorCursos > 0) {
			usuarioPerfil = usuarioPerfil.replaceFirst("<!-- CURSOS USUÁRIO -->", cursosUsuario);
		} else {
			String nenhumCurso = new String();
			
			nenhumCurso += "<div class=\"principal__cursos-criados--erro text-center\">\n" +
						   "<img src=\"http://localhost:6789/imgs/error-404.png\" alt=\"Não encontrado\">\n" +
						   "<p>" + usuario.getNome() + " ainda não criou nenhum curso!</p>\n" +
						   "</div>";
			
			usuarioPerfil = usuarioPerfil.replaceFirst("<!-- CURSOS USUÁRIO -->", nenhumCurso);
		}
		
		usuarioPerfil = usuarioPerfil.replaceFirst("<!-- CONTADOR DE CURSOS -->", "" + contadorCursos);
	}
}