package com.example.ltpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ltpc.ui.AppScreen
import com.example.ltpc.ui.FiestaScreen
import com.example.ltpc.ui.HomeScreen
import com.example.ltpc.ui.MainViewModel
import com.example.ltpc.ui.RecipeScreen
import com.example.ltpc.ui.theme.LTPCTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LTPCTheme(darkTheme = viewModel.isNightMode.observeAsState(false).value) {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.observeAsState(AppScreen.HOME)
    val isNightMode by viewModel.isNightMode.observeAsState(false)
    val recipeState by viewModel.recipeState.observeAsState(null)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val currentTime = viewModel.getFormattedCurrentTime()

    LTPCTheme(darkTheme = isNightMode) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentScreen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            isNightMode = isNightMode,
                            currentTime = currentTime,
                            onToggleNightMode = { viewModel.toggleTimeOffset() },
                            onNavigateToRecipe = {
                                viewModel.fetchMealWithPokemon()
                                viewModel.navigateTo(AppScreen.RECIPE)
                            },
                            onNavigateToFiesta = {
                                viewModel.fetchCocktailWithPokemon()
                                viewModel.navigateTo(AppScreen.FIESTA)
                            },
                            onNavigateToPokedex = {
                                viewModel.navigateTo(AppScreen.POKEDEX)
                            }
                        )
                    }

                    AppScreen.RECIPE -> {
                        RecipeScreen(
                            isNightMode = isNightMode,
                            recipe = recipeState,
                            isLoading = isLoading,
                            onFetchNext = { viewModel.fetchMealWithPokemon() },
                            onBackToHome = { viewModel.navigateTo(AppScreen.HOME) }
                        )
                    }

                    AppScreen.FIESTA -> {
                        FiestaScreen(
                            recipe = recipeState,
                            isLoading = isLoading,
                            onFetchNext = { viewModel.fetchCocktailWithPokemon() },
                            onBackToHome = { viewModel.navigateTo(AppScreen.HOME) }
                        )
                    }

                    AppScreen.POKEDEX -> {
                        // Vista temporal de la Pokédex (se implementarán las pantallas de la guía en los siguientes pasos)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Pokédex UPB",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Paso 1 completado: Dependencias de Room agregadas.",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
                                Text("Volver al inicio")
                            }
                        }
                    }
                }
            }
        }
    }
}
