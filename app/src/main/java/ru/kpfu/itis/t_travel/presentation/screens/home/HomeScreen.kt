package ru.kpfu.itis.t_travel.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.persistentListOf
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Budget
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.Trip
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBar
import ru.kpfu.itis.t_travel.presentation.common.ui.shimmer
import ru.kpfu.itis.t_travel.presentation.navigation.NavigationAction
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationAction: (NavigationAction) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.navigationAction.collect { action ->
            onNavigationAction(action)
        }
    }
    InternalHomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InternalHomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onEvent(HomeEvent.Refresh) }
    )
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                stringResource(R.string.main),
                actions = {
                    IconButton(onClick = { onEvent(HomeEvent.AddTripClicked) }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_trip)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                state.error != null -> {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    HomeScreenContent(state = state, onEvent = onEvent)
                }

            }
            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun HomeScreenContent(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OperationCard(
                    title = stringResource(R.string.all_operations),
                    amount = state.totalOperationsAmount,
                    isSetupSteps = state.showSetupSteps,
                    onClick = { if (!state.isLoading) onEvent(HomeEvent.AllOperationsClicked) },
                    modifier = Modifier.weight(1f)
                )

                ParticipantsCard(
                    participants = state.participants,
                    isSetupSteps = state.showSetupSteps,
                    onClick = { if (!state.isLoading) onEvent(HomeEvent.ParticipantsListClicked) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (state.showSetupSteps) {
            item {
                StepCard(
                    text = stringResource(R.string.setup_profile),
                    step = 1,
                    onClick = { onEvent(HomeEvent.SetupProfileClicked) })
            }
            item {
                StepCard(
                    text = stringResource(R.string.create_trip),
                    step = 2,
                    onClick = { onEvent(HomeEvent.CreateTripClicked) })
            }
            item {
                StepCard(
                    text = stringResource(R.string.ready_to_go),
                    step = 3,
                    onClick = { onEvent(HomeEvent.ReadyToGoClicked) })
            }
        }

        if (!state.showSetupSteps && state.favoriteTrip != null) {
            item {
                Text(
                    text = state.favoriteTrip.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                InfoBlockCard(
                    title = stringResource(R.string.all_budget),
                    amount = state.favoriteTrip.budget.totalBudget,
                    iconResId = R.drawable.ic_budget, //ic_budget
                    onClick = { onEvent(HomeEvent.BudgetClicked) }
                )
            }

            item {
                InfoBlockCard(
                    title = stringResource(R.string.my_expenses),
                    amount = state.myExpensesAmount,
                    iconResId = R.drawable.ic_expense, // ic_expense
                    onClick = { onEvent(HomeEvent.MyExpensesClicked) }
                )
            }

            item {
                InfoBlockCard(
                    title = stringResource(R.string.my_debts),
                    amount = state.myDebtsAmount,
                    iconResId = R.drawable.ic_debt, //ic_debt
                    onClick = { onEvent(HomeEvent.MyDebtsClicked) }
                )
            }

            item {
                InfoBlockCard(
                    title = stringResource(R.string.owed_to_me),
                    amount = state.owedToMeAmount,
                    iconResId = R.drawable.ic_owed_to_me, // ic_owed_to_me
                    participants = state.owedToMeParticipants,
                    onClick = { onEvent(HomeEvent.OwedToMeClicked) }
                )
            }
        }
    }
}

@Composable
fun StepCard(text: String, step: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Шаг $step", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun OperationCard(
    title: String,
    amount: Double,
    isSetupSteps: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.elevatedCardElevation(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (isSetupSteps) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(20.dp)
                            .background(shimmer(), CircleShape)
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(20.dp)
                            .background(shimmer(), CircleShape)
                    )
                }
            } else {
                Text(
                    text = amount.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)

                )
            }
        }
    }
}


@Composable
fun ParticipantsCard(
    participants: List<Participant>,
    isSetupSteps: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.elevatedCardElevation(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.participants_list),
                style = MaterialTheme.typography.titleMedium
            )
            if (isSetupSteps) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(shimmer())
                        )
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(participants.take(3).size) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                    if (participants.size > 3) {
                        Text(
                            text = "+${participants.size - 3}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBlockCard(
    title: String,
    amount: Double,
    iconResId: Int,
    onClick: () -> Unit,
    participants: List<Participant> = emptyList()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.elevatedCardElevation(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = title,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${amount.toInt()} ₽",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                if (participants.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(participants.take(3).size) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Cyan)
                            )
                        }
                        if (participants.size > 3) {
                            Text(
                                text = "+${participants.size - 3}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenSetup() {
    MaterialTheme {
        InternalHomeScreen(
            state = HomeState(showSetupSteps = true),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreenDetails() {
    MaterialTheme {
        InternalHomeScreen(
            state = HomeState(
                favoriteTrip = Trip(
                    id = 1,
                    title = "Это Питер, детка!",
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now().plusDays(5),
                    createdBy = 0, departureCity = "", destinationCity = "",
                    participants = listOf(Participant.mock(1), Participant.mock(2)),
                    budget = Budget.mock(10000.0),
                    expenses = listOf(Expense.mock())
                ),
                totalOperationsAmount = 52000.0,
                participants = persistentListOf(
                    Participant(
                        id = 1, name = "A", contact = "", tripId = 1,
                        confirmed = true
                    ),
                    Participant(
                        id = 2, name = "B", contact = "", tripId = 1,
                        confirmed = true
                    ),
                    Participant(
                        id = 3, name = "C", contact = "", tripId = 1,
                        confirmed = true
                    ),
                    Participant(
                        id = 4, name = "D", contact = "", tripId = 1,
                        confirmed = true
                    )
                ),
                myExpensesAmount = 40000.0,
                myDebtsAmount = 0.0,
                owedToMeAmount = 5000.0,
                owedToMeParticipants = persistentListOf(
                    Participant(
                        id = 1,
                        name = "E",
                        contact = "",
                        tripId = 1,
                        confirmed = true
                    ),
                    Participant(id = 2, name = "A", contact = "", tripId = 1, confirmed = true),
                    Participant(id = 3, name = "O", contact = "", tripId = 1, confirmed = true),
                    Participant(id = 4, name = "P", contact = "", tripId = 1, confirmed = true)
                ),
                showSetupSteps = false
            ),
            onEvent = {}
        )
    }
}
