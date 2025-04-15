package com.example.alquranapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.alquranapp.model.Surah
import com.example.alquranapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSurahScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("📖 Surah", "📚 Juz")
    var surahList by remember { mutableStateOf<List<Surah>>(emptyList()) }
    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) {
            RetrofitInstance.api.getAllSurah()
        }
        surahList = result.data ?: emptyList()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Al-Qur'an Digital") },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Cari Ayat")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> SurahTab(surahList, navController)
                1 -> JuzTab(navController)
            }
        }
    }
}

@Composable
fun SurahTab(surahList: List<Surah>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(surahList) { surah ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { navController.navigate("detail/${surah.number}") },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Icon Surah",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${surah.number}. ${surah.englishName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = surah.englishNameTranslation,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = translateRevelationType(surah.revelationType),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = surah.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${surah.numberOfAyahs} Ayat",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

fun translateRevelationType(type: String): String {
    return when (type.lowercase()) {
        "meccan" -> "Makkiyah"
        "medinan" -> "Madaniyah"
        else -> type
    }
}

@Composable
fun JuzTab(navController: NavController) {
    val totalJuz = 30
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(totalJuz) { index ->
            val juzNumber = index + 1
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        navController.navigate("juzDetail/$juzNumber")
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Icon Juz",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 12.dp)
                    )
                    Text("Juz $juzNumber", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
