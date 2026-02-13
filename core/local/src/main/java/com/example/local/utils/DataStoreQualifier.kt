package com.example.local.utils

import javax.inject.Qualifier

@Qualifier
@Retention
annotation class DataStoreQualifier(val libertyFlowDataStore: LibertyFlowDataStore)

enum class LibertyFlowDataStore {
    Auth,
    Onboarding,
    PlayerSettings,
    Theme
}