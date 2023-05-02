package hsmida.exam.fdj.compose.leagues

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hsmida.exam.fdj.R
import hsmida.exam.fdj.compose.components.SearchBar
import hsmida.exam.fdj.compose.components.ApiResponseView
import hsmida.exam.fdj.model.League
import hsmida.exam.fdj.ui.theme.ExamTheme

@Composable
fun LeagueListScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchLeagueViewModel = hiltViewModel(),
    onNavigateToTeams: (League?) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LeagueListScreen(
        modifier = modifier.padding(dimensionResource(id = R.dimen.default_screen_margin)),
        uiState = uiState,
        onQueryChanged = {
            if (!uiState.isLoading) {
                viewModel.filterItems(it)
            }
        },
        onRetryClicked = { viewModel.getAllLeagues() },
        onNavigateToTeams = onNavigateToTeams,
        onNavigateUp = onNavigateUp,
    )
}

@Composable
fun LeagueListScreen(
    modifier: Modifier = Modifier,
    uiState: SearchLeagueUiState = SearchLeagueUiState(),
    onRetryClicked: () -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    onNavigateToTeams: (League?) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            SearchBar(
                modifier = modifier,
                title = uiState.query,
                canEdit = true,
                focused = true,
                onQueryChanged = onQueryChanged,
                onCloseClicked = onNavigateUp,
            )
        },
        content = { innerPadding ->
            ApiResponseView(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                isLoading = uiState.isLoading,
                isEmpty = uiState.isEmpty,
                emptyMessage = stringResource(id = R.string.emptyLeaguesMessage),
                isError = uiState.isError,
                errorMessage = stringResource(id = uiState.errorMessage),
                onRetryClicked = onRetryClicked
            ) {
                LazyColumn {
                    items(uiState.leagues) { league ->
                        LeagueRow(league = league, onSelected = {
                            onNavigateToTeams(league)
                        })
                        if (league != uiState.leagues.last()) {
                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LeagueRow(
    modifier: Modifier = Modifier,
    league: League,
    onSelected: (League) -> Unit
) {
    MaterialTheme {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onSelected(league)
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = league.strLeague,
                fontSize = 16.sp,
            )
            if (!league.strLeagueAlternate.isNullOrBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "(${league.strLeagueAlternate})",
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(
    showBackground = true, widthDp = 300, heightDp = 420, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LeagueListPreview() {
    ExamTheme {
        LeagueListScreen(
            uiState = SearchLeagueUiState()
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LeagueRowPreview() {
    ExamTheme {
        LeagueRow(
            league = League("", "Test", "Test", "Test"),
            onSelected = {}
        )
    }
}


