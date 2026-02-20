package com.example.settings.components

import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.data.models.theme.ThemeValue
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography
import com.example.settings.R
import com.example.settings.screen.SettingsIntent

/**
 * A segmented toggle button row for selecting the application's theme mode.
 *
 * This component maps [ThemeValue] entries to a [SingleChoiceSegmentedButtonRow].
 * It automatically handles:
 * 1. **OS Compatibility**: Disables the 'Dynamic' option on devices below Android 12 (S).
 * 2. **Selection State**: Highlights the segment matching the current [theme].
 * 3. **UI Layout**: Applies standard padding and ensures the row fills the available width.
 *
 * @param theme The currently active [ThemeValue] from the user settings.
 * @param onIntent A lambda to dispatch [SettingsIntent.SetTheme] when a new segment is selected.
 */
@Composable
internal fun LazyItemScope.SegmentedThemeButton(
    theme: ThemeValue,
    onIntent: (SettingsIntent) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
    ) {
        ThemeValue.entries.forEachIndexed { index, value ->
            val isSupported = if (value == ThemeValue.DYNAMIC) {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            } else {
                true
            }

            SegmentedButton(
                enabled = isSupported,
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = ThemeValue.entries.size
                ),
                onClick = { onIntent(SettingsIntent.SetTheme(value)) },
                selected = theme == value,
                label = {
                    Text(
                        text = stringResource(value.toLabelRes()),
                        style = mTypography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {}
            )
        }
    }
}

private val LightLabel = R.string.light_theme_label
private val DarkLabel = R.string.dark_theme_label
private val SystemLabel = R.string.system_theme_label
private val DynamicLabel = R.string.dynamic_theme_label

private fun ThemeValue.toLabelRes(): Int {
    return when(this) {
        ThemeValue.LIGHT -> LightLabel
        ThemeValue.DARK -> DarkLabel
        ThemeValue.SYSTEM -> SystemLabel
        ThemeValue.DYNAMIC -> DynamicLabel
    }
}