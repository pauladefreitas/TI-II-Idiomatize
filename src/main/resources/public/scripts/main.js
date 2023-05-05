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

function exibePopupChatbot() {
    document.getElementById("janelaChatbot").innerHTML = `<div class="janela-chatbot__topo">
                                                                <div class="janela-chatbot__topo--mascote">
                                                                    <img src="https://live.staticflickr.com/65535/52864929548_2ccfcc0aba_m.jpg" alt="Mascote do site">
                                                                    <p>Oi, eu sou o João!</p>
                                                                </div>
                                                                <div class="janela-chatbot__topo--fechar">
                                                                    <button type="button" onclick="fechaPopupChatbot()"><i class="fa-solid fa-xmark fa-lg"></i></button>
                                                                </div>
                                                            </div>
                                                            <div class="janela-chatbot__conversa">
                                                                Talk
                                                            </div>
                                                            <form action="#">
                                                                <div class="janela-chatbot__input">
                                                                    <div>
                                                                        <input type="textarea" placeholder="Converse com o João!">
                                                                    </div>
                                                                    <div>
                                                                        <button type="submit"><i class="fa-solid fa-paper-plane"></i></button>
                                                                    </div>
                                                                </div>
                                                            </form>`;
}

function abrePopupChatbot() {
    exibePopupChatbot();

    document.getElementById("janelaChatbot").style.display = "block";
    document.getElementById("chatbot").style.display = "none";
}

function fechaPopupChatbot() {
    document.getElementById("janelaChatbot").style.display = "none";
    document.getElementById("chatbot").style.display = "block";
}