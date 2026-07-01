package com.maher.focus.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.RoutineFrequency
import com.maher.focus.data.TaskEntity
import com.maher.focus.data.TaskPriority
import com.maher.focus.data.TaskStatus
import java.time.LocalDate

@Composable
fun ScreenTitle(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CalmCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 5.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.34f)),
        content = { Column(Modifier.padding(16.dp), content = content) }
    )
}

@Composable
fun SoftPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(18.dp),
        content = content
    )
}

@Composable
fun FocusPill(
    text: String,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = color,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TaskCard(
    task: TaskEntity,
    onCheckedChange: (Boolean) -> Unit,
    onWaiting: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    compact: Boolean = false
) {
    val category = FocusCategory.fromKey(task.categoryKey)
    val priority = TaskPriority.fromKey(task.priorityKey)
    val status = TaskStatus.fromKey(task.statusKey)
    val isDone = status == TaskStatus.DONE

    CalmCard {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Checkbox(
                checked = isDone,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.size(34.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(
                    task.title,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = if (compact) 2 else 3,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null,
                    color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(7.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FocusPill(
                        text = category.label,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FocusPill(
                        text = priority.label,
                        color = when (priority) {
                            TaskPriority.DUE_TODAY -> MaterialTheme.colorScheme.errorContainer
                            TaskPriority.IMPORTANT_WEEK -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.primaryContainer
                        },
                        contentColor = when (priority) {
                            TaskPriority.DUE_TODAY -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                }
                if (!compact && task.dueDateEpochDay != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Date limite : ${LocalDate.ofEpochDay(task.dueDateEpochDay)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (!compact && task.note.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(task.note, style = MaterialTheme.typography.bodyMedium)
                }
                if (!compact && task.isRecurring) {
                    Spacer(Modifier.height(6.dp))
                    FocusPill("Récurrente")
                }
            }
        }
        if (!compact && (onWaiting != null || onDelete != null)) {
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (onWaiting != null) {
                    OutlinedButton(onClick = onWaiting) { Text("En attente") }
                }
                if (onDelete != null) {
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) { Text("Supprimer") }
                }
            }
        }
    }
}

@Composable
fun EmptyState(text: String) {
    CalmCard {
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun CategoryDropdown(selected: FocusCategory, onSelected: (FocusCategory) -> Unit) {
    SimpleDropdown(
        label = "Catégorie",
        selectedLabel = selected.label,
        options = FocusCategory.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun PriorityDropdown(selected: TaskPriority, onSelected: (TaskPriority) -> Unit) {
    SimpleDropdown(
        label = "Priorité",
        selectedLabel = selected.label,
        options = TaskPriority.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun RoutineFrequencyDropdown(selected: RoutineFrequency, onSelected: (RoutineFrequency) -> Unit) {
    SimpleDropdown(
        label = "Récurrence",
        selectedLabel = selected.label,
        options = RoutineFrequency.entries,
        optionLabel = { it.label },
        onSelected = onSelected
    )
}

@Composable
fun <T> SimpleDropdown(
    label: String,
    selectedLabel: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(selectedLabel, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
