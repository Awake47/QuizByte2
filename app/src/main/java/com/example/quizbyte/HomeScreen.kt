package com.example.quizbyte

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    name: String,
    level: Int,
    xp: Int,
    xpToNext: Int,
    isDark: Boolean,
    onOpenMenu: () -> Unit,
    onStartQuiz: (QuizMode) -> Unit
) {
    val mixedCount = quizQuestions.size
    val baseCount = quizQuestions.count { it.difficulty != "Hard" }
    val advancedCount = quizQuestions.count { it.difficulty != "Easy" }
    val progress = (xp.toFloat() / xpToNext.toFloat()).coerceIn(0f, 1f)
    val advancedEnabled = level >= 2
    var currentTab by remember { mutableStateOf(HomeTab.Learn) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Привет, $name 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF38BDF8)
            )
            Text(
                text = if (isDark) "Тёмная тема" else "Светлая тема",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )
        }

        Text(
            text = "Прокачивайся перед собесом, как в игре.",
            color = Color(0xFFE5E7EB)
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeTab.values().forEach { tab ->
                val selected = currentTab == tab
                TextButton(onClick = { currentTab = tab }) {
                    Text(
                        text = when (tab) {
                            HomeTab.Learn -> "Учиться"
                            HomeTab.Progress -> "Прогресс"
                            HomeTab.Profile -> "Профиль"
                        },
                        color = if (selected) Color(0xFF38BDF8) else Color(0xFF9CA3AF),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        when (currentTab) {
            HomeTab.Learn -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF020617)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Уровень $level • Junior Hunter", color = Color(0xFFA5B4FC))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .background(Color(0xFF020617), RoundedCornerShape(999.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progress)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF22C55E), Color(0xFF38BDF8))
                                        ),
                                        RoundedCornerShape(999.dp)
                                    )
                            )
                        }
                        Text(
                            text = "$xp / $xpToNext XP до следующего уровня",
                            color = Color(0xFF9CA3AF),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Режимы тренировок",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModeCard(
                        title = "Быстрый матч (Python)",
                        description = "Смешанные вопросы по синтаксису и базовым концептам Python.",
                        badge = "$mixedCount вопросов",
                        enabled = true,
                        onClick = { onStartQuiz(QuizMode.PythonMixed) }
                    )
                    ModeCard(
                        title = "Python база",
                        description = "Типы данных, списки, функции, циклы.",
                        badge = "$baseCount вопросов • для уровня Junior",
                        enabled = true,
                        onClick = { onStartQuiz(QuizMode.PythonBase) }
                    )
                    ModeCard(
                        title = "Python продвинутый",
                        description = "Генераторы, изменяемые аргументы, нюансы языка.",
                        badge = if (advancedEnabled) {
                            "$advancedCount вопросов • сложный режим"
                        } else {
                            "Откроется с уровня 2"
                        },
                        enabled = advancedEnabled,
                        onClick = { onStartQuiz(QuizMode.PythonAdvanced) }
                    )
                }
            }

            HomeTab.Progress -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF020617)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Прогресс",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Уровень: $level",
                            color = Color(0xFFE5E7EB)
                        )
                        Text(
                            text = "Текущий опыт: $xp / $xpToNext XP",
                            color = Color(0xFFE5E7EB)
                        )
                        Text(
                            text = "Всего вопросов: $mixedCount",
                            color = Color(0xFF9CA3AF),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            HomeTab.Profile -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF020617)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Профиль",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Ник: $name",
                            color = Color(0xFFE5E7EB)
                        )
                        Text(
                            text = "Уровень: $level",
                            color = Color(0xFFE5E7EB)
                        )
                        Text(
                            text = "Здесь позже можно будет добавить аватар, достижения и т.д.",
                            color = Color(0xFF9CA3AF),
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onOpenMenu,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Открыть настройки")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    description: String,
    badge: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color(0xFF020617) else Color(0xFF020617).copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = description,
                color = Color(0xFFCBD5F5),
                fontSize = 13.sp
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(
                        Color(0x3310B981),
                        RoundedCornerShape(999.dp)
                    )
            ) {
                Text(
                    text = badge,
                    color = Color(0xFF6EE7B7),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}


