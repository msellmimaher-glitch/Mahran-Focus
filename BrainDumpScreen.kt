package com.maher.focus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoryEntity::class,
        TaskEntity::class,
        BrainDumpItemEntity::class,
        ChildProfileEntity::class,
        GoalEntity::class,
        GoalStepEntity::class,
        RoutineEntity::class,
        EveningReviewEntity::class,
        AdminDocumentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun focusDao(): FocusDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maher_focus.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
