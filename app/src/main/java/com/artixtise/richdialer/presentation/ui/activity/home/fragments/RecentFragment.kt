package com.artixtise.richdialer.presentation.ui.activity.home.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.databinding.DialpadBinding
import com.artixtise.richdialer.databinding.FragmentRecentBinding
import com.artixtise.richdialer.databinding.SimSelectionDialogBinding
import com.artixtise.richdialer.dialogs.BottomSheetDialogs
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.RecentAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.SimAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments.*
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.artixtise.richdialer.utility.Utility
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList

class RecentFragment : BaseFragment(R.layout.fragment_recent),RecentAdapter.OnRecenInterface {

    lateinit var binding : FragmentRecentBinding
    lateinit var recentAdapter: RecentAdapter

    companion object {

        var Instance: RecentFragment? = null
        var viewModel: HomeViewModel? = null
        fun newInstance(viewmodel: HomeViewModel): RecentFragment? {
            viewModel = viewmodel
            if (Instance == null) {
                Instance = RecentFragment()
            }
            return Instance
        }
    }

    var recentData = ArrayList<String>().also {
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
        it.add("Ishant")
    }

    override fun WorkStation() {
        setupRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecentBinding.bind(view)
        WorkStation()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            linearLayout.setOnClickListener {
                openDialpad()
            }
            linearLayout2.setOnClickListener {}
        }
    }

    private fun openDialpad() {
        val dialog = BottomSheetDialogs.getDialpad(requireContext())
        val binding = DialpadBinding.inflate(layoutInflater)
        binding.apply {

        }
        dialog.show()
    }

    private fun setupRecyclerView() {
        with(binding.rvRecents){
            recentAdapter=RecentAdapter(requireContext(),this@RecentFragment)
            layoutManager = LinearLayoutManager(context)
            adapter = recentAdapter
            recentAdapter.UpdateList(recentData)
        }
    }

    override fun add() {

    }

    override fun cloud() {

    }

    override fun smile() {

    }

    override fun videoCall() {

    }

    override fun showData(tabLayout: TabLayout, fragmentsViewpager: ViewPager2) {
    }


    override fun updateContact(phoneNumber: String) {
        //update views
        getUserId(phoneNumber)
    }

    private fun getUserId(phoneNumber: String) {
        lifecycleScope.launchWhenCreated {
            viewModel?.getOtherUserId(phoneNumber)!!.observe(requireActivity(), Observer {

            })
        }

    }
}