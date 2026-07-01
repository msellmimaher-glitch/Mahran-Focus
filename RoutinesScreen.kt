package com.maher.focus.notifications

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maher.focus.R

class DailyReminderWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val type = NotificationScheduler.ReminderType.fromKey(inputData.getString(KEY_TYPE).orEmpty())
        if (!NotificationScheduler.isReminderEnabled(context, type)) return Result.success()

        val notificationsAllowed = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        if (notificationsAllowed) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, NotificationScheduler.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_focus)
                .setContentTitle(type.title)
                .setContentText(type.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(type.message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            manager.notify(type.key.hashCode(), notification)
        }

        NotificationScheduler.scheduleReminder(context, type)
        return Result.success()
    }

    companion object {
        const val KEY_TYPE = "reminder_type"
    }
}
