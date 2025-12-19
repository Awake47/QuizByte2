package com.example.quizbyte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Глобальная тема QuizByte (тёмная / светлая) с неоновым фоном.
 */
@Composable
fun QuizByteTheme(
    isDark: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (isDark) {
        darkColorScheme(
            primary = Color(0xFF38BDF8),
            secondary = Color(0xFFA855F7),
            tertiary = Color(0xFFEC4899),
            background = Color(0xFF020617),
            surface = Color(0xFF0F172A),
            onBackground = Color(0xFFE5E7EB),
            onSurface = Color(0xFFE5E7EB),
            onPrimary = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF2563EB),
            secondary = Color(0xFF7C3AED),
            tertiary = Color(0xFFDB2777),
            background = Color(0xFFF0F9FF),
            surface = Color(0xFFFFFFFF),
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF1E293B),
            onPrimary = Color.White
        )
    }

    val typo = Typography(
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(
            color = colors.onBackground
        ),
        titleLarge = MaterialTheme.typography.titleLarge.copy(
            color = colors.onBackground,
            fontWeight = FontWeight.Bold
        )
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typo
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDark) {
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF020617),
                                Color(0xFF111827),
                                Color(0xFF020617)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFF0F9FF),
                                Color(0xFFE0F2FE),
                                Color(0xFFF0F9FF)
                            )
                        )
                    }
                )
        ) {
            // лёгкое неоновое “сияние” по краям
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = if (isDark) {
                                listOf(
                                    Color(0x5538BDF8),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color(0x332563EB),
                                    Color.Transparent
                                )
                            },
                            center = DpOffset(40.dp, 120.dp).let {
                                Offset(it.x.value, it.y.value)
                            },
                            radius = 500f
                        )
                    )
            )
            content()
        }
    }
}


