import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.common.data.repository.FantasyPremierLeagueRepository
import dev.johnoreilly.common.di.initKoin
import dev.johnoreilly.common.domain.entities.Player
import dev.johnoreilly.common.domain.entities.PlayerPastHistory
import dev.johnoreilly.common.ui.PlayerDetailsViewShared
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.players.PlayerView


private val koin = initKoin(enableNetworkLogs = true).koin

val lightThemeColors = lightColors(
    primary = Color(0xFFDD0D3C),
    primaryVariant = Color(0xFFC20029),
    secondary = Color.White,
    error = Color(0xFFD00036)
)


fun main() = application {
    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Fantasy Premier League"
    ) {
        MaterialTheme(
            colors = lightThemeColors
        ) {
            MainLayout()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Composable
fun MainLayout() {
    val selectedPlayer = remember { mutableStateOf<Player?>(null) }
    val repository = koin.get<FantasyPremierLeagueRepository>()

    val searchQuery = MutableStateFlow("")
    val playerList by searchQuery.debounce(250).flatMapLatest { searchQueryValue ->
        repository.playerList.mapLatest { playerList ->
            playerList
                .filter { it.name.contains(searchQueryValue, ignoreCase = true) }
                .sortedByDescending { it.points }
        }
    }.collectAsState(emptyList())

    BoxWithConstraints {
        if (maxWidth.value > 700) {
            TwoColumnsLayout(playerList, selectedPlayer, repository)
        } else {
            PlayerListView(playerList, selectedPlayer.value) {}
        }
    }
}


@Composable
fun TwoColumnsLayout(playerList: List<Player>, selectedPlayer: MutableState<Player?>, repository: FantasyPremierLeagueRepository) {
    val scope = rememberCoroutineScope()
    var playerHistory by remember { mutableStateOf(emptyList<PlayerPastHistory>()) }

    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth(0.3f), contentAlignment = Alignment.Center) {
            PlayerListView(playerList, selectedPlayer.value) {
                scope.launch {
                    selectedPlayer.value = it
                    playerHistory = repository.getPlayerHistoryData(it.id)
                }
            }
        }
        selectedPlayer.value?.let { player ->
            PlayerDetailsViewShared(player, playerHistory)
        }
    }
}


@Composable
fun PlayerListView(playerList: List<Player>, selectedPlayer: Player?, playerSelected: (player: Player) -> Unit) {
    Box(modifier = Modifier
            .padding(3.dp)
            .background(color = Color.White)
            .clip(shape = RoundedCornerShape(3.dp))
    ) {
        LazyColumn {
            items(items = playerList, itemContent = { player ->
                PlayerView(player, selectedPlayer, playerSelected)
            })
        }
    }
}