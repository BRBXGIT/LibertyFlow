package com.brbx.common.view_model.processor.auth.processor

import arrow.optics.Lens
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.common.view_model.processor.auth.model.loadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.common.view_model.view_model.postLoadingSnackbar
import com.brbx.common.view_model.view_model.removeLoadingSnackbar
import com.brbx.domain.network.model.result.RequestException
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.user.auth.use_case.UserAuthUseCase
import com.brbx.ui_compose.common.toBrbxText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonAuthSheetProcessorImpl<State>(
    private val authSheetLens: Lens<State, CommonAuthSheetState>,
    private val authUseCase: UserAuthUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonAuthSheetProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonAuthSheetIntent) {
        when (intent) {
            is CommonAuthSheetIntent.Authorize -> handleAuthorize(intent)
            is CommonAuthSheetIntent.ToggleSheet -> toggleSheet()
            is CommonAuthSheetIntent.TogglePasswordVisible -> togglePasswordVisibility()
            is CommonAuthSheetIntent.UpdateLogin -> updateLogin(intent.login)
            is CommonAuthSheetIntent.UpdatePassword -> updatePassword(intent.password)
        }
    }

    private fun LibertyFlowMviScope<State>.handleAuthorize(intent: CommonAuthSheetIntent.Authorize) {
        val authState = authSheetLens.get(state.value)
        val login = authState.login
        val password = authState.password

        coroutineScope.launch(context = dispatcherIo) {
            updateAuthSheet {
                it.copy(
                    isAuthSheetVisible = false,
                    isDataIncorrect = false,
                )
            }

            val authSnackbarId = "auth_snackbar_id"
            postLoadingSnackbar(
                text = CommonStrings.auth_snackbar_label.toBrbxText(),
                loadingSnackbarId = authSnackbarId,
            )
            makeNetworkCall(
                loadingLens = authSheetLens.loadingState,
                callDelay = 1_000L,
                call = { authUseCase(login, password) }
            ).onException { exception ->
                handleAuthError(exception, retryIntent = intent)
            }
            removeLoadingSnackbar(loadingSnackbarId = authSnackbarId)
        }
    }

    private fun LibertyFlowMviScope<State>.handleAuthError(
        exception: RequestException,
        retryIntent: CommonAuthSheetIntent
    ) {
        val incorrectDataException = exception == RequestException.IncorrectCredentials ||
            exception == RequestException.NoEmailOrPassword
        if (incorrectDataException) {
            updateAuthSheet {
                it.copy(
                    isAuthSheetVisible = true,
                    isDataIncorrect = true,
                )
            }
        } else {
            postExceptionSnackbar(
                exception = exception.toBrbxText(),
                dismissable = true,
            ) { process(retryIntent) }
        }
    }

    private fun LibertyFlowMviScope<State>.toggleSheet() {
        updateAuthSheet {
            it.copy(isAuthSheetVisible = !it.isAuthSheetVisible)
        }
    }
    private fun LibertyFlowMviScope<State>.togglePasswordVisibility() {
        updateAuthSheet {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    private fun LibertyFlowMviScope<State>.updateLogin(login: String) {
        updateAuthSheet {
            it.copy(
                login = login,
                isDataIncorrect = false,
            )
        }
    }

    private fun LibertyFlowMviScope<State>.updatePassword(password: String) {
        updateAuthSheet {
            it.copy(
                password = password,
                isDataIncorrect = false,
            )
        }
    }

    private fun LibertyFlowMviScope<State>.updateAuthSheet(
        modifier: (CommonAuthSheetState) -> CommonAuthSheetState
    ) {
        updateState {
            authSheetLens.modify(source = this, map = modifier)
        }
    }
}