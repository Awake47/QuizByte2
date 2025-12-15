package com.example.quizbyte

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            TextButton(onClick = onExit) {
                Text("◀ Выйти", color = Color(0xFF9CA3AF))
            }
            Text(
                text = "Вопрос ${index + 1} / $total",
                color = Color(0xFFCBD5F5)
            )
            Text(
                text = "Сложность: ${question.difficulty}",
                color = when (question.difficulty) {
                    "Easy" -> Color(0xFF22C55E)
                    "Medium" -> Color(0xFFEAB308)
                    "Hard" -> Color(0xFFFB7185)
                    else -> Color(0xFF9CA3AF)
                },
                fontSize = 13.sp
            )
            Text(
                text = "Счёт: $score",
                color = Color(0xFF22C55E)
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF020617)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = question.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE5E7EB)
                    )
                    question.code?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFF0F172A),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = it,
                                color = Color(0xFFBAE6FD),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        itemsIndexed(question.options) { i, option ->
            val isCorrect = i == question.correctIndex
            val isSelected = selected == i
            val bg = when {
                answered && isCorrect -> Color(0xFF16A34A)
                answered && isSelected && !isCorrect -> Color(0xFFDC2626)
                isSelected -> Color(0xFF1E293B)
                else -> Color(0xFF020617)
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
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = option,
                    modifier = Modifier.padding(14.dp),
                    color = Color.White
                )
            }
        }

        item {
            AnimatedVisibility(visible = answered, enter = fadeIn()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Объяснение",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Text(
                            text = question.explanation,
                            color = Color(0xFFE5E7EB)
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (index + 1 < total) "Следующий вопрос" else "Показать результат"
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Результат",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF38BDF8)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "$score / $total",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Можно ещё лучше — попробуй ещё раз!",
            textAlign = TextAlign.Center,
            color = Color(0xFFCBD5F5)
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text("Ещё раз")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBackHome, modifier = Modifier.fillMaxWidth()) {
            Text("К выбору режимов")
        }
    }
}


