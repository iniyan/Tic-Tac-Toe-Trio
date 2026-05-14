package `in`.iniyan.tictactoetrio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.iniyan.tictactoetrio.model.GameState
import `in`.iniyan.tictactoetrio.model.Player
import `in`.iniyan.tictactoetrio.ui.components.GameBoard
import `in`.iniyan.tictactoetrio.ui.components.ProgressBars
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun GameScreen(
    state: GameState,
    onCellClick: (Int) -> Unit,
    onNewGame: () -> Unit,
    onAbandonSeries: () -> Unit,
    onNewSeries: () -> Unit,
    onUseHint: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd))
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Progress bars
                ProgressBars(
                    seriesStats = state.seriesStats,
                    maxGames = state.maxGames,
                    playerName = state.playerName
                )

                // Game progress + hint tokens row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Streak + Shield indicator
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (state.currentStreak > 0) {
                            Text(
                                "${state.currentStreak} streak",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PlayerXGreen
                            )
                        }
                        if (state.hasShield) {
                            Box(
                                modifier = Modifier.size(18.dp).clip(CircleShape)
                                    .background(PlayerNBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("S", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Text(
                        text = "Game ${state.currentGame} of ${state.maxGames}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    // Hint button
                    if (state.hintTokens > 0 && state.winner == null && state.currentPlayer == Player.X) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PlayerNBlue.copy(alpha = 0.15f))
                                .clickable { onUseHint() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Hint (${state.hintTokens})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PlayerNBlue
                            )
                        }
                    }
                }

                // Game board
                GameBoard(
                    board = state.board,
                    winningPattern = state.winningPattern,
                    highlightedHintCell = state.highlightedHintCell,
                    onCellClick = onCellClick
                )

                // Status message
                if (state.statusMessage.isNotEmpty()) {
                    Text(
                        text = state.statusMessage,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                // Turn indicator
                if (state.winner == null && !state.isBotThinking) {
                    val turnColor = when (state.currentPlayer) {
                        Player.X -> PlayerXGreen
                        Player.O -> PlayerOOrange
                        Player.N -> PlayerNBlue
                    }
                    val turnName = when (state.currentPlayer) {
                        Player.X -> state.playerName.ifEmpty { "Player" }
                        Player.O -> "Blocker"
                        Player.N -> "Hunter"
                    }
                    Text(
                        text = "$turnName's turn (${state.currentPlayer.symbol})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = turnColor
                    )
                } else if (state.isBotThinking) {
                    Text(
                        text = "Thinking...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }

                // Control buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GradientButton("New Game", listOf(ButtonSecondaryStart, ButtonSecondaryEnd), onNewGame, Modifier.weight(1f))
                    GradientButton("End Series", listOf(ButtonSecondaryStart, ButtonSecondaryEnd), onAbandonSeries, Modifier.weight(1f))
                    GradientButton("New Series", listOf(PrimaryPurple, PrimaryPurpleEnd), onNewSeries, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GradientButton(text: String, colors: List<Color>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
