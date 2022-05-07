package com.artixtise.richdialer.domain.model.contact

object RichCallSealed {
    sealed class SaveRichCallState {
        class Success(val response: String) : SaveRichCallState()
        class Error(val response: String) : SaveRichCallState()
        class Loading(val isLoading: Boolean) : SaveRichCallState()
        object Empty : SaveRichCallState()
    }

}
