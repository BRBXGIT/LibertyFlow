package com.brbx.common.view_model.processor.auth.processor

import arrow.optics.Lens
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.common.view_model.processor.auth.model.loadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.domain.network.model.result.RequestException
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.user.auth.use_case.UserAuthUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonAuthSheetProcessorImpl<State>(
    private val authSheetLens: Lens<State, CommonAuthSheetState>,
    private val authUseCase: UserAuthUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonAuthSheetProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonAuthSheetIntent) {
        when (intent) {
            CommonAuthSheetIntent.Authorize -> {
                val login = authSheetLens.get(state.value).login
                val password = authSheetLens.get(state.value).password
                coroutineScope.launch(context = dispatcherIo) {
                    updateState {
                        authSheetLens.modify(source = this) {
                            it.copy(
                                isAuthSheetVisible = false,
                                isDataIncorrect = false,
                            )
                        }
                    }
                    makeNetworkCall(
                        loadingLens = authSheetLens.loadingState,
                        callDelay = 1_000L,
                        call = { authUseCase(login, password) }
                    ).onException { exception ->
                        if (exception == RequestException.IncorrectCredentials) {
                            updateState {
                                authSheetLens.modify(source = this) {
                                    it.copy(
                                        isAuthSheetVisible = true,
                                        isDataIncorrect = true,
                                    )
                                }
                            }
                        } else {
                            postExceptionSnackbar(
                                exception = exception.toBrbxText(),
                                dismissable = true,
                            ) { process(intent) }
                        }
                    }
                }
            }
            CommonAuthSheetIntent.ToggleSheet -> {
                updateState {
                    authSheetLens.modify(source = this) {
                        it.copy(isAuthSheetVisible = !it.isAuthSheetVisible)
                    }
                }
            }
            is CommonAuthSheetIntent.UpdateLogin -> {
                updateState {
                    authSheetLens.modify(source = this) {
                        it.copy(
                            login = intent.login,
                            isDataIncorrect = false,
                        )
                    }
                }
            }
            is CommonAuthSheetIntent.UpdatePassword -> {
                updateState {
                    authSheetLens.modify(source = this) {
                        it.copy(
                            password = intent.password,
                            isDataIncorrect = false,
                        )
                    }
                }
            }
        }
    }
}