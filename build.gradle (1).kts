package com.maher.focus.ui.screens

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(
    onFamily: () -> Unit,
    onSchool: () -> Unit,
    onImmigration: () -> Unit,
    onEveningReview: () -> Unit,
    onRoutines: () -> Unit,
    onSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        ScreenTitle("Plus", "Les espaces spécialisés restent séparés pour éviter un écran principal trop chargé.")
        LargeNavButton("Famille / Enfants", onFamily)
        LargeNavButton("École / Travail", onSchool)
        LargeNavButton("Immigration / Démarches", onImmigration)
        LargeNavButton("Routines", onRoutines)
        LargeNavButton("Bilan du soir", onEveningReview)
        LargeNavButton("Rappels", onSettings)
    }
}

@Composable
private fun LargeNavButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text(text) }
    Spacer(Modifier.height(10.dp))
}
