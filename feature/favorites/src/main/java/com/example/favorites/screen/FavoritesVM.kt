@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.common.vm_helpers.update
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import com.example.data.utils.remote.network_request.NetworkErrors
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.design_system.components.snackbars.sendRetrySnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesVM @Inject constructor(
    private val favoritesRepo: FavoritesRepo,
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _favoritesState = MutableStateFlow(FavoritesState())
    val favoritesState = _favoritesState.toLazily(FavoritesState())

    val favorites = favoritesRepo
        .getFavorites(UiCommonRequest(UiShortRequestParameters()))
        .cachedIn(viewModelScope)

    val favoritesByQuery = _favoritesState
        .map { UiCommonRequest(UiShortRequestParameters(search = it.query)) }
        .flatMapLatest { request ->
            favoritesRepo.getFavorites(request)
        }.cachedIn(viewModelScope)

    @VisibleForTesting
    internal fun observeLoggingState() {
        viewModelScope.launch(dispatcherIo) {
            authRepo.authState.collect { authState ->
                _favoritesState.update { copy(isLoggedIn = authState) }
            }
        }
    }

    private fun getAuthToken() {
        viewModelScope.launch(dispatcherIo) {
            _favoritesState.update { copy(isPasswordOrEmailIncorrect = false, isLoading = true) }
            val request = UiTokenRequest(
                login = _favoritesState.value.email,
                password = _favoritesState.value.password
            )
            authRepo.getToken(request)
                .onSuccess { uiToken ->
                    authRepo.saveToken(uiToken.token!!)
                    _favoritesState.update { copy(isLoading = false) }
                }
                .onError { error, message ->
                    if (error == NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD) {
                        _favoritesState.update { copy(isLoading = false, isPasswordOrEmailIncorrect = true) }
                    } else {
                        sendRetrySnackbar(message) { getAuthToken() }
                    }
                }
        }
    }

    fun sendIntent(intent: FavoritesIntent) {
        when(intent) {
            is FavoritesIntent.UpdateIsLoading -> _favoritesState.update { copy(isLoading = intent.isLoading) }
            is FavoritesIntent.UpdateIsError -> _favoritesState.update { copy(isError = intent.isError) }

            is FavoritesIntent.UpdateQuery -> _favoritesState.update { copy(query = intent.query) }
            FavoritesIntent.UpdateIsSearching -> _favoritesState.update { copy(isSearching = !isSearching) }

            is FavoritesIntent.UpdateEmail -> _favoritesState.update { copy(email = intent.email) }
            FavoritesIntent.UpdateIsAuthBSVisible -> _favoritesState.update { copy(isAuthBSVisible = !isAuthBSVisible) }
            is FavoritesIntent.UpdatePassword -> _favoritesState.update { copy(password = intent.password) }

            FavoritesIntent.GetTokens -> getAuthToken()
        }
    }

    init {
        observeLoggingState()
    }
}