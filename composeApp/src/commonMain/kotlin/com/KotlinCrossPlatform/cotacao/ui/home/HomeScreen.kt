package com.KotlinCrossPlatform.cotacao.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.KotlinCrossPlatform.cotacao.domain.model.CurrencyQuote
import com.KotlinCrossPlatform.cotacao.presentation.HomeUiState
import com.KotlinCrossPlatform.cotacao.presentation.HomeViewModel
import com.KotlinCrossPlatform.cotacao.ui.theme.GreenUp
import com.KotlinCrossPlatform.cotacao.ui.theme.RedDown
import com.KotlinCrossPlatform.cotacao.ui.theme.TextSecondary
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import com.KotlinCrossPlatform.cotacao.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToHistory: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val brlAmount by viewModel.brlAmount.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Modern App Logo
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "Cotacao",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(onClick = { viewModel.loadQuotes() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Atualizar",
                        tint = GoldPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HomeUiState.Success -> {
                    HomeContent(
                        state = state,
                        brlAmount = brlAmount,
                        onAmountChange = viewModel::onAmountChange,
                        onNavigateToHistory = onNavigateToHistory
                    )
                }
                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = RedDown)
                        Button(onClick = { viewModel.loadQuotes() }) {
                            Text("Tentar Novamente")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    state: HomeUiState.Success,
    brlAmount: String,
    onAmountChange: (String) -> Unit,
    onNavigateToHistory: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Highlighting USD and EUR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.highlightedQuotes.forEach { quote ->
                    QuoteCard(
                        quote = quote,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToHistory("${quote.code}-BRL") }
                    )
                }
            }
        }

        // BRL Input
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Conversor da moeda Real",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                OutlinedTextField(
                    value = brlAmount,
                    onValueChange = onAmountChange,
                    placeholder = { Text("0.00") },
                    prefix = { Text("R$ ", color = MaterialTheme.colorScheme.secondary) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
            }
        }

        // Section Title
        item {
            Text(
                text = "Outras Moedas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Others
        items(state.otherQuotes) { quote ->
            val amount = brlAmount.toDoubleOrNull() ?: 0.0
            val bid = quote.bid.replace(",", ".").toDoubleOrNull() ?: 0.0
            val converted = if (bid != 0.0) amount / bid else 0.0

            CurrencyListItem(
                quote = quote,
                convertedValue = converted
            )
        }
        
        // Bottom Spacer for navigation bars
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
fun QuoteCard(quote: CurrencyQuote, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = quote.code,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "R$ ${quote.bid}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            val pct = quote.pctChange?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
            Text(
                text = "${if (pct >= 0) "▲" else "▼"} ${quote.pctChange}%",
                color = if (pct >= 0) GreenUp else RedDown,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun CurrencyListItem(quote: CurrencyQuote, convertedValue: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = quote.code.take(1),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column {
                    Text(
                        text = quote.name.split("/")[0],
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = quote.code,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${convertedValue.format(2)} ${quote.code}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldPrimary
                )
                Text(
                    text = "1 ${quote.code} = R$ ${quote.bid}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
