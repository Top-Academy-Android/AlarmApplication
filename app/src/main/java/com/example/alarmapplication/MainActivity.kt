package com.example.alarmapplication

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.alarmapplication.ui.theme.AlarmApplicationTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val am = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setContent {
            var alarmDelaySecs by remember { mutableIntStateOf(10) }

            AlarmApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, AlarmActivity::class.java)

                                val ao = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                    ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(
                                        ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                                    )
                                } else null

                                val pendingIntent = PendingIntent.getActivity(
                                    this@MainActivity,
                                    Random.nextInt(),
                                    intent,
                                    PendingIntent.FLAG_IMMUTABLE,
                                    ao?.toBundle()
                                )

                                println("Time 1: ${System.currentTimeMillis() / 1000}")
                                am.setExact(
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
                                } catch (_: NumberFormatException) {
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmApplicationTheme {
        Greeting("Android")
    }
}