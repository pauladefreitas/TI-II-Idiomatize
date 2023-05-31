// Função para inserir o botão CHATBOT
function exibeChatbot() {
    document.getElementById("chatbot").innerHTML = `<img src="https://live.staticflickr.com/65535/52864929548_2ccfcc0aba_m.jpg" alt="Chatbot da plataforma">`;
}

// Função para inserir o botão NOVO POST 
function exibeNovoPost() {
	if(document.getElementById("novoPost")) {
    	document.getElementById("novoPost").innerHTML = `<i class="fa-regular fa-pen-to-square fa-xl"></i>`;
    }
}

// Exibir os botões
$(document).ready(exibeChatbot(), exibeNovoPost());

function abrePopupChatbot() {
    document.getElementById("janelaChatbot").style.display = "block";
    document.getElementById("chatbot").style.display = "none";
}

function fechaPopupChatbot() {
    document.getElementById("janelaChatbot").style.display = "none";
    document.getElementById("chatbot").style.display = "block";
}