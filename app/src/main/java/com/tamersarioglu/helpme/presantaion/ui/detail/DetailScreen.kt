package com.tamersarioglu.helpme.presantaion.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.presantaion.ui.home.components.getMagnitudeColor
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    earthquake: Earthquake,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Initialize OSMDroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earthquake Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AndroidView(
                    factory = { context ->
                        MapView(context).apply {
                            setMultiTouchControls(true)
                            controller.setZoom(10.0)

                            val geoPoint = GeoPoint(earthquake.latitude, earthquake.longitude)
                            controller.setCenter(geoPoint)

                            val marker = Marker(this)
                            marker.position = geoPoint
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.title = earthquake.location
                            marker.snippet = "Magnitude: ${earthquake.magnitude}"
                            overlays.add(marker)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Location header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = getMagnitudeColor(earthquake.magnitude),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = earthquake.location,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DetailRow("Date", earthquake.date)
                    DetailRow("Time", earthquake.time)
                    DetailRow("Magnitude", String.format("%.1f", earthquake.magnitude))
                    DetailRow("Depth", "${earthquake.depth} km")
                    DetailRow("Latitude", String.format("%.4f", earthquake.latitude))
                    DetailRow("Longitude", String.format("%.4f", earthquake.longitude))
                }
            }
        }
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))
}