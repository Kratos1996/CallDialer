package com.artixtise.richdialer.domain.model.contact

import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.artixtise.richdialer.domain.model.delete.DeleteResponse

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

    sealed class UplodeImage {

        class Success(val response: UplodeImageResponse) : UplodeImage()
        class Error(val response: String) : UplodeImage()
        class Loading(val isLoading: Boolean) : UplodeImage()
        object Empty : UplodeImage()
    }
    sealed class DeleteRichCall {

        class Success(val response: DeleteResponse) : DeleteRichCall()
        class Error(val response: String) : DeleteRichCall()
        class Loading(val isLoading: Boolean) : DeleteRichCall()
        object Empty : DeleteRichCall()
    }

}
