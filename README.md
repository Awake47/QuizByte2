# QuizByte 🎮

Gamified mobile application for IT interview preparation and programming language learning.

## Features

- 🎯 **Short Quizzes** - Quick Python questions from easy to hard difficulty
- ⚔️ **Duel Mode** - Compete with others (coming soon)
- 📚 **Interactive Tasks** - Learn by doing with instant feedback
- 🏆 **Leveling System** - Gain XP for correct answers and level up
- 🔒 **Content Gating** - Unlock harder levels as you progress
- 🌓 **Dark/Light Theme** - Customize your experience
- 🔐 **Firebase Authentication** - Secure login and registration

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Backend**: Firebase Authentication
- **Architecture**: MVVM-ready structure

## Project Structure

```
app/src/main/java/com/example/quizbyte/
├── MainActivity.kt          # App entry point & navigation
├── QuizModels.kt           # Data models & enums
├── QuizTheme.kt            # Theme configuration
├── AuthScreens.kt          # Onboarding & authentication UI
├── MainMenuScreen.kt        # Main menu screen
├── HomeScreen.kt           # Home screen with tabs (Learn/Progress/Profile)
└── QuizScreens.kt          # Quiz question & result screens
```

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Add your `google-services.json` file to `app/` directory
4. Enable Email/Password authentication in Firebase Console
5. Run the app!

## Quiz Modes

- **Быстрый матч (Python)** - Mixed questions covering Python basics and advanced concepts
- **Python база** - Easy and medium questions for beginners
- **Python продвинутый** - Medium and hard questions (unlocks at level 2)

## Screenshots

Coming soon...

## License

This project is private and proprietary.

