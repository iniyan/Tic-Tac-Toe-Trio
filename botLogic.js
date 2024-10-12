import { checkWinner } from './gameLogic.js';

export function botMove(board, player, mode, playTurnCallback) {
    let move;
    if (mode === 'easy' || Math.random() < 0.1) { // 10% chance of making a random move even in hard mode
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

    // Try to create a fork (two winning opportunities)
    const forkMove = findForkMove(board, player);
    if (forkMove !== -1) return forkMove;

    // Block opponent's fork
    for (let opponent of opponents) {
        const blockForkMove = findForkMove(board, opponent);
        if (blockForkMove !== -1) return blockForkMove;
    }

    // Try to create a winning opportunity
    const strategicMove = findStrategicMove(board, player);
    if (strategicMove !== -1) return strategicMove;

    // If no strategic move, choose a corner or center
    const preferredMoves = [0, 4, 20, 24, 12]; // corners and center
    for (let move of preferredMoves) {
        if (board[move] === '') return move;
    }

    // If all else fails, choose a random empty cell
    return easyBotMove(board);
}

function findWinningMove(board, player) {
    for (let i = 0; i < board.length; i++) {
        if (board[i] === '') {
            const testBoard = [...board];
            testBoard[i] = player;
            if (checkWinner(testBoard).winner === player) {
                return i;
            }
        }
    }
    return -1;
}

function findForkMove(board, player) {
    for (let i = 0; i < board.length; i++) {
        if (board[i] === '') {
            const testBoard = [...board];
            testBoard[i] = player;
            let winningMoves = 0;
            for (let j = 0; j < board.length; j++) {
                if (testBoard[j] === '') {
                    const secondTestBoard = [...testBoard];
                    secondTestBoard[j] = player;
                    if (checkWinner(secondTestBoard).winner === player) {
                        winningMoves++;
                    }
                }
            }
            if (winningMoves >= 2) {
                return i;
            }
        }
    }
    return -1;
}

function findStrategicMove(board, player) {
    const strategicPatterns = [
        [0, 1, 2, 3], [1, 2, 3, 4], [5, 6, 7, 8], [6, 7, 8, 9], [10, 11, 12, 13], [11, 12, 13, 14],
        [15, 16, 17, 18], [16, 17, 18, 19], [20, 21, 22, 23], [21, 22, 23, 24],
        [0, 5, 10, 15], [5, 10, 15, 20], [1, 6, 11, 16], [6, 11, 16, 21], [2, 7, 12, 17], [7, 12, 17, 22],
        [3, 8, 13, 18], [8, 13, 18, 23], [4, 9, 14, 19], [9, 14, 19, 24],
        [0, 6, 12, 18], [6, 12, 18, 24], [4, 8, 12, 16], [8, 12, 16, 20],
        [0, 1, 5, 6], [1, 2, 6, 7], [2, 3, 7, 8], [3, 4, 8, 9],
        [5, 6, 10, 11], [6, 7, 11, 12], [7, 8, 12, 13], [8, 9, 13, 14],
        [10, 11, 15, 16], [11, 12, 16, 17], [12, 13, 17, 18], [13, 14, 18, 19],
        [15, 16, 20, 21], [16, 17, 21, 22], [17, 18, 22, 23], [18, 19, 23, 24]
    ];

    for (let pattern of strategicPatterns) {
        const emptyInPattern = pattern.filter(index => board[index] === '');
        const playerInPattern = pattern.filter(index => board[index] === player);
        const opponentsInPattern = pattern.filter(index => board[index] !== '' && board[index] !== player);

        if (emptyInPattern.length === 2 && playerInPattern.length === 2 && opponentsInPattern.length === 0) {
            return emptyInPattern[0];
        }
    }

    return -1;
}
