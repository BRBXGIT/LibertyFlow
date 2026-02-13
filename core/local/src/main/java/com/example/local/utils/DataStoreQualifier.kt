package com.example.local.utils

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DataStoreQualifier(val libertyFlowDataStore: LibertyFlowDataStore)

enum class LibertyFlowDataStore {
    Auth,
    Onboarding,
    PlayerSettings,
    Theme
}