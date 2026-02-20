@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.info.screen

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.components.bars.basic_top_bar.LibertyFlowBasicTopBar
import com.example.design_system.components.list_tems.M3ListItem
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.info.R
import com.example.info.components.Header

private val InfoLabelRes = R.string.info_label

/**
 * The primary UI entry point for the Information screen.
 * * Displays a list of application-related details including version info, API documentation,
 * and social links using a [Scaffold] with a pinned top bar and a [LazyColumn].
 *
 * @param onCommonEffect Callback for global events like navigation or starting external [Intent]s.
 * @param onEffect Callback for feature-specific events like clipboard operations.
 */
@Composable
internal fun Info(
    onCommonEffect: (UiEffect) -> Unit,
    onEffect: (InfoEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            LibertyFlowBasicTopBar(
                label = stringResource(InfoLabelRes),
                onNavClick = { onCommonEffect(UiEffect.NavigateBack) },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + mDimens.paddingMedium,
                top = innerPadding.calculateTopPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(mDimens.paddingMedium),
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
        ) {
            header()

            infoItems(onCommonEffect, onEffect)
        }
    }
}

private enum class InfoType { Version, Api, OriginalSite, Git }

/**
 * Data model representing a single row in the Information list.
 *
 * @property titleRes String resource for the primary label.
 * @property description String resource for the secondary supporting text.
 * @property iconRes Drawable resource for the leading icon.
 * @property type The [InfoType] category used to determine click behavior.
 * @property url The destination link (required for all types except [InfoType.Version]).
 */
private data class InfoItem(
    @param:StringRes val titleRes: Int,
    @param:StringRes val description: Int,
    @param:DrawableRes val iconRes: Int,
    val type: InfoType,
    val url: String? = null
)


/**
 * Extension on [LazyListScope] to provide the header item.
 * Encapsulates the [HEADER_KEY] to ensure consistent state restoration.
 */
private const val HEADER_KEY = "HeaderKey"

private fun LazyListScope.header() {
    item(key = HEADER_KEY) {
        Header()
    }
}

private val VersionLabelRes = R.string.version_label
private val VersionDescriptionRes = R.string.version_description_label

private val ApiLabelRes = R.string.api_label
private val ApiDescriptionRes = R.string.api_description_label
private const val AniLibertyApiDocsLink = "https://aniliberty.top/api/docs/v1#/"

private val AniLibertyLabelRes = R.string.ani_liberty_label
private val AniLibertyDescriptionRes = R.string.ani_liberty_description_label
private const val AniLibertyLink = "https://aniliberty.top/"

private val GitHubLabelRes = R.string.git_label
private val GitHubDescriptionRes = R.string.git_description_label
private const val BRBXGitHubLink = "https://github.com/BRBXGIT"

private val infoItems = listOf(
    InfoItem(
        titleRes = VersionLabelRes,
        description = VersionDescriptionRes,
        iconRes = LibertyFlowIcons.Outlined.Layers,
        type = InfoType.Version
    ),
    InfoItem(
        titleRes = ApiLabelRes,
        description = ApiDescriptionRes,
        iconRes = LibertyFlowIcons.Outlined.PieChart,
        type = InfoType.Api,
        url = AniLibertyApiDocsLink
    ),
    InfoItem(
        titleRes = GitHubLabelRes,
        description = GitHubDescriptionRes,
        iconRes = LibertyFlowIcons.Outlined.GitHub,
        type = InfoType.Git,
        url = BRBXGitHubLink
    ),
    InfoItem(
        titleRes = AniLibertyLabelRes,
        description = AniLibertyDescriptionRes,
        iconRes = LibertyFlowIcons.Multicolored.AniLiberty,
        type = InfoType.OriginalSite,
        url = AniLibertyLink
    ),
)

/**
 * Extension on [LazyListScope] that renders the list of [InfoItem]s.
 * * Maps each [InfoType] to its specific action:
 * - [InfoType.Version]: Triggers an [InfoEffect.CopyVersion].
 * - Others: Triggers a [UiEffect.IntentTo] with the associated URL.
 */
private fun LazyListScope.infoItems(
    onCommonEffect: (UiEffect) -> Unit,
    onEffect: (InfoEffect) -> Unit
) {
    items(
        items = infoItems,
        key = { infoItem -> infoItem.type }
    ) { infoItem ->
        M3ListItem(
            title = stringResource(infoItem.titleRes),
            icon = infoItem.iconRes,
            description = stringResource(infoItem.description),
            onClick = {
                when(infoItem.type) {
                    InfoType.Version -> onEffect(InfoEffect.CopyVersion(infoItem.description))
                    else -> {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            infoItem.url?.toUri()
                        )
                        onCommonEffect(UiEffect.IntentTo(intent))
                    }
                }
            }
        )
    }
}