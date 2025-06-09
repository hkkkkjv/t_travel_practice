package ru.kpfu.itis.t_travel.presentation.screens.trips.addExpense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.presentation.common.ui.CustomTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.ErrorDialog
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack
import ru.kpfu.itis.t_travel.presentation.screens.trips.budget.BudgetCategoryUi
import ru.kpfu.itis.t_travel.presentation.theme.OnGray

@Composable
fun AddExpenseScreen(
    tripId: Int,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) {
        viewModel.onEvent(AddExpenseEvent.Load(tripId))
    }
    val state by viewModel.state.collectAsState()

    ErrorDialog(
        error = state.error,
        onDismiss = { viewModel.onEvent(AddExpenseEvent.ErrorDismissed) }
    )

    InternalAddExpenseScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun InternalAddExpenseScreen(
    state: AddExpenseState,
    onEvent: (AddExpenseEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.add_expense),
                onBackClick = { onEvent(AddExpenseEvent.BackClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = state.title,
                onValueChange = { onEvent(AddExpenseEvent.TitleChanged(it)) },
                label = stringResource(R.string.expense_title)
            )
            CustomTextField(
                value = state.amount,
                onValueChange = { onEvent(AddExpenseEvent.AmountChanged(it)) },
                label = stringResource(R.string.amount),
                keyboardType = KeyboardType.Number
            )

            CategorySelector(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { onEvent(AddExpenseEvent.CategoryChanged(it)) },
                categories = state.categories
            )
            Text(
                text = stringResource(R.string.participants),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.participants) { participant ->
                    ParticipantItem(
                        participant = participant,
                        isSelected = state.selectedParticipants.contains(participant),
                        onToggle = { isSelected ->
                            onEvent(AddExpenseEvent.ParticipantToggled(participant, isSelected))
                        }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.add_expense),
                onClick = { onEvent(AddExpenseEvent.SubmitClicked) },
                enabled = !state.isLoading
            )
        }
    }
}


@Composable
private fun CategorySelector(
    selectedCategory: BudgetCategoryUi?,
    onCategorySelected: (BudgetCategoryUi) -> Unit,
    categories: ImmutableList<BudgetCategoryUi>
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.expense_category),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OnGray, RoundedCornerShape(24.dp))
                .clickable { showDialog = true }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectedCategory?.let { stringResource(it.type.nameRes) }
                    ?: stringResource(R.string.error_select_category),
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedCategory != null) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.expense_category)) },
            text = {
                Column {
                    categories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(category)
                                    showDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = category == selectedCategory,
                                onClick = {
                                    onCategorySelected(category)
                                    showDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(category.type.nameRes),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun ParticipantItem(
    participant: Participant,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isSelected) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onToggle,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${participant.name.split(" ").first()} ${participant.name.split(" ").last()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}