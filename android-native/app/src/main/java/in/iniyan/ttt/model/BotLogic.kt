package `in`.iniyan.ttt.model

import kotlin.random.Random

object BotLogic {

    fun botMove(board: List<String>, player: Player, difficulty: Difficulty): Int? {
        return when (difficulty) {
            Difficulty.EASY -> easyBotMove(board)
            Difficulty.MEDIUM -> if (Random.nextFloat() < 0.5f) easyBotMove(board) else smartMove(board, player)
            Difficulty.HARD -> smartMove(board, player)
        }
    }

    private fun easyBotMove(board: List<String>): Int? {
        val emptyCells = board.indices.filter { board[it].isEmpty() }
        return if (emptyCells.isEmpty()) null else emptyCells.random()
    }

    private fun smartMove(board: List<String>, player: Player): Int? {
        // Try to win
        findWinningMove(board, player)?.let { return it }

        // Block opponents
        val opponents = Player.entries.filter { it != player }
        for (opponent in opponents) {
            findWinningMove(board, opponent)?.let { return it }
        }

        // Try fork
        findForkMove(board, player)?.let { return it }

        // Block opponent fork
        for (opponent in opponents) {
            findForkMove(board, opponent)?.let { return it }
        }

        // Strategic move
        findStrategicMove(board, player)?.let { return it }

        // Prefer corners and center
        val preferred = listOf(0, 4, 20, 24, 12)
        for (move in preferred) {
            if (board[move].isEmpty()) return move
        }

        // Random
        return easyBotMove(board)
    }

    private fun findWinningMove(board: List<String>, player: Player): Int? {
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val testBoard = board.toMutableList()
                testBoard[i] = player.symbol
                if (GameLogic.checkWinner(testBoard).winner == player.symbol) {
                    return i
                }
            }
        }
        return null
    }

    private fun findForkMove(board: List<String>, player: Player): Int? {
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val testBoard = board.toMutableList()
                testBoard[i] = player.symbol
                var winningMoves = 0
                for (j in board.indices) {
                    if (testBoard[j].isEmpty()) {
                        val secondTest = testBoard.toMutableList()
                        secondTest[j] = player.symbol
                        if (GameLogic.checkWinner(secondTest).winner == player.symbol) {
                            winningMoves++
                        }
                    }
                }
                if (winningMoves >= 2) return i
            }
        }
        return null
    }

    private fun findStrategicMove(board: List<String>, player: Player): Int? {
        val strategicPatterns = listOf(
            listOf(0, 1, 2, 3), listOf(1, 2, 3, 4), listOf(5, 6, 7, 8), listOf(6, 7, 8, 9),
            listOf(10, 11, 12, 13), listOf(11, 12, 13, 14), listOf(15, 16, 17, 18), listOf(16, 17, 18, 19),
            listOf(20, 21, 22, 23), listOf(21, 22, 23, 24),
            listOf(0, 5, 10, 15), listOf(5, 10, 15, 20), listOf(1, 6, 11, 16), listOf(6, 11, 16, 21),
            listOf(2, 7, 12, 17), listOf(7, 12, 17, 22), listOf(3, 8, 13, 18), listOf(8, 13, 18, 23),
            listOf(4, 9, 14, 19), listOf(9, 14, 19, 24),
            listOf(0, 6, 12, 18), listOf(6, 12, 18, 24), listOf(4, 8, 12, 16), listOf(8, 12, 16, 20),
            // 2x2 patterns
            listOf(0, 1, 5, 6), listOf(1, 2, 6, 7), listOf(2, 3, 7, 8), listOf(3, 4, 8, 9),
            listOf(5, 6, 10, 11), listOf(6, 7, 11, 12), listOf(7, 8, 12, 13), listOf(8, 9, 13, 14),
            listOf(10, 11, 15, 16), listOf(11, 12, 16, 17), listOf(12, 13, 17, 18), listOf(13, 14, 18, 19),
            listOf(15, 16, 20, 21), listOf(16, 17, 21, 22), listOf(17, 18, 22, 23), listOf(18, 19, 23, 24)
        )

        for (pattern in strategicPatterns) {
            val empty = pattern.filter { board[it].isEmpty() }
            val playerCells = pattern.filter { board[it] == player.symbol }
            val opponentCells = pattern.filter { board[it].isNotEmpty() && board[it] != player.symbol }

            if (empty.size == 2 && playerCells.size == 2 && opponentCells.isEmpty()) {
                return empty[0]
            }
        }
        return null
    }
}
