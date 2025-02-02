package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.copy
import nz.ac.canterbury.seng303.flashcardapp.models.Card
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightButtonPurple
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightInputGrey
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightText
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultButtonColors
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultCheckboxColors
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultRadioButtonColors
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CardViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.compose.foundation.layout.size

data class Game(val answerCard: Card, val questionCard: Card, var result: Boolean)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayCards(navController: NavController, cardViewModel: CardViewModel) {
    cardViewModel.getCards()
    val context = LocalContext.current
    val cards: List<Card> by cardViewModel.cards.collectAsState(emptyList())
    var gameList by rememberSaveable { mutableStateOf(mutableListOf<Game>()) }
    LaunchedEffect(Unit) {
        if (gameList.isEmpty()) {
            val shuffledCards = cards.shuffled()
            for (card in shuffledCards) {
                val shuffledCard = Card(
                    card.id,
                    card.question,
                    card.options.shuffled(),
                    System.currentTimeMillis(),
                    false
                )

                // Create a memory-separate copy of the shuffled card for answerCard
                val answerCard = Card(
                    card.id,
                    card.question,
                    shuffledCard.options.map { option ->
                        Card.Option(false, option.option)
                    },
                    System.currentTimeMillis(),
                    false
                )
                gameList += Game(answerCard, shuffledCard, false)
            }
        }
    }


    if (cards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "There are no cards created.\nPlease create some cards.",
                color = LightText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        var cardIndex by rememberSaveable { mutableIntStateOf(0) }
        var finalCorrectCount by rememberSaveable { mutableIntStateOf(0) }
        if (cardIndex < gameList.size) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Play flash cards",
                    color = LightText,
                    fontSize = 34.sp,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .weight(1f),
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = gameList[cardIndex].questionCard.question,
                        modifier = Modifier
                            .background(LightInputGrey)
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                    gameList[cardIndex].questionCard.options.forEachIndexed { index, rowState ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp)
                        ) {
                            var checkedState by rememberSaveable { mutableStateOf(Pair(cardIndex, false))}
                            if (cardIndex != checkedState.first) {
                                checkedState = Pair(cardIndex, false)
                            }
                            Checkbox(
                                checked = checkedState.second,
                                colors = defaultCheckboxColors(),
                                onCheckedChange = { isChecked ->
                                    checkedState = Pair(cardIndex, isChecked)
                                    gameList[cardIndex].answerCard.options[index].answer = isChecked
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = rowState.option,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${cardIndex+1}/${gameList.size}",
                        fontSize = 16.sp,
                        modifier = Modifier
                    )
                    Button(
                        onClick = {
                            var totalCount = 0
                            var correctCount = 0
                            var incorrectCount = 0
                            var uncheckedCount = 0
                            gameList[cardIndex].answerCard.options.forEachIndexed { index, rowState ->
                                if (gameList[cardIndex].questionCard.options[index].answer) {
                                    totalCount++
                                }
                                if (rowState.answer == gameList[cardIndex].questionCard.options[index].answer && gameList[cardIndex].questionCard.options[index].answer) {
                                    correctCount++
                                }
                                if (rowState.answer != gameList[cardIndex].questionCard.options[index].answer && !gameList[cardIndex].questionCard.options[index].answer) {
                                    incorrectCount++
                                }
                                if (!rowState.answer) {
                                    uncheckedCount++
                                }
                            }
                            if (uncheckedCount < gameList[cardIndex].questionCard.options.size) {
                                if (correctCount == totalCount && incorrectCount == 0) {
                                    Toast.makeText(context, "Correct Answer, ${correctCount}/${totalCount} correct answers selected", Toast.LENGTH_SHORT).show()
                                    finalCorrectCount++
                                    gameList[cardIndex].result = true
                                } else {
                                    if (correctCount == totalCount) {
                                        Toast.makeText(context, "Wrong Answer, too many answers selected", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Incomplete Answer, ${correctCount}/${totalCount} correct answers selected", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                cardIndex++
                            } else {
                                val builder = AlertDialog.Builder(context)
                                builder.setMessage("Please select at least one option")
                                    .setCancelable(true)
                                    .setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
                                val alert = builder.create()
                                alert.show()
                            }


                                  },
                        colors = defaultButtonColors()
                    ) {
                        Text("Submit")
                    }
                }
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Summary",
                        color = LightText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                    )
                    Text(
                        text = "Score : ${finalCorrectCount}/${gameList.size}",
                        color = LightText,
                        fontSize = 40.sp,
                    )
                }
                items(gameList) { game ->
                    SummaryItem(navController = navController, game, cardViewModel)
                }
            }
        }
    }
}

@Composable
fun SummaryItem(navController: NavController, game: Game, cardViewModel: CardViewModel) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = game.questionCard.question,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
            )
            if (game.result) {
                Icon(
                    imageVector = Icons.Default.Check,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Tick Icon"
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Cross Icon"
                )
            }
        }
    }
}
