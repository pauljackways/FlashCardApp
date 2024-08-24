package nz.ac.canterbury.seng303.flashcardapp.screens

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.NavController
import nz.ac.canterbury.seng303.flashcardapp.models.Card
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.LightText
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.defaultButtonColors
import nz.ac.canterbury.seng303.flashcardapp.util.convertTimestampToReadableTime

@Composable
fun CardList(navController: NavController) {
    val cards = Card.getCards()

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
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = "Flash Cards",
                    color = LightText,
                    fontSize = 34.sp,
                )
            }
            items(cards) { card ->
                CardItem(navController = navController, card = card)
            }
        }
    }
}


@Composable
fun CardItem(navController: NavController, card: Card) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = card.question,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                            putExtra(SearchManager.QUERY, card.question) // Pass the query string
                        }
                        context.startActivity(intent)
                    },
                    colors = defaultButtonColors(),
                    modifier = Modifier
                        .width(80.dp)
                        .height(45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "External Search",
                        tint = Color.White
                    )
                }
                Button(
                    onClick = {
                    },
                    colors = defaultButtonColors(),
                    modifier = Modifier
                        .width(80.dp)
                        .height(45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Card",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Delete flash card: \"" + card.question + "\"?")
                            .setCancelable(true)
                            .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
                            .setPositiveButton("DELETE") { dialog, id -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    },
                    colors = defaultButtonColors(),
                    modifier = Modifier
                        .width(80.dp)
                        .height(45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Card",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
