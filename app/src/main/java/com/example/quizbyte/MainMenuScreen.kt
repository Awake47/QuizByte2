package com.example.quizbyte

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    isDark: Boolean,
    onPlay: () -> Unit,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "QuizByte",
                fontSize = 38.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                color = colors.primary
            )
            Text(
                text = "Главное меню",
                fontSize = 22.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = colors.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Выбирай режим, настраивай тему и возвращайся к квизам когда захочешь.",
                color = colors.onBackground.copy(alpha = 0.8f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Играть",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Настройки",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Тема приложения",
                                color = colors.onBackground,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isDark) "Тёмная" else "Светлая",
                                color = colors.onBackground.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        }
                        Switch(
                            checked = isDark,
                            onCheckedChange = { onToggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.primary,
                                checkedTrackColor = colors.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                    
                    HorizontalDivider(
                        color = colors.onSurface.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF4444)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color(0xFFEF4444))
                    ) {
                        Text(
                            text = "Выйти из аккаунта",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Назад",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


