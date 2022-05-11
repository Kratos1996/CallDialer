package com.artixtise.richdialer.domain.model.contact

import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse

object RichCallSealed {
    sealed class SaveRichCallState {
        class Success(val response: String) : SaveRichCallState()
        class Error(val response: String) : SaveRichCallState()
        class Loading(val isLoading: Boolean) : SaveRichCallState()
        object Empty : SaveRichCallState()
    }
    sealed class SaveRichCalldata {
        class Success(val response: RichCallDataResponse) : SaveRichCalldata()
        class Error(val response: String) : SaveRichCalldata()
        class Loading(val isLoading: Boolean) : SaveRichCalldata()
        object Empty : SaveRichCalldata()
    }
    sealed class GetRichCalldata {

        class Success(val response: RichCallDataGetResponse) : GetRichCalldata()
        class Error(val response: String) : GetRichCalldata()
        class Loading(val isLoading: Boolean) : GetRichCalldata()
        object Empty : GetRichCalldata()
    }

}
