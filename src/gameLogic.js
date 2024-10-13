export function makeMove(board, index, player) {
    if (board[index] === '') {
        board[index] = player;
        return true;
    }
    return false;
}

export function checkWinner(board) {
    // Check for 4 in a row (horizontal, vertical, diagonal)
    const winningPatterns = [
        // Horizontal wins
        [0, 1, 2, 3], [1, 2, 3, 4], [5, 6, 7, 8], [6, 7, 8, 9], [10, 11, 12, 13], [11, 12, 13, 14],
        [15, 16, 17, 18], [16, 17, 18, 19], [20, 21, 22, 23], [21, 22, 23, 24],
        // Vertical wins
        [0, 5, 10, 15], [1, 6, 11, 16], [2, 7, 12, 17], [3, 8, 13, 18], [4, 9, 14, 19],
        [5, 10, 15, 20], [6, 11, 16, 21], [7, 12, 17, 22], [8, 13, 18, 23], [9, 14, 19, 24],
        // Diagonal wins
        [0, 6, 12, 18], [1, 7, 13, 19], [3, 7, 11, 15], [4, 8, 12, 16],
        [5, 11, 17, 23], [6, 12, 18, 24], [8, 12, 16, 20], [9, 13, 17, 21]
    ];

    for (const pattern of winningPatterns) {
        const [a, b, c, d] = pattern;
        if (board[a] && board[a] === board[b] && board[a] === board[c] && board[a] === board[d]) {
            return { winner: board[a], pattern }; // Return the winner and the winning pattern
        }
    }

    // Check for 2x2 square
    for (let i = 0; i < 4; i++) {
        for (let j = 0; j < 4; j++) {
            const index = i * 5 + j;
            if (board[index] && 
                board[index] === board[index + 1] && 
                board[index] === board[index + 5] && 
                board[index] === board[index + 6]) {
                return { winner: board[index], pattern: [index, index + 1, index + 5, index + 6] };
            }
        }
    }

    // Check if the board is full (draw)
    if (board.every(cell => cell !== '')) {
        return { winner: 'draw', pattern: null };
    }

    return { winner: null, pattern: null }; // No winner yet
}

function checkLine(board, start, step, length) {
    const player = board[start];
    if (player === '') return false;
    for (let i = 1; i < length; i++) {
        if (board[start + i * step] !== player) return false;
    }
    return true;
}