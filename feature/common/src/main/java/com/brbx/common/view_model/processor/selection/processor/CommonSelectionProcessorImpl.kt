package com.brbx.common.view_model.processor.selection.processor

import androidx.annotation.StringRes
import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.utils.toggle
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.common.view_model.view_model.postLoadingSnackbar
import com.brbx.common.view_model.view_model.removeLoadingSnackbar
import com.brbx.domain.network.model.result.DomainRequestResult
import com.brbx.domain.network.model.result.RequestException
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.user.lists.collections.collections.model.CollectionItem
import com.brbx.domain.network.user.lists.collections.collections.use_case.UserAddToCollectionUseCase
import com.brbx.domain.network.user.lists.collections.collections.use_case.UserDeleteFromCollectionUseCase
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserAddToFavoritesUseCase
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserDeleteFromFavoritesUseCase
import com.brbx.ui_compose.common.toBrbxText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonSelectionProcessorImpl<State>(
    private val addToFavoritesUseCase: UserAddToFavoritesUseCase,
    private val deleteFromFavoritesUseCase: UserDeleteFromFavoritesUseCase,
    private val addToCollectionUseCase: UserAddToCollectionUseCase,
    private val deleteFromCollectionUseCase: UserDeleteFromCollectionUseCase,
    private val selectionLens: Lens<State, CommonSelectionState>,
    private val dispatcherIo: CoroutineDispatcher,
    private val onUnauthorized: LibertyFlowMviScope<State>.() -> Unit = {},
) : CommonSelectionProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonSelectionIntent) {
        when (intent) {
            is CommonSelectionIntent.Selection -> handleSelection(intent)
            is CommonSelectionIntent.Lists -> handleLists(intent)
        }
    }

    private fun LibertyFlowMviScope<State>.handleSelection(intent: CommonSelectionIntent.Selection) {
        when (intent) {
            CommonSelectionIntent.Selection.DropSelection -> updateSelectionState {
                it.copy(ids = emptySet())
            }
            is CommonSelectionIntent.Selection.ToggleItemSelected -> updateSelectionState {
                it.copy(ids = it.ids.toggle(element = intent.id))
            }
        }
    }

    private fun LibertyFlowMviScope<State>.handleLists(intent: CommonSelectionIntent.Lists) {
        when (intent) {
            is CommonSelectionIntent.Lists.Favorites -> handleFavorites(intent)
            is CommonSelectionIntent.Lists.Collection -> handleCollection(intent)
        }
    }

    private fun LibertyFlowMviScope<State>.handleFavorites(intent: CommonSelectionIntent.Lists.Favorites) {
        executeRequest(
            intent = intent,
            loadingSnackbarRes = when (intent.action) {
                SelectionAction.Add -> CommonStrings.adding_to_favorites
                SelectionAction.Delete -> CommonStrings.deleting_from_favorites
            }
        ) { ids ->
            when (intent.action) {
                SelectionAction.Add -> addToFavoritesUseCase(items = ids)
                SelectionAction.Delete -> deleteFromFavoritesUseCase(items = ids)
            }
        }
    }

    private fun LibertyFlowMviScope<State>.handleCollection(intent: CommonSelectionIntent.Lists.Collection) {
        when (intent) {
            is CommonSelectionIntent.Lists.Collection.Interact -> {
                executeRequest(
                    intent = intent,
                    loadingSnackbarRes = when (intent.action) {
                        SelectionAction.Add -> CommonStrings.adding_to_collection
                        SelectionAction.Delete -> CommonStrings.deleting_from_collection
                    }
                ) { ids ->
                    val items = ids.map { CollectionItem(id = it, collection = intent.collection) }
                    when (intent.action) {
                        SelectionAction.Add -> addToCollectionUseCase(items = items)
                        SelectionAction.Delete -> deleteFromCollectionUseCase(items = items)
                    }
                }
            }
            CommonSelectionIntent.Lists.Collection.ToggleSheet -> toggleCollectionsSheet()
        }
    }

    private fun LibertyFlowMviScope<State>.toggleCollectionsSheet() {
        updateSelectionState {
            it.copy(isCollectionsSheetVisible = !it.isCollectionsSheetVisible)
        }
    }

    private fun LibertyFlowMviScope<State>.executeRequest(
        intent: CommonSelectionIntent,
        @StringRes loadingSnackbarRes: Int,
        request: suspend (List<Int>) -> DomainRequestResult<Unit>,
    ) {
        val selectedIds = selectionLens.get(state.value).ids.toList()
        if (selectedIds.isEmpty()) return

        val loadingSnackbarId = "list_loading_snackbar_id"
        coroutineScope.launch(context = dispatcherIo) {
            updateSelectionState { it.copy(ids = emptySet()) }
            postLoadingSnackbar(
                text = loadingSnackbarRes.toBrbxText(),
                loadingSnackbarId = loadingSnackbarId,
            )

            makeNetworkCall(
                callDelay = 2_000,
                call = { request(selectedIds) }
            ).onException { exception ->
                updateSelectionState { it.copy(ids = selectedIds.toSet()) }
                postExceptionSnackbar(
                    exception = exception.toBrbxText(),
                    dismissable = true,
                    buttonText = if (exception == RequestException.Unauthorized) {
                        CommonStrings.authorize.toBrbxText()
                    } else {
                        CommonStrings.retry.toBrbxText()
                    },
                ) {
                    if (exception == RequestException.Unauthorized) {
                        onUnauthorized()
                    } else {
                        process(intent)
                    }
                }
            }
            removeLoadingSnackbar(loadingSnackbarId)
        }
    }

    private fun LibertyFlowMviScope<State>.updateSelectionState(
        modifier: (CommonSelectionState) -> CommonSelectionState
    ) {
        updateState {
            selectionLens.modify(source = this, map = modifier)
        }
    }
}
