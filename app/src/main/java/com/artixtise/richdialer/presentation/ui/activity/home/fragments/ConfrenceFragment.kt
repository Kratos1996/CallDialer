package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.databinding.FragmentConfrenceBinding
import com.artixtise.richdialer.databinding.FragmentFavouriteBinding
import com.artixtise.richdialer.presentation.ui.activity.confrence_calling.ConfrenceCallingActivity
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.ConfrenceRecentAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.FavouriteAdapter
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import java.util.ArrayList


class ConfrenceFragment : BaseFragment(R.layout.fragment_confrence) {
    lateinit var binding: FragmentConfrenceBinding
    lateinit var conAdapter: ConfrenceRecentAdapter
    var favData = ArrayList<String>().also {
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
    }

    override fun WorkStation() {
        setupRecyclerView()
        initObserver()
        binding.apply {
            linearLayout3.setOnClickListener {
               // startActivity(Intent(requireContext(), ConfrenceCallingActivity::class.java))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConfrenceBinding.bind(view)
        WorkStation()
    }

    private fun initObserver() {

    }


    private fun setupRecyclerView() {
        with(binding.rvRecent) {
            conAdapter = ConfrenceRecentAdapter(context)
            layoutManager = LinearLayoutManager(context)
            adapter = conAdapter
            conAdapter.UpdateList(favData)
        }
    }
}