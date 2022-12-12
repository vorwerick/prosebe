package cz.dzubera.prosebe.android.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cz.dzubera.prosebe.android.App
import cz.dzubera.prosebe.android.StyleColors

@Composable
fun AddAchieveDialog(
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit,
) {
    val name = remember { mutableStateOf(TextFieldValue()) }
    val time = remember { mutableStateOf(TextFieldValue("15:30")) }
    var errorText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,

    ) {
        Card(
            elevation = 16.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = StyleColors.acid,
        ) {
            Column(modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 12.dp)) {
                // TITLE
                Text(
                    text = "zapiš si úspěch!",
                    style = TextStyle(color = StyleColors.ivory, fontSize = 22.sp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                        },
                        Modifier.padding(top = 8.dp),
                        label = { Text(text = "úspěch", style = TextStyle(color = Color.LightGray)) })

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, style = TextStyle(fontSize = 16.sp, color = Color.Red))
                }

                // BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Zpět")
                    }
                    TextButton(onClick = {
                        val times = time.value.text.split(":")
                        if (times.size == 2) {
                            val h = times[0].toIntOrNull()
                            val m = times[1].toIntOrNull()
                            if (h != null && m != null) {
                                if (h in 0..23 && m >= 0 && m <= 59) {
                                    if(!name.value.text.isEmpty()){
                                        val newId = App.repository.getAchieves().size
                                        App.repository.addAchieve(
                                                h,
                                                m,
                                                name.value.text,
                                                false

                                        )
                                        onConfirmClicked.invoke()
                                    } else {
                                        errorText = "žádný název"
                                        return@TextButton
                                    }

                                }
                            }
                        }
                        errorText = "neplatný formát času"
                    }) {
                        Text(text = "Uložit")
                    }
                }
            }
        }

    }
}