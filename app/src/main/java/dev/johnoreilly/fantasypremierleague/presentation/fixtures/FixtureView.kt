package dev.johnoreilly.fantasypremierleague.presentation.fixtures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.placeholder
import dev.johnoreilly.common.domain.entities.GameFixture
import dev.johnoreilly.common.domain.entities.Prediction
import dev.johnoreilly.fantasypremierleague.presentation.global.lowfidelitygray
import dev.johnoreilly.fantasypremierleague.presentation.global.maroon200

@Composable
fun FixtureView(
    fixture: GameFixture,
    onFixtureSelected: (fixtureId: Int) -> Unit,
    isDataLoading: Boolean,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .clickable { onFixtureSelected(fixture.id) }
            .placeholder(visible = isDataLoading, lowfidelitygray),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            if (fixture.isLive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = if (fixture.isPredicted) {
                            "Prediction submitted! Watch the game live!"
                        } else {
                            "Live!"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFFC5A8FF),
                                        Color(0xFF7E43A3),
                                    )
                                )
                            )
                            .padding(8.dp)
                    )
                }

            } else if (fixture.isNotStartedYet) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = if (fixture.isPredicted) {
                            "Prediction submitted!"
                        } else {
                            "Join the Betting Action!"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFFC5A8FF),
                                        Color(0xFF7E43A3),
                                    )
                                )
                            )
                            .padding(8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClubInFixtureView(
                    fixture.homeTeam,
                    fixture.homeTeamPhotoUrl
                )
                Text(
                    text = fixture.homeScore,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Divider(
                    modifier = Modifier
                        .heightIn(min = 20.dp, max = 30.dp)
                        .width(1.dp)
                        .background(color = maroon200)
                )
                Text(
                    text = fixture.awayScore,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                ClubInFixtureView(
                    fixture.awayTeam,
                    fixture.awayTeamPhotoUrl
                )
            }
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = fixture.localKickoffTime.date.toString(),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )

            fixture.localKickoffTime.let { localKickoffTime ->
                val formattedTime =
                    "%02d:%02d".format(localKickoffTime.hour, localKickoffTime.minute)
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = formattedTime,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ClubInFixtureView(
    teamName: String,
    teamPhotoUrl: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = teamPhotoUrl,
            contentDescription = teamName,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(60.dp)
        )
        Text(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 4.dp),
            text = teamName,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewFixtureView() {
    val placeholderKickoffTime = kotlinx.datetime.LocalDateTime(2022, 9, 5, 13, 30, 0)
    Column(modifier = Modifier.height(200.dp)) {
        FixtureView(
            fixture = GameFixture(
                id = 1,
                localKickoffTime = placeholderKickoffTime,
                homeTeam = "Liverpool",
                "Spurs",
                "",
                "",
                3,
                0,
                5,
                prediction = Prediction(
                    fixtureId = 1,
                    homeScores = "1",
                    awayScores = "0"
                )
            ),
            onFixtureSelected = {},
            isDataLoading = false
        )
    }
}