package com.brbx.preferences.base

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

internal enum class DataStoreQualifier : Qualifier {
    Auth, Appearance, Player;
    override val value: QualifierValue = this.name
}