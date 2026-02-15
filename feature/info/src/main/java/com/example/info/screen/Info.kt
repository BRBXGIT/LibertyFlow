@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.info.screen

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.components.bars.basic_top_bar.LibertyFlowBasicTopBar
import com.example.design_system.components.list_tems.M3ListItem
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.info.R
import com.example.info.components.Header

private val InfoLabel = R.string.info_label

private val LCSpacedBy = 16.dp

private val LCBottomPadding = 16.dp

@Composable
internal fun Info(
    onCommonEffect: (UiEffect) -> Unit,
    onEffect: (InfoEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            LibertyFlowBasicTopBar(
                label = stringResource(InfoLabel),
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
                bottom = innerPadding.calculateBottomPadding() + LCBottomPadding,
                top = innerPadding.calculateTopPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(LCSpacedBy),
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

private data class InfoItem(
    val title: Int,
    val description: Int,
    val icon: Int,
    val type: InfoType,
    val url: String? = null
)

private const val HEADER_KEY = "HeaderKey"

private fun LazyListScope.header() {
    item(key = HEADER_KEY) {
        Header()
    }
}

private val VersionLabel = R.string.version_label
private val VersionDescription = R.string.version_description_label

private val ApiLabel = R.string.api_label
private val ApiDescription = R.string.api_description_label
private const val AniLibertyApiDocsLink = "https://aniliberty.top/api/docs/v1#/"

private val AniLibertyLabel = R.string.ani_liberty_label
private val AniLibertyDescription = R.string.ani_liberty_description_label
private const val AniLibertyLink = "https://aniliberty.top/"

private val GitHubLabel = R.string.git_label
private val GitHubDescription = R.string.git_description_label
private const val BRBXGitHubLink = "https://github.com/BRBXGIT"

private val infoItems = listOf(
    InfoItem(
        title = VersionLabel,
        description = VersionDescription,
        icon = LibertyFlowIcons.Outlined.Layers,
        type = InfoType.Version
    ),
    InfoItem(
        title = ApiLabel,
        description = ApiDescription,
        icon = LibertyFlowIcons.Outlined.PieChart,
        type = InfoType.Api,
        url = AniLibertyApiDocsLink
    ),
    InfoItem(
        title = GitHubLabel,
        description = GitHubDescription,
        icon = LibertyFlowIcons.Outlined.GitHub,
        type = InfoType.Git,
        url = BRBXGitHubLink
    ),
    InfoItem(
        title = AniLibertyLabel,
        description = AniLibertyDescription,
        icon = LibertyFlowIcons.Multicolored.AniLiberty,
        type = InfoType.OriginalSite,
        url = AniLibertyLink
    ),
)

private fun LazyListScope.infoItems(
    onCommonEffect: (UiEffect) -> Unit,
    onEffect: (InfoEffect) -> Unit
) {
    items(
        items = infoItems,
        key = { infoItem -> infoItem.type }
    ) { infoItem ->
        M3ListItem(
            title = stringResource(infoItem.title),
            icon = infoItem.icon,
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