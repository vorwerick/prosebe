package cz.dzubera.prosebe.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.dzubera.prosebe.android.ui.screen.*
import cz.dzubera.prosebe.android.utils.AlarmUtils


object StyleColors {
    val ivory = Color(0xFFFAF2DB)
    val coffee = Color(0xFF363031)
    val avocado = Color(0xFF3A7E55)
    val acid = Color(0xFF8A6A74)

}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = StyleColors.ivory,
            primaryVariant = StyleColors.ivory,
            secondary = StyleColors.ivory,
        )
    } else {
        lightColors(
            primary = StyleColors.coffee,
            primaryVariant = StyleColors.coffee,
            secondary = StyleColors.coffee
        )

    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, DeviceOnReceiverService::class.java))
        } else {
            startService(Intent(this, DeviceOnReceiverService::class.java))
        }
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.e("HOHO", "onCreate: PERMISSION GRANTED")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            else -> {
                // The registered ActivityResultCallback gets the result of this request
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        }

        App.repository.getAlarms().forEach {
            AlarmUtils.setAlarm(
                TaskInfo(
                    it.id.toInt(),
                    it.name,
                    it.hour,
                    it.minute,
                ), this
            )
        }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController = navController) }
                        composable("register") { RegisterScreen(navController = navController) }
                        composable("home") { HomeScreen(navController = navController) }
                        composable(
                            "affirmation/{affirmationId}",
                            arguments = listOf(navArgument("affirmationId") {
                                type = NavType.IntType
                            })
                        ) {
                            val affirmationId = it.arguments?.getInt("affirmationId")
                            /* We check if is null */
                            affirmationId?.let {
                                AffirmationScreen(navController = navController, affirmationId)
                            }
                        }
                        composable(
                            "exercise/{exerciseId}",
                            arguments = listOf(navArgument("exerciseId") {
                                type = NavType.IntType
                            })
                        ) {
                            val exerciseId = it.arguments?.getInt("exerciseId")
                            /* We check if is null */
                            exerciseId?.let {
                                ExerciseScreen(navController = navController, exerciseId)
                            }
                        }

                    }
                }
            }
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }
}