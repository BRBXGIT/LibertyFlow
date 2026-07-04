package com.brbx.common.view_model.processor.selection.processor

import arrow.optics.Lens
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

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
        updateState {
            selectionLens.modify(source = this) {
                when (intent) {
                    is CommonSelectionIntent.Selection.ToggleItemSelected ->
                        it.copy(ids = it.ids.toggle(intent.id))
                    CommonSelectionIntent.Selection.DropSelection ->
                        it.copy(ids = emptySet())
                }
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
        executeRequest(intent) { ids ->
            when (intent.action) {
                SelectionAction.Add -> addToFavoritesUseCase(items = ids)
                SelectionAction.Remove -> deleteFromFavoritesUseCase(items = ids)
            }
        }
    }

    private fun LibertyFlowMviScope<State>.processCollection(intent: CommonSelectionIntent.Lists.Collection) {
        when (intent) {
            is CommonSelectionIntent.Lists.Collection.Interact -> {
                executeRequest(intent) { ids ->
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
        request: suspend (List<Int>) -> DomainRequestResult<Unit>,
    ) {
        coroutineScope.launch(context = dispatcherIo) {
            updateState { selectionLens.modify(source = this) { it.copy(ids = emptySet()) } }
            val selectedIds = selectionLens.get(state.value).ids.toList()
            if (selectedIds.isEmpty()) return@launch

            makeNetworkCall(
                loadingLens = selectionLens.loadingState,
                call = { request(selectedIds) }
            ).onSuccess {
                // TODO
            } onException { exception ->
                postExceptionSnackbar(
                    exception = exception.toBrbxText(),
                    dismissable = true,
                ) { process(intent) }
            }
        }
    }
}
