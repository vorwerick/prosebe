package cz.dzubera.prosebe.android

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.dzubera.prosebe.android.ui.dialog.AddAchieveDialog
import cz.dzubera.prosebe.android.ui.screen.*
import cz.dzubera.prosebe.repository.model.ALARM_MODEL_MORNING_AFFIRMATION


class AlarmActivity : ComponentActivity() {

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PlaySoundService::class.java))
    }

    fun cancelActivity(){
        finish()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val id =  intent.extras?.getInt("id")
        val title =  intent.extras?.getString("title")
        val isMorning = id == ALARM_MODEL_MORNING_AFFIRMATION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        startService(Intent(this, PlaySoundService::class.java))
        val ac = this
        setContent {
            MyApplicationTheme {
                val openDialogAddExercise = remember { mutableStateOf(false) }
                if (openDialogAddExercise.value) {
                    AddAchieveDialog({
                        openDialogAddExercise.value = false
                        Toast.makeText(ac, "Úspěch byl přidán", Toast.LENGTH_SHORT).show()
                    }, {
                        openDialogAddExercise.value = false
                    })
                }
                Scaffold(
                    backgroundColor = StyleColors.coffee,
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "pokračuj") },
                            onClick = {
                                cancelActivity()
                            },
                            icon = { Icon(Icons.Filled.ArrowForward, "add") },
                            backgroundColor = StyleColors.avocado,
                            contentColor = StyleColors.ivory
                        )

                    },
                    content = {

                        Box() {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = title ?: "přípomínka",
                                    modifier = Modifier.padding(16.dp),
                                    style = TextStyle(fontSize = 32.sp, color = StyleColors.ivory)
                                )
                                if (!isMorning) {
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(backgroundColor = StyleColors.acid),
                                            onClick = {
                                                openDialogAddExercise.value = true
                                            },
                                            shape = RoundedCornerShape(32.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                        ) {
                                            Text(
                                                text = "zapiš si úspěch",
                                                style = TextStyle(color = StyleColors.ivory)
                                            )
                                        }
                                    }
                                }


                            }

                        }
                    }
                )
            }
        }
    }


}