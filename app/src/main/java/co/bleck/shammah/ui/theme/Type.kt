package co.bleck.shammah.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import co.bleck.shammah.R

// ── Google Fonts Provider ──────────────────────────────────────────────────────
val googleFontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

// ── Playfair Display — Elegant serif for headings ─────────────────────────────
private val playfairDisplayGoogleFont = GoogleFont("Playfair Display")
val PlayfairDisplay = FontFamily(
    Font(googleFont = playfairDisplayGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Normal),
    Font(googleFont = playfairDisplayGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Medium),
    Font(googleFont = playfairDisplayGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.SemiBold),
    Font(googleFont = playfairDisplayGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Bold),
    Font(googleFont = playfairDisplayGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.ExtraBold),
)

// ── Inter — Clean sans-serif for body & UI text ───────────────────────────────
private val interGoogleFont = GoogleFont("Inter")
val Inter = FontFamily(
    Font(googleFont = interGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Normal),
    Font(googleFont = interGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Medium),
    Font(googleFont = interGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.SemiBold),
    Font(googleFont = interGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Bold),
    Font(googleFont = interGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.ExtraBold),
)

// ── Full Material 3 Typography Scale ──────────────────────────────────────────
val Typography = Typography(
    // Display — Playfair Display
    displayLarge = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.Bold,
        fontSize     = 57.sp,
        lineHeight   = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.Bold,
        fontSize     = 45.sp,
        lineHeight   = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.Bold,
        fontSize     = 36.sp,
        lineHeight   = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline — Playfair Display
    headlineLarge = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.Bold,
        fontSize     = 32.sp,
        lineHeight   = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 28.sp,
        lineHeight   = 36.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineSmall = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 24.sp,
        lineHeight   = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title — Playfair Display for Large, Inter for Medium/Small
    titleLarge = TextStyle(
        fontFamily   = PlayfairDisplay,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 22.sp,
        lineHeight   = 28.sp,
        letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body — Inter
    bodyLarge = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 26.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Normal,
        fontSize     = 12.sp,
        lineHeight   = 18.sp,
        letterSpacing = 0.4.sp
    ),

    // Label — Inter
    labelLarge = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily   = Inter,
        fontWeight   = FontWeight.Medium,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp
    )
)