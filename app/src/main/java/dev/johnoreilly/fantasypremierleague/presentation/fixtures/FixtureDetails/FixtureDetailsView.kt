@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package dev.johnoreilly.fantasypremierleague.presentation.fixtures.FixtureDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.johnoreilly.common.domain.entities.GameFixture
import dev.johnoreilly.common.domain.entities.Prediction
import dev.johnoreilly.fantasypremierleague.presentation.fixtures.ClubInFixtureView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixtureDetailsView(
    fixture: GameFixture,
    onSubmitPredict: (Prediction) -> Unit,
    popBackStack: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Fixture details")
                },
                navigationIcon = {
                    IconButton(onClick = { popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                Text(
                    text = "vs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
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

            fixture.localKickoffTime.let { localKickoffTime ->
                val formattedTime =
                    "%02d:%02d".format(localKickoffTime.hour, localKickoffTime.minute)
                PastFixtureStatView(statName = "Date", statValue = localKickoffTime.date.toString())
                PastFixtureStatView(statName = "Kick Off Time", statValue = formattedTime)
            }

            if (fixture.isNotStartedYet || fixture.isPredicted) {
                PredictionView(
                    gameFixture = fixture,
                    onSubmitPredict = onSubmitPredict,
                )
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PredictionView(
    gameFixture: GameFixture,
    onSubmitPredict: (Prediction) -> Unit
) {
    var homeTeamPrediction by remember { mutableStateOf(gameFixture.prediction?.homeScores.orEmpty()) }
    var awayTeamPrediction by remember { mutableStateOf(gameFixture.prediction?.awayScores.orEmpty()) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .focusRequester(focusRequester)
            .padding(top = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        val isNotPredicted = remember(gameFixture.isPredicted) { gameFixture.isPredicted.not() }

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFC5A8FF),
                            Color(0xFF7E43A3),
                        )
                    )
                )
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = if (isNotPredicted) "Make Your Predictions" else "Predictions Submitted!",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium, // Customize the style
                modifier = Modifier
                    .padding(12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            OutlinedTextField(
                enabled = isNotPredicted,
                value = homeTeamPrediction,
                onValueChange = { homeTeamPrediction = it.take(2) },
                placeholder = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            style = TextStyle(
                                fontSize = 40.sp,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // Move focus to the next field when "Next" is pressed
                        focusRequester.requestFocus()
                    },
                ),
                textStyle = TextStyle(
                    fontSize = 34.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                modifier = Modifier
                    .size(120.dp)
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                enabled = isNotPredicted,
                value = awayTeamPrediction,
                onValueChange = { awayTeamPrediction = it.take(2) },
                placeholder = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "?",
                            style = TextStyle(
                                fontSize = 40.sp,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusRequester.freeFocus()
                    }
                ),
                textStyle = TextStyle(
                    fontSize = 34.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                modifier = Modifier
                    .size(120.dp)
                    .padding(horizontal = 16.dp),
            )

        }


        if (isNotPredicted) {
            Button(
                onClick = {
                    onSubmitPredict(
                        Prediction(
                            fixtureId = gameFixture.id,
                            homeScores = homeTeamPrediction,
                            awayScores = awayTeamPrediction,
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Submit Predictions")
            }
        } else {
            Text(
                text = gameFixture.getPredictionMessage(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        }

    }
}

@Preview
@Composable
fun FixtureDetailsViewPreview() {
    MaterialTheme {

        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

            FixtureDetailsView(
                fixture = GameFixture.mockGameFixture,
                onSubmitPredict = { _ -> },
                popBackStack = {},
            )
        }
    }
}

@Composable
fun PastFixtureStatView(statName: String, statValue: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = statName,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = statValue,
                    color = Color(0xFF3179EA),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Divider(thickness = 1.dp)
    }
}