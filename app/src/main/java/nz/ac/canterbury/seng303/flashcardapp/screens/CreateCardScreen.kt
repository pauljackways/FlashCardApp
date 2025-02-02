package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.Card
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.*
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(navController: NavController, cardViewModel: CardViewModel, id: String?) {
    val cardId: Int? = id?.toIntOrNull()
    val createCardViewModel: CreateCardViewModel = viewModel()
    LaunchedEffect(cardId) {
        cardViewModel.getCardById(cardId)
    }
    val card by cardViewModel.selectedCard.collectAsState()
    val cards: List<Card> by cardViewModel.cards.collectAsState(emptyList())
    val context = LocalContext.current
    LaunchedEffect(card) {
        if (card != null) {
            createCardViewModel.initWithCard(card!!)
        } else if (createCardViewModel.options.isEmpty()) {
            createCardViewModel.initNewCard(4)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (card == null) {
            Text(
                text = "Add a new flash card",
                color = LightText,
                fontSize = 34.sp,
            )
        } else {
            Text(
                text = "Edit flash card",
                color = LightText,
                fontSize = 34.sp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f),
        ) {
            TextField(
                value = createCardViewModel.question,
                onValueChange = { createCardViewModel.updateQuestion(it) },
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
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp))
                    .wrapContentHeight()
                    .heightIn(min = 80.dp)
                    .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp)),
                maxLines = Int.MAX_VALUE
            )
            createCardViewModel.options.forEachIndexed { index, rowState ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(min = 60.dp)
                ) {
                    Checkbox(
                        checked = createCardViewModel.options[index].answer,
                        onCheckedChange = { newValue ->
                            createCardViewModel.updateOption(index, newValue, createCardViewModel.options[index].option)
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                    TextField(
                        value = createCardViewModel.options[index].option,
                        onValueChange = { newValue ->
                            createCardViewModel.updateOption(index, createCardViewModel.options[index].answer, newValue)
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .wrapContentHeight()
                            .heightIn(min = 50.dp)
                            .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp)),
                        maxLines = Int.MAX_VALUE
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Icon",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable {
                                createCardViewModel.removeOption(index)
                            }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        createCardViewModel.addOption()
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
                    val filteredOptions = createCardViewModel.options.filter { option -> option.option.isNotBlank() }
                    if (createCardViewModel.question.isBlank()) {
                        builder.setMessage("A flash card must have a question")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (!(filteredOptions.any { rowState -> rowState.answer })) {
                        builder.setMessage("A flash card must have at least 1 correct answer")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (filteredOptions.count { it.option.isNotBlank() } < 2) {
                        builder.setMessage("A flash card must have at least 2 answer options")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (filteredOptions
                            .filter { it.option.isNotBlank() }
                            .distinctBy { it.option }
                            .size < filteredOptions.count { it.option.isNotBlank() }
                    ) {
                        builder.setMessage("A flash card must have unique options")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else if (cards.any { card ->
                            card.question == createCardViewModel.question && (cardId == null || card.id != cardId)
                        }) {
                        builder.setMessage("A flash card with this question already exists")
                            .setCancelable(true)
                            .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    } else {
                        if (card != null) {
                            val newCard = Card(
                                id = card!!.id,
                                question = createCardViewModel.question,
                                options = filteredOptions,
                                timestamp = card!!.timestamp,
                                false
                            )
                            cardViewModel.editCardById(cardId, newCard)
                            navController.popBackStack()
                        } else {
                            cardViewModel.createCard(createCardViewModel.question, filteredOptions)
                            navController.popBackStack()
                        }

                    }
                },
                colors = defaultButtonColors(),
            ) {
                Text(text = "Save and return")
            }
        }
    }
}
