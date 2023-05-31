package model;

import java.time.LocalDate;

public class Postagem {
	private int id;
	private String titulo;
	private String subtitulo;
	private String descricao;
	private String idioma;
	private String texto;
	private LocalDate publicacao;
	private String imagem;
	private int id_usuario;
	
	public Postagem() {
		this.id = 0;
		this.titulo = "Título de Teste";
		this.subtitulo = "Subtítulo de Teste";
		this.descricao = "Descrição breve da imagem.";
		this.idioma = "Português";
		this.texto = "Texto em si.";
		this.publicacao = LocalDate.now();
		this.imagem = "";
		this.id_usuario = 0;
	}

	public Postagem(int id, String titulo, String subtitulo, String descricao, String idioma, String texto, LocalDate publicacao, String imagem, int id_usuario) {
		setId(id);
		setTitulo(titulo);
		setSubtitulo(subtitulo);
		setDescricao(descricao);
		setIdioma(idioma);
		setTexto(texto);
		setPublicacao(publicacao);
		setImagem(imagem);
		setAutor(id_usuario);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
		
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public LocalDate getPublicacao() {
		return publicacao;
	}
	
	public void setPublicacao(LocalDate publicacao) {
		this.publicacao = publicacao;
	}
	
	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public int getAutor() {
		return id_usuario;
	}
	
	public void setAutor(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	

	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Id: " + id + "   Titulo: " + titulo + "   Subtitulo: " + subtitulo + "   Descricao: " + descricao + "   Idioma: "
				+ idioma  + "   Texto: " + texto + "   Publicacao: " + publicacao + "   Imagem: " + imagem + "   Autor: " + id_usuario;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Postagem) obj).getId());
	}	
}