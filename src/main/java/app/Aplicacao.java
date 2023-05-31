package app;

import static spark.Spark.*;
import service.UsuarioService;
import service.PostagemService;
import service.CursoService;


public class Aplicacao {
	
	// Criação das variáveis
	private static UsuarioService usuarioService = new UsuarioService();
	private static PostagemService postagemService = new PostagemService();
	private static CursoService cursoService = new CursoService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        // CRUD do Usuário
        post("/usuario/insert", (request, response) -> usuarioService.insert(request, response));

        post("/usuario/login", (request, response) -> usuarioService.autenticar(request, response)); // Autenticar o login do usuário
        
        get("/usuario/:id", (request, response) -> usuarioService.get(request, response)); // Buscar perfil do usuário
        
        get("/usuario/list/:orderby", (request, response) -> usuarioService.getAll(request, response));

        get("/usuario/update/:id", (request, response) -> usuarioService.getToUpdate(request, response));
        
        post("/usuario/update/:id", (request, response) -> usuarioService.update(request, response));

        post("/usuario/premium/:id", (request, response) -> usuarioService.updatePremium(request, response));
           
        get("/usuario/delete/:id", (request, response) -> usuarioService.delete(request, response));
        
        
        // CRUD da Postagem
        post("/postagem/insert", (request, response) -> postagemService.insert(request, response));

        get("/postagem/:id", (request, response) -> postagemService.get(request, response)); // Buscar a página de um post específico
        
        get("/postagem/feed/", (request, response) -> postagemService.getAll(request, response)); // Buscar todas as postagens para o Feed

        get("/postagem/feed/:idioma", (request, response) -> postagemService.getAllIdioma(request, response)); // Filtrar a postagem por idioma

        get("/postagem/update/:id", (request, response) -> postagemService.getToUpdate(request, response));
        
        post("/postagem/update/:id", (request, response) -> postagemService.update(request, response));
           
        get("/postagem/delete/:id", (request, response) -> postagemService.delete(request, response));
        
        
        // CRUD do Curso
        post("/curso/insert", (request, response) -> cursoService.insert(request, response));
        
        get("/curso/:id", (request, response) -> cursoService.get(request, response)); // Buscar a página de um curso específico
        
        get("/curso/feed/", (request, response) -> cursoService.getAll(request, response)); // Buscar todos os cursos para o Feed

        get("/curso/feed/:idioma", (request, response) -> cursoService.getAllIdioma(request, response)); // Filtrar o curso por idioma

        get("/curso/update/:id", (request, response) -> cursoService.getToUpdate(request, response));
        
        post("/curso/update/:id", (request, response) -> cursoService.update(request, response));
           
        get("/curso/delete/:id", (request, response) -> cursoService.delete(request, response));
    }
}