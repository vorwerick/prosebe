package cz.dzubera.prosebe.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.dzubera.prosebe.android.App
import cz.dzubera.prosebe.android.StyleColors
import cz.dzubera.prosebe.android.utils.SharedPreferencesUtils
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController
) {
    Scaffold(backgroundColor = StyleColors.ivory, content = {

        val context = LocalContext.current
        val prefilledEmail = SharedPreferencesUtils.get(context, "email")
        val prefilledPass = SharedPreferencesUtils.get(context, "password")

        val username = remember { mutableStateOf(TextFieldValue(prefilledEmail)) }
        val password = remember { mutableStateOf(TextFieldValue(prefilledPass)) }

        val scope = rememberCoroutineScope()
        var errorText by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(false) }


        LaunchedEffect(true) {

            scope.launch {
                loading = true
                val response =
                    App.repository.login(prefilledEmail ?: "", prefilledPass ?: "")
                loading = false
                if (response != null && response.status == 200) {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            }
        }

        if(loading){
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            ClickableText(
                text = AnnotatedString("nejsem zaregistrovaný"),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(48.dp),
                onClick = {
                    navController.navigate("register") {
                        popUpTo("login")
                    }
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = StyleColors.coffee
                )
            )
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "PRO SEBE", style = TextStyle(fontSize = 42.sp, fontWeight = FontWeight.Thin, color = StyleColors.avocado))
            Spacer(modifier = Modifier.height(128.dp))

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "email", style = TextStyle(color = StyleColors.coffee)) },
                value = username.value,
                onValueChange = { username.value = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "heslo",style = TextStyle(color = StyleColors.coffee)) },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = StyleColors.avocado),
                    onClick = {
                        scope.launch {
                            loading = true
                            val response =
                                App.repository.login(username.value.text, password.value.text)
                            loading = false
                            if (response == null) {
                                errorText = "nesprávný email nebo heslo"
                            } else {
                                if (response.status == 200) {
                                    SharedPreferencesUtils.set(
                                        context,
                                        "email",
                                        username.value.text
                                    )
                                    SharedPreferencesUtils.set(
                                        context,
                                        "password",
                                        password.value.text
                                    )
                                    navController.navigate("home") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    errorText = response.status.toString()
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "přihlásit", style = TextStyle(color = StyleColors.ivory))
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
            Text(text = errorText, style = TextStyle(fontSize = 16.sp, color = Color.Red))

        }

    })

}