package com.maher.focus.ui.navigation

sealed class Route(val value: String, val label: String) {
    data object Today : Route("today", "Jour")
    data object AddTask : Route("add_task", "+")
    data object BrainDump : Route("brain_dump", "Capture")
    data object Goals : Route("goals", "Objectifs")
    data object More : Route("more", "Plus")
    data object Family : Route("family", "Famille")
    data object School : Route("school", "École")
    data object Immigration : Route("immigration", "Démarches")
    data object EveningReview : Route("evening_review", "Bilan")
    data object Routines : Route("routines", "Routines")
    data object Settings : Route("settings", "Rappels")
}
