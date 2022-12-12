package cz.dzubera.prosebe.android.ui.dialog

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kotlinx.coroutines.launch

@Composable
fun SendFeedbackDialog(
    onConfirmClicked: (note: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val feedback = remember { mutableStateOf(TextFieldValue()) }
    var errorText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            elevation = 16.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = StyleColors.acid,
        ) {
            Column(modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 12.dp).verticalScroll(ScrollState(0))) {
                // TITLE
                Text(
                    text = "napiš poznámku vývojáři",
                    style = TextStyle(color = Color.LightGray, fontSize = 22.sp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    OutlinedTextField(
                        value = feedback.value,
                        onValueChange = {
                            feedback.value = it
                        },
                        Modifier.padding(top = 8.dp),
                        label = { Text(text = "poznámka", style = TextStyle(color = Color.LightGray)) })

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, style = TextStyle(fontSize = 16.sp, color = Color.Red))
                }

                // BUTTONS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Zpět")
                    }
                    TextButton(onClick = {
                        if(feedback.value.text.isNotEmpty()){
                            onConfirmClicked.invoke(feedback.value.text)
                        } else {
                            errorText = "prázdné pole"
                        }
                    }) {
                        Text(text = "Odeslat")
                    }
                }
            }
        }

    }
}