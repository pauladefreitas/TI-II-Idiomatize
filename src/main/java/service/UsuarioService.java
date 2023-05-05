package service;

import java.util.Scanner;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import dao.DAO;
import dao.UsuarioDAO;
import dao.PostagemDAO;
import model.Postagem;
import model.Usuario;
import spark.Request;
import spark.Response;


public class UsuarioService {

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private String usuarioForm;
	private String usuarioPerfil;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_NOME = 2;
	private final int FORM_ORDERBY_LOGIN = 3;
	
	public UsuarioService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Usuario(), FORM_ORDERBY_NOME);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Usuario(), orderBy);
	}

	
	public void makeForm(int tipo, Usuario usuario, int orderBy) {
		String nomeArquivo = "./src/main/resources/public/administrador.html";
		usuarioForm = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	usuarioForm += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umLivro = "";
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/usuario/";
			String name, titulo, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Novo Usuário";
				titulo = "";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + usuario.getId();
				name = "Atualizar Usuário (Código " + usuario.getId() + ")";
				titulo = usuario.getNome();
				buttonLabel = "Atualizar";
			}
			umLivro += "<form class=\"form__usuario principal__form-caixa\" action=\""+ action +"\" method=\"post\" id=\"form-add\">";
			umLivro += "<div class=\"text-center\">";
			umLivro += "<p>" + name + "</p>";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form__usuario--campos\">";
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"nome\">Nome:</label><br>";
			umLivro += "<input type=\"text\" name=\"txt_nome\" id=\"txt_nome\" class=\"form-control\" placeholder=\"Nome da Pessoa\" value=\"" + titulo + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"login\">Usuário:</label><br>";
			umLivro += "<input type=\"text\" name=\"txt_login\" id=\"txt_login\" class=\"form-control\" placeholder=\"Usuário\" value=\""+ usuario.getLogin() +"\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"email\">Email:</label><br>";
			umLivro += "<input type=\"text\" name=\"txt_email\" id=\"txt_email\" class=\"form-control\" placeholder=\"exemplo@email.com\" value=\"" + usuario.getEmail() + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"senha\">Senha:</label><br>";
			umLivro += "<input type=\"password\" name=\"txt_senha\" id=\"txt_senha\" class=\"form-control\" placeholder=\"\" value=\"" + usuario.getSenha() + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group form-group-idioma\">";
			umLivro += "<label for=\"idioma\">Fluência:</label>";
			umLivro += "<select id=\"txt_idioma\" name=\"txt_idioma\">";
			umLivro += "<option disabled selected value>Idioma</option>";
			umLivro += "<option value=\"portugues\">🇧🇷 Português</option>";
			umLivro += "<option value=\"ingles\">🇺🇸 Inglês</option>";
			umLivro += "<option value=\"espanhol\">🇪🇸 Espanhol</option>";
			umLivro += "<option value=\"frances\">🇫🇷 Francês</option>";
			umLivro += "<option value=\"alemao\">🇩🇪 Alemão</option>";
			umLivro += "<option value=\"mandarim\">🇨🇳 Mandarim</option>";
			umLivro += "</select>";
			umLivro += "</div>";
			umLivro += "</div>";
			
			umLivro += "<div class=\"text-center\">";
			umLivro += "<input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input__enviar-form btn btn-info\">";
			umLivro += "</div>";
			umLivro += "</form>";
			
		} else if (tipo == FORM_DETAIL){
			umLivro += "\t<table class=\"principal__tabela--detalhes\" width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>Detalhar Livro (Código " + usuario.getId() + ")</b></font></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td>Título: "+ usuario.getNome() +"</td>";
			umLivro += "\t\t\t<td>Ano: "+ usuario.getLogin() +"</td>";
			umLivro += "\t\t\t<td>Autor: "+ usuario.getEmail() +"</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		usuarioForm = usuarioForm.replaceFirst("<!--UM-LIVRO-->", umLivro);
		
		String list = new String("<table class=\"principal__tabela--livros\" width=\"100%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"7\" align=\"center\"><font size=\"+2\"><b>Relação de Usuários</b></font></td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td align=\"center\"><a href=\"/usuario/list/" + FORM_ORDERBY_ID + "\"><b><i class=\"fa-solid fa-sort\"></i> Código</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/usuario/list/" + FORM_ORDERBY_NOME + "\"><b><i class=\"fa-solid fa-sort\"></i> Nome</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/usuario/list/" + FORM_ORDERBY_LOGIN + "\"><b><i class=\"fa-solid fa-sort\"></i> Usuário</b></td>\n" +
        		"\t<td align=\"center\"><b>Email</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Usuario> usuarios;
		if (orderBy == FORM_ORDERBY_ID) {               usuarios = usuarioDAO.getOrderById();
		} else if (orderBy == FORM_ORDERBY_NOME) {		usuarios = usuarioDAO.getOrderByNome();
		} else if (orderBy == FORM_ORDERBY_LOGIN) {			usuarios = usuarioDAO.getOrderByLogin();
		} else {											usuarios = usuarioDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Usuario u : usuarios) {
			bgcolor = (i++ % 2 == 0) ? "#bde6ee" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td width=\"150\" align=\"center\">" + u.getId() + "</td>\n" +
            		  "\t<td>" + u.getNome() + "</td>\n" +
            		  "\t<td>" + u.getLogin() + "</td>\n" +
            		  "\t<td align=\"center\">" + u.getEmail() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/" + u.getId() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/update/" + u.getId() + "\"><i class=\"fa-solid fa-pen-to-square\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/delete/" + u.getId() + "\"><i class=\"fa-solid fa-trash\"></i></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		usuarioForm = usuarioForm.replaceFirst("<!--LISTAR-LIVRO-->", list);		
	}
	
	public Object insert(Request request, Response response) throws Exception {
		String nome = request.queryParams("txt_nome");
		String login = request.queryParams("txt_login");
		String email = request.queryParams("txt_email");
		String senha = DAO.toMD5(request.queryParams("txt_senha"));
		String senha2 = request.queryParams("txt_senha2");
		String fluencia = request.queryParams("txt_idioma");
		String imagem = request.queryParams("img_perfil");
		
		
		String resp = "";
		
		Usuario usuario = new Usuario(0, nome, login, email, senha, fluencia, imagem);
		
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

	
	public Object get (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Usuario usuario = (Usuario) usuarioDAO.get(id);
		
		if (usuario != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, usuario, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
    		makeForm();
    		usuarioForm.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return usuarioForm;
	}
	
	
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Usuario usuario = (Usuario) usuarioDAO.get(id);
		
		if (usuario != null) {
			response.status(200); // success
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
		makeForm(orderBy);
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
        	//response.status(200); // success
            //resp = "Produto (ID " + usuario.getId() + ") atualizado!";
        	response.status(302);
            response.header("Location", "http://localhost:6789/usuario/perfil/" + usuario.getId());
        } else {
            response.status(404); // 404 Not found
        }
		// makeForm();
		return usuarioForm.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
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
			
			return "{\"id\": " + usuario.getId() + ", \"login\": \"" + usuario.getLogin() + "\", \"imagem\": \"" + usuario.getImagem() + "\", \"nome\": \"" + usuario.getNome() + "\", \"email\": \"" + usuario.getEmail() + "\"}";
		} else {
			return "";
		}
	}
	
	
	// Solicitar o perfil
	public Object getPerfil (Request request, Response response) {
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
	
	
	// Exibir o perfil do usuário
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
		
		// Construir a página de perfil
		list += "<div class=\"row principal__informacoes-usuario\">\n" +
				"<div class=\"col-3 principal__informacoes-usuario--foto-perfil\">\n" +
				"<img src=\"http://localhost:6789/imgs/" + usuario.getImagem() + "\" alt=\"Foto de perfil do usuário\">\n" +
				"</div>\n" +
				"<div class=\"col-9 principal__informacoes-usuario--descricao\">\n" +
				"<div class=\"principal__informacoes-usuario--descricao-identidade\">\n" +
				"<h1>" + usuario.getNome() + "</h1>\n" +
				"<p>@" + usuario.getLogin() + "</p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-estatisticas\">\n" +
				"<p>\n" +
				"<i class=\"fa-solid fa-file-pen fa-lg\"></i> <!-- CONTADOR DE POSTS -->\n" +
				"<i class=\"fa-solid fa-graduation-cap fa-lg\"></i> <!-- CONTADOR DE CURSOS -->\n" +
				"</p>\n" +
				"</div>\n" +
				"<div class=\"principal__informacoes-usuario--descricao-premium\">\n" +
				"<button type=\"button\" class=\"btn btn-warning btn-sm\"><i class=\"fa-solid fa-user-graduate\"></i> Usuário Premium</button>\n" +
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
				"<li><a href=\"#\"><i class=\"fa-solid fa-coins\"></i> Virar Premium</a></li>\n" +
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
				"<form id=\"cad-user-form\" class=\"form\" action=\"/usuario/update/" + usuario.getId() + "\" method=\"post\">\n" +
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
				"<input type=\"password\" name=\"txt_senha\" id=\"txt_senha\" class=\"form-control\" value=\"\">\n" +
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
				
				
				// Modal POSTAGEM Deletar
				"<div class=\"modal fade\" id=\"modalDeletePost\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
				"<div class=\"modal-dialog modal-dialog-centered\">\n" +
				"<div class=\"modal-content text-center\">\n" +
				"<div class=\"modal-header\">\n" +
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
				"</div>\n" +
				"<div class=\"modal-body\">\n" +
				"<p>Você está prestes a deletar a sua postagem permanentemente.</p>\n" +
				"<p>Você tem certeza mesmo disso?</p>\n" +
				"</div>\n" +
				"<div class=\"modal-footer\">\n" +
				"<button type=\"button\" class=\"btn btn-secondary btn-sm\" data-bs-dismiss=\"modal\">Cancelar</button>\n" +
				"<button type=\"button\" class=\"btn btn-danger btn-sm\" href=\"\">Deletar</button>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>";
		
			
		usuarioPerfil = usuarioPerfil.replaceFirst("<!-- PERFIL -->", list);
		
		
		// Construir o feed de postagens do usuário
		String postagensUsuario = new String();
		PostagemDAO postagemDAO = new PostagemDAO();
		int contadorPostagens = 0;
		
		List<Postagem> postagens = postagemDAO.getOrderById();
		
		for (Postagem u : postagens) {
			contadorPostagens++;
			
			postagensUsuario += "<div class=\"row principal__postagens--item\">\n" + 
								"<div class=\"col-5 col-sm-2 principal__postagens--imagem\">\n" +
								"<img src=\"http://localhost:6789/imgs/" + u.getImagem() + "\" alt=\"Imagem ilustrando a postagem\">\n" +
								"</div>\n" +
								"<div class=\"col-7 col-sm-10 principal__postagens--descricao\">\n" +
								"<div class=\"principal__postagens--descricao-autoria\">\n" +
								"<img src=\"http://localhost:6789/imgs/foto-perfil.png\" alt=\"Foto de perfil\">\n" +
								"<a href=\"perfil.html\">\n" +
								"<p>Nome do Usuário</p>\n" +
								"</a>\n" +
								"</div>\n" +
								"<div class=\"principal__postagens--descricao-titulos\">\n" +
								"<a href=\"http://localhost:6789/postagem/feed/post/" + u.getId() + "\">\n" +
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
								"<li><a href=\"http://localhost:6789/postagem/feed/post/" + u.getId() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i> Ver Post</a></li>\n" +
								"<li><a href=\"#\" data-bs-toggle=\"modal\" data-bs-target=\"#modalDeletePost\"><i class=\"fa-solid fa-trash-can\"></i> Excluir Post</a></li>\n" +
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
		
		
		// Construir o feed de cursos do usuário
		int contadorCursos = 0;
		
		if(contadorCursos > 0) {
			
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