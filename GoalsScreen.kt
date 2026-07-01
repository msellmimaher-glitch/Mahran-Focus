package com.maher.focus.data

object SeedData {
    val categories: List<CategoryEntity> = FocusCategory.entries.mapIndexed { index, category ->
        CategoryEntity(
            key = category.key,
            displayName = category.label,
            sortOrder = index
        )
    }

    val children: List<ChildProfileEntity> = listOf(
        ChildProfileEntity(name = "Ahmed"),
        ChildProfileEntity(name = "Amen Allah")
    )

    val starterAdminDocuments: List<AdminDocumentEntity> = listOf(
        AdminDocumentEntity(title = "NAS enfant", statusKey = AdminDocumentStatus.TODO.key),
        AdminDocumentEntity(title = "Permis / documents gouvernementaux", statusKey = AdminDocumentStatus.WAITING.key),
        AdminDocumentEntity(title = "Impôts", statusKey = AdminDocumentStatus.TODO.key),
        AdminDocumentEntity(title = "Assurance", statusKey = AdminDocumentStatus.TODO.key)
    )

    val starterRoutines: List<RoutineEntity> = listOf(
        RoutineEntity(title = "Vérifier courriels importants", categoryKey = FocusCategory.WORK.key, priorityKey = TaskPriority.IMPORTANT_WEEK.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Étude EDU1013", categoryKey = FocusCategory.UNIVERSITY.key, priorityKey = TaskPriority.IMPORTANT_WEEK.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Français oral", categoryKey = FocusCategory.FRENCH.key, priorityKey = TaskPriority.WHEN_ENERGY.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Suivi des enfants", categoryKey = FocusCategory.FAMILY.key, priorityKey = TaskPriority.IMPORTANT_WEEK.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Budget", categoryKey = FocusCategory.FINANCES.key, priorityKey = TaskPriority.IMPORTANT_WEEK.key, frequencyKey = RoutineFrequency.WEEKLY.key),
        RoutineEntity(title = "Santé / énergie", categoryKey = FocusCategory.HEALTH.key, priorityKey = TaskPriority.WHEN_ENERGY.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Spiritualité", categoryKey = FocusCategory.SPIRITUALITY.key, priorityKey = TaskPriority.WHEN_ENERGY.key, frequencyKey = RoutineFrequency.DAILY.key),
        RoutineEntity(title = "Préparation des cours", categoryKey = FocusCategory.WORK.key, priorityKey = TaskPriority.IMPORTANT_WEEK.key, frequencyKey = RoutineFrequency.WEEKLY.key)
    )
}
