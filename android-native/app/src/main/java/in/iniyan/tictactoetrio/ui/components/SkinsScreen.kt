package `in`.iniyan.tictactoetrio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import `in`.iniyan.tictactoetrio.gamification.SkinDefinition
import `in`.iniyan.tictactoetrio.gamification.SkinManager
import `in`.iniyan.tictactoetrio.gamification.XPManager
import `in`.iniyan.tictactoetrio.ui.theme.*

@Composable
fun SkinsDialog(
    currentLevel: XPManager.PlayerLevel,
    activeSkinId: String?,
    onSelectSkin: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 25.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    "Customize",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Symbol skins
                SkinSection(
                    title = "X Symbol Skins",
                    skins = SkinManager.symbolSkins,
                    currentLevel = currentLevel,
                    activeSkinId = activeSkinId,
                    onSelect = onSelectSkin
                )

                // Board themes
                SkinSection(
                    title = "Board Themes",
                    skins = SkinManager.boardSkins,
                    currentLevel = currentLevel,
                    activeSkinId = activeSkinId,
                    onSelect = onSelectSkin
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Close", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SkinSection(
    title: String,
    skins: List<SkinDefinition>,
    currentLevel: XPManager.PlayerLevel,
    activeSkinId: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            skins.forEach { skin ->
                val isLocked = SkinManager.isLocked(skin, currentLevel)
                val isActive = skin.id == activeSkinId

                SkinCard(
                    skin = skin,
                    isLocked = isLocked,
                    isActive = isActive,
                    onClick = { if (!isLocked) onSelect(skin.id) }
                )
            }
        }
    }
}

@Composable
private fun SkinCard(
    skin: SkinDefinition,
    isLocked: Boolean,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .then(if (isLocked) Modifier.alpha(0.5f) else Modifier)
            .then(
                if (isActive) Modifier.border(2.dp, PrimaryPurple, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(enabled = !isLocked) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) PrimaryPurple.copy(alpha = 0.1f) else SurfaceWhite
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isLocked)
                            Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
                        else
                            Brush.linearGradient(listOf(PrimaryPurple, PrimaryPurpleEnd)),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isLocked) "🔒" else skin.name.first().toString(),
                    fontSize = if (isLocked) 20.sp else 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                skin.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            if (isLocked) {
                Text(
                    "Lvl ${skin.unlockLevel.title}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            } else if (isActive) {
                Text(
                    "Active",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            }
        }
    }
}
