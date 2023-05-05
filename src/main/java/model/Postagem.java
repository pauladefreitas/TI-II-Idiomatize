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
	
	public Postagem() {
		this.id = 0;
		this.titulo = "Título de Teste";
		this.subtitulo = "Subtítulo de Teste";
		this.descricao = "Descrição breve da imagem.";
		this.idioma = "Português";
		this.texto = "Texto em si.";
		this.publicacao = LocalDate.now();
		this.imagem = "";
	}

	public Postagem(int id, String titulo, String subtitulo, String descricao, String idioma, String texto, LocalDate publicacao, String imagem) {
		setId(id);
		setTitulo(titulo);
		setSubtitulo(subtitulo);
		setDescricao(descricao);
		setIdioma(idioma);
		setTexto(texto);
		setPublicacao(publicacao);
		setImagem(imagem);
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
	

	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Id: " + id + "   Titulo: " + titulo + "   Subtitulo: " + subtitulo + "   Descricao: " + descricao + "   Idioma: "
				+ idioma  + "   Texto: " + texto + "   Publicacao: " + publicacao + "   Imagem: " + imagem;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Usuario) obj).getId());
	}	
}