package cz.dzubera.prosebe.android

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.dzubera.prosebe.android.ui.dialog.AddAchieveDialog
import cz.dzubera.prosebe.android.ui.screen.*
import cz.dzubera.prosebe.repository.model.AchieveModel
import java.util.*


class AchieveActivity : ComponentActivity() {

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PlaySoundService::class.java))
    }

    fun cancelActivity() {
        finish()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            ExtendedFloatingActionButton(
                                text = { Text(text = "zapiš si úspěch") },
                                onClick = {
                                    openDialogAddExercise.value = true
                                },
                                icon = { Icon(Icons.Filled.Edit, "add") },
                                backgroundColor = StyleColors.acid,
                                contentColor = StyleColors.ivory
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            ExtendedFloatingActionButton(
                                text = { Text(text = "pokračuj") },
                                onClick = {
                                    cancelActivity()
                                },
                                icon = { Icon(Icons.Filled.ArrowForward, "add") },
                                backgroundColor = StyleColors.acid,
                                contentColor = StyleColors.ivory
                            )
                        }

                    },
                    content = {
                        val achieves = App.repository.getAchieves().filter {
                            DateUtils.isToday(it.created)
                        }
                        if (achieves.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "žádné úspěchy",
                                    modifier = Modifier.padding(16.dp),
                                    style = TextStyle(fontSize = 20.sp, color = StyleColors.coffee)
                                )
                            }
                        } else {
                            AchievesGrid(
                                achieves = App.repository.getAchieves()
                            )
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun AchievesGrid(achieves: List<AchieveModel>) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 256.dp),
            modifier = Modifier.padding(8.dp),

            ) {
            items(achieves.size) { a ->
                val achievement = achieves[a]
                val doneDay = Calendar.getInstance().apply {
                    timeInMillis = achievement.doneAt
                }
                val today = Calendar.getInstance()
                var done = false
                done = doneDay.get(Calendar.DATE) <= today.get(Calendar.DATE)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StyleColors.avocado)
                        .padding(10.dp),

                    ) {

                    Text(
                        text = achievement.name,
                        style = TextStyle(fontSize = 20.sp, color = StyleColors.ivory)
                    )

                }
            }

        }
    }


}