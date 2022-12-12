package cz.dzubera.prosebe.android.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.dzubera.prosebe.android.StyleColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Bar(home: Boolean, title: String, scaffoldState: ScaffoldState, scope: CoroutineScope, navController: NavController){
    TopAppBar(

        title = {
            Row(horizontalArrangement = Arrangement.Center, content = {
                Text(text = title)
            })
        },
        navigationIcon = {
            if(home){
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(Icons.Filled.Menu, "menuIcon")
                }
            } else {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Filled.ArrowBack, "backIcon")
                }
            }

        },
        backgroundColor = StyleColors.coffee,
        contentColor = StyleColors.ivory,
        elevation = 10.dp
    )
}