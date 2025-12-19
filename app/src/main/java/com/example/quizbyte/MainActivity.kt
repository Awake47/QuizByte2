package com.example.quizbyte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Проверяем, не инициализирован ли Firebase уже
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
        } catch (e: Exception) {
            // Если Firebase не настроен, продолжаем без него
            android.util.Log.e("MainActivity", "Firebase initialization failed: ${e.message}")
        }
        setContent {
            var isDark by remember { mutableStateOf(true) }
            QuizByteTheme(isDark = isDark) {
                val auth = remember { 
                    try {
                        FirebaseAuth.getInstance()
                    } catch (e: Exception) {
                        null
                    }
                }
                if (auth != null) {
                    QuizByteApp(
                        auth = auth,
                        isDark = isDark,
                        onToggleTheme = { isDark = !isDark }
                    )
                } else {
                    // Показываем сообщение об ошибке конфигурации
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ошибка конфигурации Firebase",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Пожалуйста, настройте Firebase:\n1. Создайте проект в Firebase Console\n2. Скачайте google-services.json\n3. Замените файл в app/",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

/* ---------- Приложение ---------- */

@Composable
fun QuizByteApp(
    auth: FirebaseAuth,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    var screen by remember { mutableStateOf<Screen>(Screen.MainMenu) }
    var previousScreen by remember { mutableStateOf<Screen?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var level by remember { mutableStateOf(1) }
    var xp by remember { mutableStateOf(0) }
    val xpToNext = 100
    var currentMode by remember { mutableStateOf(QuizMode.PythonMixed) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var forgotPasswordEmail by remember { mutableStateOf("") }
    var forgotPasswordError by remember { mutableStateOf<String?>(null) }
    var forgotPasswordSuccess by remember { mutableStateOf(false) }
    var forgotPasswordLoading by remember { mutableStateOf(false) }
    // PvP состояние
    var pvpPlayer1Name by remember { mutableStateOf("") }
    var pvpPlayer2Name by remember { mutableStateOf("") }
    var pvpPlayer1Score by remember { mutableStateOf(0) }
    var pvpPlayer2Score by remember { mutableStateOf(0) }
    var pvpCurrentPlayerTurn by remember { mutableStateOf(0) } // 0 или 1

    // Если пользователь уже залогинен — сразу на главный экран
    LaunchedEffect(auth.currentUser) {
        if (auth.currentUser != null) {
            val firebaseUser = auth.currentUser
            val fallbackName = firebaseUser?.email?.substringBefore("@").orEmpty()
            name = firebaseUser?.displayName?.ifBlank { fallbackName }.orEmpty()
            email = firebaseUser?.email.orEmpty()
            screen = Screen.Home
        }
    }

    val currentQuestions = when (currentMode) {
        QuizMode.PythonMixed -> quizQuestions
        QuizMode.PythonBase -> quizQuestions.filter { it.difficulty != "Hard" }
        QuizMode.PythonAdvanced -> quizQuestions.filter { it.difficulty != "Easy" }
        QuizMode.PvP -> quizQuestions.shuffled().take(10) // Для PvP берём 10 случайных вопросов
    }

    when (val s = screen) {
        Screen.MainMenu -> MainMenuScreen(
            isDark = isDark,
            onPlay = {
                if (auth.currentUser != null) {
                    screen = Screen.Home
                } else {
                    screen = Screen.Onboarding
                }
            },
            onToggleTheme = onToggleTheme,
            onLogout = {
                auth.signOut()
                name = ""
                email = ""
                password = ""
                score = 0
                level = 1
                xp = 0
                previousScreen = null
                screen = Screen.Auth
            },
            onBack = {
                screen = previousScreen ?: Screen.Home
                previousScreen = null
            }
        )
        Screen.Onboarding -> OnboardingScreen { screen = Screen.Auth }
        Screen.ForgotPassword -> ForgotPasswordScreen(
            email = forgotPasswordEmail,
            error = forgotPasswordError,
            success = forgotPasswordSuccess,
            isLoading = forgotPasswordLoading,
            onEmailChange = { forgotPasswordEmail = it },
            onSendReset = {
                if (forgotPasswordEmail.isBlank()) {
                    forgotPasswordError = "Введите email"
                    return@ForgotPasswordScreen
                }
                forgotPasswordLoading = true
                forgotPasswordError = null
                try {
                    auth.sendPasswordResetEmail(forgotPasswordEmail)
                        .addOnCompleteListener { task ->
                            forgotPasswordLoading = false
                            if (task.isSuccessful) {
                                forgotPasswordSuccess = true
                            } else {
                                val exception = task.exception
                                forgotPasswordError = when {
                                    exception?.message?.contains("API key") == true -> 
                                        "Ошибка конфигурации Firebase. Проверьте google-services.json"
                                    else -> exception?.localizedMessage
                                        ?: "Не удалось отправить письмо. Проверьте email."
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            forgotPasswordLoading = false
                            forgotPasswordError = if (e.message?.contains("API key") == true) {
                                "Ошибка конфигурации Firebase. Проверьте google-services.json"
                            } else {
                                e.localizedMessage ?: "Ошибка отправки письма"
                            }
                        }
                } catch (e: Exception) {
                    forgotPasswordLoading = false
                    forgotPasswordError = "Ошибка: ${e.localizedMessage ?: "Проверьте конфигурацию Firebase"}"
                }
            },
            onBack = {
                forgotPasswordEmail = ""
                forgotPasswordError = null
                forgotPasswordSuccess = false
                screen = Screen.Auth
            }
        )
        Screen.Auth -> AuthScreen(
            name = name,
            email = email,
            password = password,
            error = errorMessage,
            isLoading = isLoading,
            onNameChange = { name = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onForgotPassword = {
                forgotPasswordEmail = email
                screen = Screen.ForgotPassword
            },
            onSubmit = { isLogin ->
                if (email.isBlank() || password.length < 6) {
                    errorMessage = "Введите корректный email и пароль от 6 символов."
                    return@AuthScreen
                }
                isLoading = true
                errorMessage = null
                try {
                    if (isLogin) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val firebaseUser = task.result?.user
                                    val fallbackName = firebaseUser?.email?.substringBefore("@").orEmpty()
                                    name = firebaseUser?.displayName?.ifBlank { fallbackName }.orEmpty()
                                    score = 0
                                    screen = Screen.Home
                                } else {
                                    val exception = task.exception
                                    errorMessage = when {
                                        exception?.message?.contains("API key") == true -> 
                                            "Ошибка конфигурации Firebase. Проверьте google-services.json"
                                        exception?.message?.contains("network") == true -> 
                                            "Проблема с сетью. Проверьте подключение к интернету"
                                        else -> exception?.localizedMessage
                                            ?: "Не удалось войти. Попробуйте ещё раз."
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = if (e.message?.contains("API key") == true) {
                                    "Ошибка конфигурации Firebase. Проверьте google-services.json"
                                } else {
                                    e.localizedMessage ?: "Ошибка входа"
                                }
                            }
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val firebaseUser = task.result?.user
                                    val finalName = name.ifBlank {
                                        email.substringBefore("@")
                                    }
                                if (firebaseUser != null) {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(finalName)
                                        .build()
                                    firebaseUser.updateProfile(profileUpdates)
                                }
                                    name = finalName
                                    score = 0
                                    screen = Screen.Home
                                } else {
                                    val exception = task.exception
                                    errorMessage = when {
                                        exception?.message?.contains("API key") == true -> 
                                            "Ошибка конфигурации Firebase. Проверьте google-services.json"
                                        exception?.message?.contains("network") == true -> 
                                            "Проблема с сетью. Проверьте подключение к интернету"
                                        else -> exception?.localizedMessage
                                            ?: "Не удалось создать аккаунт."
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = if (e.message?.contains("API key") == true) {
                                    "Ошибка конфигурации Firebase. Проверьте google-services.json"
                                } else {
                                    e.localizedMessage ?: "Ошибка регистрации"
                                }
                            }
                    }
                } catch (e: Exception) {
                    isLoading = false
                    errorMessage = "Ошибка: ${e.localizedMessage ?: "Проверьте конфигурацию Firebase"}"
                }
            }
        )
        Screen.Home -> HomeScreen(
            name = name.ifBlank { email.substringBefore("@").ifBlank { "Гость" } },
            level = level,
            xp = xp,
            xpToNext = xpToNext,
            isDark = isDark,
            onOpenMenu = {
                previousScreen = Screen.Home
                screen = Screen.MainMenu
            },
            onStartQuiz = { mode ->
                currentMode = mode
                score = 0
                if (mode == QuizMode.PvP) {
                    pvpPlayer1Score = 0
                    pvpPlayer2Score = 0
                    pvpCurrentPlayerTurn = 0
                    screen = Screen.PvPPlayerSetup
                } else {
                    screen = Screen.Question(0)
                }
            }
        )
        Screen.PvPPlayerSetup -> PvPPlayerSetupScreen(
            player1Name = pvpPlayer1Name,
            player2Name = pvpPlayer2Name,
            onPlayer1NameChange = { pvpPlayer1Name = it },
            onPlayer2NameChange = { pvpPlayer2Name = it },
            onStart = {
                if (pvpPlayer1Name.isNotBlank() && pvpPlayer2Name.isNotBlank()) {
                    pvpPlayer1Score = 0
                    pvpPlayer2Score = 0
                    pvpCurrentPlayerTurn = 0
                    screen = Screen.PvPQuestion(0, 0)
                }
            },
            onBack = { screen = Screen.Home }
        )
        is Screen.PvPQuestion -> {
            val q = currentQuestions[s.index]
            PvPQuestionScreen(
                question = q,
                index = s.index,
                total = currentQuestions.size,
                playerTurn = s.playerTurn,
                player1Name = pvpPlayer1Name,
                player2Name = pvpPlayer2Name,
                player1Score = pvpPlayer1Score,
                player2Score = pvpPlayer2Score,
                onAnswered = { isCorrect, playerNum ->
                    if (isCorrect) {
                        if (playerNum == 0) {
                            pvpPlayer1Score++
                        } else {
                            pvpPlayer2Score++
                        }
                    }
                },
                onNext = {
                    val nextIndex = s.index + 1
                    val nextPlayerTurn = if (s.playerTurn == 0) 1 else 0
                    screen = if (nextIndex < currentQuestions.size) {
                        Screen.PvPQuestion(nextIndex, nextPlayerTurn)
                    } else {
                        Screen.PvPResult(
                            pvpPlayer1Score,
                            pvpPlayer2Score,
                            currentQuestions.size,
                            pvpPlayer1Name,
                            pvpPlayer2Name
                        )
                    }
                },
                onExit = { screen = Screen.Home }
            )
        }
        is Screen.PvPResult -> PvPResultScreen(
            player1Score = s.player1Score,
            player2Score = s.player2Score,
            total = s.total,
            player1Name = s.player1Name,
            player2Name = s.player2Name,
            onRestart = {
                pvpPlayer1Score = 0
                pvpPlayer2Score = 0
                pvpCurrentPlayerTurn = 0
                screen = Screen.PvPQuestion(0, 0)
            },
            onBackHome = { screen = Screen.Home }
        )
        is Screen.Question -> {
            val q = currentQuestions[s.index]
            QuestionScreen(
                question = q,
                index = s.index,
                total = currentQuestions.size,
                score = score,
                onAnswered = { isCorrect ->
                    if (isCorrect) {
                        score++
                        // начисляем опыт за правильный ответ
                        xp += 20
                        while (xp >= xpToNext) {
                            xp -= xpToNext
                            level++
                        }
                    }
                },
                onNext = {
                    val next = s.index + 1
                    screen = if (next < currentQuestions.size) {
                        Screen.Question(next)
                    } else {
                        Screen.Result(score, currentQuestions.size)
                    }
                },
                onExit = { screen = Screen.Home }
            )
        }
        is Screen.Result -> ResultScreen(
            score = s.score,
            total = s.total,
            onRestart = {
                score = 0
                screen = Screen.Question(0)
            },
            onBackHome = { screen = Screen.Home }
        )
    }
}


