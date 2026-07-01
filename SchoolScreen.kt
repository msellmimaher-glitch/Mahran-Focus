package com.maher.focus.repository

import com.maher.focus.data.AdminDocumentEntity
import com.maher.focus.data.BrainDumpItemEntity
import com.maher.focus.data.CategoryEntity
import com.maher.focus.data.ChildProfileEntity
import com.maher.focus.data.EveningReviewEntity
import com.maher.focus.data.FocusDao
import com.maher.focus.data.GoalEntity
import com.maher.focus.data.GoalStepEntity
import com.maher.focus.data.GoalWithSteps
import com.maher.focus.data.RoutineEntity
import com.maher.focus.data.SeedData
import com.maher.focus.data.TaskEntity
import kotlinx.coroutines.flow.Flow

class FocusRepository(private val dao: FocusDao) {
    val categories: Flow<List<CategoryEntity>> = dao.observeCategories()
    val children: Flow<List<ChildProfileEntity>> = dao.observeChildren()
    val tasks: Flow<List<TaskEntity>> = dao.observeTasks()
    val brainDumpItems: Flow<List<BrainDumpItemEntity>> = dao.observeBrainDumpItems()
    val goalsWithSteps: Flow<List<GoalWithSteps>> = dao.observeGoalsWithSteps()
    val routines: Flow<List<RoutineEntity>> = dao.observeRoutines()
    val eveningReviews: Flow<List<EveningReviewEntity>> = dao.observeRecentEveningReviews()
    val adminDocuments: Flow<List<AdminDocumentEntity>> = dao.observeAdminDocuments()

    suspend fun seedIfNeeded() {
        if (dao.categoryCount() == 0) dao.insertCategories(SeedData.categories)
        if (dao.childCount() == 0) dao.insertChildren(SeedData.children)
        if (dao.adminDocumentCount() == 0) SeedData.starterAdminDocuments.forEach { dao.insertAdminDocument(it) }
        if (dao.routineCount() == 0) SeedData.starterRoutines.forEach { dao.insertRoutine(it) }
    }

    suspend fun addTask(task: TaskEntity): Long = dao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
    suspend fun deleteTask(taskId: Long) = dao.deleteTask(taskId)

    suspend fun addBrainDumpItem(text: String): Long = dao.insertBrainDumpItem(BrainDumpItemEntity(text = text))
    suspend fun updateBrainDumpItem(item: BrainDumpItemEntity) = dao.updateBrainDumpItem(item)
    suspend fun deleteBrainDumpItem(itemId: Long) = dao.deleteBrainDumpItem(itemId)

    suspend fun addGoal(goal: GoalEntity): Long = dao.insertGoal(goal)
    suspend fun addGoalStep(step: GoalStepEntity): Long = dao.insertGoalStep(step)
    suspend fun updateGoalStep(step: GoalStepEntity) = dao.updateGoalStep(step)

    suspend fun addRoutine(routine: RoutineEntity): Long = dao.insertRoutine(routine)
    suspend fun updateRoutine(routine: RoutineEntity) = dao.updateRoutine(routine)

    suspend fun saveEveningReview(review: EveningReviewEntity) = dao.upsertEveningReview(review)

    suspend fun addAdminDocument(document: AdminDocumentEntity): Long = dao.insertAdminDocument(document)
    suspend fun updateAdminDocument(document: AdminDocumentEntity) = dao.updateAdminDocument(document)
}
