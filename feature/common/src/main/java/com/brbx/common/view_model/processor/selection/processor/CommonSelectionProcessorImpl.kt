package com.brbx.common.view_model.processor.selection.processor

import arrow.optics.Lens
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.common.view_model.processor.selection.model.loadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserAddToFavoritesUseCase
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserDeleteFromFavoritesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

private typealias FavoritesIntent = CommonSelectionIntent.Lists.Favorites

internal class CommonSelectionProcessorImpl<State>(
    private val addToFavoritesUseCase: UserAddToFavoritesUseCase,
    private val deleteFromFavoritesUseCase: UserDeleteFromFavoritesUseCase,
    private val selectionLens: Lens<State, CommonSelectionState>,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonSelectionProcessor<State> {

    // TODO Add loading snackbar
    override fun LibertyFlowMviScope<State>.process(intent: CommonSelectionIntent) {
        when (intent) {
            is FavoritesIntent -> {
                when (intent.action) {
                    SelectionAction.Add -> {
                        coroutineScope.launch(context = dispatcherIo) {
                            val selectionState = selectionLens.get(state.value)
                            val selectedIds = selectionState.ids.toList()
                            makeNetworkCall(
                                loadingLens = selectionLens.loadingState,
                                call = { addToFavoritesUseCase(items = selectedIds) }
                            ).onSuccess {
                                selectionLens.modify(source = state.value) {
                                    it.copy(ids = emptySet())
                                }
                            } onException { exception ->
                                postExceptionSnackbar(
                                    exception = exception.toBrbxText(),
                                    dismissable = true,
                                ) { process(intent = FavoritesIntent(action = SelectionAction.Add)) }
                            }
                        }
                    }
                    SelectionAction.Remove -> {
                        coroutineScope.launch(context = dispatcherIo) {
                            val selectionState = selectionLens.get(state.value)
                            val selectedIds = selectionState.ids.toList()
                            makeNetworkCall(
                                loadingLens = selectionLens.loadingState,
                                call = { deleteFromFavoritesUseCase(items = selectedIds) }
                            ).onSuccess {
                                selectionLens.modify(source = state.value) {
                                    it.copy(ids = emptySet())
                                }
                            } onException { exception ->
                                postExceptionSnackbar(
                                    exception = exception.toBrbxText(),
                                    dismissable = true,
                                ) { process(intent = FavoritesIntent(action = SelectionAction.Add)) }
                            }
                        }
                    }
                }
            }
            is CommonSelectionIntent.Selection -> {
                TODO()
            }
        }
    }
}

//selectionLens.modify(source = this) {
//    it.copy(ids = it.ids.toggle(element = intent.id))
//}