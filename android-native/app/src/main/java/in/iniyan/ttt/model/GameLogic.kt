package `in`.iniyan.ttt.model

object GameLogic {

    private val winningPatterns = listOf(
        // Horizontal 4-in-a-row
        listOf(0, 1, 2, 3), listOf(1, 2, 3, 4),
        listOf(5, 6, 7, 8), listOf(6, 7, 8, 9),
        listOf(10, 11, 12, 13), listOf(11, 12, 13, 14),
        listOf(15, 16, 17, 18), listOf(16, 17, 18, 19),
        listOf(20, 21, 22, 23), listOf(21, 22, 23, 24),
        // Vertical 4-in-a-row
        listOf(0, 5, 10, 15), listOf(1, 6, 11, 16),
        listOf(2, 7, 12, 17), listOf(3, 8, 13, 18),
        listOf(4, 9, 14, 19), listOf(5, 10, 15, 20),
        listOf(6, 11, 16, 21), listOf(7, 12, 17, 22),
        listOf(8, 13, 18, 23), listOf(9, 14, 19, 24),
        // Diagonal 4-in-a-row
        listOf(0, 6, 12, 18), listOf(1, 7, 13, 19),
        listOf(3, 7, 11, 15), listOf(4, 8, 12, 16),
        listOf(5, 11, 17, 23), listOf(6, 12, 18, 24),
        listOf(8, 12, 16, 20), listOf(9, 13, 17, 21)
    )

    fun makeMove(board: List<String>, index: Int, player: Player): List<String>? {
        if (board[index].isNotEmpty()) return null
        return board.toMutableList().also { it[index] = player.symbol }
    }

    fun checkWinner(board: List<String>): WinResult {
        // Check 4-in-a-row patterns
        for (pattern in winningPatterns) {
            val (a, b, c, d) = pattern
            if (board[a].isNotEmpty() &&
                board[a] == board[b] &&
                board[a] == board[c] &&
                board[a] == board[d]
            ) {
                return WinResult(winner = board[a], pattern = pattern)
            }
        }

        // Check 2x2 squares
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val index = i * 5 + j
                if (board[index].isNotEmpty() &&
                    board[index] == board[index + 1] &&
                    board[index] == board[index + 5] &&
                    board[index] == board[index + 6]
                ) {
                    return WinResult(
                        winner = board[index],
                        pattern = listOf(index, index + 1, index + 5, index + 6)
                    )
                }
            }
        }

        // Check draw
        if (board.all { it.isNotEmpty() }) {
            return WinResult(winner = "draw", pattern = null)
        }

        return WinResult()
    }
}
