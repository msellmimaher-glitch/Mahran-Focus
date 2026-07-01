package com.maher.focus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maher.focus.FocusViewModel
import com.maher.focus.data.TaskEntity
import com.maher.focus.ui.screens.AddTaskScreen
import com.maher.focus.ui.screens.BrainDumpScreen
import com.maher.focus.ui.screens.EveningReviewScreen
import com.maher.focus.ui.screens.FamilyScreen
import com.maher.focus.ui.screens.GoalsScreen
import com.maher.focus.ui.screens.ImmigrationScreen
import com.maher.focus.ui.screens.MoreScreen
import com.maher.focus.ui.screens.RoutinesScreen
import com.maher.focus.ui.screens.SchoolScreen
import com.maher.focus.ui.screens.SettingsScreen
import com.maher.focus.ui.screens.TodayScreen

@Composable
fun MaherFocusNavHost(
    viewModel: FocusViewModel,
    tasks: List<TaskEntity>,
    hardDayMode: Boolean
) {
    val navController = rememberNavController()
    val bottomRoutes = listOf(Route.Today, Route.BrainDump, Route.AddTask, Route.Goals, Route.More)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    bottomRoutes.forEach { route ->
                        NavigationBarItem(
                            selected = currentRoute == route.value,
                            onClick = {
                                navController.navigate(route.value) {
                                    popUpTo(Route.Today.value) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = {
                                Text(
                                    route.label,
                                    fontWeight = if (route == Route.AddTask) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = { Text(symbolFor(route), fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Today.value,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Today.value) {
                TodayScreen(
                    tasks = tasks,
                    hardDayMode = hardDayMode,
                    onHardDayChange = viewModel::setHardDayMode,
                    onTaskChecked = viewModel::completeTask,
                    onTaskWaiting = viewModel::moveTaskToWaiting,
                    onTaskDelete = viewModel::deleteTask
                )
            }
            composable(Route.AddTask.value) { AddTaskScreen(viewModel = viewModel) }
            composable(Route.BrainDump.value) { BrainDumpScreen(viewModel = viewModel) }
            composable(Route.Goals.value) { GoalsScreen(viewModel = viewModel) }
            composable(Route.More.value) {
                MoreScreen(
                    onFamily = { navController.navigate(Route.Family.value) },
                    onSchool = { navController.navigate(Route.School.value) },
                    onImmigration = { navController.navigate(Route.Immigration.value) },
                    onEveningReview = { navController.navigate(Route.EveningReview.value) },
                    onRoutines = { navController.navigate(Route.Routines.value) },
                    onSettings = { navController.navigate(Route.Settings.value) }
                )
            }
            composable(Route.Family.value) { FamilyScreen(viewModel = viewModel) }
            composable(Route.School.value) { SchoolScreen(viewModel = viewModel) }
            composable(Route.Immigration.value) { ImmigrationScreen(viewModel = viewModel) }
            composable(Route.EveningReview.value) { EveningReviewScreen(viewModel = viewModel) }
            composable(Route.Routines.value) { RoutinesScreen(viewModel = viewModel) }
            composable(Route.Settings.value) { SettingsScreen() }
        }
    }
}

private fun symbolFor(route: Route): String = when (route) {
    Route.Today -> "✓"
    Route.BrainDump -> "✎"
    Route.AddTask -> "+"
    Route.Goals -> "◇"
    Route.More -> "…"
    else -> "•"
}
