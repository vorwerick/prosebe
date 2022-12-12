package cz.dzubera.prosebe.android.ui.screen

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.dzubera.prosebe.android.*
import cz.dzubera.prosebe.android.ui.component.Bar
import cz.dzubera.prosebe.android.ui.dialog.AddAchieveDialog
import cz.dzubera.prosebe.android.ui.dialog.AddAffirmationDialog
import cz.dzubera.prosebe.android.ui.dialog.SendFeedbackDialog
import cz.dzubera.prosebe.android.utils.AlarmUtils
import cz.dzubera.prosebe.android.utils.SharedPreferencesUtils
import cz.dzubera.prosebe.android.utils.observeAsState
import cz.dzubera.prosebe.repository.model.AchieveModel
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
    val state = lifecycleState.value

    val openDialogAddExercise = remember { mutableStateOf(false) }
    val openDialogFeedback = remember { mutableStateOf(false) }
    val openDialogSetAlarm = remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    if (openDialogAddExercise.value) {
        AddAchieveDialog({
            openDialogAddExercise.value = false
        }, {
            openDialogAddExercise.value = false
        })
    }
    if (openDialogSetAlarm.value) {
        AddAffirmationDialog({
            openDialogSetAlarm.value = false
            Toast.makeText(context, "Budík nastaven", Toast.LENGTH_SHORT).show()

            App.repository.getAlarms().forEach {
                AlarmUtils.setAlarm(
                    TaskInfo(
                        it.id.toInt(),
                        it.name,
                        it.hour,
                        it.minute,
                    ), context
                )
            }
        }, {
            openDialogSetAlarm.value = false
        })
    }
    if (openDialogFeedback.value) {
        SendFeedbackDialog({
            openDialogFeedback.value = false
            scope.launch {
                App.repository.sendFeedback(it)
            }
        }, {
            openDialogFeedback.value = false
        })
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { Bar(true, "tvoje dnešní úspěchy", scaffoldState, scope, navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "přidat úspěch") },
                onClick = { openDialogAddExercise.value = true },
                icon = { Icon(Icons.Filled.Edit, "add") },
                backgroundColor = StyleColors.acid,
                contentColor = StyleColors.ivory
            )
        },
        drawerGesturesEnabled = true,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Box(
                    Modifier
                        .height(162.dp)
                        .fillMaxWidth()
                        .background(StyleColors.avocado)
                ) {
                    if (App.repository.isLogged()) {
                        Column(modifier = Modifier.align(Alignment.BottomStart)) {
                            Text(
                                App.repository.getUser()!!.name,
                                modifier = Modifier.padding(24.dp, 0.dp),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontFamily = FontFamily.Default,
                                    color = StyleColors.ivory
                                )
                            )
                            Text(
                                "dnes jsi měl ${App.repository.getAchieves().size} úspěchů",
                                modifier = Modifier.padding(24.dp, 8.dp, 12.dp, 12.dp),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Default,
                                    color = StyleColors.ivory
                                )
                            )
                        }

                    }
                }
                Divider()
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(StyleColors.ivory)
                ) {
                    Column {
                        TextButton(onClick = {
                            openDialogSetAlarm.value = true
                        }) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "ranní budík",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    color = StyleColors.coffee
                                ),
                                textAlign = TextAlign.Start
                            )
                        }
                        Divider()

                        TextButton(onClick = {
                            openDialogFeedback.value = true
                        }) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "poznámky vývojáři",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    color = StyleColors.coffee
                                ),
                                textAlign = TextAlign.Start
                            )
                        }
                        Divider()

                        TextButton(onClick = {
                            SharedPreferencesUtils.set(context, "email", "")
                            SharedPreferencesUtils.set(context, "password", "")
                            App.repository.logout()
                            navController.navigate("login") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "odhlásit se",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    color = StyleColors.coffee
                                ),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }


            // Drawer items
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
                    achieves = App.repository.getAchieves(), navController
                )
            }


        },
        backgroundColor = StyleColors.ivory
    )
}

@Composable
fun AchievesGrid(achieves: List<AchieveModel>, navController: NavController) {
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
                    .clickable {
                        navController.navigate("exercise/${achievement.id}")
                    }
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

