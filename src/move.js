const players = ['X', 'O', 'N'];
let currentPlayer = 0;

export function makeMove(cell, index) {
    if (cell.textContent === '') {
        cell.textContent = players[currentPlayer];
        currentPlayer = (currentPlayer + 1) % players.length;
        // Add logic for Blocker and Hunter moves
    }
}

