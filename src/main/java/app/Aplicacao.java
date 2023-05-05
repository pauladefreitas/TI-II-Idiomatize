package app;

import static spark.Spark.*;
import service.UsuarioService;
import service.PostagemService;


public class Aplicacao {
	
	// Criação das variáveis
	private static UsuarioService usuarioService = new UsuarioService();
	private static PostagemService postagemService = new PostagemService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        // CRUD do Usuário
        post("/usuario/insert", (request, response) -> usuarioService.insert(request, response));

        post("/usuario/login", (request, response) -> usuarioService.autenticar(request, response)); // Autenticar o login do usuário
        
        get("/usuario/perfil/:id", (request, response) -> usuarioService.getPerfil(request, response)); // Exibir perfil do usuário

        get("/usuario/:id", (request, response) -> usuarioService.get(request, response));
        
        get("/usuario/list/:orderby", (request, response) -> usuarioService.getAll(request, response));

        get("/usuario/update/:id", (request, response) -> usuarioService.getToUpdate(request, response));
        
        post("/usuario/update/:id", (request, response) -> usuarioService.update(request, response));
           
        get("/usuario/delete/:id", (request, response) -> usuarioService.delete(request, response));
        
        
        // CRUD da Postagem
        post("/postagem/insert", (request, response) -> postagemService.insert(request, response));

        get("/postagem/:id", (request, response) -> postagemService.get(request, response));
        
        get("/postagem/list/:orderby", (request, response) -> postagemService.getAll(request, response));
        
        get("/postagem/feed/", (request, response) -> postagemService.getPostagens(request, response)); // Exibir todas as postagens no Feed

        get("/postagem/feed/post/:id", (request, response) -> postagemService.detalhePostagem(request, response)); // Exibir a página de um post específico

        get("/postagem/update/:id", (request, response) -> postagemService.getToUpdate(request, response));
        
        post("/postagem/update/:id", (request, response) -> postagemService.update(request, response));
           
        get("/postagem/delete/:id", (request, response) -> postagemService.delete(request, response));
        
        
        // CRUD do Curso
    }
}