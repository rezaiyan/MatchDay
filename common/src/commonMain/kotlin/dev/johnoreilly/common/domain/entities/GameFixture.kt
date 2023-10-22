package dev.johnoreilly.common.domain.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class GameFixture(
    val id: Int,
    val localKickoffTime: LocalDateTime,
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamPhotoUrl: String,
    val awayTeamPhotoUrl: String,
    val homeTeamScore: Int?,
    val awayTeamScore: Int?,
    val event: Int,
    val prediction: Prediction?,
) {

    val isPredicted: Boolean = prediction != null
    val isNotStartedYet = localKickoffTime.isPast()
    val isLive = isFixtureLive()

    val homeScore = if (isNotStartedYet) RESULT_PLACE_HOLDER else homeTeamScore.toString()
    val awayScore = if (isNotStartedYet) RESULT_PLACE_HOLDER else awayTeamScore.toString()

    companion object {
        private const val RESULT_PLACE_HOLDER = "-"
        val mockGameFixture = GameFixture(
            id = 0,
            localKickoffTime = LocalDateTime.parse("2023-12-14T11:30"),
            homeTeam = "Brentford",
            awayTeam = "Arsenal",
            homeTeamPhotoUrl = "https://resources.premierleague.com/premierleague/badges/t8.svg",
            awayTeamPhotoUrl = "https://resources.premierleague.com/premierleague/badges/t3.svg",
            homeTeamScore = 2,
            awayTeamScore = 0,
            event = 1,
            prediction = null,

            )
    }

    fun getPredictionMessage(): String {
        if (isPredicted.not() || homeScore == RESULT_PLACE_HOLDER || awayScore == RESULT_PLACE_HOLDER) return ""

        val homeDifference = prediction!!.homeScores.toInt() - homeTeamScore!!
        val awayDifference = prediction.awayScores.toInt() - awayTeamScore!!

        val totalDifference = homeDifference + awayDifference

        return when {
            homeDifference == 0 && awayDifference == 0 -> "Wow! You predicted the exact score for both teams. You're a prediction genius!"
            homeDifference in -2..2 && awayDifference in -2..2 -> "Impressive! You were very close for both teams. Keep up the good work!"
            totalDifference in -5..5 -> "You were close on the total score. Keep predicting, and you'll get even better!"
            homeDifference < -5 || awayDifference < -5 -> "It was a tough one, but don't give up. Predicting can be tricky sometimes!"
            else -> "Your prediction was quite different from the result. No worries, better luck next time!"
        }
    }

}

fun GameFixture.isFixtureLive(): Boolean {
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val fixtureMillis = localKickoffTime.toInstant(TimeZone.UTC).toEpochMilliseconds()
    val currentMillis = currentDateTime.toInstant(TimeZone.UTC).toEpochMilliseconds()

    val durationMinutes = (currentMillis - fixtureMillis) / (1000 * 60) // Milliseconds to minutes

    return durationMinutes in 1..95
}

fun LocalDateTime.isPast(): Boolean =
    this > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())