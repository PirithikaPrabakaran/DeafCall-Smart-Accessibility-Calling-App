let callActive = false;
let muted = false;
let fontSize = 18;
let timer = 0;
let timerInterval;
let history = [];

const subtitleText = document.getElementById("subtitleText");
const partialText = document.getElementById("partialText");
const historyList = document.getElementById("historyList");
const statusDot = document.getElementById("statusDot");
const timerDisplay = document.getElementById("timer");

// 🎤 Speech Recognition Safety Check
const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

if (!SpeechRecognition) {
  alert("Speech Recognition not supported in this browser ❌");
}

const recognition = SpeechRecognition ? new SpeechRecognition() : null;

if (recognition) {
  recognition.continuous = true;
  recognition.interimResults = true;

  recognition.onresult = (event) => {
    let final = "";
    let interim = "";

    for (let i = event.resultIndex; i < event.results.length; i++) {
      let text = event.results[i][0].transcript;
      if (event.results[i].isFinal) final += text;
      else interim += text;
    }

    if (final) {
      subtitleText.innerText = final;
      addHistory(final);
    }

    partialText.innerText = interim;
  };

  recognition.onerror = () => {
    if (callActive && !muted) startListening();
  };
}

// 📞 Start Call with Name
function startCallPrompt() {
  const name = prompt("Enter Caller Name:", "John");
  if (name) startCall(name);
}

function startCall(name) {
  callActive = true;
  document.getElementById("callerName").innerText = name;
  document.getElementById("avatar").innerText = name.slice(0,2).toUpperCase();
  statusDot.classList.add("active");

  startTimer();
  startListening();
}

function startListening() {
  if (recognition && !muted) {
    try { recognition.start(); } catch {}
  }
}

function stopListening() {
  if (recognition) recognition.stop();
}

// ⏱ Timer
function startTimer() {
  timer = 0;
  timerInterval = setInterval(() => {
    timer++;
    let m = String(Math.floor(timer / 60)).padStart(2, '0');
    let s = String(timer % 60).padStart(2, '0');
    timerDisplay.innerText = `${m}:${s}`;
  }, 1000);
}

// 📴 End Call
function endCall() {
  callActive = false;
  stopListening();
  clearInterval(timerInterval);
  statusDot.classList.remove("active");
  subtitleText.innerText = "Call ended";
}

// 🔇 Mute
function toggleMute() {
  muted = !muted;
  muted ? stopListening() : startListening();
}

// 💡 Flash Simulation
function toggleFlash() {
  document.body.style.background =
    document.body.style.background === "rgb(34, 34, 34)" ? "#0f0f1a" : "#222";
}

// 🔡 Font size
function changeFont() {
  const sizes = [14, 18, 22, 28];
  let index = sizes.indexOf(fontSize);
  fontSize = sizes[(index + 1) % sizes.length];
  subtitleText.style.fontSize = fontSize + "px";
}

// 📝 History
function addHistory(text) {
  history.unshift(text);
  history = history.slice(0, 10);

  historyList.innerHTML = history.map(h =>
    `<div class="history-item">${h}</div>`
  ).join("");
}
