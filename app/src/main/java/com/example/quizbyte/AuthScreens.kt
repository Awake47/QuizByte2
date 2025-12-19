package com.example.quizbyte

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(80.dp))
            Text(
                text = "QuizByte",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primary
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Квизы по программированию,\nкоторые ощущаются как игра.",
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.8f),
                fontSize = 18.sp,
                lineHeight = 26.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "🎮 Прокачивай навыки\n💡 Учись на ошибках\n🏆 Достигай новых уровней",
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.6f),
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Поехали!",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AuthScreen(
    name: String,
    email: String,
    password: String,
    error: String?,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: (isLogin: Boolean) -> Unit,
    onForgotPassword: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Кибер‑арена QuizByte",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Создай свой профиль и сражайся в квиз‑дуэлях.\nОпыт — за каждый правильный ответ.",
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Ник в игре", color = colors.onSurface.copy(alpha = 0.7f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email", color = colors.onSurface.copy(alpha = 0.7f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Пароль (мин. 6 символов)", color = colors.onSurface.copy(alpha = 0.7f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Забыли пароль?",
                        color = colors.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                AnimatedVisibility(
                    visible = error != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = error ?: "",
                            color = Color(0xFFFCA5A5),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { onSubmit(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = colors.onPrimary,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            "Войти в аккаунт",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                OutlinedButton(
                    onClick = { onSubmit(false) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Создать новый профиль",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

            Text(
                text = "Режим офлайн тоже работает — квизы кешируются на устройстве.",
                color = colors.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
    }
}

@Composable
fun ForgotPasswordScreen(
    email: String,
    error: String?,
    success: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onSendReset: () -> Unit,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = colors.primary
                )
            }
            
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = colors.primary
            )
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "Восстановление пароля",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primary,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = if (success) {
                    "Письмо с инструкциями отправлено на ваш email!"
                } else {
                    "Введите email, и мы отправим вам\nинструкции по восстановлению пароля."
                },
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.8f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }

        if (!success) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Email", color = colors.onSurface.copy(alpha = 0.7f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    )

                    AnimatedVisibility(
                        visible = error != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = error ?: "",
                                color = Color(0xFFFCA5A5),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onSendReset,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading && email.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = colors.onPrimary,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                "Отправить инструкции",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "✓",
                        fontSize = 48.sp,
                        color = Color(0xFF34D399)
                    )
                    Text(
                        text = "Проверьте почту",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF34D399),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Мы отправили письмо на $email",
                        color = Color(0xFFA7F3D0),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF34D399)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Вернуться к входу",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}










