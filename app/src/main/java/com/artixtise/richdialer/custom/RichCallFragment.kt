package com.artixtise.richdialer.custom

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.RichCallOptionsBinding
import com.artixtise.richdialer.presentation.managers.getAvailableSIMCardLabels
import com.artixtise.richdialer.presentation.ui.activity.calling.CallingActivity
import com.artixtise.richdialer.presentation.ui.activity.home.adapter.SimAdapter
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RichCallFragment : BottomSheetDialogFragment() {
    private lateinit var binding: RichCallOptionsBinding

    lateinit var simAdapter: SimAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.rich_call_options, container,
            false
        )
        init()
        return binding.root
    }

    fun init() {
        with(binding.rvSim) {
            simAdapter = SimAdapter(requireContext(), object : SimAdapter.OnSimSelectInterface {
                override fun onSimClick(senderNumber: String, pos: Int) {
                    if (pos == 0) {
                            callWithSim(number!!, true)
                        } else {
                            callWithSim(number!!, false)
                        }

                }

            })
            layoutManager = GridLayoutManager(context, 2)
            adapter = simAdapter
            simAdapter.UpdateList(requireContext().getAvailableSIMCardLabels())
        }
    }

    companion object {
        var Instance: RichCallFragment? = null
        var viewModel: HomeViewModel? = null
        var number: String? = null
        fun newInstance(viewmodel: HomeViewModel, phnNumber: String): RichCallFragment? {
            viewModel = viewmodel
            number = phnNumber
            Instance = RichCallFragment()
            return Instance
        }
    }

    fun callWithSim(recipient: String, useSimOne: Boolean) {
        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val wantedSimIndex = if (useSimOne) 0 else 1
                        val handle = requireActivity().getAvailableSIMCardLabels()
                            .sortedBy { it.id }[wantedSimIndex].handle
                        val action = Intent.ACTION_CALL
                        /*val intent = Intent(action).apply {
                            data = Uri.fromParts("tel", recipient, null)
                            if (handle != null) {
                                putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                            }
                        }*/
                        val uri = "tel:${recipient}".toUri()
                        startActivity(Intent(Intent.ACTION_CALL, uri).apply {
                            if (handle != null) {
                                putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
                            }
                        })

                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        Log.e("permission", "no permission")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener {

            }.check()
    }
}