package hsmida.exam.fdj

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import hsmida.exam.fdj.compose.teams.TeamListViewModel
import hsmida.exam.fdj.data.TeamRepository
import hsmida.exam.fdj.model.ApiResult
import hsmida.exam.fdj.model.Team
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TeamListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var repository: TeamRepository

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle()
    }

    @Test
    fun teamListViewModel_Init_InitialUiState() = runTest {
        // Given
        // No data passed to SavedStateHandle

        // When
        val viewModel = TeamListViewModel(savedStateHandle, repository)
        val uiState = viewModel.uiState.value

        // Then
        assertEquals("", uiState.title)
        assertEquals(false, uiState.isLoading)
        assertEquals(true, uiState.isEmpty)
        assertEquals(false, uiState.isError)
        assertEquals(R.string.selectLeague, uiState.emptyMessage)
        assertEquals(emptyList<Team>(), uiState.teams)
        verifyNoInteractions(repository)
    }

    @Test
    fun teamListViewModel_TitlePassedAndNotTeamsFound_EmptyStateReturned() = runTest {
        // Given
        val leagueName = "fakeLeagueName"
        savedStateHandle["leagueName"] = leagueName
        doReturn(flowOf(ApiResult.Success(emptyList<Team>()))).`when`(repository).searchAllTeams(leagueName)

        // When
        val viewModel = TeamListViewModel(savedStateHandle, repository)
        val uiState = viewModel.uiState.value

        // Then
        assertEquals(leagueName, uiState.title)
        assertEquals(false, uiState.isLoading)
        assertEquals(true, uiState.isEmpty)
        assertEquals(false, uiState.isError)
        assertEquals(R.string.emptyTeamsMessage, uiState.emptyMessage)
        assertEquals(emptyList<Team>(), uiState.teams)
        verify(repository).searchAllTeams(leagueName)
    }

    @Test
    fun teamListViewModel_TeamsFound_TeamsReturned() = runTest {
        // Given
        val leagueName = "fakeLeagueName"
        val teamList = listOf(
            Team("", "", "", "")
        )
        savedStateHandle["leagueName"] = leagueName
        doReturn(
            flowOf(
                ApiResult.Success(teamList)
            )
        ).`when`(repository).searchAllTeams(leagueName)

        // When
        val viewModel = TeamListViewModel(savedStateHandle, repository)
        val uiState = viewModel.uiState.value

        // Then
        assertEquals(leagueName, uiState.title)
        assertEquals(false, uiState.isLoading)
        assertEquals(false, uiState.isEmpty)
        assertEquals(false, uiState.isError)
        assertEquals(teamList, uiState.teams)
        verify(repository).searchAllTeams(leagueName)
    }

    @Test
    fun teamListViewModel_Exception_ErrorReturned() = runTest {
        // Given
        val leagueName = "fakeLeagueName"
        savedStateHandle["leagueName"] = leagueName
        val exception = "fakeError"
        doReturn(
            flowOf(
                ApiResult.Error(exception)
            )
        ).`when`(repository).searchAllTeams(leagueName)

        // When
        val viewModel = TeamListViewModel(savedStateHandle, repository)
        val uiState = viewModel.uiState.value

        // Then
        assertEquals(leagueName, uiState.title)
        assertEquals(false, uiState.isLoading)
        assertEquals(false, uiState.isEmpty)
        assertEquals(true, uiState.isError)
        assertEquals(emptyList<Team>(), uiState.teams)
        verify(repository).searchAllTeams(leagueName)
    }

}