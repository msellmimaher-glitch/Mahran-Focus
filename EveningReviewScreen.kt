package com.maher.focus.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    const val CHANNEL_ID = "maher_focus_reminders"
    private const val PREFS = "maher_focus_notification_prefs"

    enum class ReminderType(val key: String, val title: String, val message: String, val time: LocalTime) {
        MORNING(
            "morning",
            "Maher Focus",
            "Choisis seulement quelques priorités réalistes pour aujourd’hui.",
            LocalTime.of(7, 30)
        ),
        AFTER_SCHOOL(
            "after_school",
            "Après l’école",
            "Relance une tâche importante, pas toute la liste.",
            LocalTime.of(16, 30)
        ),
        EVENING(
            "evening",
            "Bilan rapide",
            "Deux minutes : terminé, blocage, priorité de demain.",
            LocalTime.of(21, 0)
        );

        companion object {
            fun fromKey(key: String): ReminderType = entries.firstOrNull { it.key == key } ?: MORNING
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Rappels Maher Focus",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Trois rappels calmes maximum : matin, après l’école, soir."
            }
            manager.createNotificationChannel(channel)
        }
    }

    fun isReminderEnabled(context: Context, type: ReminderType): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(type.key, false)
    }

    fun setReminderEnabled(context: Context, type: ReminderType, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(type.key, enabled).apply()
        if (enabled) scheduleReminder(context, type) else cancelReminder(context, type)
    }

    fun scheduleAllEnabledReminders(context: Context) {
        ReminderType.entries.filter { isReminderEnabled(context, it) }.forEach { scheduleReminder(context, it) }
    }

    fun scheduleReminder(context: Context, type: ReminderType) {
        val delayMillis = millisUntilNext(type.time)
        val request = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(Data.Builder().putString(DailyReminderWorker.KEY_TYPE, type.key).build())
            .addTag(type.key)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "maher_focus_${type.key}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminder(context: Context, type: ReminderType) {
        WorkManager.getInstance(context).cancelUniqueWork("maher_focus_${type.key}")
    }

    private fun millisUntilNext(time: LocalTime): Long {
        val now = LocalDateTime.now()
        var next = now.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return Duration.between(now, next).toMillis()
    }
}
