import { checkWinner } from './gameLogic.js';

export function botMove(board, player, mode, playTurnCallback) {
    let move;
    if (mode === 'easy' || Math.random() < 0.2) { // 20% chance of making a random move even in hard mode
        move = easyBotMove(board);
    } else {
        move = smartMove(board, player);
    }
    
    if (move !== undefined) {
        setTimeout(() => playTurnCallback(move), 500);
    }
}

function easyBotMove(board) {
    const emptyCells = board.reduce((acc, cell, index) => {
        if (cell === '') acc.push(index);
        return acc;
    }, []);
    return emptyCells[Math.floor(Math.random() * emptyCells.length)];
}

function smartMove(board, player) {
    // Check for winning move
    const winningMove = findWinningMove(board, player);
    if (winningMove !== -1) return winningMove;

    // Block opponent's winning move
    const opponents = ['X', 'O', 'N'].filter(p => p !== player);
    for (let opponent of opponents) {
        const blockingMove = findWinningMove(board, opponent);
        if (blockingMove !== -1) return blockingMove;
    }

    // Try to create a winning opportunity
    const strategicMove = findStrategicMove(board, player);
    if (strategicMove !== -1) return strategicMove;

    // If no strategic move, choose a random empty cell
    return easyBotMove(board);
}

function findWinningMove(board, player) {
    for (let i = 0; i < board.length; i++) {
        if (board[i] === '') {
            const testBoard = [...board];
            testBoard[i] = player;
            if (checkWinner(testBoard) === player) {
                return i;
            }
        }
    }
    return -1;
}

function findStrategicMove(board, player) {
    const strategicPatterns = [
        [0, 1, 2], [1, 2, 3], [2, 3, 4], // Horizontal
        [0, 5, 10], [5, 10, 15], [10, 15, 20], // Vertical
        [0, 6, 12], [6, 12, 18], [12, 18, 24], // Diagonal
        [4, 8, 12], [8, 12, 16], [12, 16, 20], // Other diagonal
        [0, 1, 5], [1, 2, 6], [2, 3, 7], [3, 4, 8], // 2x2 squares
        [5, 6, 10], [6, 7, 11], [7, 8, 12], [8, 9, 13],
        [10, 11, 15], [11, 12, 16], [12, 13, 17], [13, 14, 18],
        [15, 16, 20], [16, 17, 21], [17, 18, 22], [18, 19, 23]
    ];

    for (let pattern of strategicPatterns) {
        const emptyInPattern = pattern.filter(index => board[index] === '');
        const playerInPattern = pattern.filter(index => board[index] === player);

        if (emptyInPattern.length === 1 && playerInPattern.length === pattern.length - 1) {
            return emptyInPattern[0];
        }
    }

    return -1;
}