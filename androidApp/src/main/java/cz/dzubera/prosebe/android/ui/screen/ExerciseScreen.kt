package cz.dzubera.prosebe.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.dzubera.prosebe.android.App
import cz.dzubera.prosebe.android.StyleColors
import cz.dzubera.prosebe.android.ui.component.Bar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExerciseScreen(navController: NavController, exerciseId: Int) {
    val exerciseModel = App.repository.getAchieveById(exerciseId)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { Bar(false, "", scaffoldState, scope, navController) },

        content = {
            Box() {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = exerciseModel?.name ?: "neznámý",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 32.sp, color = StyleColors.coffee)
                    )
                }

            }

        },
        backgroundColor = StyleColors.ivory
    )
}