package com.csu_itc303_team1.adhdtaskmanager.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.graphics.Color
import com.csu_itc303_team1.adhdtaskmanager.data.TagColor

@Stable
class AppColors(
    star: Color,
    tagTexts: List<Pair<TagColor, Color>>,
    tagBackgrounds: List<Pair<TagColor, Color>>,
) {
    /**
     * The color for the star icon.
     */
    var star by mutableStateOf(star)
        private set

    private val tagTexts = tagTexts.toMutableStateMap()
    private val tagBackgrounds = tagBackgrounds.toMutableStateMap()

    /**
     * Retrieves the color of the tag text for the specified [TagColor].
     */
    @Composable
    fun tagText(tagColor: TagColor) = tagTexts.getOrDefault(tagColor, Color.Black)

    /**
     * Retrieves the color of the tag background for the specified [TagColor].
     */
    @Composable
    fun tagBackground(tagColor: TagColor) = tagBackgrounds.getOrDefault(tagColor, Color.LightGray)

    internal fun update(other: AppColors) {
        star = other.star
        tagTexts.clear()
        tagTexts.putAll(other.tagTexts)
    }

    internal fun copy(): AppColors = AppColors(
        star = star,
        tagTexts = tagTexts.toList(),
        tagBackgrounds = tagBackgrounds.toList(),
    )
}

@Composable
internal fun ProvideAppColors(
    colors: AppColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors.copy() }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

internal val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}