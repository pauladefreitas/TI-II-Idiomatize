package service;

import java.util.Scanner;
import java.io.File;
import java.util.List;
import dao.CursoDAO;
import dao.UsuarioDAO;
import model.Curso;
import model.Usuario;
import spark.Request;
import spark.Response;


public class CursoService {

	private CursoDAO cursoDAO = new CursoDAO();
	private String cursoUpdate;
	private String cursoFeed;
	private String cursoPagina;

	
	
	public Object insert(Request request, Response response) {
		String titulo = request.queryParams("informacoes--nome").replaceAll("'", "''");
		String descricao = request.queryParams("informacoes--descricao").replaceAll("'", "''");
		String idioma = request.queryParams("informacoes--idioma");
		String imagem = request.queryParams("informacoes--ilustracao");
		String quiz = request.queryParams("quiz--texto").replaceAll("'", "''");
		int dur_horas = Integer.parseInt(request.queryParams("informacoes--duracao-horas"));
		int dur_minutos = Integer.parseInt(request.queryParams("informacoes--duracao-minutos"));		
		int id_usuario = Integer.parseInt(request.queryParams("autor-curso"));
		
		
		String resp = "";
		
		Curso curso = new Curso(0, titulo, descricao, idioma, imagem, quiz, dur_horas, dur_minutos, id_usuario);
		
		if(cursoDAO.insert(curso) == true) {
            resp = "Curso (" + titulo + ") cadastrado!";
            response.status(302);
            response.header("Location", "http://localhost:6789/curso/feed/");
		} else {
			resp = "Curso (" + titulo + ") não cadastrado!";
			response.status(404); // 404 Not Found
		}
			
		return resp;
	}
	
	
	
	// Solicitar detalhes do CURSO
	public Object get (Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Curso curso = (Curso) cursoDAO.get(id);
		
		if (curso != null) {
			response.status(200); // Success
			exibirCurso(curso);
        } else {
            response.status(404); // 404 Not Found
        }

		return cursoPagina;
	}
	
	
	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Curso curso = (Curso) cursoDAO.get(id);
		
		if (curso != null) {
			response.status(200); // Success
			
			// montaCursoUpdate(curso); // DEPOIS TEM QUE ARRUMAR AQUI
        } else {
            response.status(404); // 404 Not Found    
        }

		return cursoUpdate; // Retorna a página criada pela função
	}
	
	
	
	// Exibir os CURSOS cadastrados no banco de dados de forma descendente
	public Object getAll(Request request, Response response) {
		// Chamar a função que exibe o feed passando parâmetro vazio (para desconsiderar o campo de filtragem de idioma)
		exibirFeed("");
		
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		
		return cursoFeed;
	}
	
	
	
	// Exibir os CURSOS cadastrados no banco de dados, filtradas por idioma, de forma descendente
	public Object getAllIdioma(Request request, Response response) {
		String idioma = request.params(":idioma");
		
		// Chamar a função que exibe o feed
		exibirFeed(idioma);
		
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		
		return cursoFeed;
	}
			
	
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Curso curso = cursoDAO.get(id);
        String resp = "";       

        if (curso != null) {
        	curso.setTitulo(request.queryParams("informacoes--nome").replaceAll("'", "''"));
        	curso.setDescricao(request.queryParams("informacoes--descricao").replaceAll("'", "''"));
        	curso.setIdioma(request.queryParams("informacoes--idioma").replaceAll("'", "''"));
        	curso.setQuiz(request.queryParams("quiz--texto").replaceAll("'", "''"));
        	curso.setHoras(Integer.parseInt(request.queryParams("informacoes--duracao-horas")));
        	curso.setMinutos(Integer.parseInt(request.queryParams("informacoes--duracao-minutos")));
        	
        	cursoDAO.update(curso);
        	
        	response.status(302);
            response.header("Location", "http://localhost:6789/curso/" + curso.getId());
        } else {
            response.status(404); // 404 Not Found
        }

		return resp;
	}
	
	
	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Curso curso = cursoDAO.get(id);
        String resp = "";       

        if (curso != null) {
        	cursoDAO.delete(id);
        	response.status(302);
            response.header("Location", "http://localhost:6789/curso/feed/");
        } else {
            response.status(404); // 404 Not found
            resp = "Postagem (" + id + ") não encontrada!";
        }

		return resp;
	}
	
	
	
	// FIM DO CRUD

	
	
	// Exibir página do CURSO
	public void exibirCurso(Curso curso) {
		String nomeArquivo = "./src/main/resources/public/quiz.html";
		cursoPagina = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while(entrada.hasNext()){
				cursoPagina += (entrada.nextLine() + "\n");
			}
			entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		
		String cabecalho = new String();
		String quiz = new String();
		
		// Buscar o autor do CURSO
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = (Usuario) usuarioDAO.get(curso.getAutor());	
		
		cabecalho += "<div class=\"col-9\">\n" + 
					 "<div class=\"curso-banner__informacoes\">\n" +
					 "<div class=\"text-center\">\n" +
					 "<img src=\"http://localhost:6789/imgs/curso/mascote-quiz.png\" alt=\"Coruja estudiosa\">\n" +
					 "</div>\n" +
					 "<div>\n" +
					 "<h3>" + curso.getTitulo() + "</h3>\n" +
					 "<p>" + curso.getDescricao() + "</p>\n" +
					 "<p>Criado por: <a href=\"http://localhost:6789/usuario/" + usuario.getId() + "\">" + usuario.getNome() + "</a></p>\n" +
					 "</div>\n" +
					 "</div>\n" +
					 "</div>\n" +
					 
					 "<div class=\"col-3\">\n" +
					 "<div class=\"curso-banner__estatisticas\">\n" +
					 "<div>\n" +
					 "<div>\n" +
					 "<i class=\"fa-regular fa-clock fa-lg\"></i>\n" +
					 "</div>\n" +
					 "<div>\n" +
					 "<p>Duração:</p>\n" +
					 "<p>" + curso.getHoras() + "hr " + curso.getMinutos() + "min</p>\n" +
					 "</div>\n" +
					 "</div>\n" +
					 "<div>\n" +
					 "<div>\n" +
					 "<i class=\"fa-regular fa-comments fa-lg\"></i>\n" +
					 "</div>\n" +
					 "<div>\n" +
					 "<p>Idioma:</p>\n" +
					 "<p>" + curso.getIdioma() + "</p>\n" +
					 "</div>\n" +
					 "</div>\n" +
					 "</div>\n" +
					 "</div>\n";
		
			
		cursoPagina = cursoPagina.replaceFirst("<!-- CABECALHO CURSO -->", cabecalho);
		cursoPagina = cursoPagina.replaceFirst("<!-- QUIZ CURSO -->", curso.getQuiz()); // Inserir o Quiz no corpo da página
	}
	
	
	/*
	// Montar a página de UPDATE do CURSO
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
					  "<textarea name=\"texto-postagem\" placeholder=\"Conte algo interessante para as outras pessoas!\">" + postagem.getTexto().replaceAll("<br>", "") + "</textarea>\n" +
					  "</div>\n" +
					  "<div class=\"principal__cadastro-postagem--enviar text-center\">\n" +
					  "<button type=\"submit\" id=\"btn_salvar\" class=\"btn btn-dark\" disabled=\"disabled\">Enviar</button>\n" +
					  "</div>\n" +
					  "</form>";
		
		postagemUpdate = postagemUpdate.replaceFirst("<!-- UPDATE POSTAGEM -->", updateForm);				
	}
	*/
	
	
	// Exibir o feed de CURSOS
	public void exibirFeed(String idioma) {
		String nomeArquivo = "./src/main/resources/public/cursos.html";
		cursoFeed = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while(entrada.hasNext()){
				cursoFeed += (entrada.nextLine() + "\n");
			}
			entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		
		String list = new String();
		
		List<Curso> cursos = cursoDAO.getIdioma(idioma);
		
		int contadorCursoIdioma = 0;
		
		for (Curso u : cursos) {
			
			// Buscar o autor do CURSO
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			Usuario usuario = (Usuario) usuarioDAO.get(u.getAutor());
			
			contadorCursoIdioma++;
			
			list += "<div class=\"col-12 col-sm-6 col-md-4 col-lg-3\">\n" + 
					"<div class=\"principal-cursos__cursos-item\">\n" +
					"<a href=\"http://localhost:6789/curso/" + u.getId() + "\">\n" +
					"<div class=\"principal-cursos__cursos-item--imagem\">\n" +
					"<img src=\"http://localhost:6789/imgs/curso/" + u.getImagem() + "\" alt=\"Imagem do curso\">\n" +
					"</div>\n" +
					"<div class=\"principal-cursos__cursos-item--corpo\">\n" +
					"<p><img src=\"http://localhost:6789/imgs/idioma-" + u.getIdioma() + ".png\" alt=\"Bandeira do idioma\"> portugues</p>\n" +
					"<h6>" + u.getTitulo() + "</h6>\n" +
					"</div>\n" +
					"<div class=\"principal-cursos__cursos-item--rodape\">\n" +
					"<div class=\"principal-cursos__cursos-item--rodape-modulos\">\n" +
					"<i class=\"fa-regular fa-file fa-lg\"></i>\n" +
					"<p>3 Módulos</p>\n" +
					"</div>\n" +
					"<div class=\"principal-cursos__cursos-item--rodape-duracao\">\n" +
					"<i class=\"fa-regular fa-clock fa-lg\"></i>\n" +
					"<p>" + u.getHoras() + "hr " + u.getMinutos() + "min</p>\n" +
					"</div>\n" +
					"</div>\n" +
					"</a>\n" +
					"</div>\n" +
					"</div>\n";
		}
		
		if(contadorCursoIdioma > 0) {
			cursoFeed = cursoFeed.replaceFirst("<!-- ITEM CURSO -->", list);
		} else {
			String nenhumCurso = new String();
			
			nenhumCurso += "<div class=\"principal__cursos--erro text-center\">\n" +
						   "<img src=\"http://localhost:6789/imgs/error-404.png\" alt=\"Não encontrado\">\n" +
						   "<p>Não existe nenhum curso no idioma selecionado! :(</p>\n" +
						   "</div>";
			
			cursoFeed = cursoFeed.replaceFirst("<!-- ITEM CURSO -->", nenhumCurso);
		}	
	}
}