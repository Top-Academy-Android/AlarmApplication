package com.example.alarmapplication

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.alarmapplication.ui.theme.AlarmApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(ACTION_MANAGE_OVERLAY_PERMISSION))
        }

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            !notificationManager.canUseFullScreenIntent()
        ) {
            startActivity(Intent(ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT))
        }

        val alarmManager = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager

        setContent {
            var alarmDelaySecs by remember { mutableIntStateOf(5) }

            AlarmApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Button(
                            onClick = {
                                val intent = Intent("com.example.alarmapplication.ACTION_ALARM").apply {
                                    setPackage("com.example.alarmapplication")
                                }

                                val pendingIntent = PendingIntent.getBroadcast(
                                    this@MainActivity,
                                    Random.nextInt(),
                                    intent,
                                    PendingIntent.FLAG_IMMUTABLE,
                                )

                                println("Scheduled alarm in $alarmDelaySecs at ${System.currentTimeMillis() / 1000}")
                                alarmManager.setExact(
                                    AlarmManager.RTC_WAKEUP,
                                    System.currentTimeMillis() + alarmDelaySecs*1000,
                                    pendingIntent,
                                )
                            },
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            Text("Set Alarm in $alarmDelaySecs secs")
                        }
                        TextField(
                            value = alarmDelaySecs.toString(),
                            onValueChange = { newValue ->
                                if (newValue == "") {
                                    alarmDelaySecs = 0
                                    return@TextField
                                }
                                try {
                                    val int = newValue.toInt()
                                    alarmDelaySecs = int
                                } catch (_: NumberFormatException) { }
                            }
                        )
                        IconButton(
                            onClick = {
                                val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.padenie_truby)
                                mediaPlayer.start()
                            }
                        ) {
                            Icon(Icons.Default.PlayArrow, null)
                        }
                    }
                }
            }
        }
    }
}

