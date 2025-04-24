package com.tamersarioglu.helpme.presantaion.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tamersarioglu.helpme.presantaion.navigation.Screen
import com.tamersarioglu.helpme.presantaion.ui.home.components.EarthquakeItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earthquake Monitor") },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            Icons.Default.Filter,
                            contentDescription = "Sort"
                        )
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Latest First") },
                            onClick = {
                                viewModel.setSortType(HomeViewModel.SortType.TIME)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Magnitude (High to Low)") },
                            onClick = {
                                viewModel.setSortType(HomeViewModel.SortType.MAGNITUDE_DESC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Magnitude (Low to High)") },
                            onClick = {
                                viewModel.setSortType(HomeViewModel.SortType.MAGNITUDE_ASC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Depth (Deep to Shallow)") },
                            onClick = {
                                viewModel.setSortType(HomeViewModel.SortType.DEPTH_DESC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Depth (Shallow to Deep)") },
                            onClick = {
                                viewModel.setSortType(HomeViewModel.SortType.DEPTH_ASC)
                                showSortMenu = false
                            }
                        )
                    }

                    IconButton(onClick = { viewModel.getEarthquakes() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error.isNotBlank() -> {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }
                state.earthquakes.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.earthquakes) { earthquake ->
                            EarthquakeItem(
                                earthquake = earthquake,
                                onItemClick = {
                                    navController.navigate(Screen.Detail.createRoute(earthquake))
                                }
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No earthquakes found",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}