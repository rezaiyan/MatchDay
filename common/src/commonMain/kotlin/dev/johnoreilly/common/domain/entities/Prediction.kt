package dev.johnoreilly.common.domain.entities


data class Prediction(
    val id: Int? = null,
    val fixtureId: Int,
    val homeScores: String,
    val awayScores: String,
)
