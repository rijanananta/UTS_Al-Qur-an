package com.example.alquranapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.alquranapp.nav.QuranNavGraph
import com.example.alquranapp.ui.theme.AlQuranAppTheme
import com.example.alquranapp.notif.AlarmScheduler
import com.example.alquranapp.notif.NotificationHelper

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            NotificationHelper.createChannel(this)
            AlarmScheduler.scheduleDailyReminder(this)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            NotificationHelper.createChannel(this)
            AlarmScheduler.scheduleDailyReminder(this)
        }
        setContent {
            AlQuranAppTheme {
                QuranApp()
            }
        }
    }
}
@Composable
fun QuranApp() {
    val navController = rememberNavController()
    QuranNavGraph(navController)
}