package co.bleck.shammah.composeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// ── Shapes ────────────────────────────────────────────────────────────────────
val ShammahShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(20.dp),
    large      = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// ── Color Schemes ─────────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary              = PrimaryDark,
    onPrimary            = OnPrimaryDark,
    primaryContainer     = PrimaryContainerDark,
    onPrimaryContainer   = OnPrimaryContainerDark,
    secondary            = SecondaryDark,
    onSecondary          = OnSecondaryDark,
    secondaryContainer   = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary             = TertiaryDark,
    onTertiary           = OnTertiaryDark,
    tertiaryContainer    = TertiaryContainerDark,
    onTertiaryContainer  = OnTertiaryContainerDark,
    background           = BackgroundDark,
    onBackground         = OnBackgroundDark,
    surface              = SurfaceDark,
    onSurface            = OnSurfaceDark,
    surfaceVariant       = SurfaceVariantDark,
    onSurfaceVariant     = OnSurfaceVariantDark,
    outline              = OutlineDark,
    outlineVariant       = OutlineVariantDark,
    inverseSurface       = InverseSurfaceDark,
    inverseOnSurface     = InverseOnSurfaceDark,
    surfaceTint          = SurfaceTintDark,
    error                = ErrorDark,
    onError              = OnErrorDark,
    errorContainer       = ErrorContainerDark,
    onErrorContainer     = OnErrorContainerDark,
    scrim                = ScrimDark,
)

private val LightColorScheme = lightColorScheme(
    primary              = PrimaryLight,
    onPrimary            = OnPrimaryLight,
    primaryContainer     = PrimaryContainerLight,
    onPrimaryContainer   = OnPrimaryContainerLight,
    secondary            = SecondaryLight,
    onSecondary          = OnSecondaryLight,
    secondaryContainer   = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary             = TertiaryLight,
    onTertiary           = OnTertiaryLight,
    tertiaryContainer    = TertiaryContainerLight,
    onTertiaryContainer  = OnTertiaryContainerLight,
    background           = BackgroundLight,
    onBackground         = OnBackgroundLight,
    surface              = SurfaceLight,
    onSurface            = OnSurfaceLight,
    surfaceVariant       = SurfaceVariantLight,
    onSurfaceVariant     = OnSurfaceVariantLight,
    outline              = OutlineLight,
    outlineVariant       = OutlineVariantLight,
    inverseSurface       = InverseSurfaceLight,
    inverseOnSurface     = InverseOnSurfaceLight,
    surfaceTint          = SurfaceTintLight,
    error                = ErrorLight,
    onError              = OnErrorLight,
    errorContainer       = ErrorContainerLight,
    onErrorContainer     = OnErrorContainerLight,
    scrim                = ScrimLight,
)

// ── Theme ─────────────────────────────────────────────────────────────────────
// Dynamic (Material You) color is intentionally disabled to keep Shammah
// church branding consistent across every platform.
@Composable
fun ShammahTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = shammahTypography(),
        shapes      = ShammahShapes,
        content     = content
    )
}
