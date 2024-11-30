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
let ask = (question, yes, no) => {
  if (confirm(question)) yes()
  else no();
}

ask(
  "Вы согласны?",
  function() { alert("Вы согласились."); },
  function() { alert("Вы отменили выполнение."); }
);
for (let i = 0; i < 5; i++) {
  console.log("value,", i);
}