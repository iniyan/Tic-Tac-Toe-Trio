package `in`.iniyan.tictactoetrio.gamification

import `in`.iniyan.tictactoetrio.data.entity.Achievement

object AchievementDefinitions {

    fun allAchievements(): List<Achievement> = listOf(
        Achievement("first_blood", "First Blood", "Win your first game"),
        Achievement("triple_threat", "Triple Threat", "Win 3 games in a row"),
        Achievement("untouchable", "Untouchable", "Complete a 5-series without losing once"),
        Achievement("speed_demon", "Speed Demon", "Win a game in under 10 moves"),
        Achievement("comeback_kid", "Comeback Kid", "Win after trailing in a series"),
        Achievement("exterminator", "Exterminator", "Beat Hard AI 10 times total"),
        Achievement("strategist", "Strategist", "Win using the 2\u00d72 square pattern"),
        Achievement("diagonal_master", "Diagonal Master", "Win using a diagonal 4-in-a-row 5 times"),
        Achievement("marathon_runner", "Marathon Runner", "Play 100 total games"),
        Achievement("daily_warrior", "Daily Warrior", "Play 7 days in a row"),
        Achievement("perfect_series", "Perfect Series", "Win all games in a 10-game series"),
        Achievement("lone_wolf", "Lone Wolf", "Win a game where both AIs had 3+ points"),
        Achievement("centurion", "Centurion", "Reach 100 total wins"),
        Achievement("xp_hunter", "XP Hunter", "Earn 1000 XP total"),
        Achievement("grandmaster", "Grandmaster", "Reach Grandmaster level"),
        Achievement("legendary", "Legendary", "Reach Legendary level"),
        Achievement("shield_bearer", "Shield Bearer", "Earn your first Shield"),
        Achievement("mystery_opener", "Mystery Opener", "Open your first Mystery Box"),
        Achievement("daily_streak_14", "Dedicated Player", "Complete 14 daily challenges in a row"),
        Achievement("outnumbered", "Outnumbered", "Win a series in Power Player Mode"),
        Achievement("collector", "Collector", "Unlock 3 skins"),
        Achievement("five_streak", "On Fire", "Win 5 games in a row")
    )
}
