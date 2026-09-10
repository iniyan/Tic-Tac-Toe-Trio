package `in`.iniyan.ttt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.iniyan.ttt.data.database.AppDatabase
import `in`.iniyan.ttt.data.entity.Achievement
import `in`.iniyan.ttt.gamification.XPManager
import `in`.iniyan.ttt.model.Player
import `in`.iniyan.ttt.model.Screen
import `in`.iniyan.ttt.ui.components.*
import `in`.iniyan.ttt.ui.screens.GameScreen
import `in`.iniyan.ttt.ui.screens.SetupScreen
import `in`.iniyan.ttt.ui.theme.TicTacToeTrioTheme
import `in`.iniyan.ttt.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTrioTheme {
                TicTacToeTrioApp()
            }
        }
    }
}

@Composable
fun TicTacToeTrioApp(viewModel: GameViewModel = viewModel()) {
    val state = viewModel.state

    when (state.screen) {
        Screen.SETUP -> {
            SetupScreen(
                playerName = state.playerName,
                difficulty = state.difficulty,
                maxGames = state.maxGames,
                totalXP = state.totalXP,
                currentLevel = state.currentLevel,
                currentStreak = state.currentStreak,
                bestStreak = state.bestStreak,
                hintTokens = state.hintTokens,
                hasShield = state.hasShield,
                dailyChallengeCompleted = state.dailyChallengeCompleted,
                dailyChallengeExpiresAt = state.dailyChallengeExpiresAt,
                isPowerPlayerMode = state.isPowerPlayerMode,
                canUsePowerPlayer = state.currentLevel.ordinal >= XPManager.PlayerLevel.STRATEGIST.ordinal,
                onPlayerNameChange = viewModel::updatePlayerName,
                onDifficultyChange = viewModel::updateDifficulty,
                onMaxGamesChange = viewModel::updateMaxGames,
                onStartSeries = viewModel::startNewSeries,
                onShowRules = viewModel::showRules,
                onShowAchievements = viewModel::showAchievements,
                onShowSkins = viewModel::showSkins,
                onTogglePowerPlayer = viewModel::togglePowerPlayerMode
            )
        }

        Screen.GAME -> {
            GameScreen(
                state = state,
                onCellClick = viewModel::playTurn,
                onNewGame = viewModel::onNewGame,
                onAbandonSeries = viewModel::requestAbandonSeries,
                onNewSeries = { viewModel.abandonSeries() },
                onUseHint = viewModel::useHintToken
            )
        }
    }

    // Dialogs
    if (state.showRulesDialog) {
        RulesDialog(onDismiss = viewModel::hideRules)
    }

    if (state.showStatsDialog) {
        SeriesStatsDialog(
            seriesWinnerText = viewModel.getSeriesWinnerText(),
            seriesStats = state.seriesStats,
            playerName = state.playerName,
            onDismiss = viewModel::dismissStats
        )
    }

    if (state.showSurrenderDialog) {
        SurrenderDialog(
            playerName = state.playerName,
            xWins = state.seriesStats[Player.X] ?: 0,
            maxOtherWins = maxOf(state.seriesStats[Player.O] ?: 0, state.seriesStats[Player.N] ?: 0),
            onConfirm = viewModel::confirmAbandonSeries,
            onCancel = viewModel::cancelAbandonSeries
        )
    }

    if (state.showPostGameScreen) {
        PostGameDialog(
            postGameInfo = state.postGameInfo,
            onDismiss = viewModel::dismissPostGame
        )
    }

    if (state.showAchievementsScreen) {
        // Collect achievements from DB
        val db = AppDatabase.getInstance(androidx.compose.ui.platform.LocalContext.current)
        val achievements by db.achievementDao().getAllAchievements()
            .collectAsState(initial = emptyList())

        AchievementsDialog(
            achievements = achievements,
            onDismiss = viewModel::hideAchievements
        )
    }

    if (state.showSkinsScreen) {
        SkinsDialog(
            currentLevel = state.currentLevel,
            activeSkinId = "default_x",
            onSelectSkin = { /* TODO: wire up skin selection */ },
            onDismiss = viewModel::hideSkins
        )
    }
}
