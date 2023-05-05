package model;

public class Usuario {
	private int id;
	private String nome;
	private String login;
	private String email;
	private String senha;
	private String fluencia;
	private String imagem;
	
	public Usuario() {
		this.id = 0;
		this.nome = "Teste";
		this.login = "teste";
		this.email = "email@email.com";
		this.senha = "senha";
		this.fluencia = "Português";
		this.imagem = "";
	}

	public Usuario(int id, String nome, String login, String email, String senha, String fluencia, String imagem) {
		setId(id);
		setNome(nome);
		setLogin(login);
		setEmail(email);
		setSenha(senha);
		setFluencia(fluencia);
		setImagem(imagem);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
		
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getFluencia() {
		return fluencia;
	}

	public void setFluencia(String fluencia) {
		this.fluencia = fluencia;
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
		return "Id: " + id + "   Usuario: " + nome + "   Login: " + login + "   Email: " + email + "   Senha: "
				+ senha + "   Fluência: " + fluencia + "   Imagem: " + imagem;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Usuario) obj).getId());
	}	
}