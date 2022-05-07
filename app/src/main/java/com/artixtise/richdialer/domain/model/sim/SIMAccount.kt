package com.artixtise.richdialer.domain.model.sim

import android.telecom.PhoneAccountHandle

data class SIMAccount(val id: Int, val handle: PhoneAccountHandle, val label: String, val phoneNumber: String)
