package nz.ac.canterbury.seng303.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.res.colorResource
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.FlashCardAppTheme
import nz.ac.canterbury.seng303.flashcardapp.ui.theme.*
import nz.ac.canterbury.seng303.flashcardapp.screens.*
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.*
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val cardViewModel: CardViewModel by koinViewModel()


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardViewModel.loadDefaultCardsIfNoneExist()

        setContent {
            FlashCardAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        // Add your AppBar content here
                        TopAppBar(
                            title = { Text("Flash Cards App") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                ) {

                    Box(modifier = Modifier.padding(it)) {
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LightBackgroundBlue)
                            .border(
                                width = 1.5.dp,
                                color = LightButtonPurple,
                                shape = RoundedCornerShape(16.dp)
                            )) {
                            NavHost(navController = navController, startDestination = "Home") {
                                composable("Home") {
                                    Home(navController = navController)
                                }
                                composable("CardList") {
                                    CardList(navController, cardViewModel)
                                }
                                composable("CreateCard") {
                                    CreateCard(navController, cardViewModel, null)
                                }
                                composable("PlayCards") {
                                    PlayCards(navController, cardViewModel)
                                }
                                composable("createCard/{cardId}") { backStackEntry ->
                                    val cardId = backStackEntry.arguments?.getString("cardId")
                                    CreateCard(navController, cardViewModel, cardId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("CardList") },
            colors = defaultButtonColors()) {
            Text("View Flash Cards")
        }
        Button(onClick = { navController.navigate("CreateCard") },
            colors = defaultButtonColors()) {
            Text("Create Flash Card")
        }
        Button(onClick = { navController.navigate("PlayCards") },
            colors = defaultButtonColors()) {
            Text("Play Flash Cards")
        }
    }
}
