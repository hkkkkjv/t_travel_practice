package ru.kpfu.itis.t_travel.presentation.screens.trips.budget

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.PrimaryTextField
import ru.kpfu.itis.t_travel.presentation.common.ui.SecondaryButton
import ru.kpfu.itis.t_travel.presentation.common.ui.TransparentTopAppBarWithBack
import ru.kpfu.itis.t_travel.presentation.theme.OnGray

@Composable
fun BudgetFlowScreen(
    tripId: Int,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    LaunchedEffect(tripId) {
        viewModel.init(tripId)
    }
    val state by viewModel.state.collectAsState()

    when (state.currentScreen) {
        BudgetScreens.Input -> InternalBudgetInputScreen(state, viewModel::onEvent)
        BudgetScreens.CategorySelect -> InternalCategorySelectScreen(state, viewModel::onEvent)
        BudgetScreens.Amounts -> InternalBudgetAmountsScreen(state, viewModel::onEvent)
    }
}

@Composable
fun InternalBudgetInputScreen(
    state: BudgetState,
    onEvent: (BudgetEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.budget),
                onBackClick = { onEvent(BudgetEvent.BackToAddingParticipantsClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(OnGray, RoundedCornerShape(24.dp))
                    .clickable { onEvent(BudgetEvent.ShowBudgetSheet) },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${state.totalBudgetValue.toInt()} ₽",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.amoout_from_to),
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.next),
                onClick = { onEvent(BudgetEvent.NextToCategorySelectClicked) },
                enabled = state.totalBudgetValue in 1000.0..10_000_000.0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }

        if (state.showBudgetSheet) {
            BudgetAmountBottomSheet(
                value = state.totalBudget,
                onValueChange = { onEvent(BudgetEvent.TotalBudgetChanged(it)) },
                onSave = { onEvent(BudgetEvent.BudgetSheetDismissed) },
                onDismiss = { onEvent(BudgetEvent.BudgetSheetDismissed) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetAmountBottomSheet(
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.enter_budget_amount),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            PrimaryTextField(
                value = value,
                onValueChange = onValueChange,
                label = stringResource(R.string.amount),
                keyboardType = KeyboardType.Number,
            )
            Spacer(Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.save),
                onClick = onSave
            )
        }
    }
}

@Composable
fun InternalCategorySelectScreen(
    state: BudgetState,
    onEvent: (BudgetEvent) -> Unit,
) {
    val availableCategories by remember { derivedStateOf { state.availableCategories } }
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.create_expense_categories),
                onBackClick = { onEvent(BudgetEvent.BackToBudgetInputClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            availableCategories.forEach { categoryType ->
                val checked = state.categories.any { it.type == categoryType }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onEvent(BudgetEvent.CategoryChecked(categoryType, !checked)) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            onEvent(
                                BudgetEvent.CategoryChecked(
                                    categoryType,
                                    it
                                )
                            )
                        })
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(categoryType.nameRes))
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.next),
                onClick = { onEvent(BudgetEvent.NextToCategoryAmountsClicked) },
                enabled = state.categories.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InternalBudgetAmountsScreen(
    state: BudgetState,
    onEvent: (BudgetEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TransparentTopAppBarWithBack(
                title = stringResource(R.string.allocate_your_budget),
                onBackClick = { onEvent(BudgetEvent.BackToCategorySelect) }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(OnGray, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(stringResource(R.string.total_amount), color = Color.White)
                    Text(
                        "${state.totalBudgetValue.toInt()} ₽",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.categories.size) { index ->
                    val category = state.categories[index]
                    CategoryItem(
                        category = category,
                        onClick = { onEvent(BudgetEvent.EditCategoryAmount(category)) }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                "Остаток: ${(state.totalBudgetValue - state.distributed).toInt()} ₽",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            PrimaryButton(
                text = stringResource(R.string.done),
                onClick = { onEvent(BudgetEvent.SaveBudgetClicked) },
                enabled = state.canSubmit,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.showAmountSheet && state.editingCategory != null) {
            CategoryAmountBottomSheet(
                category = state.editingCategory,
                onAmountChange = { onEvent(BudgetEvent.AmountChanged(it)) },
                onSave = { onEvent(BudgetEvent.AmountConfirmed) },
                onDismiss = { onEvent(BudgetEvent.AmountSheetDismissed) }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: BudgetCategoryUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = category.color()
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = category.name(),
                style = MaterialTheme.typography.titleMedium,
                color = if (category.color().isDark()) Color.White else MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${category.amount.toInt()} ₽",
                style = MaterialTheme.typography.headlineLarge,
                color = if (category.color().isDark()) Color.White else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

private fun Color.isDark(): Boolean {
    return (0.299 * red + 0.587 * green + 0.114 * blue) < 0.5
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAmountBottomSheet(
    category: BudgetCategoryUi,
    onAmountChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf(category.amount.takeIf { it > 0 }?.toString() ?: "") }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                category.name(),
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .background(category.color(), MaterialTheme.shapes.medium)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = {
                    amount = it
                    onAmountChange(it)
                },
                label = stringResource(R.string.summary),
                keyboardType = KeyboardType.Number
            )
            Spacer(Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.save_changes),
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}