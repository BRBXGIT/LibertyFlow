package com.brbx.common.view_model.processor.selection.processor

import arrow.optics.Lens
import com.brbx.common.utils.toggle
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.common.view_model.processor.selection.model.loadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.user.lists.favorites.favorites.use_case.UserAddToFavoritesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonSelectionProcessorImpl<State>(
    private val addToFavoritesUseCase: UserAddToFavoritesUseCase,
    private val selectionLens: Lens<State, CommonSelectionState>,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonSelectionProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonSelectionIntent) {
        when (intent) {
            CommonSelectionIntent.Lists.Collection.ToggleSheet -> TODO()
            is CommonSelectionIntent.Lists.Favorites -> {
                when (intent.action) {
                    SelectionAction.Add -> {
                        coroutineScope.launch(context = dispatcherIo) {
                            val selectionState = selectionLens.get(state.value)
                            val selectedIds = selectionState.ids.toList()

                            makeNetworkCall(
                                loadingLens = selectionLens.loadingState,
                                call = { addToFavoritesUseCase(selectedIds) }
                            ).onSuccess {

                            } onException {

                            }
                        }
                    }
                    SelectionAction.Remove -> TODO()
                }
            }
            is CommonSelectionIntent.ToggleItemSelected -> updateState {
                selectionLens.modify(source = this) {
                    it.copy(ids = it.ids.toggle(element = intent.id))
                }
            }
        }
    }
}