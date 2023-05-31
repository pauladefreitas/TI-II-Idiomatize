package service;

import java.util.Scanner;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import dao.PostagemDAO;
import dao.UsuarioDAO;
import model.Postagem;
import model.Usuario;
import spark.Request;
import spark.Response;


public class PostagemService {

	private PostagemDAO postagemDAO = new PostagemDAO();
	private String postagemUpdate;
	private String postagemFeed;
	private String postagemPagina;

	
	
	public Object insert(Request request, Response response) {
		String titulo = request.queryParams("titulo-postagem").replaceAll("'", "''");
		String subtitulo = request.queryParams("subtitulo-postagem").replaceAll("'", "''");
		String descricao = request.queryParams("legenda-postagem").replaceAll("'", "''");
		String idioma = request.queryParams("idioma-postagem");
		String texto = request.queryParams("texto-postagem").replaceAll("\n", "<br>").replaceAll("'", "''");
		LocalDate publicacao = LocalDate.now();
		String imagem = request.queryParams("imagem-postagem");
		int id_usuario = Integer.parseInt(request.queryParams("autor-postagem"));
		
		
		String resp = "";
		
		Postagem postagem = new Postagem(0, titulo, subtitulo, descricao, idioma, texto, publicacao, imagem, id_usuario);
		
		if(postagemDAO.insert(postagem) == true) {
            resp = "Postagem (" + titulo + ") cadastrada!";
            response.status(302);
            response.header("Location", "http://localhost:6789/postagem/feed/");
		} else {
			resp = "Postagem (" + titulo + ") não cadastrada!";
			response.status(404); // 404 Not Found
		}
			
		return resp;
	}
	

	
	// Solicitar detalhes da postagem
	public Object get (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Postagem postagem = (Postagem) postagemDAO.get(id);
		
		if (postagem != null) {
			response.status(200); // Success
			exibirPostagem(postagem);
        } else {
            response.status(404); // 404 Not Found
        }

		return postagemPagina;
	}
	
	
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Postagem postagem = (Postagem) postagemDAO.get(id);
		
		if (postagem != null) {
			response.status(200); // Success
			
			montaPostagemUpdate(postagem);
        } else {
            response.status(404); // 404 Not Found    
        }

		return postagemUpdate; // Retorna a página criada pela função
	}
	
	
	
	// Exibir as postagens cadastradas no banco de dados de forma descendente
	public Object getAll(Request request, Response response) {
		// Chamar a função que exibe o feed passando parâmetro vazio (para desconsiderar o campo de filtragem de idioma)
		exibirFeed("");
		
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		
		return postagemFeed;
	}
	
	
	
	// Exibir as postagens cadastradas no banco de dados, filtradas por idioma, de forma descendente
	public Object getAllIdioma(Request request, Response response) {
		String idioma = request.params(":idioma");
		
		// Chamar a função que exibe o feed
		exibirFeed(idioma);
		
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		
		return postagemFeed;
	}
			
	
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Postagem postagem = postagemDAO.get(id);
        String resp = "";       

        if (postagem != null) {
        	postagem.setTitulo(request.queryParams("titulo-postagem").replaceAll("'", "''"));
        	postagem.setSubtitulo(request.queryParams("subtitulo-postagem").replaceAll("'", "''"));
        	postagem.setDescricao(request.queryParams("legenda-postagem").replaceAll("'", "''"));
        	postagem.setIdioma(request.queryParams("idioma-postagem"));
        	postagem.setTexto(request.queryParams("texto-postagem").replaceAll("\n", "<br>").replaceAll("'", "''"));
        	
        	postagemDAO.update(postagem);
        	
        	response.status(302);
            response.header("Location", "http://localhost:6789/postagem/" + postagem.getId());
        } else {
            response.status(404); // 404 Not Found
        }

		return resp;
	}
	
	
	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Postagem postagem = postagemDAO.get(id);
        String resp = "";       

        if (postagem != null) {
        	postagemDAO.delete(id);
        	response.status(302);
            response.header("Location", "http://localhost:6789/postagem/feed/");
        } else {
            response.status(404); // 404 Not found
            resp = "Postagem (" + id + ") não encontrada!";
        }

		return resp;
	}
	
	
	
	// FIM DO CRUD
	
	
	
	// Exibir página da POSTAGEM
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
		
		// Buscar o autor da postagem
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = (Usuario) usuarioDAO.get(postagem.getAutor());	
		
		list += "<div class=\"row\">\n" + 
				"<div class=\"col-12 principal__autoria\">\n" +
				"<div class=\"principal__autoria--imagem-perfil\">\n" +
				"<img src=\"http://localhost:6789/imgs/perfil/" + usuario.getImagem() + "\" alt=\"Foto de perfil\">\n" +
				"</div>\n" +
				"<div class=\"principal__autoria--descricao\">\n" +
				"<h6>" + usuario.getNome() + "</h6>\n" +
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
				"<img src=\"http://localhost:6789/imgs/postagem/" + postagem.getImagem() + "\" alt=\"Imagem da postagem\">\n" +
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
	
	
	
	// Montar a página de UPDATE da POSTAGEM
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
					  "<img src=\"http://localhost:6789/imgs/postagem/" + postagem.getImagem() + "\" alt=\"Ilustração da postagem\">\n" +
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
					  "<textarea name=\"texto-postagem\" placeholder=\"Conte algo interessante para as outras pessoas!\">" + postagem.getTexto().replaceAll("<br>", "") + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"principal__cadastro-postagem--enviar text-center\">\n" +
					  "<button type=\"submit\" id=\"btn_salvar\" class=\"btn btn-dark\" disabled=\"disabled\">Enviar</button>\n" +
					  "</div>\n" +
					  "</form>";
		
		postagemUpdate = postagemUpdate.replaceFirst("<!-- UPDATE POSTAGEM -->", updateForm);				
	}
	
	
	
	// Exibir o feed de POSTAGENS
	public void exibirFeed(String idioma) {
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
		
		List<Postagem> postagens = postagemDAO.getIdioma(idioma);
		
		int contadorPostagemIdioma = 0;
		
		for (Postagem u : postagens) {
			
			// Buscar o autor da postagem
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			Usuario usuario = (Usuario) usuarioDAO.get(u.getAutor());
			
			contadorPostagemIdioma++;
			
			list += "<div class=\"row principal__postagens--item\">\n" + 
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
					"</div>\n" +
					"</div>\n";
		}
		
		if(contadorPostagemIdioma > 0) {
			postagemFeed = postagemFeed.replaceFirst("<!-- POSTAGEM -->", list);
		} else {
			String nenhumaPostagem = new String();
			
			nenhumaPostagem += "<div class=\"principal__postagens--erro text-center\">\n" +
							   "<img src=\"http://localhost:6789/imgs/error-404.png\" alt=\"Não encontrado\">\n" +
							   "<p>Não existe nenhuma postagem no idioma selecionado! :(</p>\n" +
							   "</div>";
			
			postagemFeed = postagemFeed.replaceFirst("<!-- POSTAGEM -->", nenhumaPostagem);
		}	
	}
}