const campoComentario = document.getElementById('campoComentario');
const contadorCaracteres = document.getElementById('contadorCaracteres');

campoComentario.addEventListener('input', function (e) {
    const target = e.target;

    // Pegar o atributo "maxlength" da textarea
    const tamanhoMaximo = target.getAttribute('maxlength');

    // Contar o número atual de caracteres
    const tamanhoAtual = target.value.length;

    contadorCaracteres.innerHTML = `${tamanhoAtual} / ${tamanhoMaximo}`;
});