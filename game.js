console.log('Game script loaded');

import { initializeBoard, updateBoard } from './board.js';
import { checkWinner } from './gameLogic.js';
import { botMove } from './botLogic.js';

let currentPlayer = 'X';
let gameMode = 'hard'; // 'easy' or 'hard'
let board = Array(25).fill('');
let seriesStats = { X: 0, O: 0, N: 0 }; // Initialize scores
let currentGame = 1;
let MAX_GAMES = 10;

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('start-series').addEventListener('click', startNewSeries);
    document.getElementById('new-game').addEventListener('click', startNewGame);
    document.getElementById('new-series').addEventListener('click', showSetupScreen);
    document.getElementById('close-stats').addEventListener('click', closeStats);
    document.getElementById('help-button').addEventListener('click', showRules);
    document.getElementById('close-rules').addEventListener('click', hideRules);

    initializeBoard(playTurn);
    showSetupScreen();
});

function showSetupScreen() {
    document.getElementById('setup-screen').style.display = 'flex';
    document.getElementById('game-screen').style.display = 'none';
}

function showGameScreen() {
    document.getElementById('setup-screen').style.display = 'none';
    document.getElementById('game-screen').style.display = 'flex';
}

function startNewSeries() {
    MAX_GAMES = parseInt(document.getElementById('series-length').value);
    seriesStats = { X: 0, O: 0, N: 0 }; // Reset scores
    currentGame = 1;
    updateProgressBars();
    startNewGame();
    document.getElementById('series-stats').style.display = 'none';
    const playerNameInput = document.getElementById('player-name');
    const playerProgressName = document.querySelector('#player-progress .player-name');
    if (playerNameInput && playerProgressName) {
        playerProgressName.textContent = playerNameInput.value || 'Player';
    }
    showGameScreen();
    updateGameProgress();
}

function startNewGame() {
    board = Array(25).fill('');
    currentPlayer = 'X';
    updateBoard(board);
    document.getElementById('status').textContent = '';
    updateGameProgress();
    
    // Remove winning-cell class from all cells
    const cells = document.querySelectorAll('.cell');
    cells.forEach(cell => {
        cell.classList.remove('winning-cell');
    });
}

function updateGameProgress() {
    document.getElementById('game-progress').textContent = `Game ${currentGame} of ${MAX_GAMES}`;
}

function updateProgressBars() {
    updateProgressBar('player-progress', seriesStats.X);
    updateProgressBar('blocker-progress', seriesStats.O);
    updateProgressBar('hunter-progress', seriesStats.N);
}

function updateProgressBar(id, wins) {
    const progressBar = document.querySelector(`#${id} .progress`);
    const progressText = document.querySelector(`#${id} .progress-text`);
    if (progressBar && progressText) {
        const width = (wins / MAX_GAMES) * 100; // Calculate percentage
        progressBar.style.width = `${width}%`;
        progressText.textContent = wins.toString();
    } else {
        console.error(`Progress bar not found: ${id}`);
    }
}

export function playTurn(index) {
    if (currentGame > MAX_GAMES) return; // Series is over
    if (board[index] === '') {
        board[index] = currentPlayer;
        updateBoard(board);
        
        const { winner, pattern } = checkWinner(board);
        console.log(`Current board: ${board}`); // Debugging line
        console.log(`Winner after move: ${winner}`); // Debugging line

        if (winner) {
            handleWin(winner, pattern);
        } else {
            currentPlayer = getNextPlayer(currentPlayer);
            if (currentPlayer !== 'X') {
                setTimeout(() => botMove(board, currentPlayer, gameMode, playTurn), 500);
            }
        }
    } else {
        console.log("This cell is already occupied or the game has ended. Please start a new game.");
    }
}

function handleWin(winner, pattern) {
    console.log(`Winner detected: ${winner}`); // Debugging line

    if (winner === 'draw') {
        handleDraw();
    } else {
        // Update the score based on the winner
        seriesStats[winner] += 1;

        // Update the progress bars
        updateProgressBars();

        // Show the winning message
        document.getElementById('status').textContent = `${getPlayerName(winner)} wins!`;

        // Highlight the winning pattern
        highlightWinningPattern(pattern);

        // Delay before starting a new game
        setTimeout(() => {
            if (currentGame >= MAX_GAMES) {
                endSeries();
            } else {
                currentGame++;
                startNewGame();
            }
        }, 3000); // 3 seconds delay
    }
}

function handleDraw() {
    document.getElementById('status').textContent = 'It\'s a draw!';

    // Delay before starting a new game
    setTimeout(() => {
        if (currentGame >= MAX_GAMES) {
            endSeries();
        } else {
            currentGame++;
            startNewGame();
        }
    }, 3000); // 3 seconds delay
}

function highlightWinningPattern(pattern) {
    if (pattern) {
        const cells = document.querySelectorAll('.cell');
        pattern.forEach(index => {
            if (cells[index]) {
                cells[index].classList.add('winning-cell');
            }
        });
    }
}

function endSeries() {
    const maxScore = Math.max(seriesStats.X, seriesStats.O, seriesStats.N);
    const winners = Object.keys(seriesStats).filter(player => seriesStats[player] === maxScore);
    
    document.getElementById('series-stats').style.display = 'flex';
    
    if (winners.length > 1) {
        // It's a tie
        document.getElementById('series-winner').textContent = "Series Result: It's a draw!";
    } else {
        // We have a single winner
        const seriesWinner = winners[0];
        document.getElementById('series-winner').textContent = `Series Winner: ${getPlayerName(seriesWinner)}`;
    }
    
    document.getElementById('player-wins').textContent = `${getPlayerName('X')} wins: ${seriesStats.X}`;
    document.getElementById('blocker-wins').textContent = `Blocker wins: ${seriesStats.O}`;
    document.getElementById('hunter-wins').textContent = `Hunter wins: ${seriesStats.N}`;
    
    createConfetti();
}

function closeStats() {
    document.getElementById('series-stats').style.display = 'none';
    const confetti = document.querySelectorAll('.confetti');
    confetti.forEach(c => c.remove());
    showSetupScreen();
}

function createConfetti() {
    const confettiCount = 100;
    const colors = ['red', 'blue', 'green', 'yellow'];
    const container = document.querySelector('.modal-content');

    for (let i = 0; i < confettiCount; i++) {
        const confetti = document.createElement('div');
        confetti.classList.add('confetti', colors[Math.floor(Math.random() * colors.length)]);
        confetti.style.left = `${Math.random() * 100}%`;
        confetti.style.animationDuration = `${Math.random() * 3 + 2}s`;
        confetti.style.animationDelay = `${Math.random() * 2}s`;
        container.appendChild(confetti);

        confetti.addEventListener('animationiteration', () => {
            confetti.remove();
        });
    }
}

function getPlayerName(player) {
    if (player === 'X') return document.getElementById('player-name').value || 'Player';
    if (player === 'O') return 'Blocker';
    if (player === 'N') return 'Hunter';
}

function getNextPlayer(player) {
    const players = ['X', 'O', 'N'];
    const currentIndex = players.indexOf(player);
    return players[(currentIndex + 1) % players.length];
}

function showRules() {
    const rulesModal = document.getElementById('rules-modal');
    rulesModal.style.display = 'flex';
}

function hideRules() {
    const rulesModal = document.getElementById('rules-modal');
    rulesModal.style.display = 'none';
}