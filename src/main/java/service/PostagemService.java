package service;

import java.util.Scanner;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import dao.PostagemDAO;
import model.Postagem;
import spark.Request;
import spark.Response;


public class PostagemService {

	private PostagemDAO postagemDAO = new PostagemDAO();
	private String postagemForm;
	private String postagemUpdate;
	private String postagemFeed;
	private String postagemPagina;

	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_TITULO = 2;
	private final int FORM_ORDERBY_DATA = 3;
	
	public PostagemService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Postagem(), FORM_ORDERBY_TITULO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Postagem(), orderBy);
	}

	
	public void makeForm(int tipo, Postagem postagem, int orderBy) {
		String nomeArquivo = "./src/main/resources/public/administrador.html";
		postagemForm = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	postagemForm += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umLivro = "";
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/postagem/";
			String name, titulo, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Nova Postagem";
				titulo = "";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + postagem.getId();
				name = "Atualizar Postagem (Código " + postagem.getId() + ")";
				titulo = postagem.getTitulo();
				buttonLabel = "Atualizar";
			}
			umLivro += "<form class=\"form__usuario principal__form-caixa\" action=\""+ action +"\" method=\"post\" id=\"form-add\">";
			umLivro += "<div class=\"text-center\">";
			umLivro += "<p>" + name + "</p>";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form__usuario--campos\">";
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"nome\">Título:</label><br>";
			umLivro += "<input type=\"text\" name=\"titulo-postagem\" id=\"titulo-postagem\" class=\"form-control\" placeholder=\"Título\" value=\"" + titulo + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"login\">Subtítulo:</label><br>";
			umLivro += "<input type=\"text\" name=\"subtitulo-postagem\" id=\"subtitulo-postagem\" class=\"form-control\" placeholder=\"Usuário\" value=\""+ postagem.getSubtitulo() +"\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"email\">Descricao:</label><br>";
			umLivro += "<input type=\"text\" name=\"legenda-postagem\" id=\"legenda-postagem\" class=\"form-control\" placeholder=\"exemplo@email.com\" value=\"" + postagem.getDescricao() + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group\">";
			umLivro += "<label for=\"senha\">Texto:</label><br>";
			umLivro += "<input type=\"text\" name=\"texto-postagem\" id=\"texto-postagem\" class=\"form-control\" placeholder=\"\" value=\"" + postagem.getTexto() + "\">";
			umLivro += "</div>";
			
			umLivro += "<div class=\"form-group form-group-idioma\">";
			umLivro += "<label for=\"idioma\">Fluência:</label>";
			umLivro += "<select id=\"idioma-postagem\" name=\"idioma-postagem\">";
			umLivro += "<option disabled selected value>Idioma</option>";
			umLivro += "<option value=\"Português\">🇧🇷 Português</option>";
			umLivro += "<option value=\"Inglês\">🇺🇸 Inglês</option>";
			umLivro += "<option value=\"Espanhol\">🇪🇸 Espanhol</option>";
			umLivro += "<option value=\"Francês\">🇫🇷 Francês</option>";
			umLivro += "<option value=\"Alemão\">🇩🇪 Alemão</option>";
			umLivro += "<option value=\"Mandarim\">🇨🇳 Mandarim</option>";
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
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>Detalhar Livro (Código " + postagem.getId() + ")</b></font></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td>Título: "+ postagem.getTitulo() +"</td>";
			umLivro += "\t\t\t<td>Subtítulo: "+ postagem.getSubtitulo() +"</td>";
			umLivro += "\t\t\t<td>Idioma: "+ postagem.getIdioma() +"</td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t\t<tr>";
			umLivro += "\t\t\t<td></td>";
			umLivro += "\t\t</tr>";
			umLivro += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		postagemForm = postagemForm.replaceFirst("<!--UM-LIVRO-->", umLivro);
		
		String list = new String("<table class=\"principal__tabela--livros\" width=\"100%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"7\" align=\"center\"><font size=\"+2\"><b>Relação de Usuários</b></font></td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td align=\"center\"><a href=\"/postagem/list/" + FORM_ORDERBY_ID + "\"><b><i class=\"fa-solid fa-sort\"></i> Código</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/postagem/list/" + FORM_ORDERBY_TITULO + "\"><b><i class=\"fa-solid fa-sort\"></i> Título</b></a></td>\n" +
        		"\t<td align=\"center\"><a href=\"/postagem/list/" + FORM_ORDERBY_DATA + "\"><b><i class=\"fa-solid fa-sort\"></i> Data de Publicação</b></td>\n" +
        		"\t<td align=\"center\"><b>Idioma</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Postagem> postagens;
		if (orderBy == FORM_ORDERBY_ID) {               postagens = postagemDAO.getOrderById();
		} else if (orderBy == FORM_ORDERBY_TITULO) {		postagens = postagemDAO.getOrderByTitulo();
		} else if (orderBy == FORM_ORDERBY_DATA) {			postagens = postagemDAO.getOrderByPublicacao();
		} else {											postagens = postagemDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Postagem u : postagens) {
			bgcolor = (i++ % 2 == 0) ? "#bde6ee" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td width=\"150\" align=\"center\">" + u.getId() + "</td>\n" +
            		  "\t<td>" + u.getTitulo() + "</td>\n" +
            		  "\t<td align=\"center\">" + u.getPublicacao() + "</td>\n" +
            		  "\t<td align=\"center\">" + u.getIdioma() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/postagem/" + u.getId() + "\"><i class=\"fa-solid fa-magnifying-glass\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/postagem/update/" + u.getId() + "\"><i class=\"fa-solid fa-pen-to-square\"></i></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/postagem/delete/" + u.getId() + "\"><i class=\"fa-solid fa-trash\"></i></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		postagemForm = postagemForm.replaceFirst("<!--LISTAR-LIVRO-->", list);		
	}
	
	
	// EXIBIR FEED DE POSTAGENS	
	public void exibirFeed() {
		String nomeArquivo = "./src/main/resources/public/home.html";
		postagemFeed = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while(entrada.hasNext()){
				postagemFeed += (entrada.nextLine() + "\n");
			}
			entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		
		String list = new String();
		
		List<Postagem> postagens = postagemDAO.getOrderById();
		
		for (Postagem u : postagens) {
			
			list += "<div class=\"row principal__postagens--item\">\n" + 
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
					"</div>\n" +
					"</div>\n";
		}
			
		postagemFeed = postagemFeed.replaceFirst("<!-- POSTAGEM -->", list);	
	}
	
	// SOLICITAR DETALHES DO POST
	public Object detalhePostagem (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Postagem postagem = (Postagem) postagemDAO.get(id);
		
		if (postagem != null) {
			response.status(200); // success
			exibirPostagem(postagem);
        } else {
            response.status(404); // 404 Not found
        }

		return postagemPagina;
	}
	
	public void exibirPostagem(Postagem postagem) {
		String nomeArquivo = "./src/main/resources/public/post.html";
		postagemPagina = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while(entrada.hasNext()){
				postagemPagina += (entrada.nextLine() + "\n");
			}
			entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		
		String list = new String();
		
		// ALTERAR PARA APENAS O POST PASSADO
		list += "<div class=\"row\">\n" + 
				"<div class=\"col-12 principal__autoria\">\n" +
				"<div class=\"principal__autoria--imagem-perfil\">\n" +
				"<img src=\"http://localhost:6789/imgs/foto-perfil.png\" alt=\"Foto de perfil\">\n" +
				"</div>\n" +
				"<div class=\"principal__autoria--descricao\">\n" +
				"<h6>Nome do Usuário</h6>\n" +
				"<p>" + postagem.getPublicacao() + "</p>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"<div class=\"row\">\n" +
				"<div class=\"col-12 principal__postagem--titulo\">\n" +
				"<h1>" + postagem.getTitulo() + "</h1>\n" +
				"</div>\n" +
				"<div class=\"col-12 principal__postagem--subtitulo\">\n" +
				"<p>" + postagem.getSubtitulo() + "</p>\n" +
				"</div>\n" +
				"</div>\n" +
				"<div class=\"row\">\n" +
				"<div class=\"col-12 principal__postagem--imagem\">\n" +
				"<img src=\"http://localhost:6789/imgs/" + postagem.getImagem() + "\" alt=\"Imagem da postagem\">\n" +
				"</div>\n" +
				"<div class=\"col-12 principal__postagem--legenda text-center\">\n" +
				"<p>" + postagem.getDescricao() + "</p>\n" +
				"</div>\n" +
				"</div>\n" +
				"<div class=\"row\">\n" +
				"<div class=\"col-12 principal__postagem--texto\">\n" +
				"<p>" + postagem.getTexto() + "</p>\n" +
				"</div>\n" +
				"</div>\n";
		
			
		postagemPagina = postagemPagina.replaceFirst("<!-- POST -->", list);
	}
	
	public Object insert(Request request, Response response) {
		String titulo = request.queryParams("titulo-postagem");
		String subtitulo = request.queryParams("subtitulo-postagem");
		String descricao = request.queryParams("legenda-postagem");
		String idioma = request.queryParams("idioma-postagem");
		String texto = request.queryParams("texto-postagem");
		LocalDate publicacao = LocalDate.now();
		String imagem = request.queryParams("imagem-postagem");
		
		
		String resp = "";
		
		Postagem postagem = new Postagem(0, titulo, subtitulo, descricao, idioma, texto, publicacao, imagem);
		
		if(postagemDAO.insert(postagem) == true) {
            resp = "Postagem (" + titulo + ") cadastrada!";
            response.status(302);
            response.header("Location", "http://localhost:6789/postagem/feed/");
		} else {
			resp = "Postagem (" + titulo + ") não cadastrada!";
			response.status(404); // 404 Not found
		}
			
		return resp;
	}

	
	public Object get (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Postagem postagem = (Postagem) postagemDAO.get(id);
		
		if (postagem != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, postagem, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Produto " + id + " não encontrado.";
    		makeForm();
    		postagemForm.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return postagemForm;
	}
	
	
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Postagem postagem = (Postagem) postagemDAO.get(id);
		
		if (postagem != null) {
			response.status(200); // success
			// makeForm(FORM_UPDATE, postagem, FORM_ORDERBY_TITULO);
			montaPostagemUpdate(postagem);
        } else {
            response.status(404); // 404 Not found
            String resp = "Postagem " + id + " não encontrada.";
    		// makeForm();
    		// form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return postagemUpdate; // Retorna a página criada pela função
	}
	
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
	    
		return postagemForm;
	}
	
	
	// Exibir as postagens cadastradas no Banco de Dados no feed da plataforma
	public Object getPostagens(Request request, Response response) {
		exibirFeed();
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		
		return postagemFeed;
	}
			
	
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Postagem postagem = postagemDAO.get(id);
        String resp = "";       

        if (postagem != null) {
        	postagem.setTitulo(request.queryParams("titulo-postagem"));
        	postagem.setSubtitulo(request.queryParams("subtitulo-postagem"));
        	postagem.setDescricao(request.queryParams("legenda-postagem"));
        	postagem.setIdioma(request.queryParams("idioma-postagem"));
        	postagem.setTexto(request.queryParams("texto-postagem"));
        	
        	postagemDAO.update(postagem);
        	// response.status(200); // success
            // resp = "Postagem (ID " + postagem.getId() + ") atualizada!";
        	response.status(302);
            response.header("Location", "http://localhost:6789/postagem/feed/post/" + postagem.getId());
        } else {
            response.status(404); // 404 Not found
            resp = "Postagem (ID " + postagem.getId() + ") não encontrado!";
        }
		// makeForm();
		return postagemForm.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
	
	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Postagem postagem = postagemDAO.get(id);
        String resp = "";       

        if (postagem != null) {
        	postagemDAO.delete(id);
            response.status(200); // success
            resp = "Postagem (" + id + ") excluída!";
        } else {
            response.status(404); // 404 Not found
            resp = "Postagem (" + id + ") não encontrada!";
        }
		makeForm();
		return postagemForm.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
	
	
	// Função para montar a página de UPDATE da POSTAGEM
	public void montaPostagemUpdate(Postagem postagem) {
		String nomeArquivo = "./src/main/resources/public/update-post.html";
		postagemUpdate = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	postagemUpdate += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String updateForm = new String();
		
		// Construir o formulário de update com as informações da postagem recebida
		updateForm += "<form id=\"cad-user-form\" class=\"form\" action=\"/postagem/update/" + postagem.getId() + "\" method=\"post\">\n" +
					  "<div class=\"col-12 principal__cadastro-postagem--titulo\">\n" +
					  "<textarea name=\"titulo-postagem\" maxlength=\"60\" placeholder=\"Digite o título do texto\">" + postagem.getTitulo() + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"col-12 principal__cadastro-postagem--subtitulo\">\n" +
					  "<textarea name=\"subtitulo-postagem\" maxlength=\"120\" placeholder=\"Digite o subtítulo do texto\">" + postagem.getSubtitulo() + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"col-12 principal__update-postagem--imagem-idioma\">\n" +
					  "<div>\n" +
					  "<img src=\"http://localhost:6789/imgs/" + postagem.getImagem() + "\" alt=\"Ilustração da postagem\">\n" +
					  "</div>\n" +
					  "<div>\n" +
					  "<label for=\"idioma-postagem\">Idioma:</label>\n" +
					  "<select id=\"idioma-postagem\" name=\"idioma-postagem\">\n" +
					  "<option value=\"portugues\" selected>🇧🇷 Português</option>\n" +
					  "<option value=\"ingles\">🇺🇸 Inglês</option>\n" +
					  "<option value=\"espanhol\">🇪🇸 Espanhol</option>\n" +
					  "<option value=\"frances\">🇫🇷 Francês</option>\n" +
					  "<option value=\"alemao\">🇩🇪 Alemão</option>\n" +
					  "<option value=\"mandarim\">🇨🇳 Mandarim</option>\n" +
					  "</select>\n" +
					  "</div>\n" +
					  "</div>\n" +
					  "<div class=\"col-12 principal__cadastro-postagem--legenda\">\n" +
					  "<textarea name=\"legenda-postagem\" maxlength=\"120\" placeholder=\"Digite uma descrição para a imagem\">" + postagem.getDescricao() + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"col-12 principal__cadastro-postagem--texto\">\n" +
					  "<textarea name=\"texto-postagem\" placeholder=\"Conte algo interessante para as outras pessoas!\">" + postagem.getTexto() + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"principal__cadastro-postagem--enviar text-center\">\n" +
					  "<button type=\"submit\" id=\"btn_salvar\" class=\"btn btn-dark\" disabled=\"disabled\">Enviar</button>\n" +
					  "</div>\n" +
					  "</form>";
		
		postagemUpdate = postagemUpdate.replaceFirst("<!-- UPDATE POSTAGEM -->", updateForm);				
	}
}