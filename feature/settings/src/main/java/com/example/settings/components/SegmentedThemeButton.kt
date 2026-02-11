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
import androidx.compose.ui.unit.dp
import com.example.data.models.theme.ThemeValue
import com.example.design_system.theme.theme.mTypography
import com.example.settings.R
import com.example.settings.screen.SettingsIntent
import com.example.settings.screen.SettingsState

private val HorizontalPadding = 16.dp

@Composable
internal fun LazyItemScope.SegmentedThemeButton(
    onIntent: (SettingsIntent) -> Unit,
    state: SettingsState
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding)
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
                selected = state.themeSettings.userThemePreference == value,
                label = {
                    Text(
                        text = stringResource(value.toLabel()),
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

private fun ThemeValue.toLabel(): Int {
    return when(this) {
        ThemeValue.LIGHT -> LightLabel
        ThemeValue.DARK -> DarkLabel
        ThemeValue.SYSTEM -> SystemLabel
        ThemeValue.DYNAMIC -> DynamicLabel
    }
}