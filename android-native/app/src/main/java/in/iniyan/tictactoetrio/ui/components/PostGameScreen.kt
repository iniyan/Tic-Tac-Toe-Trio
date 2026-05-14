package `in`.iniyan.tictactoetrio.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.iniyan.tictactoetrio.gamification.MysteryReward
import `in`.iniyan.tictactoetrio.model.PostGameInfo
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun PostGameDialog(
    postGameInfo: PostGameInfo,
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
                    "Game Results",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // XP Earned
                XPEarnedSection(postGameInfo.xpEarned)

                // Level Up
                if (postGameInfo.leveledUp && postGameInfo.newLevel != null) {
                    LevelUpBanner(postGameInfo.newLevel.title)
                }

                // Achievements
                if (postGameInfo.achievementsUnlocked.isNotEmpty()) {
                    AchievementUnlockSection(postGameInfo.achievementsUnlocked)
                }

                // Mystery Box
                if (postGameInfo.showMysteryBox && postGameInfo.mysteryReward != null) {
                    MysteryBoxSection(postGameInfo.mysteryReward)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(
                                Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd)),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Continue", fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun XPEarnedSection(xp: Int) {
    val animatedXP by animateIntAsState(
        targetValue = xp,
        animationSpec = tween(1000),
        label = "xp"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = PlayerXGreen.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("XP Earned", fontWeight = FontWeight.SemiBold, color = TextDark)
            Text(
                "+$animatedXP XP",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = PlayerXGreen
            )
        }
    }
}

@Composable
private fun LevelUpBanner(levelTitle: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "level_up")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier.scale(scale),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(WinningPurple, WinningPurpleEnd)),
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LEVEL UP!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Text(levelTitle, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun AchievementUnlockSection(achievements: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Achievements Unlocked!",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = PlayerOOrange
        )
        achievements.forEach { name ->
            Card(
                colors = CardDefaults.cardColors(containerColor = PlayerOOrange.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("  ", fontSize = 20.sp) // Trophy placeholder
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(name, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
            }
        }
    }
}

@Composable
private fun MysteryBoxSection(reward: MysteryReward) {
    val infiniteTransition = rememberInfiniteTransition(label = "mystery")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "box_scale"
    )

    Card(
        modifier = Modifier.scale(scale),
        colors = CardDefaults.cardColors(containerColor = PlayerNBlue.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Mystery Box!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PlayerNBlue)
            Text(
                reward.displayName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextDark,
                textAlign = TextAlign.Center
            )
        }
    }
}
