package cz.dzubera.prosebe.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
fun AffirmationScreen(
    navController: NavController,affirmationId: Int
) {
    val affirmationModel = App.repository.getAlarmById(affirmationId)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { Bar(false, "", scaffoldState, scope, navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "děkuji") },
                onClick = {
                    navController.popBackStack()
                },
                icon = { Icon(Icons.Filled.Star, "done") },
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
                        text = affirmationModel?.name ?: "neznámý",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 32.sp, color = StyleColors.coffee)
                    )
                }

            }

        },
        backgroundColor = StyleColors.ivory
    )
}