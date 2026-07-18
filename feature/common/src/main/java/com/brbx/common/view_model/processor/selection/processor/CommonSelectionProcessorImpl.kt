package com.brbx.common.view_model.processor.selection.processor

import androidx.annotation.StringRes
import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.utils.toggle
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.common.view_model.processor.selection.model.loadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.user.lists.collections.collections.model.CollectionItem
import com.brbx.domain.network.user.lists.collections.collections.use_case.UserAddToCollectionUseCase
import com.brbx.domain.network.user.lists.collections.collections.use_case.UserDeleteFromCollectionUseCase
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserAddToFavoritesUseCase
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserDeleteFromFavoritesUseCase
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.common.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.loading_snackbar.config.DefaultBrbxLoadingSnackbarConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class CommonSelectionProcessorImpl<State>(
    private val addToFavoritesUseCase: UserAddToFavoritesUseCase,
    private val deleteFromFavoritesUseCase: UserDeleteFromFavoritesUseCase,
    private val addToCollectionUseCase: UserAddToCollectionUseCase,
    private val deleteFromCollectionUseCase: UserDeleteFromCollectionUseCase,
    private val selectionLens: Lens<State, CommonSelectionState>,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonSelectionProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonSelectionIntent) {
        when (intent) {
            is CommonSelectionIntent.Selection -> processSelection(intent)
            is CommonSelectionIntent.Lists -> processLists(intent)
        }
    }

    private fun LibertyFlowMviScope<State>.processSelection(intent: CommonSelectionIntent.Selection) {
        when (intent) {
            CommonSelectionIntent.Selection.DropSelection -> updateState {
                selectionLens.modify(source = this) { it.copy(ids = emptySet()) }
            }
            is CommonSelectionIntent.Selection.ToggleItemSelected -> updateState {
                selectionLens.modify(source = this) { it.copy(ids = it.ids.toggle(element = intent.id)) }
            }
        }
    }

    private fun LibertyFlowMviScope<State>.processLists(intent: CommonSelectionIntent.Lists) {
        when (intent) {
            is CommonSelectionIntent.Lists.Favorites -> processFavorites(intent)
            is CommonSelectionIntent.Lists.Collection -> processCollection(intent)
        }
    }

    private fun LibertyFlowMviScope<State>.processFavorites(intent: CommonSelectionIntent.Lists.Favorites) {
        executeRequest(
            intent = intent,
            loadingSnackbarRes = when (intent.action) {
                SelectionAction.Add -> CommonStrings.adding_to_favorites
                SelectionAction.Remove -> CommonStrings.deleting_from_favorites
            }
        ) { ids ->
            when (intent.action) {
                SelectionAction.Add -> addToFavoritesUseCase(items = ids)
                SelectionAction.Remove -> deleteFromFavoritesUseCase(items = ids)
            }
        }
    }

    private fun LibertyFlowMviScope<State>.processCollection(intent: CommonSelectionIntent.Lists.Collection) {
        when (intent) {
            is CommonSelectionIntent.Lists.Collection.Interact -> {
                executeRequest(
                    intent = intent,
                    loadingSnackbarRes = when (intent.action) {
                        SelectionAction.Add -> CommonStrings.adding_to_collection
                        SelectionAction.Remove -> CommonStrings.deleting_from_collection
                    }
                ) { ids ->
                    val items = ids.map { CollectionItem(id = it, collection = intent.collection) }
                    when (intent.action) {
                        SelectionAction.Add -> addToCollectionUseCase(items = items)
                        SelectionAction.Remove -> deleteFromCollectionUseCase(items = items)
                    }
                }
            }

            CommonSelectionIntent.Lists.Collection.ToggleSheet -> {
                updateState {
                    selectionLens.modify(source = this) {
                        it.copy(isCollectionsSheetVisible = !it.isCollectionsSheetVisible)
                    }
                }
            }
        }
    }

    private fun LibertyFlowMviScope<State>.executeRequest(
        intent: CommonSelectionIntent,
        @StringRes loadingSnackbarRes: Int,
        request: suspend (List<Int>) -> DomainRequestResult<Unit>,
    ) {
        val selectedIds = selectionLens.get(state.value).ids.toList()
        if (selectedIds.isEmpty()) return

        val loadingSnackbarId = "loading_snackbar_id"
        coroutineScope.launch(context = dispatcherIo) {
            updateState { selectionLens.modify(source = this) { it.copy(ids = emptySet()) } }
            postCommonEffect(
                BrbxEffect.ShowSnackbar(
                    config = DefaultBrbxLoadingSnackbarConfig(
                        id = loadingSnackbarId,
                        text = loadingSnackbarRes.toBrbxText(),
                        duration = BrbxSnackbarDuration.Infinite,
                        isDismissable = false,
                    )
                )
            )
            delay(duration = 2_000.milliseconds) // Animation delay for snackbar (antipattern, maybe will be rewritten)

            makeNetworkCall(
                loadingLens = selectionLens.loadingState,
                call = { request(selectedIds) }
            ).onSuccess {
                postCommonEffect(BrbxEffect.RemoveSnackbarById(loadingSnackbarId))
                // TODO
            } onException { exception ->
                postCommonEffect(BrbxEffect.RemoveSnackbarById(loadingSnackbarId))
                updateState { selectionLens.modify(source = this) { it.copy(ids = selectedIds.toSet()) } }
                postExceptionSnackbar(
                    exception = exception.toBrbxText(),
                    dismissable = true,
                ) { process(intent) }
            }
        }
    }
}
