package com.example.quizbyte

/**
 * Навигация между экранами приложения.
 */
sealed class Screen {
    object MainMenu : Screen()
    object Onboarding : Screen()
    object Auth : Screen()
    object Home : Screen()
    data class Question(val index: Int) : Screen()
    data class Result(val score: Int, val total: Int) : Screen()
}

/**
 * Режимы квиза.
 */
enum class QuizMode {
    PythonMixed,
    PythonBase,
    PythonAdvanced
}

/**
 * Вкладки домашнего экрана.
 */
enum class HomeTab {
    Learn,
    Progress,
    Profile
}

/**
 * Модель вопроса для квиза.
 */
data class QuestionData(
    val title: String,
    val code: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val difficulty: String // Например: "Easy", "Medium", "Hard"
)

/**
 * Статический набор вопросов по Python (от Easy до Hard).
 */
val quizQuestions = listOf(
    // --- EASY ---
    QuestionData(
        title = "Что выведет этот код?",
        code = "print(1 + 2)",
        options = listOf("1", "2", "3", "Ошибка"),
        correctIndex = 2,
        explanation = "В Python оператор + для целых чисел просто складывает их: 1 + 2 = 3.",
        difficulty = "Easy"
    ),
    QuestionData(
        title = "Как создать список из чисел 1, 2, 3?",
        options = listOf(
            "list = 1, 2, 3",
            "list = [1, 2, 3]",
            "list = (1, 2, 3)",
            "list = {1, 2, 3}"
        ),
        correctIndex = 1,
        explanation = "Список в Python создаётся с помощью квадратных скобок: [1, 2, 3].",
        difficulty = "Easy"
    ),
    QuestionData(
        title = "Как правильно объявить функцию?",
        options = listOf(
            "func my_func():",
            "def my_func:",
            "def my_func():",
            "function my_func():"
        ),
        correctIndex = 2,
        explanation = "Функция объявляется через ключевое слово def и круглые скобки: def my_func():",
        difficulty = "Easy"
    ),

    // --- MEDIUM ---
    QuestionData(
        title = "Что напечатает цикл?",
        code = "for i in range(3):\\n    print(i)",
        options = listOf("0 1 2", "1 2 3", "0 1 2 3", "Ошибка"),
        correctIndex = 0,
        explanation = "range(3) генерирует числа 0, 1, 2. Они и выводятся по строкам.",
        difficulty = "Medium"
    ),
    QuestionData(
        title = "Как отфильтровать только чётные числа из списка?",
        code = "nums = [1, 2, 3, 4, 5, 6]",
        options = listOf(
            "[n for n in nums if n % 2 == 0]",
            "[n for n in nums where n % 2 == 0]",
            "filter(nums, n % 2 == 0)",
            "nums.filter(lambda n: n % 2 == 0)"
        ),
        correctIndex = 0,
        explanation = "Списковое включение с условием if внутри — типичный питоновский способ фильтрации.",
        difficulty = "Medium"
    ),
    QuestionData(
        title = "Что выведет код?",
        code = "def add(x, y=1):\\n    return x + y\\n\\nprint(add(5))",
        options = listOf("5", "6", "Ошибка", "None"),
        correctIndex = 1,
        explanation = "y имеет значение по умолчанию 1, поэтому add(5) = 5 + 1 = 6.",
        difficulty = "Medium"
    ),

    // --- HARD ---
    QuestionData(
        title = "Что напечатает этот код с изменяемым аргументом по умолчанию?",
        code = "def append_item(item, lst=[]):\\n    lst.append(item)\\n    return lst\\n\\nprint(append_item(1))\\nprint(append_item(2))",
        options = listOf(
            "[1] и [2]",
            "[1] и [1, 2]",
            "[1, 2] и [1, 2]",
            "Будет исключение"
        ),
        correctIndex = 1,
        explanation = "Список lst создаётся один раз при объявлении функции, поэтому второй вызов продолжает использовать тот же список: [1] затем [1, 2].",
        difficulty = "Hard"
    ),
    QuestionData(
        title = "Какой вывод у этого кода с генераторами?",
        code = "nums = [1, 2, 3]\\nsquares = (n*n for n in nums)\\nnums.append(4)\\nprint(list(squares))",
        options = listOf(
            "[1, 4, 9]",
            "[1, 4, 9, 16]",
            "[16]",
            "Ошибка"
        ),
        correctIndex = 1,
        explanation = "Генератор squares вычисляется лениво, он увидит уже изменённый список nums: 1, 2, 3, 4 → квадраты [1, 4, 9, 16].",
        difficulty = "Hard"
    )
)


