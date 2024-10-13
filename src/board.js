export function initializeBoard(clickHandler) {
    const gameBoard = document.getElementById('game-board');
    gameBoard.innerHTML = '';
    for (let i = 0; i < 25; i++) {
        const cell = document.createElement('div');
        cell.classList.add('cell');
        cell.addEventListener('click', () => clickHandler(i));
        gameBoard.appendChild(cell);
    }
}

export function updateBoard(board) {
    const cells = document.querySelectorAll('.cell');
    cells.forEach((cell, index) => {
        cell.textContent = board[index];
        cell.className = 'cell';
        if (board[index] === 'X') cell.classList.add('player-x');
        if (board[index] === 'O') cell.classList.add('player-o');
        if (board[index] === 'N') cell.classList.add('player-n');
    });
}
