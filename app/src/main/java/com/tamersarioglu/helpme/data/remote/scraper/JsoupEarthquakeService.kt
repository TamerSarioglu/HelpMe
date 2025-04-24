package com.tamersarioglu.helpme.data.remote.scraper

import android.os.Build
import androidx.annotation.RequiresApi
import com.tamersarioglu.helpme.data.remote.dto.EarthquakeDto
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

// JsoupEarthquakeService.kt
@Singleton
class JsoupEarthquakeService @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) {
    private val baseUrl = "https://www.koeri.boun.edu.tr/scripts/lst0.asp"

    // Regex with 10 capture groups:
    // 1=yyyy.MM.dd  2=HH:mm:ss  3=lat  4=lon  5=depth
    // 6=MD (or -.-)  7=ML  8=Mw  9=place (greedy but stops before last token)  10=quality
    private val earthquakePattern = Regex(
        """^(\d{4}\.\d{2}\.\d{2})\s+(\d{2}:\d{2}:\d{2})\s+([\d.]+)\s+([\d.]+)\s+([\d.]+)\s+(-\.-|\d+\.\d+)\s+(-\.-|\d+\.\d+)\s+(-\.-|\d+\.\d+)\s+(.+?)\s+(\S+)$"""
    )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchEarthquakes(): List<EarthquakeDto> = withContext(dispatcher) {
        val doc = Jsoup.connect(baseUrl)
            .timeout(10_000)
            .userAgent("Mozilla/5.0")
            .get()

        // grab the first <pre> block
        val pre = doc.selectFirst("pre")
            ?: throw IllegalStateException("Earthquake list not found in page")

        // preserve newlines / spacing exactly
        val lines = pre.wholeText().lines()

        // drop everything until the first real data line (starts with yyyy.MM.dd)
        val dataLines = lines.dropWhile { !it.matches(Regex("^\\d{4}\\.\\d{2}\\.\\d{2}\\s+.*")) }

        return@withContext dataLines.mapIndexedNotNull { idx, line ->
            earthquakePattern.matchEntire(line)?.destructured?.let { (date, time, lat, lon, depth, md, ml, mw, place, quality) ->
                // parse into proper types
                val dateTime = LocalDateTime.parse(
                    "$date $time",
                    DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
                )
                val magnitude = when {
                    ml != "-.-" -> ml.toDoubleOrNull()
                    md != "-.-" -> md.toDoubleOrNull()
                    mw != "-.-" -> mw.toDoubleOrNull()
                    else        -> null
                }
                EarthquakeDto(
                    id        = idx,
                    dateTime  = dateTime,
                    latitude  = lat.toDouble(),
                    longitude = lon.toDouble(),
                    depthKm   = depth.toDouble(),
                    magnitude = magnitude,
                    location  = place.trim(),
                    quality   = quality
                )
            }
        }
    }
}
