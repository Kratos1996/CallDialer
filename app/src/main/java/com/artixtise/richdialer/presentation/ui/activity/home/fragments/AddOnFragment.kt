package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.FragmentAddOnBinding
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.AddonAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.RecentAdapter

class AddOnFragment : Fragment(R.layout.fragment_add_on) {
    lateinit var binding : FragmentAddOnBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddOnBinding.bind(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding.rvAddon){
            layoutManager = LinearLayoutManager(context)
            adapter = AddonAdapter()
        }
    }

}