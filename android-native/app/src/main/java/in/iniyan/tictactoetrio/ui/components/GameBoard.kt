package `in`.iniyan.tictactoetrio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun GameBoard(
    board: List<String>,
    winningPattern: List<Int>?,
    highlightedHintCell: Int? = null,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (row in 0 until 5) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (col in 0 until 5) {
                        val index = row * 5 + col
                        val isWinning = winningPattern?.contains(index) == true
                        val isHinted = highlightedHintCell == index
                        GameCell(
                            symbol = board[index],
                            isWinning = isWinning,
                            isHinted = isHinted,
                            onClick = { onCellClick(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameCell(
    symbol: String,
    isWinning: Boolean,
    isHinted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cell_anim")
    val winScale by infiniteTransition.animateFloat(
        initialValue = 1.05f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "win_scale"
    )
    val hintAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hint_alpha"
    )

    val bgBrush = when {
        isWinning -> Brush.linearGradient(listOf(WinningPurple, WinningPurpleEnd))
        symbol == "X" -> Brush.linearGradient(listOf(PlayerXGreen, PlayerXGreenEnd))
        symbol == "O" -> Brush.linearGradient(listOf(PlayerOOrange, PlayerOOrangeEnd))
        symbol == "N" -> Brush.linearGradient(listOf(PlayerNBlue, PlayerNBlueEnd))
        else -> Brush.linearGradient(listOf(Color.White, Color.White))
    }

    val textColor = if (symbol.isNotEmpty() || isWinning) Color.White else Color.Transparent

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(if (isWinning) Modifier.scale(winScale) else Modifier)
            .shadow(
                elevation = if (symbol.isNotEmpty()) 4.dp else 1.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(bgBrush)
            .then(
                if (isHinted) Modifier.border(
                    2.dp,
                    PlayerXGreen.copy(alpha = hintAlpha),
                    RoundedCornerShape(8.dp)
                ) else Modifier
            )
            .clickable(enabled = symbol.isEmpty() && !isWinning) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isHinted && symbol.isEmpty()) {
            Text(
                text = "X",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PlayerXGreen.copy(alpha = hintAlpha)
            )
        } else {
            Text(
                text = symbol,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
