package `in`.iniyan.ttt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.iniyan.ttt.model.Player
import `in`.iniyan.ttt.ui.theme.*

@Composable
fun SeriesStatsDialog(
    seriesWinnerText: String,
    seriesStats: Map<Player, Int>,
    playerName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 25.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Series Complete!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlayerXGreen
                )

                Text(
                    seriesWinnerText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatRow(
                        label = "${playerName.ifEmpty { "Player" }} wins:",
                        value = "${seriesStats[Player.X] ?: 0}",
                        color = PlayerXGreen
                    )
                    StatRow(
                        label = "Blocker wins:",
                        value = "${seriesStats[Player.O] ?: 0}",
                        color = PlayerOOrange
                    )
                    StatRow(
                        label = "Hunter wins:",
                        value = "${seriesStats[Player.N] ?: 0}",
                        color = PlayerNBlue
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                Modifier.background(
                                    Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd)),
                                    RoundedCornerShape(12.dp)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("New Series", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = TextPrimary)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
