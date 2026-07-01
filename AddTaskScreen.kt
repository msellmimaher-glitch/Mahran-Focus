package com.maher.focus.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun categoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM child_profiles")
    suspend fun childCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChildren(children: List<ChildProfileEntity>)

    @Query("SELECT * FROM child_profiles ORDER BY id ASC")
    fun observeChildren(): Flow<List<ChildProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Long)

    @Query("""
        SELECT * FROM tasks
        ORDER BY
            CASE priorityKey
                WHEN 'due_today' THEN 0
                WHEN 'important_week' THEN 1
                WHEN 'when_energy' THEN 2
                WHEN 'waiting' THEN 3
                ELSE 4
            END ASC,
            CASE WHEN dueDateEpochDay IS NULL THEN 1 ELSE 0 END ASC,
            dueDateEpochDay ASC,
            createdAtMillis DESC
    """)
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE categoryKey = :categoryKey
        ORDER BY statusKey ASC, dueDateEpochDay ASC, createdAtMillis DESC
    """)
    fun observeTasksByCategory(categoryKey: String): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE childId = :childId
        ORDER BY statusKey ASC, dueDateEpochDay ASC, createdAtMillis DESC
    """)
    fun observeTasksForChild(childId: Long): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrainDumpItem(item: BrainDumpItemEntity): Long

    @Update
    suspend fun updateBrainDumpItem(item: BrainDumpItemEntity)

    @Query("SELECT * FROM brain_dump_items ORDER BY createdAtMillis DESC")
    fun observeBrainDumpItems(): Flow<List<BrainDumpItemEntity>>

    @Query("DELETE FROM brain_dump_items WHERE id = :itemId")
    suspend fun deleteBrainDumpItem(itemId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoalStep(step: GoalStepEntity): Long

    @Update
    suspend fun updateGoalStep(step: GoalStepEntity)

    @Transaction
    @Query("SELECT * FROM goals ORDER BY createdAtMillis DESC")
    fun observeGoalsWithSteps(): Flow<List<GoalWithSteps>>

    @Query("SELECT COUNT(*) FROM routines")
    suspend fun routineCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Query("SELECT * FROM routines ORDER BY isActive DESC, createdAtMillis DESC")
    fun observeRoutines(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEveningReview(review: EveningReviewEntity)

    @Query("SELECT * FROM evening_reviews ORDER BY dateEpochDay DESC LIMIT 14")
    fun observeRecentEveningReviews(): Flow<List<EveningReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdminDocument(document: AdminDocumentEntity): Long

    @Update
    suspend fun updateAdminDocument(document: AdminDocumentEntity)

    @Query("SELECT * FROM admin_documents ORDER BY statusKey ASC, dueDateEpochDay ASC, createdAtMillis DESC")
    fun observeAdminDocuments(): Flow<List<AdminDocumentEntity>>

    @Query("SELECT COUNT(*) FROM admin_documents")
    suspend fun adminDocumentCount(): Int
}
