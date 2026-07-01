package com.maher.focus.ui.screens

import androidx.compose.foundation.layout.*

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maher.focus.FocusViewModel
import com.maher.focus.data.FocusCategory
import com.maher.focus.data.RoutineFrequency
import com.maher.focus.data.TaskPriority

@Composable
fun RoutinesScreen(viewModel: FocusViewModel) {
    val routines by viewModel.routines.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(FocusCategory.WORK) }
    var priority by remember { mutableStateOf(TaskPriority.IMPORTANT_WEEK) }
    var frequency by remember { mutableStateOf(RoutineFrequency.DAILY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenTitle("Routines", "Les routines évitent de redécider chaque jour. Garde-les peu nombreuses.")

        CalmCard {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nouvelle routine") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            CategoryDropdown(selected = category, onSelected = { category = it })
            Spacer(Modifier.height(8.dp))
            PriorityDropdown(selected = priority, onSelected = { priority = it })
            Spacer(Modifier.height(8.dp))
            RoutineFrequencyDropdown(selected = frequency, onSelected = { frequency = it })
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addRoutine(title, category, priority, frequency)
                    title = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ajouter la routine") }
        }

        if (routines.isEmpty()) {
            EmptyState("Aucune routine.")
        } else {
            routines.forEach { routine ->
                CalmCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(routine.title, fontWeight = FontWeight.SemiBold)
                            Text("${FocusCategory.fromKey(routine.categoryKey).label} • ${RoutineFrequency.fromKey(routine.frequencyKey).label}")
                        }
                        Switch(checked = routine.isActive, onCheckedChange = { viewModel.toggleRoutine(routine) })
                    }
                }
            }
        }
    }
}
