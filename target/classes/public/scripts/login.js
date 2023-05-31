// Página inicial de Login
const INDEX_URL = "http://localhost:6789/index.html";
const LOGIN_URL = "http://localhost:6789/postagem/feed";

// Objeto para o usuário corrente
var usuarioCorrente = {};

function initLoginApp() {
	// PARTE 1 - INICIALIZA USUARIOCORRENTE A PARTIR DE DADOS NO LOCAL STORAGE, CASO EXISTA
    var usuarioCorrenteJSON = sessionStorage.getItem('usuarioCorrente');
    if (usuarioCorrenteJSON) {
        usuarioCorrente = JSON.parse(usuarioCorrenteJSON);
    }
}

// Verifica se o login do usuário está ok e, se positivo, direciona para a página inicial
async function loginUser(login, senha) {
	let resp = false;
	
	await fetch(`http://localhost:6789/usuario/login?username=${login}&senha=${senha}`, {
		method: "POST"
	})
		.then(response => response.text())
    	.then(resposta => {
			
        if(resposta !== "") {
			resp = true;
			console.log(resposta);
			
			// Armazena o nome de usuário na Session Storage
        	sessionStorage.setItem('usuarioCorrente', resposta);
		}
    });
    
    return resp;
}

// Apaga os dados do usuário corrente no sessionStorage
function logoutUser() {
    usuarioCorrente = {};
    sessionStorage.setItem('usuarioCorrente', JSON.stringify(usuarioCorrente));
    window.location = INDEX_URL;
}

initLoginApp();

// Função para exibir a navbar em usuário logado
function initPage() {
    // Associa a função de logout ao botão
    document.getElementById('botaoLogin').addEventListener('click', logoutUser);

    // Informa o nome do usuário logado
    document.getElementById('nomeUsuario').innerHTML = `<a class="nav-link links-usuario--profile" href="http://localhost:6789/usuario/${usuarioCorrente.id}"><img src="http://localhost:6789/imgs/perfil/${usuarioCorrente.imagem}" alt="Foto de perfil">${usuarioCorrente.nome}</a>`;
}