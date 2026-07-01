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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.maher.focus.data.AdminDocumentStatus
import com.maher.focus.data.FocusCategory
import java.time.LocalDate
import kotlin.OptIn

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImmigrationScreen(viewModel: FocusViewModel) {
    val documents by viewModel.adminDocuments.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(AdminDocumentStatus.TODO) }
    var dueDate by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenTitle("Immigration / Démarches", "Documents à envoyer, statuts, dates limites et notes importantes.")

        CalmCard {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Document ou démarche") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminDocumentStatus.entries.forEach { item ->
                    FilterChip(
                        selected = status == item,
                        onClick = { status = item },
                        label = { Text(item.label) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Date limite optionnelle : AAAA-MM-JJ") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note importante") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addAdminDocument(title, status, dueDate, note)
                    title = ""
                    dueDate = ""
                    note = ""
                    status = AdminDocumentStatus.TODO
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ajouter la démarche") }
        }

        Text("Suivi des documents", fontWeight = FontWeight.Bold)
        documents.forEach { document ->
            CalmCard {
                Text(document.title, fontWeight = FontWeight.SemiBold)
                Text(AdminDocumentStatus.fromKey(document.statusKey).label, color = MaterialTheme.colorScheme.primary)
                if (document.dueDateEpochDay != null) Text("Date limite : ${LocalDate.ofEpochDay(document.dueDateEpochDay)}")
                if (document.note.isNotBlank()) Text(document.note)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AdminDocumentStatus.entries.forEach { newStatus ->
                        FilterChip(
                            selected = document.statusKey == newStatus.key,
                            onClick = { viewModel.updateAdminDocumentStatus(document, newStatus) },
                            label = { Text(newStatus.label) }
                        )
                    }
                }
            }
        }

        val immigrationTasks = tasks.filter { it.categoryKey == FocusCategory.IMMIGRATION.key }
        if (immigrationTasks.isNotEmpty()) {
            Text("Tâches liées aux démarches", fontWeight = FontWeight.Bold)
            immigrationTasks.forEach { task ->
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
