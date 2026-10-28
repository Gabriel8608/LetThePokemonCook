package com.example.ltpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.ltpc.ui.AppScreen
import com.example.ltpc.ui.screens.FiestaScreen
import com.example.ltpc.ui.screens.HomeScreen
import com.example.ltpc.ui.MainViewModel
import com.example.ltpc.ui.screens.RecipeScreen
import com.example.ltpc.ui.screens.pokedex.FavoritosScreen
import com.example.ltpc.ui.screens.pokedex.PokemonDetailScreen
import com.example.ltpc.ui.screens.pokedex.PokemonListScreen
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
                        PokemonListScreen(
                            onPokemonClick = { pokemon ->
                                viewModel.verDetallePokemon(pokemon.nombre)
                            },
                            onVerFavoritos = {
                                viewModel.navigateTo(AppScreen.FAVORITOS)
                            },
                            onVolver = {
                                viewModel.navigateTo(AppScreen.HOME)
                            }
                        )
                    }

                    AppScreen.FAVORITOS -> {
                        FavoritosScreen(
                            onVolver = {
                                viewModel.navigateTo(AppScreen.POKEDEX)
                            },
                            onPokemonClick = { pokemon ->
                                viewModel.verDetallePokemon(pokemon.nombre)
                            }
                        )
                    }

                    AppScreen.DETALLE -> {
                        PokemonDetailScreen(
                            onVolver = {
                                viewModel.navigateTo(AppScreen.POKEDEX)
                            }
                        )
                    }
                }
            }
        }
    }
}
