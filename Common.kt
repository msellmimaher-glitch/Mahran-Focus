package com.maher.focus.data

enum class FocusCategory(val key: String, val label: String) {
    WORK("work", "École / Travail"),
    FAMILY("family", "Famille / Enfants"),
    IMMIGRATION("immigration", "Immigration / Démarches"),
    UNIVERSITY("university", "Formation universitaire"),
    FRENCH("french", "Français / Communication"),
    HEALTH("health", "Santé / Énergie"),
    FINANCES("finances", "Finances"),
    SPIRITUALITY("spirituality", "Spiritualité"),
    HOME("home", "Maison"),
    BRAIN_DUMP("brain_dump", "Vide-tête");

    companion object {
        fun fromKey(key: String): FocusCategory = entries.firstOrNull { it.key == key } ?: BRAIN_DUMP
    }
}

enum class TaskPriority(val key: String, val label: String, val rank: Int) {
    DUE_TODAY("due_today", "Obligatoire aujourd’hui", 0),
    IMPORTANT_WEEK("important_week", "Important cette semaine", 1),
    WHEN_ENERGY("when_energy", "Quand j’ai de l’énergie", 2),
    WAITING("waiting", "En attente", 3),
    DELEGATED_BLOCKED("delegated_blocked", "Délégué ou bloqué", 4);

    companion object {
        fun fromKey(key: String): TaskPriority = entries.firstOrNull { it.key == key } ?: WHEN_ENERGY
    }
}

enum class TaskStatus(val key: String, val label: String) {
    ACTIVE("active", "À faire"),
    DONE("done", "Terminée"),
    WAITING("waiting", "En attente"),
    BLOCKED("blocked", "Bloquée"),
    DELEGATED("delegated", "Déléguée");

    companion object {
        fun fromKey(key: String): TaskStatus = entries.firstOrNull { it.key == key } ?: ACTIVE
    }
}

enum class RoutineFrequency(val key: String, val label: String) {
    DAILY("daily", "Chaque jour"),
    WEEKLY("weekly", "Chaque semaine"),
    CUSTOM_DAYS("custom_days", "Jours personnalisés");

    companion object {
        fun fromKey(key: String): RoutineFrequency = entries.firstOrNull { it.key == key } ?: DAILY
    }
}

enum class AdminDocumentStatus(val key: String, val label: String) {
    TODO("todo", "À faire"),
    SENT("sent", "Envoyé"),
    WAITING("waiting", "En attente"),
    RECEIVED("received", "Reçu"),
    PROBLEM("problem", "Problème");

    companion object {
        fun fromKey(key: String): AdminDocumentStatus = entries.firstOrNull { it.key == key } ?: TODO
    }
}
