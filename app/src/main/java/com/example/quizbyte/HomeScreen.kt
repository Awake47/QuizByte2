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
import androidx.compose.ui.text.style.TextAlign
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
    val colors = MaterialTheme.colorScheme
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Привет, $name",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.primary
                        )
                        Text(
                            text = "👋",
                            fontSize = 28.sp
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isDark) "🌙 Тёмная тема" else "☀️ Светлая тема",
                            color = colors.onBackground.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "•",
                            color = colors.onBackground.copy(alpha = 0.4f),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Уровень $level",
                            color = colors.primary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        
        Text(
            text = "Прокачивайся перед собесом, как в игре.",
            color = colors.onBackground.copy(alpha = 0.8f),
            fontSize = 15.sp
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HomeTab.values().forEach { tab ->
                val selected = currentTab == tab
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { currentTab = tab },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) colors.surface else colors.surface.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(if (selected) 8.dp else 0.dp)
                ) {
                    Text(
                        text = when (tab) {
                            HomeTab.Learn -> "Учиться"
                            HomeTab.Progress -> "Прогресс"
                            HomeTab.Profile -> "Профиль"
                        },
                        color = if (selected) colors.primary else colors.onBackground.copy(alpha = 0.6f),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        when (currentTab) {
            HomeTab.Learn -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Уровень $level",
                                    color = colors.secondary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                // Бейдж уровня
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colors.primary.copy(alpha = 0.2f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when {
                                            level >= 10 -> "🏆 Мастер"
                                            level >= 5 -> "⭐ Эксперт"
                                            level >= 3 -> "💎 Профи"
                                            else -> "🌱 Новичок"
                                        },
                                        color = colors.primary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Junior Hunter",
                                color = colors.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                    RoundedCornerShape(999.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(progress)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF22C55E),
                                                Color(0xFF38BDF8),
                                                Color(0xFFA855F7)
                                            )
                                        ),
                                        RoundedCornerShape(999.dp)
                                    )
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$xp / $xpToNext XP",
                                color = colors.onBackground.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${((xp.toFloat() / xpToNext.toFloat()) * 100).toInt()}%",
                                color = colors.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Режимы тренировок",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
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
                    ModeCard(
                        title = "⚔️ PvP Дуэль",
                        description = "Соревнуйтесь 1 на 1 с другом! Каждый отвечает на свои вопросы.",
                        badge = "Соревновательный режим",
                        enabled = true,
                        onClick = { onStartQuiz(QuizMode.PvP) }
                    )
                }
            }

            HomeTab.Progress -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Прогресс",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                        HorizontalDivider(
                            color = colors.onSurface.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Уровень:",
                                color = colors.onBackground.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$level",
                                color = colors.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Текущий опыт:",
                                color = colors.onBackground.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$xp / $xpToNext XP",
                                color = Color(0xFF22C55E),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        HorizontalDivider(
                            color = colors.onSurface.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Всего вопросов: $mixedCount",
                                color = colors.onBackground.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = colors.primary.copy(alpha = 0.15f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "📊 Статистика",
                                    color = colors.primary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            HomeTab.Profile -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Профиль",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                        HorizontalDivider(
                            color = colors.onSurface.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ник:",
                                color = colors.onBackground.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = name,
                                color = colors.onBackground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Уровень:",
                                color = colors.onBackground.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$level",
                                color = colors.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        HorizontalDivider(
                            color = colors.onSurface.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                        Text(
                            text = "Здесь позже можно будет добавить аватар, достижения и т.д.",
                            color = colors.onBackground.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onOpenMenu,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Открыть настройки",
                                fontSize = 15.sp,
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
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(if (enabled) 10.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = description,
                color = if (enabled) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(
                        if (enabled) Color(0x3310B981) else Color(0x3364748B),
                        RoundedCornerShape(999.dp)
                    )
            ) {
                Text(
                    text = badge,
                    color = if (enabled) Color(0xFF6EE7B7) else Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}


