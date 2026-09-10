import { initializeBoard, updateBoard } from "./board.js";
import { checkWinner } from "./gameLogic.js";
import { botMove } from "./botLogic.js";
import {
  trackGameStart,
  trackGameEnd,
  trackSeriesEnd,
  trackMove,
  trackModalOpen,
  trackButtonClick,
  trackSeriesAbandoned
} from "./analytics.js";

let currentPlayer = "X";
let gameMode = "medium"; // 'easy', 'medium', or 'hard'
let board = Array(25).fill("");
let seriesStats = { X: 0, O: 0, N: 0 };
let currentGame = 1;
let MAX_GAMES = 10;
let moveCount = 0;

document.addEventListener("DOMContentLoaded", () => {
  document
    .getElementById("start-series")
    .addEventListener("click", startNewSeries);
  document.getElementById("new-game").addEventListener("click", () => {
    trackButtonClick('new_game');
    startNewGame();
  });
  document
    .getElementById("new-series")
    .addEventListener("click", () => {
      trackButtonClick('new_series');
      showSetupScreen();
    });
  document.getElementById("close-stats").addEventListener("click", closeStats);
  document.getElementById("help-button").addEventListener("click", showRules);
  document.getElementById("close-rules").addEventListener("click", hideRules);
  document
    .getElementById("abandon-series")
    .addEventListener("click", abandonSeries);

  initializeBoard(playTurn);
  showSetupScreen();
});

function showSetupScreen() {
  document.getElementById("setup-screen").style.display = "flex";
  document.getElementById("game-screen").style.display = "none";
}

function showGameScreen() {
  document.getElementById("setup-screen").style.display = "none";
  document.getElementById("game-screen").style.display = "flex";
}

function startNewSeries() {
  MAX_GAMES = parseInt(document.getElementById("series-length").value);
  gameMode = document.getElementById("difficulty").value;
  seriesStats = { X: 0, O: 0, N: 0 };
  currentGame = 1;

  // Track series start
  trackGameStart(MAX_GAMES, gameMode);
  trackButtonClick('start_series');

  updateProgressBars();
  startNewGame();
  document.getElementById("series-stats").style.display = "none";
  const playerNameInput = document.getElementById("player-name");
  const playerProgressName = document.querySelector(
    "#player-progress .player-name"
  );
  if (playerNameInput && playerProgressName) {
    playerProgressName.textContent = playerNameInput.value || "Player";
  }
  showGameScreen();
  updateGameProgress();
}

function startNewGame() {
  board = Array(25).fill("");
  currentPlayer = "X";
  moveCount = 0;
  updateBoard(board);
  document.getElementById("status").textContent = "";
  updateGameProgress();

  const cells = document.querySelectorAll(".cell");
  cells.forEach((cell) => {
    cell.classList.remove("winning-cell");
  });
}

function updateGameProgress() {
  document.getElementById(
    "game-progress"
  ).textContent = `Game ${currentGame} of ${MAX_GAMES}`;
}

function updateProgressBars() {
  updateProgressBar("player-progress", seriesStats.X);
  updateProgressBar("blocker-progress", seriesStats.O);
  updateProgressBar("hunter-progress", seriesStats.N);
}

function updateProgressBar(id, wins) {
  const progressBar = document.querySelector(`#${id} .progress`);
  const progressText = document.querySelector(`#${id} .progress-text`);
  if (progressBar && progressText) {
    const width = (wins / MAX_GAMES) * 100;
    progressBar.style.width = `${width}%`;
    progressText.textContent = wins.toString();
  } else {
    console.error(`Progress bar not found: ${id}`);
  }
}
export function playTurn(index) {
  if (currentGame > MAX_GAMES) return;
  if (board[index] === "") {
    moveCount++;
    board[index] = currentPlayer;
    updateBoard(board);

    // Track the move
    trackMove(currentPlayer, index, moveCount);

    const { winner, pattern } = checkWinner(board);

    if (winner) {
      handleWin(winner, pattern);
    } else {
      currentPlayer = getNextPlayer(currentPlayer);
      if (currentPlayer !== "X") {
        botMove(board, currentPlayer, gameMode, playTurn);
      }
    }
  } else {
    console.log(
      "This cell is already occupied or the game has ended. Please start a new game."
    );
  }
}

function handleWin(winner, pattern) {
  if (winner === "draw") {
    handleDraw();
  } else {
    seriesStats[winner] += 1;
    // Track game end
    trackGameEnd(winner, currentGame);
    updateProgressBars();
    document.getElementById("status").textContent = `${getPlayerName(
      winner
    )} wins!`;
    highlightWinningPattern(pattern);
    setTimeout(() => {
      if (currentGame >= MAX_GAMES) {
        endSeries();
      } else {
        currentGame++;
        startNewGame();
      }
    }, 3000);
  }
}

function handleDraw() {
  // Track draw
  trackGameEnd("draw", currentGame);
  document.getElementById("status").textContent = "It's a draw!";
  setTimeout(() => {
    if (currentGame >= MAX_GAMES) {
      endSeries();
    } else {
      currentGame++;
      startNewGame();
    }
  }, 3000);
}

function highlightWinningPattern(pattern) {
  if (pattern) {
    const cells = document.querySelectorAll(".cell");
    pattern.forEach((index) => {
      if (cells[index]) {
        cells[index].classList.add("winning-cell");
      }
    });
  }
}

function endSeries() {
  const maxScore = Math.max(seriesStats.X, seriesStats.O, seriesStats.N);
  const winners = Object.keys(seriesStats).filter(
    (player) => seriesStats[player] === maxScore
  );

  const seriesWinner = winners.length > 1 ? "draw" : winners[0];

  // Track series end
  trackSeriesEnd(seriesWinner, MAX_GAMES, seriesStats.X, seriesStats.O, seriesStats.N);

  document.getElementById("series-stats").style.display = "flex";

  if (winners.length > 1) {
    document.getElementById("series-winner").textContent =
      "Series Result: It's a draw!";
  } else {
    document.getElementById(
      "series-winner"
    ).textContent = `Series Winner: ${getPlayerName(seriesWinner)}`;
  }

  document.getElementById("player-wins").textContent = `${getPlayerName(
    "X"
  )} wins: ${seriesStats.X}`;
  document.getElementById(
    "blocker-wins"
  ).textContent = `Blocker wins: ${seriesStats.O}`;
  document.getElementById(
    "hunter-wins"
  ).textContent = `Hunter wins: ${seriesStats.N}`;
}

function closeStats() {
  trackButtonClick('close_stats');
  document.getElementById("series-stats").style.display = "none";
  showSetupScreen();
}

function getPlayerName(player) {
  if (player === "X")
    return document.getElementById("player-name").value || "Player";
  if (player === "O") return "Blocker";
  if (player === "N") return "Hunter";
}

function getNextPlayer(player) {
  const players = ["X", "O", "N"];
  const currentIndex = players.indexOf(player);
  return players[(currentIndex + 1) % players.length];
}

function showRules() {
  trackModalOpen('rules');
  trackButtonClick('help_button');
  const rulesModal = document.getElementById("rules-modal");
  rulesModal.style.display = "flex";
}

function hideRules() {
  trackButtonClick('close_rules');
  const rulesModal = document.getElementById("rules-modal");
  rulesModal.style.display = "none";
}

function abandonSeries() {
  trackSeriesAbandoned(currentGame, MAX_GAMES);
  trackButtonClick('abandon_series');
  alert("Series abandoned.");
  showSetupScreen();
}
