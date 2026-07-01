package com.maher.focus.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val key: String,
    val displayName: String,
    val sortOrder: Int,
    val isDefault: Boolean = true
)

@Entity(
    tableName = "tasks",
    indices = [Index("categoryKey"), Index("childId")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val categoryKey: String,
    val priorityKey: String,
    val statusKey: String = TaskStatus.ACTIVE.key,
    val dueDateEpochDay: Long? = null,
    val note: String = "",
    val isRecurring: Boolean = false,
    val routineFrequencyKey: String? = null,
    val customDaysCsv: String? = null,
    val childId: Long? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val completedAtMillis: Long? = null
)

@Entity(tableName = "brain_dump_items")
data class BrainDumpItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val convertedTaskId: Long? = null
)

@Entity(tableName = "child_profiles")
data class ChildProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val domainKey: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "goal_steps",
    foreignKeys = [
        ForeignKey(
            entity = GoalEntity::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("goalId")]
)
data class GoalStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val title: String,
    val isDone: Boolean = false
)

data class GoalWithSteps(
    @Embedded val goal: GoalEntity,
    @Relation(parentColumn = "id", entityColumn = "goalId") val steps: List<GoalStepEntity>
)

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val categoryKey: String,
    val priorityKey: String,
    val frequencyKey: String,
    val customDaysCsv: String? = null,
    val isActive: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "evening_reviews")
data class EveningReviewEntity(
    @PrimaryKey val dateEpochDay: Long,
    val completedText: String,
    val blockedText: String,
    val tomorrowPriorityText: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "admin_documents")
data class AdminDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val statusKey: String = AdminDocumentStatus.TODO.key,
    val dueDateEpochDay: Long? = null,
    val note: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)
