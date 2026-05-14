package `in`.iniyan.tictactoetrio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun RulesDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 25.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "How to Play",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlayerNBlue
                )

                RuleSection("Basic Rules", listOf(
                    "Play on a 5\u00d75 grid with 3 players",
                    "You are X (green), against Blocker O (orange) and Hunter N (blue)",
                    "Take turns: X \u2192 O \u2192 N",
                    "Win by getting 4 in a row (horizontal, vertical, or diagonal)",
                    "OR win by forming a 2\u00d72 square of your symbol"
                ))

                RuleSection("Series Rules", listOf(
                    "Play a series of 1, 3, 5, or 10 games",
                    "Each win earns 1 point",
                    "Player with the most points wins the series",
                    "If scores are tied, it's a draw"
                ))

                RuleSection("Difficulty", listOf(
                    "Easy: AI makes random moves",
                    "Medium: AI plays smart 50% of the time",
                    "Hard: AI always plays the best move"
                ))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PlayerNBlue)
                ) {
                    Text("Got it!", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun RuleSection(title: String, rules: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PlayerXGreen
        )
        rules.forEach { rule ->
            Text(
                "\u2022 $rule",
                fontSize = 13.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )
        }
    }
}
