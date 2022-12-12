package cz.dzubera.prosebe.android.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.dzubera.prosebe.android.App
import cz.dzubera.prosebe.android.StyleColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController
) {
    Scaffold(content = {

        val scope = rememberCoroutineScope()
        var errorText by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            val secondPassword = remember { mutableStateOf(TextFieldValue()) }


            Text(text = "registrace", style = TextStyle(fontSize = 18.sp, color = StyleColors.coffee))
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                label = { Text(text = "jméno",style = TextStyle(color = StyleColors.coffee)) },
                value = username.value,
                onValueChange = { username.value = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "email",style = TextStyle(color = StyleColors.coffee)) },
                value = email.value,
                onValueChange = { email.value = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "heslo",style = TextStyle(color = StyleColors.coffee)) },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "heslo znovu",style = TextStyle(color = StyleColors.coffee)) },
                value = secondPassword.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { secondPassword.value = it })
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            errorText = ""
                            if (password.value.text == secondPassword.value.text) {
                                val response = App.repository.register(
                                    email.value.text,
                                    username.value.text,
                                    password.value.text
                                )
                                if (response.status == 200) {
                                    // ok
                                    navController.popBackStack()
                                } else {
                                    errorText = response.status.toString()
                                }
                            } else {
                                errorText = "hesla se neshodují"
                                scope.launch {
                                    delay(3000)
                                    errorText = ""
                                }
                            }

                        }

                    },
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = StyleColors.avocado),

                    ) {
                    Text(text = "registrovat", style = TextStyle(color = StyleColors.ivory))
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
            Text(text = errorText, style = TextStyle(fontSize = 16.sp, color = Color.Red))
        }

    }, backgroundColor = StyleColors.ivory)

}