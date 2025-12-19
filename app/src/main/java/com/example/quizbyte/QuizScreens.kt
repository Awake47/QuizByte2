package com.example.quizbyte

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuestionScreen(
    question: QuestionData,
    index: Int,
    total: Int,
    score: Int,
    onAnswered: (Boolean) -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    // состояние привязываем к конкретному вопросу, чтобы оно сбрасывалось при переходе
    var selected by remember(question) { mutableStateOf<Int?>(null) }
    var answered by remember(question) { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
            TextButton(onClick = onExit) {
                Text("◀ Выйти", color = colors.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
            }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Счёт: $score",
                            color = Color(0xFF22C55E),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Вопрос ${index + 1} / $total",
                        color = colors.onBackground.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (question.difficulty) {
                            "Easy" -> Color(0xFF064E3B)
                            "Medium" -> Color(0xFF78350F)
                            "Hard" -> Color(0xFF7F1D1D)
                            else -> Color(0xFF1E293B)
                        }
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = question.difficulty,
                        color = when (question.difficulty) {
                            "Easy" -> Color(0xFF34D399)
                            "Medium" -> Color(0xFFFBBF24)
                            "Hard" -> Color(0xFFFCA5A5)
                            else -> Color(0xFF9CA3AF)
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = question.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground,
                        lineHeight = 28.sp
                    )
                    question.code?.let {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF020617).copy(alpha = 0.8f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = it.replace("\\n", "\n"),
                                    color = Color(0xFFBAE6FD),
                                    fontSize = 13.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        itemsIndexed(question.options) { i, option ->
            val isCorrect = i == question.correctIndex
            val isSelected = selected == i
            val bg = when {
                answered && isCorrect -> Color(0xFF064E3B)
                answered && isSelected && !isCorrect -> Color(0xFF7F1D1D)
                isSelected -> colors.surface.copy(alpha = 0.7f)
                else -> colors.surface
            }
            val borderColor = when {
                answered && isCorrect -> Color(0xFF34D399)
                answered && isSelected && !isCorrect -> Color(0xFFFCA5A5)
                isSelected -> Color(0xFF38BDF8)
                else -> Color.Transparent
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !answered) {
                        selected = i
                        answered = true
                        onAnswered(isCorrect)
                    },
                colors = CardDefaults.cardColors(containerColor = bg),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(if (isSelected || answered) 8.dp else 4.dp),
                border = if (borderColor != Color.Transparent) {
                    androidx.compose.foundation.BorderStroke(2.dp, borderColor)
                } else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        color = colors.onBackground,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f),
                        lineHeight = 22.sp
                    )
                    if (answered) {
                        Text(
                            text = when {
                                isCorrect -> "✓"
                                isSelected && !isCorrect -> "✗"
                                else -> ""
                            },
                            color = when {
                                isCorrect -> Color(0xFF34D399)
                                else -> Color(0xFFFCA5A5)
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = answered,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "💡",
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Объяснение",
                                fontWeight = FontWeight.Bold,
                                color = colors.primary,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = question.explanation,
                            color = colors.onBackground.copy(alpha = 0.8f),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (index + 1 < total) "Следующий вопрос →" else "Показать результат →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    onRestart: () -> Unit,
    onBackHome: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val percentage = (score.toFloat() / total.toFloat() * 100f).toInt()
    
    // Анимация прогресс-бара
    var animatedProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(percentage) {
        animatedProgress = percentage / 100f
    }
    val progressAnimation by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    val resultColor = when {
        percentage >= 90 -> Color(0xFF22C55E)
        percentage >= 70 -> Color(0xFF3B82F6)
        percentage >= 50 -> Color(0xFFEAB308)
        else -> Color(0xFFFB7185)
    }
    val resultEmoji = when {
        percentage >= 90 -> "🏆"
        percentage >= 70 -> "⭐"
        percentage >= 50 -> "👍"
        else -> "💪"
    }
    val resultText = when {
        percentage >= 90 -> "Превосходно!"
        percentage >= 70 -> "Отлично!"
        percentage >= 50 -> "Хорошо!"
        else -> "Продолжай!"
    }
    val achievement = when {
        percentage == 100 -> "Идеальный результат! 🎯"
        percentage >= 90 -> "Мастер Python! 🐍"
        percentage >= 70 -> "Опытный разработчик! 💻"
        percentage >= 50 -> "Начинающий профи! 📚"
        else -> "Новичок! 🌱"
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Достижение
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = resultColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = achievement,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = resultColor,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Эмодзи и процент
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = resultEmoji,
                        fontSize = 64.sp
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$percentage%",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = resultColor
                        )
                        Text(
                            text = resultText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.onBackground
                        )
                    }
                }
                
                // Счёт
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colors.surface.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Правильных ответов",
                                color = colors.onBackground.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$score из $total",
                                color = colors.onBackground,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = resultEmoji,
                            fontSize = 40.sp
                        )
                    }
                }
                
                // Прогресс-бар с анимацией
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Точность",
                            color = colors.onBackground.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$percentage%",
                            color = resultColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(
                                colors.surface.copy(alpha = 0.3f),
                                RoundedCornerShape(999.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressAnimation)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(resultColor, resultColor.copy(alpha = 0.7f))
                                    ),
                                    RoundedCornerShape(999.dp)
                                )
                        )
                    }
                }
                
                HorizontalDivider(
                    color = colors.onSurface.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                
                // Мотивационное сообщение
                Text(
                    text = when {
                        percentage >= 90 -> "Ты на пути к мастерству! Продолжай в том же духе! 🚀"
                        percentage >= 70 -> "Отличный результат! Ещё немного практики и ты станешь профи! 💪"
                        percentage >= 50 -> "Хороший старт! Продолжай учиться и практиковаться! 📖"
                        else -> "Не сдавайся! Каждая ошибка — это шаг к успеху! 🌟"
                    },
                    textAlign = TextAlign.Center,
                    color = colors.onBackground.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Кнопки
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🔄 Ещё раз",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                OutlinedButton(
                    onClick = onBackHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🏠 К выбору режимов",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun PvPPlayerSetupScreen(
    player1Name: String,
    player2Name: String,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("◀ Назад", color = colors.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "⚔️ PvP Режим",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Соревнуйтесь 1 на 1!\nКаждый отвечает на свой вопрос.",
                color = colors.onBackground.copy(alpha = 0.8f),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "👤 Игрок 1",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    OutlinedTextField(
                        value = player1Name,
                        onValueChange = onPlayer1NameChange,
                        label = { Text("Имя игрока 1", color = colors.onSurface.copy(alpha = 0.7f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text("Введите имя", color = colors.onSurface.copy(alpha = 0.5f)) }
                    )
                }
                
                HorizontalDivider(
                    color = colors.onSurface.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "👤 Игрок 2",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                    OutlinedTextField(
                        value = player2Name,
                        onValueChange = onPlayer2NameChange,
                        label = { Text("Имя игрока 2", color = colors.onSurface.copy(alpha = 0.7f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            focusedBorderColor = colors.secondary,
                            unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text("Введите имя", color = colors.onSurface.copy(alpha = 0.5f)) }
                    )
                }
                
                Spacer(Modifier.height(8.dp))
                
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = player1Name.isNotBlank() && player2Name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "⚔️ Начать дуэль",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun PvPQuestionScreen(
    question: QuestionData,
    index: Int,
    total: Int,
    playerTurn: Int,
    player1Name: String,
    player2Name: String,
    player1Score: Int,
    player2Score: Int,
    onAnswered: (Boolean, Int) -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var selected by remember(question, playerTurn) { mutableStateOf<Int?>(null) }
    var answered by remember(question, playerTurn) { mutableStateOf(false) }
    val currentPlayerName = if (playerTurn == 0) player1Name else player2Name
    val currentPlayerColor = if (playerTurn == 0) colors.primary else colors.secondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onExit) {
                    Text("◀ Выйти", color = colors.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = colors.primary.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$player1Name: $player1Score",
                            color = colors.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = colors.secondary.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$player2Name: $player2Score",
                            color = colors.secondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Вопрос ${index + 1} / $total",
                        color = colors.onBackground.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = currentPlayerColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Ход: $currentPlayerName",
                        color = currentPlayerColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = question.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground,
                        lineHeight = 28.sp
                    )
                    question.code?.let {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF020617).copy(alpha = 0.8f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = it.replace("\\n", "\n"),
                                    color = Color(0xFFBAE6FD),
                                    fontSize = 13.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        itemsIndexed(question.options) { i, option ->
            val isCorrect = i == question.correctIndex
            val isSelected = selected == i
            val bg = when {
                answered && isCorrect -> Color(0xFF064E3B)
                answered && isSelected && !isCorrect -> Color(0xFF7F1D1D)
                isSelected -> currentPlayerColor.copy(alpha = 0.2f)
                else -> colors.surface
            }
            val borderColor = when {
                answered && isCorrect -> Color(0xFF34D399)
                answered && isSelected && !isCorrect -> Color(0xFFFCA5A5)
                isSelected -> currentPlayerColor
                else -> Color.Transparent
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !answered) {
                        selected = i
                        answered = true
                        onAnswered(isCorrect, playerTurn)
                    },
                colors = CardDefaults.cardColors(containerColor = bg),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(if (isSelected || answered) 8.dp else 4.dp),
                border = if (borderColor != Color.Transparent) {
                    BorderStroke(2.dp, borderColor)
                } else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        color = colors.onBackground,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f),
                        lineHeight = 22.sp
                    )
                    if (answered) {
                        Text(
                            text = when {
                                isCorrect -> "✓"
                                isSelected && !isCorrect -> "✗"
                                else -> ""
                            },
                            color = when {
                                isCorrect -> Color(0xFF34D399)
                                else -> Color(0xFFFCA5A5)
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = answered,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "💡", fontSize = 20.sp)
                            Text(
                                text = "Объяснение",
                                fontWeight = FontWeight.Bold,
                                color = currentPlayerColor,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = question.explanation,
                            color = colors.onBackground.copy(alpha = 0.8f),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentPlayerColor
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (index + 1 < total) "Следующий вопрос →" else "Показать результат →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PvPResultScreen(
    player1Score: Int,
    player2Score: Int,
    total: Int,
    player1Name: String,
    player2Name: String,
    onRestart: () -> Unit,
    onBackHome: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val winner = when {
        player1Score > player2Score -> 1
        player2Score > player1Score -> 2
        else -> 0
    }
    val winnerName = when (winner) {
        1 -> player1Name
        2 -> player2Name
        else -> "Ничья"
    }
    val winnerColor = when (winner) {
        1 -> colors.primary
        2 -> colors.secondary
        else -> Color(0xFFEAB308)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "⚔️ Результаты дуэли",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = winnerColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = when (winner) {
                                1 -> "🏆 Победитель"
                                2 -> "🏆 Победитель"
                                else -> "🤝 Ничья"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = winnerColor
                        )
                        Text(
                            text = winnerName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = winnerColor
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.primary.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = player1Name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )
                            Text(
                                text = "$player1Score / $total",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.primary
                            )
                            Text(
                                text = "${(player1Score.toFloat() / total.toFloat() * 100f).toInt()}%",
                                fontSize = 14.sp,
                                color = colors.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.secondary.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = player2Name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.secondary
                            )
                            Text(
                                text = "$player2Score / $total",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.secondary
                            )
                            Text(
                                text = "${(player2Score.toFloat() / total.toFloat() * 100f).toInt()}%",
                                fontSize = 14.sp,
                                color = colors.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = colors.onSurface.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Text(
                    text = when (winner) {
                        1 -> "🎉 $player1Name одержал победу! Отличная игра!"
                        2 -> "🎉 $player2Name одержал победу! Отличная игра!"
                        else -> "🤝 Отличная игра! Оба игрока показали одинаковый результат!"
                    },
                    textAlign = TextAlign.Center,
                    color = colors.onBackground.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🔄 Ещё раз",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onBackHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🏠 К выбору режимов",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


