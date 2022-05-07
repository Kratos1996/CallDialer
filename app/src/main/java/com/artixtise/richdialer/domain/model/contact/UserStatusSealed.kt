package com.artixtise.richdialer.domain.model.contact

object UserStatusSealed {
    sealed class SaveUserStatusState {
        class Success(val response: String) : UserStatusSealed.SaveUserStatusState()
        class Error(val response: String) : UserStatusSealed.SaveUserStatusState()
        class Loading(val isLoading: Boolean) : UserStatusSealed.SaveUserStatusState()
        object Empty : UserStatusSealed.SaveUserStatusState()
    }
}