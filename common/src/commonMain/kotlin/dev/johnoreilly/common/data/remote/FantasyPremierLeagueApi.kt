package dev.johnoreilly.common.data.remote

import dev.johnoreilly.common.data.model.BootstrapStaticInfoDto
import dev.johnoreilly.common.data.model.ElementSummaryDto
import dev.johnoreilly.common.data.model.EventStatusListDto
import dev.johnoreilly.common.data.model.FixtureDto
import dev.johnoreilly.common.data.model.GameWeekLiveDataDto
import dev.johnoreilly.common.data.model.LeagueStandingsDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent


class FantasyPremierLeagueApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://fantasy.premierleague.com/api",
) : KoinComponent {

    suspend fun fetchBootstrapStaticInfo() = client.get("$baseUrl/bootstrap-static/").body<BootstrapStaticInfoDto>()
    suspend fun fetchFixtures() = client.get("$baseUrl/fixtures").body<List<FixtureDto>>()
    suspend fun fetchUpcomingFixtures() = client.get("$baseUrl/fixtures?future=1").body<List<FixtureDto>>()
    suspend fun fetchGameWeekLiveData(eventId: Int) = client.get("$baseUrl/event/$eventId/live/").body<GameWeekLiveDataDto>()
    suspend fun fetchPlayerData(playerId: Int) = client.get("$baseUrl/element-summary/$playerId/").body<ElementSummaryDto>()
    suspend fun fetchLeagueStandings(leagueId: Int) = client.get("$baseUrl/leagues-classic/$leagueId/standings/").body<LeagueStandingsDto>()
    suspend fun fetchEventStatus() = client.get("$baseUrl/event-status/").body<EventStatusListDto>()
}
