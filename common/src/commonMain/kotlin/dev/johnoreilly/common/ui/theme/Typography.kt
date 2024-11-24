package dev.johnoreilly.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import fantasypremierleague.common.generated.resources.PremierLeague_Bold
import fantasypremierleague.common.generated.resources.PremierLeague_Italic
import fantasypremierleague.common.generated.resources.PremierLeague_Regular
import fantasypremierleague.common.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun PremierLeagueFontFamily() = FontFamily(
    Font(Res.font.PremierLeague_Regular, FontWeight.Normal),
    Font(Res.font.PremierLeague_Italic, style = FontStyle.Italic),
    Font(Res.font.PremierLeague_Bold, FontWeight.Bold),
)

@Composable
fun PremierLeagueTypography() = Typography().run {
    val fontFamily = PremierLeagueFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}
