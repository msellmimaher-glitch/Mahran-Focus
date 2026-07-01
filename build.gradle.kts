package com.maher.focus.ui.screens

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maher.focus.FocusViewModel
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.TaskPriority
import kotlin.OptIn

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SchoolScreen(viewModel: FocusViewModel) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    val templates = listOf(
        "Préparation de cours",
        "Corrections",
        "Examens",
        "Messages aux parents",
        "Remédiations",
        "Groupes d’élèves",
        "Idée simulation / ressource numérique"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenTitle("École / Travail", "Préparer, corriger, communiquer, remédier. Rien de plus compliqué que nécessaire.")

        CalmCard {
            Text("Ajout rapide travail", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                templates.forEach { template ->
                    FilterChip(
                        selected = false,
                        onClick = { title = "$template : " },
                        label = { Text(template) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tâche de travail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addTask(
                        title = title,
                        category = FocusCategory.WORK,
                        priority = TaskPriority.IMPORTANT_WEEK,
                        dueDateText = null,
                        note = "",
                        isRecurring = false,
                        routineFrequency = null
                    )
                    title = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ajouter") }
        }

        val workTasks = tasks.filter { it.categoryKey == FocusCategory.WORK.key }
        Text("Tâches École / Travail", fontWeight = FontWeight.Bold)
        if (workTasks.isEmpty()) {
            EmptyState("Aucune tâche de travail.")
        } else {
            workTasks.forEach { task ->
                TaskCard(
                    task = task,
                    onCheckedChange = { checked -> viewModel.completeTask(task, checked) },
                    onWaiting = { viewModel.moveTaskToWaiting(task) },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }
    }
}
