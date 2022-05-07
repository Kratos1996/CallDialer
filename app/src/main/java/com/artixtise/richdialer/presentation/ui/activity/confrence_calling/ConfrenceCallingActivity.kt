package com.artixtise.richdialer.presentation.ui.activity.confrence_calling

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityConfrenceCallingBinding
import com.artixtise.richdialer.presentation.ui.activity.confrence_calling.fragments.ConferenceCallingFragment
import com.artixtise.richdialer.presentation.ui.activity.confrence_calling.viewmodel.ConferenceCallingViewModel

class ConfrenceCallingActivity : BaseActivity() {
    private lateinit var binding: ActivityConfrenceCallingBinding
    val viewmodel: ConferenceCallingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confrence_calling)
        startFragment(ConferenceCallingFragment.newInstance(viewmodel),true, ConferenceCallingFragment::class.java.simpleName)
    }
}