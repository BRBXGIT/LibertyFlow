package com.brbx.common.view_model.processor.selection.model

import com.brbx.domain.network.model.common.DomainCollection

sealed interface CommonSelectionIntent {

    sealed interface Selection : CommonSelectionIntent {
        @JvmInline value class ToggleItemSelected(val id: Int) : Selection

        data object DropSelection : Selection
    }

    sealed interface Lists : CommonSelectionIntent {
        @JvmInline value class Favorites(val action: SelectionAction) : Lists

        sealed interface Collection : Lists {
            data class Interact(
                val collection: DomainCollection,
                val action: SelectionAction,
            ) : Collection

            data object ToggleSheet : Collection
        }
    }
}