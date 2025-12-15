package com.example.quizbyte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            var isDark by remember { mutableStateOf(true) }
            QuizByteTheme(isDark = isDark) {
                val auth = remember { FirebaseAuth.getInstance() }
                QuizByteApp(
                    auth = auth,
                    isDark = isDark,
                    onToggleTheme = { isDark = !isDark }
                )
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
        Screen.Auth -> AuthScreen(
            name = name,
            email = email,
            password = password,
            error = errorMessage,
            isLoading = isLoading,
            onNameChange = { name = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onSubmit = { isLogin ->
                if (email.isBlank() || password.length < 6) {
                    errorMessage = "Введите корректный email и пароль от 6 символов."
                    return@AuthScreen
                }
                isLoading = true
                errorMessage = null
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
                                errorMessage = task.exception?.localizedMessage
                                    ?: "Не удалось войти. Попробуйте ещё раз."
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
                                    firebaseUser.updateProfile(
                                        userProfileChangeRequest {
                                            displayName = finalName
                                        }
                                    )
                                }
                                name = finalName
                                score = 0
                                screen = Screen.Home
                            } else {
                                errorMessage = task.exception?.localizedMessage
                                    ?: "Не удалось создать аккаунт."
                            }
                        }
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
                screen = Screen.Question(0)
            }
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


