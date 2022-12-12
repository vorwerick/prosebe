package cz.dzubera.prosebe.android.ui.dialog

import android.widget.Toast
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
import cz.dzubera.prosebe.android.TaskInfo
import cz.dzubera.prosebe.android.utils.AlarmUtils
import cz.dzubera.prosebe.repository.model.ALARM_MODEL_MORNING_AFFIRMATION
import java.util.Calendar

@Composable
fun AddAffirmationDialog(
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit,
) {
    val alarm = App.repository.getAlarmById(ALARM_MODEL_MORNING_AFFIRMATION)

    val hour = remember { mutableStateOf(TextFieldValue(alarm?.hour.toString())) }
    val min = remember { mutableStateOf(TextFieldValue(alarm?.minute.toString())) }

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
                    text = "nastav čas ranního budíku",
                    style = TextStyle(color = StyleColors.ivory, fontSize = 22.sp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    OutlinedTextField(
                        value = hour.value,
                        onValueChange = {
                            hour.value = it
                        },
                        Modifier.padding(top = 8.dp),
                        label = {
                            Text(
                                text = "hodina",
                                style = TextStyle(color = Color.DarkGray)
                            )
                        })
                    OutlinedTextField(
                        value = min.value,
                        onValueChange = {
                            min.value = it
                        },
                        Modifier.padding(top = 8.dp),
                        label = {
                            Text(
                                text = "minuta",
                                style = TextStyle(color = Color.DarkGray)
                            )
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, style = TextStyle(fontSize = 16.sp, color = Color.Red))
                }

                // BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Zpět")
                    }
                    TextButton(onClick = {
                        val h = hour.value.text.toIntOrNull()
                        val m = min.value.text.toIntOrNull()
                        if (h != null && m != null) {
                            if (h in 0..23 && m >= 0 && m <= 59) {
                                App.repository.updateAlarm(
                                    ALARM_MODEL_MORNING_AFFIRMATION,
                                    h,
                                    m,
                                )

                                onConfirmClicked.invoke()
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