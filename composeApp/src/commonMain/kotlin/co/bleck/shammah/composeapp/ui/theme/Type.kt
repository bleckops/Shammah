package co.bleck.shammah.composeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import shammah.composeapp.generated.resources.Res
import shammah.composeapp.generated.resources.inter_bold
import shammah.composeapp.generated.resources.inter_extrabold
import shammah.composeapp.generated.resources.inter_medium
import shammah.composeapp.generated.resources.inter_regular
import shammah.composeapp.generated.resources.inter_semibold
import shammah.composeapp.generated.resources.playfair_display_bold
import shammah.composeapp.generated.resources.playfair_display_extrabold
import shammah.composeapp.generated.resources.playfair_display_medium
import shammah.composeapp.generated.resources.playfair_display_regular
import shammah.composeapp.generated.resources.playfair_display_semibold

// ── Playfair Display — Elegant serif for headings ─────────────────────────────
@Composable
fun playfairDisplayFamily(): FontFamily = FontFamily(
    Font(Res.font.playfair_display_regular, weight = FontWeight.Normal),
    Font(Res.font.playfair_display_medium, weight = FontWeight.Medium),
    Font(Res.font.playfair_display_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.playfair_display_bold, weight = FontWeight.Bold),
    Font(Res.font.playfair_display_extrabold, weight = FontWeight.ExtraBold),
)

// ── Inter — Clean sans-serif for body & UI text ───────────────────────────────
@Composable
fun interFamily(): FontFamily = FontFamily(
    Font(Res.font.inter_regular, weight = FontWeight.Normal),
    Font(Res.font.inter_medium, weight = FontWeight.Medium),
    Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.inter_bold, weight = FontWeight.Bold),
    Font(Res.font.inter_extrabold, weight = FontWeight.ExtraBold),
)

// ── Full Material 3 Typography Scale ──────────────────────────────────────────
@Composable
fun shammahTypography(): Typography {
    val playfairDisplay = playfairDisplayFamily()
    val inter = interFamily()

    return Typography(
        // Display — Playfair Display
        displayLarge = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.Bold,
            fontSize     = 57.sp,
            lineHeight   = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.Bold,
            fontSize     = 45.sp,
            lineHeight   = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.Bold,
            fontSize     = 36.sp,
            lineHeight   = 44.sp,
            letterSpacing = 0.sp
        ),

        // Headline — Playfair Display
        headlineLarge = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.Bold,
            fontSize     = 32.sp,
            lineHeight   = 40.sp,
            letterSpacing = (-0.5).sp
        ),
        headlineMedium = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.SemiBold,
            fontSize     = 28.sp,
            lineHeight   = 36.sp,
            letterSpacing = (-0.25).sp
        ),
        headlineSmall = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.SemiBold,
            fontSize     = 24.sp,
            lineHeight   = 32.sp,
            letterSpacing = 0.sp
        ),

        // Title — Playfair Display for Large, Inter for Medium/Small
        titleLarge = TextStyle(
            fontFamily   = playfairDisplay,
            fontWeight   = FontWeight.SemiBold,
            fontSize     = 22.sp,
            lineHeight   = 28.sp,
            letterSpacing = (-0.2).sp
        ),
        titleMedium = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.SemiBold,
            fontSize     = 16.sp,
            lineHeight   = 24.sp,
            letterSpacing = 0.1.sp
        ),
        titleSmall = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Medium,
            fontSize     = 14.sp,
            lineHeight   = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Body — Inter
        bodyLarge = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Normal,
            fontSize     = 16.sp,
            lineHeight   = 26.sp,
            letterSpacing = 0.15.sp
        ),
        bodyMedium = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Normal,
            fontSize     = 14.sp,
            lineHeight   = 22.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Normal,
            fontSize     = 12.sp,
            lineHeight   = 18.sp,
            letterSpacing = 0.4.sp
        ),

        // Label — Inter
        labelLarge = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Medium,
            fontSize     = 14.sp,
            lineHeight   = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Medium,
            fontSize     = 12.sp,
            lineHeight   = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily   = inter,
            fontWeight   = FontWeight.Medium,
            fontSize     = 11.sp,
            lineHeight   = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}
