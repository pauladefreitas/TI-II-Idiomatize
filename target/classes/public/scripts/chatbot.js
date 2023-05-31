// Função para adicionar uma mensagem no log do chat
function appendMessage(sender, content) {
    const chatLog = document.getElementById('chat-log');
    const messageElement = document.createElement('div');

    var dataAtual = new Date();
    if (dataAtual.getMinutes() < 10) {
        var horario = dataAtual.getHours() + ":0" + dataAtual.getMinutes();
    } else {
        var horario = dataAtual.getHours() + ":" + dataAtual.getMinutes();
    }

    if (sender === 'usuario') {
        messageElement.innerHTML = `<div class="janela-chatbot__conversa--janela-usuario">
                                        <div class="janela-chatbot__conversa--usuario">
                                            ${content}
                                            <div class="janela-chatbot__conversa--hora-usuario text-end">
                                                ${horario}
                                            </div>
                                        </div>
                                        <div class="janela-chatbot__conversa--triangulo-usuario"></div>
                                    </div>`;
    } else if (sender === 'mascote') {
        messageElement.innerHTML = `<div class="janela-chatbot__conversa--janela-mascote">
                                        <div class="janela-chatbot__conversa--mascote">
                                            ${content}
                                            <div class="janela-chatbot__conversa--hora-mascote text-end">
                                                ${horario}
                                            </div>
                                        </div>
                                        <div class="janela-chatbot__conversa--triangulo-mascote"></div>
                                    </div>`;
    }

    chatLog.appendChild(messageElement);

    scrollToBottom();
}

// Variável para armazenar o histórico de mensagens
let historicoMensagens = [];

// Função para enviar a mensagem do usuário para o ChatGPT API
async function sendMessage(message) {
    appendMessage('usuario', message);
    
    // Adicionar a mensagem do usuário ao histórico
    historicoMensagens.push({ role: 'user', content: message });

	try {
		const response = await fetch('https://api.openai.com/v1/chat/completions', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/json',
	            'Authorization': 'Bearer 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'
	        },
	        body: JSON.stringify({
	            'model': 'gpt-3.5-turbo',
	            'messages': [{ 'role': 'system', 'content': 'Você é um assistente de bate-papo chamado João.' }, ...historicoMensagens]
	        })
    	});
    	
    	if(response.ok) {
			const data = await response.json();
		    const reply = data.choices[0].message.content;
		    appendMessage('mascote', reply);
		    
		    // Adicionar a resposta do assistente ao histórico
		    historicoMensagens.push({ role: 'assistant', content: reply });
		} else {
			throw new Error('Erro na solicitação à API.');
		}
	} catch (error) {
		appendMessage('mascote', 'Desculpe, não consegui entender. Você poderia repetir?');
	}    
}

// Lidar com o envio de mensagens quando o botão é clicado
document.getElementById('send-button').addEventListener('click', function () {
    const userInput = document.getElementById('user-input');
    const message = userInput.value.trim();
    if (message !== '') {
        userInput.value = '';
        sendMessage(message);
    }
});

// Lidar com o envio de mensagens quando a tecla Enter é pressionada
document.getElementById('user-input').addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        const userInput = document.getElementById('user-input');
        const message = userInput.value.trim();
        if (message !== '') {
            userInput.value = '';
            sendMessage(message);
        }
    }
});

// Descer com a conversa automaticamente
function scrollToBottom() {
    document.getElementById('chat-log').scrollTop = document.getElementById('chat-log').scrollHeight;
}