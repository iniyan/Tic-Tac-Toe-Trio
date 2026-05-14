package `in`.iniyan.tictactoetrio.gamification

import `in`.iniyan.tictactoetrio.data.entity.DailyChallenge
import java.text.SimpleDateFormat
import java.util.*

object DailyChallengeGenerator {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Pre-defined challenges (board states + target moves)
    // Board state format: comma-separated 25 cells, empty = ""
    private val challenges = listOf(
        // Challenge 1: Corner trap
        ChallengeTemplate(
            filledCells = mapOf(0 to "X", 4 to "X", 6 to "O", 12 to "N", 18 to "O"),
            targetMoves = 8
        ),
        // Challenge 2: Center control
        ChallengeTemplate(
            filledCells = mapOf(12 to "X", 7 to "O", 17 to "N", 11 to "O", 13 to "N"),
            targetMoves = 7
        ),
        // Challenge 3: Edge play
        ChallengeTemplate(
            filledCells = mapOf(2 to "X", 10 to "O", 14 to "N", 22 to "X", 8 to "O"),
            targetMoves = 6
        ),
        // Challenge 4: Diagonal setup
        ChallengeTemplate(
            filledCells = mapOf(0 to "X", 6 to "X", 12 to "O", 18 to "N", 24 to "O"),
            targetMoves = 7
        ),
        // Challenge 5: Block and win
        ChallengeTemplate(
            filledCells = mapOf(5 to "O", 6 to "O", 7 to "O", 1 to "X", 11 to "X", 21 to "N"),
            targetMoves = 5
        ),
        // Challenge 6: Square opportunity
        ChallengeTemplate(
            filledCells = mapOf(0 to "X", 1 to "X", 5 to "N", 10 to "O", 16 to "O"),
            targetMoves = 8
        ),
        // Challenge 7: Dense board
        ChallengeTemplate(
            filledCells = mapOf(
                0 to "O", 1 to "N", 3 to "X", 5 to "X", 9 to "O",
                10 to "N", 14 to "X", 15 to "O", 19 to "N", 20 to "X"
            ),
            targetMoves = 4
        ),
        // Challenge 8
        ChallengeTemplate(
            filledCells = mapOf(2 to "X", 7 to "X", 12 to "N", 17 to "O", 6 to "O"),
            targetMoves = 7
        ),
        // Challenge 9
        ChallengeTemplate(
            filledCells = mapOf(0 to "N", 4 to "N", 20 to "O", 24 to "O", 12 to "X"),
            targetMoves = 8
        ),
        // Challenge 10
        ChallengeTemplate(
            filledCells = mapOf(
                1 to "X", 3 to "X", 6 to "O", 8 to "O",
                11 to "N", 13 to "N", 16 to "X"
            ),
            targetMoves = 5
        ),
        // Challenges 11-30 follow similar patterns
        ChallengeTemplate(mapOf(0 to "X", 12 to "O", 24 to "N"), 9),
        ChallengeTemplate(mapOf(2 to "O", 7 to "O", 12 to "X", 17 to "X", 22 to "N"), 6),
        ChallengeTemplate(mapOf(0 to "X", 1 to "O", 5 to "N", 6 to "X", 12 to "O"), 7),
        ChallengeTemplate(mapOf(4 to "X", 8 to "N", 12 to "O", 16 to "X", 20 to "N"), 6),
        ChallengeTemplate(mapOf(3 to "O", 8 to "X", 13 to "N", 18 to "X"), 7),
        ChallengeTemplate(mapOf(10 to "X", 11 to "O", 15 to "N", 16 to "X", 21 to "O"), 6),
        ChallengeTemplate(mapOf(0 to "N", 6 to "X", 12 to "X", 18 to "O", 24 to "N"), 7),
        ChallengeTemplate(mapOf(1 to "X", 6 to "O", 11 to "N", 16 to "X", 21 to "O"), 6),
        ChallengeTemplate(mapOf(2 to "X", 3 to "X", 7 to "O", 8 to "N"), 8),
        ChallengeTemplate(mapOf(12 to "X", 6 to "O", 18 to "O", 8 to "N", 16 to "N"), 6),
        ChallengeTemplate(mapOf(0 to "O", 4 to "N", 20 to "N", 24 to "O", 12 to "X", 2 to "X"), 5),
        ChallengeTemplate(mapOf(5 to "X", 10 to "X", 15 to "O", 20 to "N", 6 to "O"), 7),
        ChallengeTemplate(mapOf(1 to "O", 2 to "N", 3 to "O", 11 to "X", 12 to "X"), 6),
        ChallengeTemplate(mapOf(0 to "X", 5 to "X", 10 to "O", 15 to "N", 1 to "O"), 7),
        ChallengeTemplate(mapOf(9 to "X", 13 to "O", 17 to "N", 21 to "X", 14 to "O"), 6),
        ChallengeTemplate(mapOf(0 to "O", 1 to "X", 2 to "N", 5 to "X", 7 to "O", 12 to "N"), 5),
        ChallengeTemplate(mapOf(20 to "X", 21 to "O", 22 to "N", 15 to "X", 16 to "O"), 6),
        ChallengeTemplate(mapOf(4 to "X", 3 to "O", 2 to "N", 9 to "X", 8 to "O"), 7),
        ChallengeTemplate(mapOf(12 to "O", 6 to "X", 18 to "X", 0 to "N", 24 to "N"), 6),
        ChallengeTemplate(mapOf(0 to "X", 4 to "O", 20 to "N", 24 to "X", 12 to "O", 6 to "N"), 5)
    )

    data class ChallengeTemplate(
        val filledCells: Map<Int, String>,
        val targetMoves: Int
    )

    fun getChallengeForDate(date: Date = Date()): DailyChallenge {
        val dateStr = dateFormat.format(date)
        val dayOfYear = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_YEAR)
        val template = challenges[dayOfYear % challenges.size]

        val board = MutableList(25) { "" }
        template.filledCells.forEach { (index, symbol) -> board[index] = symbol }

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        return DailyChallenge(
            date = dateStr,
            boardState = board.joinToString(","),
            targetMoves = template.targetMoves,
            isCompleted = false,
            expiresAt = calendar.timeInMillis
        )
    }

    fun parseBoardState(boardState: String): List<String> {
        return boardState.split(",")
    }
}
