package `in`.iniyan.tictactoetrio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun SurrenderDialog(
    playerName: String,
    xWins: Int,
    maxOtherWins: Int,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White.copy(alpha = 0.95f),
        title = {
            Text(
                "Abandon Series?",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53E3E)
            )
        },
        text = {
            Text(
                "You're ahead $xWins\u2013$maxOtherWins. Leaving now forfeits the series and resets your streak. Are you sure?",
                fontSize = 14.sp,
                color = TextDark,
                textAlign = TextAlign.Start,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    "Leave",
                    color = Color(0xFFE53E3E),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    "Stay",
                    color = PrimaryPurple,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}
