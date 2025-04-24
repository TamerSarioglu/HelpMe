package com.tamersarioglu.helpme.presantaion.ui.home.components

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import com.tamersarioglu.helpme.domain.model.Earthquake
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun EarthquakeItem(
    earthquake: Earthquake,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(getMagnitudeColor(earthquake.magnitude)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%.1f", earthquake.magnitude),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = earthquake.location,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${earthquake.date} | ${earthquake.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Depth: ${earthquake.depth} km",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
    }
}

fun getMagnitudeColor(magnitude: Double): Color {
    return when {
        magnitude < 2.0 -> Color(0xFF4CAF50) // Green
        magnitude < 4.0 -> Color(0xFFFFC107) // Yellow
        magnitude < 6.0 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }
}