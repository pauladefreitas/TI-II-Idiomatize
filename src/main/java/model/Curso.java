package model;

public class Curso {
	private int id;
	private String titulo;
	private String descricao;
	private String idioma;
	private String quiz;
	private String imagem;
	private int dur_horas;
	private int dur_minutos;
	private int id_usuario;
	
	public Curso() {
		this.id = 0;
		this.titulo = "Título de Teste";
		this.descricao = "Descrição do Curso";;
		this.idioma = "Português";
		this.quiz = "Código em HTML";
		this.imagem = "";
		this.dur_horas = 1;
		this.dur_minutos = 30;
		this.id_usuario = 0;
	}

	public Curso(int id, String titulo, String descricao, String idioma, String imagem, String quiz, int dur_horas, int dur_minutos, int id_usuario) {
		setId(id);
		setTitulo(titulo);
		setDescricao(descricao);
		setIdioma(idioma);
		setQuiz(quiz);
		setImagem(imagem);
		setHoras(dur_horas);
		setMinutos(dur_minutos);
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
	
	public String getQuiz() {
		return quiz;
	}

	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}
	
	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public int getHoras() {
		return dur_horas;
	}
	
	public void setHoras(int dur_horas) {
		this.dur_horas = dur_horas;
	}

	public int getMinutos() {
		return dur_minutos;
	}
	
	public void setMinutos(int dur_minutos) {
		this.dur_minutos = dur_minutos;
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
		return "Id: " + id + "   Titulo: " + titulo + "   Descricao: " + descricao + "   Idioma: "
				+ idioma  + "   Quiz: " + quiz + "   Imagem: " + imagem + "   Horas: " + dur_horas + "   Minutos: " + dur_minutos + "   Autor: " + id_usuario;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Curso) obj).getId());
	}
}