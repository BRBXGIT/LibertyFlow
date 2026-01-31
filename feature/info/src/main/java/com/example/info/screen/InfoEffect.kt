package com.example.info.screen

sealed interface InfoEffect {
    data class CopyVersion(val versionTextRes: Int): InfoEffect
}