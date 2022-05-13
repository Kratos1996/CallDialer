package com.artixtise.richdialer.presentation.ui.activity.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.SecondFragmentBinding
import com.artixtise.richdialer.databinding.ThirdFragmentBinding


class ThirdFragment : Fragment() {
    private lateinit var binding: ThirdFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ThirdFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}