function startGame1() {
    fetch('/game1/start', { method: 'POST' })
        .then(response => response.text())
        .then(data => {
            document.getElementById("result").innerText = data;
        })
        .catch(error => console.error('Ошибка:', error));
}

function startGame2() {
    fetch('/game2/start', { method: 'POST' })
        .then(response => response.text())
        .then(data => {
            document.getElementById("result").innerText = data;
        })
        .catch(error => console.error('Ошибка:', error));
}
alert( 1 || 3 || 2 || 4);
