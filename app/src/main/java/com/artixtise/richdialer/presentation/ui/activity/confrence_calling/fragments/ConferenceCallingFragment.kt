package com.artixtise.richdialer.presentation.ui.activity.confrence_calling.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.databinding.FragmentConferenceCallingBinding
import com.artixtise.richdialer.presentation.ui.activity.confrence_calling.viewmodel.ConferenceCallingViewModel

class ConferenceCallingFragment : BaseFragment(R.layout.fragment_conference_calling) {
    private lateinit var binding: FragmentConferenceCallingBinding

    companion object {

        var Instance: ConferenceCallingFragment? = null
        var viewModel: ConferenceCallingViewModel? = null
        fun newInstance(viewmodel: ConferenceCallingViewModel): ConferenceCallingFragment? {
            viewModel = viewmodel
            if (Instance == null) {
                Instance = ConferenceCallingFragment()
            }
            return Instance
        }
    }

    override fun WorkStation() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conference_calling, container, false)
        WorkStation()
        return binding.root
    }

}