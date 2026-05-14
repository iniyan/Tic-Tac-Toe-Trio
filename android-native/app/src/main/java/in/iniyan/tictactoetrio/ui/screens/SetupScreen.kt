package `in`.iniyan.tictactoetrio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.iniyan.tictactoetrio.gamification.XPManager
import `in`.iniyan.tictactoetrio.model.Difficulty
import `in`.iniyan.tictactoetrio.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import `in`.iniyan.tictactoetrio.ui.theme.TicTacToeTrioTheme

@Preview(showBackground = true)
@Composable
fun SetupScreenPreview() {
    TicTacToeTrioTheme {
        SetupScreen(
            playerName = "Player 1",
            difficulty = Difficulty.MEDIUM,
            maxGames = 3,
            totalXP = 120,
            currentLevel = XPManager.PlayerLevel.NOVICE,
            currentStreak = 2,
            bestStreak = 5,
            hintTokens = 3,
            hasShield = true,
            dailyChallengeCompleted = false,
            dailyChallengeExpiresAt = System.currentTimeMillis() + 3600000,
            isPowerPlayerMode = false,
            canUsePowerPlayer = false,
            onPlayerNameChange = {},
            onDifficultyChange = {},
            onMaxGamesChange = {},
            onStartSeries = {},
            onShowRules = {},
            onShowAchievements = {},
            onShowSkins = {},
            onTogglePowerPlayer = {}
        )
    }
}

@Composable
fun SetupScreen(
    playerName: String,
    difficulty: Difficulty,
    maxGames: Int,
    totalXP: Int,
    currentLevel: XPManager.PlayerLevel,
    currentStreak: Int,
    bestStreak: Int,
    hintTokens: Int,
    hasShield: Boolean,
    dailyChallengeCompleted: Boolean,
    dailyChallengeExpiresAt: Long,
    isPowerPlayerMode: Boolean,
    canUsePowerPlayer: Boolean,
    onPlayerNameChange: (String) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onMaxGamesChange: (Int) -> Unit,
    onStartSeries: () -> Unit,
    onShowRules: () -> Unit,
    onShowAchievements: () -> Unit,
    onShowSkins: () -> Unit,
    onTogglePowerPlayer: () -> Unit
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
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Tic Tac Toe Trio",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        letterSpacing = (-0.5).sp
                    )

                    Text(
                        text = "3 Players, 1 Board, Endless Fun",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    // Level badge + XP bar
                    LevelBadge(currentLevel, totalXP)

                    // Stats row: Streak, Hint Tokens, Shield
                    StatsRow(currentStreak, bestStreak, hintTokens, hasShield)

                    // Daily challenge status
                    DailyChallengeStatus(dailyChallengeCompleted, dailyChallengeExpiresAt)

                    Divider(color = BorderGray, modifier = Modifier.padding(vertical = 4.dp))

                    // Player name
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = onPlayerNameChange,
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = BorderGray
                        )
                    )

                    // Series length
                    SeriesLengthSelector(selected = maxGames, onSelect = onMaxGamesChange)

                    // Difficulty
                    DifficultySelector(selected = difficulty, onSelect = onDifficultyChange)

                    // Power Player Mode toggle
                    if (canUsePowerPlayer) {
                        PowerPlayerToggle(isPowerPlayerMode, onTogglePowerPlayer)
                    }

                    // Action buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onShowAchievements,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PlayerOOrange)
                        ) {
                            Text("Badges", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = onShowSkins,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PlayerNBlue)
                        ) {
                            Text("Skins", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Start button
                    Button(
                        onClick = onStartSeries,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd)),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Start Series",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                // Help button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PlayerNBlue, PlayerNBlueEnd))
                        )
                        .clickable { onShowRules() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun LevelBadge(level: XPManager.PlayerLevel, totalXP: Int) {
    val progress = XPManager.getXPProgressInLevel(totalXP)
    val xpToNext = XPManager.getXPToNextLevel(totalXP)

    Card(
        colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${level.ordinal + 1}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        level.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryPurple
                    )
                }
                Text(
                    "$totalXP XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrimaryPurple,
                trackColor = PrimaryPurple.copy(alpha = 0.15f)
            )

            if (xpToNext > 0) {
                Text("$xpToNext XP to next level", fontSize = 11.sp, color = TextSecondary)
            } else {
                Text("Max Level!", fontSize = 11.sp, color = PlayerXGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatsRow(streak: Int, bestStreak: Int, hintTokens: Int, hasShield: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatChip("Streak", "$streak", PlayerXGreen, if (hasShield) "+shield" else null, Modifier.weight(1f))
        StatChip("Best", "$bestStreak", PlayerOOrange, modifier = Modifier.weight(1f))
        StatChip("Hints", "$hintTokens", PlayerNBlue, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color, extra: String? = null, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
            Text(label + if (extra != null) " $extra" else "", fontSize = 11.sp, color = TextSecondary, maxLines = 1)
        }
    }
}

@Composable
private fun DailyChallengeStatus(isCompleted: Boolean, expiresAt: Long) {
    val timeRemaining = remember(expiresAt) {
        val diff = expiresAt - System.currentTimeMillis()
        if (diff <= 0) "Expired"
        else {
            val hours = diff / (1000 * 60 * 60)
            val minutes = (diff / (1000 * 60)) % 60
            "${hours}h ${minutes}m"
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) PlayerXGreen.copy(alpha = 0.1f) else PlayerOOrange.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Daily Challenge", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextDark)
                Text(
                    if (isCompleted) "Completed! 2x XP active" else "Expires in $timeRemaining",
                    fontSize = 11.sp,
                    color = if (isCompleted) PlayerXGreen else PlayerOOrange
                )
            }
            Box(
                modifier = Modifier.size(28.dp).clip(CircleShape)
                    .background(if (isCompleted) PlayerXGreen else PlayerOOrange),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isCompleted) "✓" else "!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun PowerPlayerToggle(isEnabled: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) WinningPurple.copy(alpha = 0.15f) else SurfaceWhite
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Power Player Mode", fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    color = if (isEnabled) WinningPurpleEnd else TextDark)
                Text("Blocker gets 1-move head start", fontSize = 11.sp, color = TextSecondary)
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(checkedTrackColor = WinningPurple, checkedThumbColor = Color.White)
            )
        }
    }
}

@Composable
private fun SeriesLengthSelector(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(1, 3, 5, 10)
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Series Length", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark,
            modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { count ->
                val isSelected = count == selected
                Box(
                    modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(10.dp))
                        .then(if (isSelected) Modifier.background(
                            Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd)), RoundedCornerShape(10.dp))
                        else Modifier.border(1.5.dp, BorderGray, RoundedCornerShape(10.dp)))
                        .clickable { onSelect(count) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("$count", color = if (isSelected) Color.White else TextDark, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun DifficultySelector(selected: Difficulty, onSelect: (Difficulty) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Difficulty", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark,
            modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.entries.forEach { diff ->
                val isSelected = diff == selected
                val color = when (diff) {
                    Difficulty.EASY -> PlayerXGreen
                    Difficulty.MEDIUM -> PlayerOOrange
                    Difficulty.HARD -> Color(0xFFE53E3E)
                }
                Box(
                    modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(10.dp))
                        .then(if (isSelected) Modifier.background(color, RoundedCornerShape(10.dp))
                        else Modifier.border(1.5.dp, BorderGray, RoundedCornerShape(10.dp)))
                        .clickable { onSelect(diff) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(diff.label, color = if (isSelected) Color.White else TextDark,
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}
