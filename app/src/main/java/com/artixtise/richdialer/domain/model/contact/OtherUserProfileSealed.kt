package com.artixtise.richdialer.domain.model.contact

import com.artixtise.richdialer.data.profile.model.UserAccessData


object OtherUserProfileSealed {
    sealed class FetchOtherUserState {
        class Success(val response: UserAccessData) : FetchOtherUserState()
        class Error(val response: String?) : FetchOtherUserState()
        class Loading(val isLoading: Boolean) : FetchOtherUserState()
        object Empty : FetchOtherUserState()
    }

}
