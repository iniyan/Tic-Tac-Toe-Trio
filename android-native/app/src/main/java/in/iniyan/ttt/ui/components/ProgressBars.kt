package `in`.iniyan.ttt.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.iniyan.ttt.model.Player
import `in`.iniyan.ttt.ui.theme.*

@Composable
fun ProgressBars(
    seriesStats: Map<Player, Int>,
    maxGames: Int,
    playerName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayerProgressBar(
            name = playerName.ifEmpty { "Player" },
            symbol = "X",
            wins = seriesStats[Player.X] ?: 0,
            maxGames = maxGames,
            gradientColors = listOf(PlayerXGreen, PlayerXGreenEnd),
            modifier = Modifier.weight(1f)
        )
        PlayerProgressBar(
            name = "Blocker",
            symbol = "O",
            wins = seriesStats[Player.O] ?: 0,
            maxGames = maxGames,
            gradientColors = listOf(PlayerOOrange, PlayerOOrangeEnd),
            modifier = Modifier.weight(1f)
        )
        PlayerProgressBar(
            name = "Hunter",
            symbol = "N",
            wins = seriesStats[Player.N] ?: 0,
            maxGames = maxGames,
            gradientColors = listOf(PlayerNBlue, PlayerNBlueEnd),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlayerProgressBar(
    name: String,
    symbol: String,
    wins: Int,
    maxGames: Int,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (maxGames > 0) wins.toFloat() / maxGames else 0f,
        animationSpec = tween(400),
        label = "progress"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BorderGray.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .background(SurfaceWhite.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                maxLines = 1
            )
            Text(
                text = "$wins",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Brush.horizontalGradient(gradientColors))
            )
        }
    }
}
