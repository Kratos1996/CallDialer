package com.artixtise.richdialer.domain.model.login

object LoginSealed {
    sealed class VerificationCodeState {
        class Success(val response: LoginModel) : VerificationCodeState()
        class Message(val message: String?) : VerificationCodeState()
        class Loading(val isLoading: Boolean) : VerificationCodeState()
        object Empty : VerificationCodeState()
    }
    sealed class VerificationState {
        class Success(val response: VerificationModel) : VerificationState()
        class Error(val response: VerificationModel) : VerificationState()
        class Loading(val isLoading: Boolean) : VerificationState()
        object Empty : VerificationState()
    }
    sealed class RegisterUserState {
        class Success(val response: String) : RegisterUserState()
        class Error(val response: String) : RegisterUserState()
        class Loading(val isLoading: Boolean) : RegisterUserState()
        object Empty : RegisterUserState()
    }
}