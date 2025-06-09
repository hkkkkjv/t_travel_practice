package ru.kpfu.itis.t_travel.presentation.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.kpfu.itis.t_travel.R
import ru.kpfu.itis.t_travel.domain.model.Expense
import ru.kpfu.itis.t_travel.domain.model.Participant
import ru.kpfu.itis.t_travel.domain.model.SettlementItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantsBottomSheet(
    participants: List<Participant>,
    onDismiss: () -> Unit,
    onAddParticipant: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.participants_list),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                IconButton (onClick = onAddParticipant) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_participants)
                    )
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(participants) { participant ->
                    ParticipantItem(participant = participant)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ParticipantItem(participant: Participant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomAvatar(
                name = participant.name,
                backgroundColor = getAvatarColor(participant.id),
                size = 48
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = participant.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = participant.contact,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            if (!participant.confirmed) {
                Text(
                    text = stringResource(R.string.no_confirmation),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesBottomSheet(
    expenses: ImmutableList<Expense>,
    participants: ImmutableList<Participant>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = { WindowInsets(0) },
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.all_operations),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    participants.find { it.id == expense.paidBy }?.let {
                        ExpenseItem(expense = expense, participant = it)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ExpenseItem(expense: Expense, participant: Participant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            CustomAvatar(
                name = participant.name,
                backgroundColor = getAvatarColor(participant.id),
                size = 48
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = participant.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${expense.amount} ₽",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExpensesBottomSheet(
    expenses: List<Expense>,
    participants: ImmutableList<Participant>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.my_expenses),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    participants.find { it.id == expense.paidBy }?.let {
                        ExpenseItem(
                            expense = expense,
                            participant = it
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OweMeBottomSheet(
    settlements: List<SettlementItem>,
    participants: List<Participant>,
    onConfirm: (SettlementItem) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { WindowInsets(0) },
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 600.dp, max = 800.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.owed_to_me),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (settlements.isEmpty()) {
                Text(text = stringResource(R.string.no_debts), color = Color.Gray)
            } else {
                settlements.forEach { settlement ->
                    val from = participants.find { it.id == settlement.from }
                    if (from != null) {
                        DebtItem(
                            participant = from,
                            amount = settlement.amount,
                            status = settlement.status,
                            onConfirm = if (settlement.status == "REQUESTED") {
                                { onConfirm(settlement) }
                            } else null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDebtsBottomSheet(
    settlements: List<SettlementItem>,
    participants: List<Participant>,
    onRequest: (SettlementItem) -> Unit,
    onDismiss: () -> Unit
) {
    val filtered = settlements.filter { it.status == "PENDING" || it.status == "REQUESTED" }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { WindowInsets(0) },
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 600.dp, max = 800.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.my_debts),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (filtered.isEmpty()) {
                Text(text = stringResource(R.string.no_debts), color = Color.Gray)
            } else {
                filtered.forEach { settlement ->
                    val to = participants.find { it.id == settlement.to }
                    if (to != null) {
                        DebtItem(
                            participant = to,
                            amount = settlement.amount,
                            status = settlement.status,
                            onRequest = if (settlement.status == "PENDING") {
                                { onRequest(settlement) }
                            } else null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DebtItem(
    participant: Participant,
    amount: Double,
    status: String,
    onRequest: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null
) {
    val bgColor: Color
    val statusText: String?
    val statusColor: Color
    val icon: Int
    val iconTint: Color
    val showButton: Boolean
    when (status) {
        "PENDING" -> {
            bgColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            statusText = null
            statusColor = Color.Unspecified
            icon = R.drawable.ic_check
            iconTint = MaterialTheme.colorScheme.primary
            showButton = onRequest != null
        }

        "REQUESTED" -> {
            bgColor = Color(0xFFFFF9C4)
            statusText = "Ожидает подтверждения"
            statusColor = Color.Red
            icon = R.drawable.ic_check
            iconTint = MaterialTheme.colorScheme.primary
            showButton = onConfirm != null
        }

        "CONFIRMED" -> {
            bgColor = Color(0xFFF0F0F0)
            statusText = "Подтверждено"
            statusColor = Color.Gray
            icon = 0
            iconTint = Color(0xFF4CAF50)
            showButton = false
        }

        else -> {
            bgColor = Color.Transparent
            statusText = null
            statusColor = Color.Unspecified
            icon = 0
            iconTint = Color.Unspecified
            showButton = false
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, shape = MaterialTheme.shapes.extraLarge)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomAvatar(
            size = 48,
            name = participant.name,
            backgroundColor = getAvatarColor(participant.id),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(participant.name, style = MaterialTheme.typography.titleMedium)
            if (statusText != null) {
                Text(statusText, color = statusColor, style = MaterialTheme.typography.bodySmall)
            }
        }
        Text("${amount.toInt()} ₽", style = MaterialTheme.typography.headlineSmall)
        if (onRequest != null) {
            IconButton(onClick = onRequest) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
        if (onConfirm != null) {
            IconButton(onClick = onConfirm) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}