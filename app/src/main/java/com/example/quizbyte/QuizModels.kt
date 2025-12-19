package com.example.quizbyte

/**
 * Навигация между экранами приложения.
 */
sealed class Screen {
    object MainMenu : Screen()
    object Onboarding : Screen()
    object Auth : Screen()
    object ForgotPassword : Screen()
    object Home : Screen()
    object PvPPlayerSetup : Screen()
    data class Question(val index: Int) : Screen()
    data class PvPQuestion(val index: Int, val playerTurn: Int) : Screen() // playerTurn: 0 или 1
    data class Result(val score: Int, val total: Int) : Screen()
    data class PvPResult(val player1Score: Int, val player2Score: Int, val total: Int, val player1Name: String, val player2Name: String) : Screen()
}

/**
 * Режимы квиза.
 */
enum class QuizMode {
    PythonMixed,
    PythonBase,
    PythonAdvanced,
    PvP // Режим 1 на 1
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
    QuestionData(
        title = "Что такое кортеж (tuple) в Python?",
        options = listOf(
            "Изменяемая коллекция",
            "Неизменяемая коллекция",
            "Словарь",
            "Множество"
        ),
        correctIndex = 1,
        explanation = "Кортеж (tuple) — это неизменяемая упорядоченная коллекция элементов, создаётся с помощью круглых скобок: (1, 2, 3).",
        difficulty = "Easy"
    ),
    QuestionData(
        title = "Как получить длину списка?",
        code = "my_list = [1, 2, 3, 4, 5]",
        options = listOf(
            "my_list.length()",
            "len(my_list)",
            "my_list.size()",
            "length(my_list)"
        ),
        correctIndex = 1,
        explanation = "В Python для получения длины списка используется встроенная функция len(): len(my_list).",
        difficulty = "Easy"
    ),
    QuestionData(
        title = "Что выведет этот код?",
        code = "x = 5\nif x > 3:\n    print('Больше')\nelse:\n    print('Меньше')",
        options = listOf("Больше", "Меньше", "Ошибка", "Ничего"),
        correctIndex = 0,
        explanation = "Условие x > 3 истинно (5 > 3), поэтому выполнится блок if и выведется 'Больше'.",
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
    QuestionData(
        title = "Что такое lambda-функция?",
        options = listOf(
            "Анонимная функция",
            "Именованная функция",
            "Встроенная функция",
            "Рекурсивная функция"
        ),
        correctIndex = 0,
        explanation = "Lambda — это анонимная функция, которая может принимать любое количество аргументов, но только одно выражение. Пример: lambda x: x * 2",
        difficulty = "Medium"
    ),
    QuestionData(
        title = "Что выведет этот код?",
        code = "a = [1, 2, 3]\\nb = a\\nb.append(4)\\nprint(a)",
        options = listOf("[1, 2, 3]", "[1, 2, 3, 4]", "[4]", "Ошибка"),
        correctIndex = 1,
        explanation = "b ссылается на тот же объект, что и a. Изменение b изменяет и a, так как это один и тот же список в памяти.",
        difficulty = "Medium"
    ),
    QuestionData(
        title = "Как правильно использовать словарь?",
        code = "data = {'name': 'John', 'age': 30}",
        options = listOf(
            "data['name']",
            "data.name",
            "data.get('name')",
            "И data['name'], и data.get('name')"
        ),
        correctIndex = 3,
        explanation = "Оба способа работают: data['name'] и data.get('name'). Но get() безопаснее — не вызовет ошибку, если ключа нет.",
        difficulty = "Medium"
    ),
    QuestionData(
        title = "Что такое декоратор в Python?",
        options = listOf(
            "Функция, которая изменяет другую функцию",
            "Специальный синтаксис для классов",
            "Тип данных",
            "Оператор"
        ),
        correctIndex = 0,
        explanation = "Декоратор — это функция, которая принимает другую функцию и расширяет её поведение без явного изменения. Используется с @ символом.",
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
    ),
    QuestionData(
        title = "Что такое замыкание (closure) в Python?",
        options = listOf(
            "Функция внутри функции с доступом к внешним переменным",
            "Способ закрытия файла",
            "Метод класса",
            "Тип данных"
        ),
        correctIndex = 0,
        explanation = "Замыкание — это вложенная функция, которая имеет доступ к переменным из внешней (охватывающей) области видимости, даже после завершения работы внешней функции.",
        difficulty = "Hard"
    ),
    QuestionData(
        title = "Что выведет этот код с *args и **kwargs?",
        code = "def func(*args, **kwargs):\\n    return len(args) + len(kwargs)\\n\\nprint(func(1, 2, 3, a=4, b=5))",
        options = listOf("2", "3", "5", "Ошибка"),
        correctIndex = 2,
        explanation = "*args собирает позиционные аргументы (1, 2, 3) — 3 элемента. **kwargs собирает именованные (a=4, b=5) — 2 элемента. Итого: 3 + 2 = 5.",
        difficulty = "Hard"
    ),
    QuestionData(
        title = "Что такое MRO (Method Resolution Order) в Python?",
        options = listOf(
            "Порядок поиска методов при множественном наследовании",
            "Метод оптимизации",
            "Тип данных",
            "Способ работы с файлами"
        ),
        correctIndex = 0,
        explanation = "MRO определяет порядок, в котором Python ищет методы в иерархии классов при множественном наследовании. Используется алгоритм C3 linearization.",
        difficulty = "Hard"
    ),
    QuestionData(
        title = "Что выведет этот код с метаклассами?",
        code = "class Meta(type):\\n    def __new__(cls, name, bases, dct):\\n        dct['x'] = 10\\n        return super().__new__(cls, name, bases, dct)\\n\\nclass A(metaclass=Meta):\\n    pass\\n\\nprint(A.x)",
        options = listOf("10", "Ошибка", "None", "0"),
        correctIndex = 0,
        explanation = "Метакласс Meta добавляет атрибут x = 10 в словарь класса A при его создании. Поэтому A.x вернёт 10.",
        difficulty = "Hard"
    )
)










