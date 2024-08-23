package nz.ac.canterbury.seng303.flashcardapp.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.Card
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightButtonPurple
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightInputGrey
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightText
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultButtonColors
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultRadioButtonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayCards(navController: NavController) {
    val cards = Card.getCards()
    val card = cards.first()

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
            Text(
                fontSize = 16.sp,
                text = card.question,
                modifier = Modifier
                    .background(LightInputGrey)
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, LightButtonPurple, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
            card.options.forEachIndexed { index, rowState ->
                var selectedOption by rememberSaveable { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp)
                ) {
                    RadioButton(
                        selected = selectedOption,
                        colors = defaultRadioButtonColors(),
                        onClick = {
                            selectedOption = !selectedOption
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "1/3",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                )
                Button(onClick = { },
                    colors = defaultButtonColors()) {
                    Text("Submit")
                }
            }
        }
    }
}