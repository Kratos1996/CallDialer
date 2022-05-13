package com.artixtise.richdialer.presentation.ui.activity.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.FirstFragmentBinding
import com.artixtise.richdialer.databinding.SecondFragmentBinding


class SecondFragment :  Fragment() {
    private lateinit var binding: SecondFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}