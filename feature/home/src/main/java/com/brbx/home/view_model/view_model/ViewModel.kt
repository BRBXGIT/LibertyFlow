package com.brbx.home.view_model.view_model

import com.brbx.common.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State

internal abstract class ViewModel : LibertyFlowViewModel<State, Intent>(initialState = State())