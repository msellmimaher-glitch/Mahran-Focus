package com.maher.focus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.TaskEntity
import com.maher.focus.data.TaskPriority
import com.maher.focus.data.TaskStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TodayScreen(
    tasks: List<TaskEntity>,
    hardDayMode: Boolean,
    onHardDayChange: (Boolean) -> Unit,
    onTaskChecked: (TaskEntity, Boolean) -> Unit,
    onTaskWaiting: (TaskEntity) -> Unit,
    onTaskDelete: (TaskEntity) -> Unit
) {
    val today = LocalDate.now().toEpochDay()
    val activeTasks = tasks.filter { it.statusKey != TaskStatus.DONE.key }
    val completedToday = tasks.count { task ->
        task.completedAtMillis?.let { millis ->
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay() == today
        } ?: false
    }

    val dueToday = activeTasks
        .filter { it.priorityKey == TaskPriority.DUE_TODAY.key || it.dueDateEpochDay == today }
        .sortedWith(compareBy<TaskEntity> { it.dueDateEpochDay ?: Long.MAX_VALUE }.thenBy { it.createdAtMillis })

    val importantWeek = activeTasks
        .filter {
            it.priorityKey == TaskPriority.IMPORTANT_WEEK.key &&
                (it.dueDateEpochDay == null || it.dueDateEpochDay <= today + 7)
        }
        .sortedWith(compareBy<TaskEntity> { it.dueDateEpochDay ?: Long.MAX_VALUE }.thenBy { it.createdAtMillis })

    val waitingCount = activeTasks.count {
        it.priorityKey == TaskPriority.WAITING.key ||
            it.statusKey == TaskStatus.WAITING.key ||
            it.statusKey == TaskStatus.BLOCKED.key
    }

    val focusTasks = if (hardDayMode) {
        buildHardDayList(activeTasks)
    } else {
        buildNormalFocusList(dueToday, importantWeek, activeTasks)
    }

    val remainingCount = (activeTasks.size - focusTasks.size).coerceAtLeast(0)
    val progress = if (focusTasks.isEmpty()) 0f else (completedToday.toFloat() / (completedToday + focusTasks.size)).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        TodayHeader(
            hardDayMode = hardDayMode,
            onHardDayChange = onHardDayChange
        )

        Spacer(Modifier.height(12.dp))

        SoftPanel {
            Text(
                text = if (hardDayMode) "Mode calme" else "Plan simple",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (hardDayMode)
                    "Aujourd’hui, tu avances avec 3 tâches seulement. Le reste attend."
                else
                    "Choisis peu. Termine mieux. Ne transforme pas ta journée en inventaire.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(14.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
            )
            Spacer(Modifier.height(7.dp))
            Text(
                "$completedToday terminé aujourd’hui",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("À faire maintenant", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    if (hardDayMode) "Maximum 3 tâches." else "Maximum 4 tâches visibles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FocusPill("${focusTasks.size} visibles")
        }

        Spacer(Modifier.height(8.dp))

        if (focusTasks.isEmpty()) {
            EmptyState("Rien d’urgent. Ajoute une seule tâche utile, pas dix.")
        } else {
            focusTasks.forEach { task ->
                TaskCard(
                    task = task,
                    compact = true,
                    onCheckedChange = { checked -> onTaskChecked(task, checked) }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        CalmCard {
            Text("Hors de l’écran", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FocusPill("${dueToday.size} aujourd’hui")
                FocusPill("${importantWeek.size} semaine")
                FocusPill("$waitingCount attente")
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "$remainingCount tâche(s) cachée(s) pour protéger ton attention.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(8.dp))

        if (hardDayMode) {
            Button(
                onClick = { onHardDayChange(false) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Revenir au mode normal")
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun TodayHeader(
    hardDayMode: Boolean,
    onHardDayChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text("Maher Focus", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "Une journée lisible, pas une liste infinie.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedButton(
            onClick = { onHardDayChange(!hardDayMode) },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (hardDayMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        ) {
            Text(if (hardDayMode) "Normal" else "Mode calme")
        }
    }
}

private fun buildNormalFocusList(
    dueToday: List<TaskEntity>,
    importantWeek: List<TaskEntity>,
    activeTasks: List<TaskEntity>
): List<TaskEntity> {
    val selected = mutableListOf<TaskEntity>()
    selected.addAll(dueToday.take(2))
    selected.addAll(importantWeek.filterNot { task -> selected.any { it.id == task.id } }.take(2))

    if (selected.isEmpty()) {
        selected.addAll(
            activeTasks
                .filter { it.priorityKey != TaskPriority.WAITING.key }
                .sortedWith(compareBy<TaskEntity> { TaskPriority.fromKey(it.priorityKey).rank }.thenBy { it.createdAtMillis })
                .take(3)
        )
    }

    return selected.distinctBy { it.id }.take(4)
}

private fun buildHardDayList(tasks: List<TaskEntity>): List<TaskEntity> {
    fun firstFrom(categories: List<FocusCategory>) = tasks
        .filter { it.priorityKey != TaskPriority.WAITING.key && it.statusKey == TaskStatus.ACTIVE.key }
        .sortedWith(compareBy<TaskEntity> { TaskPriority.fromKey(it.priorityKey).rank }.thenBy { it.createdAtMillis })
        .firstOrNull { task -> categories.any { it.key == task.categoryKey } }

    val work = firstFrom(listOf(FocusCategory.WORK, FocusCategory.UNIVERSITY))
    val family = firstFrom(listOf(FocusCategory.FAMILY))
    val personal = firstFrom(
        listOf(
            FocusCategory.HEALTH,
            FocusCategory.IMMIGRATION,
            FocusCategory.FINANCES,
            FocusCategory.SPIRITUALITY,
            FocusCategory.FRENCH,
            FocusCategory.HOME,
            FocusCategory.BRAIN_DUMP
        )
    )
    return listOfNotNull(work, family, personal).distinctBy { it.id }.take(3)
}
