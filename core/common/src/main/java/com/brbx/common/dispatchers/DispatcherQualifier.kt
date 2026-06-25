package com.brbx.common.dispatchers

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

enum class DispatcherQualifier : Qualifier {
    Io, Default, Main;
    override val value: QualifierValue = this.name
}