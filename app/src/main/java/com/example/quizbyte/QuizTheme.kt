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
            surface = Color(0xFF020617)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF0F172A),
            secondary = Color(0xFF1E293B),
            tertiary = Color(0xFF2563EB),
            background = Color(0xFFE5F2FF),
            surface = Color(0xFFFFFFFF)
        )
    }

    val typo = Typography(
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xFFE5E7EB)
        ),
        titleLarge = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
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
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF020617),
                            Color(0xFF111827),
                            Color(0xFF020617)
                        )
                    )
                )
        ) {
            // лёгкое неоновое “сияние” по краям
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x5538BDF8),
                                Color.Transparent
                            ),
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


