package com.example.khadra.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.khadra.R
import com.example.khadra.R.*

/*
 * Typography configuration for the application
 * Defines text styles used throughout the app
 */

val Inter = FontFamily(
    Font(R.font.inter, weight = FontWeight.Normal

    ),
    Font(R.font.inter_bold, weight = FontWeight.Bold

    ),
    Font(R.font.inter_thin, weight = FontWeight.Thin

    ),
    Font(R.font.inter_light, weight = FontWeight.Light

    ),
    Font(R.font.inter_extrabold, weight = FontWeight.ExtraBold

    ),
)

// Default typography settings using Material Design 3 guidelines
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )


    // Additional text styles can be added here following the same pattern
    // (commented out examples are shown in the original code)
)

