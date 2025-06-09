package ru.kpfu.itis.t_travel.presentation.screens.trips.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.SettlementItem
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomAvatar
import ru.kpfu.itis.t_travel.presentation.common.ui.ExpensesBottomSheet
import ru.kpfu.itis.t_travel.presentation.common.ui.MyDebtsBottomSheet
import ru.kpfu.itis.t_travel.presentation.common.ui.OweMeBottomSheet
import ru.kpfu.itis.t_travel.presentation.common.ui.ParticipantsBottomSheet
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack
import ru.kpfu.itis.t_travel.presentation.common.ui.getAvatarColor
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel = hiltViewModel(),
    tripId: Int,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(tripId) {
        viewModel.onEvent(TripDetailsEvent.Load(tripId))
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { viewModel.onEvent(TripDetailsEvent.Refresh) }
    )
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.trip_detail),
                titleColor = MaterialTheme.colorScheme.onSecondary,
                onBackClick = { viewModel.onEvent(TripDetailsEvent.BackClicked) },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(TripDetailsEvent.AddExpenseClicked) }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_expense)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.White),
                        startY = 0f
                    )
                )
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        state.error ?: stringResource(R.string.error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                TripDetailsContent(
                    state,
                    viewModel::onEvent,
                    Modifier
                        .padding(padding)
                        .pullRefresh(pullRefreshState)
                )
            }
        }
    }
}

@Composable
fun TripDetailsContent(
    state: TripDetailsState,
    onEvent: (TripDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    if (state.showParticipantsSheet) {
        ParticipantsBottomSheet(
            participants = state.participants,
            onDismiss = { onEvent(TripDetailsEvent.DismissParticipantsSheet) },
            onAddParticipant = {
                state.trip?.id?.let { tripId ->
                    onEvent(TripDetailsEvent.AddParticipantsClicked(tripId))
                }
            }
        )
    }
    if (state.showExpensesSheet) {
        ExpensesBottomSheet(
            participants = state.participants,
            onDismiss = { onEvent(TripDetailsEvent.DismissExpensesSheet) },
            expenses = state.expenses,
        )
    }
    if (state.showMyDebtsSheet) {
        MyDebtsBottomSheet(
            participants = state.participants,
            onDismiss = { onEvent(TripDetailsEvent.DismissMyDebtsSheet) },
            settlements = state.myDebts,
            onRequest = { onEvent(TripDetailsEvent.RequestDebtConfirmation(it)) },
        )
    }
    if (state.showOwedMeSheet) {
        OweMeBottomSheet(
            settlements = state.oweMe,
            participants = state.participants,
            onConfirm = { onEvent(TripDetailsEvent.ConfirmDebtReturn(it)) },
            onDismiss = { onEvent(TripDetailsEvent.DismissOwedToMeSheet) }
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.trip_name),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.trip?.title ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        item {
            Card(
                Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.route),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${state.trip?.departureCity ?: "-"} - ${state.trip?.destinationCity ?: "-"}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        item {
            Card(
                Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Total budget",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.totalBudget.toInt()
                            .let { String.format("%,d ₽", it).replace(',', ' ') },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        if (state.categories.isNotEmpty()) {
            item {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.elevatedCardElevation(6.dp)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            BudgetPieChart(
                                total = state.totalBudget,
                                categories = state.categories
                            )
                        }
                        Spacer(Modifier.height(64.dp))
                        BudgetCategoriesLegend(state.categories)
                    }
                }
            }
        }
        item {
            ParticipantsCardStyled(
                participants = state.participants,
                creatorId = state.trip?.createdBy,
                onClick = { onEvent(TripDetailsEvent.ParticipantsClicked) }
            )
        }
        item {
            ExpensesCardStyled(
                expenses = state.expenses.takeLast(3).reversed().toImmutableList(),
                participants = state.participants,
                onClick = { onEvent(TripDetailsEvent.ExpensesClicked) }
            )
        }
        item {
            DebtsCardStyled(
                isMyDebt = true,
                title = stringResource(R.string.my_debts),
                debts = state.myDebts.take(2).toImmutableList(),
                participants = state.participants,
                onClick = { onEvent(TripDetailsEvent.MyDebtsClicked) }
            )
        }
        item {
            DebtsCardStyled(
                isMyDebt = false,
                title = stringResource(R.string.owed_to_me),
                debts = state.oweMe.take(2).toImmutableList(),
                participants = state.participants,
                onClick = { onEvent(TripDetailsEvent.OweMeClicked) }
            )
        }
    }
}

@Composable
fun BudgetPieChart(total: Double, categories: ImmutableList<BudgetCategoryUi>) {
    if (total <= 0 || categories.isEmpty()) return
    val sweepAngles = categories.map { (it.amount / total * 360f).toFloat() }
    val colors = categories.map { colorResource(it.type.colorRes) }
    Canvas(Modifier.size(160.dp)) {
        var startAngle = -90f
        for (i in categories.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = false,
                style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Butt)
            )
            startAngle += sweepAngles[i]
        }
    }
}

@Composable
fun BudgetCategoriesLegend(categories: ImmutableList<BudgetCategoryUi>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.forEach { cat ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(24.dp)
                        .background(colorResource(cat.type.colorRes), shape = CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    stringResource(cat.type.nameRes),
                    Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${cat.amount.toInt().let { String.format("%,d ₽", it).replace(',', ' ') }}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF888888)
                )
            }
        }
    }
}

@Composable
fun ParticipantsCardStyled(
    participants: ImmutableList<Participant>,
    creatorId: Int? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            if (participants.isEmpty()) {
                Text(
                    stringResource(R.string.no_participants),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                participants.take(3).forEachIndexed { idx, p ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomAvatar(
                            name = p.name,
                            backgroundColor = getAvatarColor(idx),
                            size = 48
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                p.name,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                p.contact,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        if (!p.confirmed) {
                            Text(
                                stringResource(R.string.no_confirmation),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (creatorId != null && p.id == creatorId) {
                            Text(
                                stringResource(R.string.admin),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
                if (participants.size > 3) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(16.dp)
                                .background(Color(0xFFB2FF59), shape = CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.more_participants, participants.size - 3),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        participants.drop(3).joinToString(", ") { it.name },
                        color = Color(0xFFB0B0B0),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 32.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ExpensesCardStyled(
    expenses: ImmutableList<Expense>,
    participants: ImmutableList<Participant>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            if (expenses.isEmpty()) {
                Text(
                    stringResource(R.string.no_expenses),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                expenses.forEachIndexed { idx, e ->
                    val paidByParticipant = participants.find { it.id == e.paidBy }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomAvatar(
                            name = e.description,
                            backgroundColor = getAvatarColor(idx),
                            size = 48
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                e.description ?: "?",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                paidByParticipant?.name ?: "?",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Text(
                            e.amount.toInt().let { String.format("%,d ₽", it).replace(',', ' ') },
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DebtsCardStyled(
    isMyDebt: Boolean,
    title: String,
    debts: ImmutableList<SettlementItem>,
    participants: ImmutableList<Participant>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            if (debts.isEmpty()) {
                Text(
                    stringResource(R.string.no_debts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                debts.forEachIndexed { idx, e ->
                    val paidByParticipant =
                        participants.find { it.id == if (isMyDebt) e.to else e.from }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomAvatar(
                            name = paidByParticipant?.name ?: "?",
                            backgroundColor = getAvatarColor(paidByParticipant?.id ?:idx),
                            size = 48
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                paidByParticipant?.name ?: "?",
                                style = MaterialTheme.typography.titleMedium
                            )
//Описание нам не отдают бэки:(
//                            Text(
//                                e.description ?: "-",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color.Gray
//                            )
                        }
                        Text(
                            e.amount.toInt().let { String.format("%,d ₽", it).replace(',', ' ') },
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

