package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(navController: NavController) {
    val context = LocalContext.current
    var question by rememberSaveable { mutableStateOf("") }
    var rows by rememberSaveable { mutableStateOf(
        List(4) { OptionRowState(false, "") }
    ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add a new flash card",
            color = LightText,
            fontSize = 34.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f),
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
                placeholder = {
                    Text(
                        text = "Input question here",
                        style = TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp))
            )
            rows.forEachIndexed { index, rowState ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(min = 60.dp)
                ) {
                    Checkbox(
                        checked = rowState.answer,
                        onCheckedChange = { newValue ->
                            rows = rows.toMutableList().apply {
                                this[index] = this[index].copy(answer = newValue)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TextField(
                        value = rowState.option,
                        onValueChange = { newValue ->
                            rows = rows.toMutableList().apply {
                                this[index] = this[index].copy(option = newValue)
                            }
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        rows = rows + OptionRowState(false, "")
                    },
                    colors = defaultButtonColors(),
                    modifier = Modifier
                        .width(80.dp)
                        .height(45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Option",
                        tint = Color.White
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(LightBackgroundBlue),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val builder = AlertDialog.Builder(context)
                    if (question.isBlank()) {
                        builder.setMessage("A flash card must have a question")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (!(rows.any { rowState -> rowState.answer })) {
                        builder.setMessage("A flash card must have at least 1 correct answer")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (rows.count { it.option.isNotBlank() } < 2) {
                        builder.setMessage("A flash card must have at least 2 answer options")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else {
                        //There should also checks for uniqueness of options and title
                    }
                },
                colors = defaultButtonColors(),
            ) {
                Text(text = "Save and return")
            }
        }
    }
}

data class OptionRowState(
    val answer: Boolean, // Checkbox state
    val option: String   // TextField content
)