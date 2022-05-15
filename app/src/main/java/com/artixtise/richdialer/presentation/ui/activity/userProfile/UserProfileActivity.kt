package com.artixtise.richdialer.presentation.ui.activity.userProfile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.application.ErrorMessage.ADD_TO_FAV_FAILURE
import com.artixtise.richdialer.application.ErrorMessage.ADD_TO_FAV_SUCCESSFULLY
import com.artixtise.richdialer.application.ErrorMessage.CONTACT_SAVED
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.databinding.ActivityUserProfileBinding
import com.artixtise.richdialer.mapper.UserAccessToContactList
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.FavouriteFragment
import com.artixtise.richdialer.presentation.ui.activity.preview.SelectScreenActivity
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    val viewmodel: ProfileViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        val phoneNumber = intent.getStringExtra("contactNumber")
        showLoadingDialog("Please Wait ...")!!.show()
        binding.backNow.setOnClickListener {
            finish()
        }
        lifecycleScope.launchWhenCreated {
            viewmodel.getOtherUserId(phoneNumber!!).observe(this@UserProfileActivity) { user->
                if(user!=null){
                    val contact=UserAccessToContactList.convert(user)
                    viewmodel.updateRichCallData(contact)
                }
            }
            viewmodel.getContactDetail(phoneNumber!!).collect {
                if (it != null) {
                    var isFavNow = it.isFav
                    binding.addToFav.setOnClickListener { view ->
                        if (it.isFav) {
                            isFavNow = false
                            binding.addToFav.setImageResource(R.drawable.ic_favourite)
                            viewmodel.repositoryImpl.setFavContact(it.phoneNumber, isFavNow)
                            showCustomAlert(ADD_TO_FAV_FAILURE, binding.root)

                        } else {
                            isFavNow = true
                            viewmodel.repositoryImpl.setFavContact(it.phoneNumber, isFavNow)
                            binding.addToFav.setImageResource(R.drawable.ic_favourite_selected)
                            showCustomAlert(ADD_TO_FAV_SUCCESSFULLY, binding.root)
                        }
                    }
                    binding.callerNameLabel.setText(it.name)
                    binding.alphabaticaName.setText(it.name)
                    binding.phoneNumber.setText(it.phoneNumber)
                    binding.email.setText(it.email)
                    if(it.instagramAccountVisible){
                        binding.flFive.visibility=View.VISIBLE
                    }else{
                        binding.flFive.visibility=View.GONE
                    }
                    if(it.twitterAccountVisible){
                        binding.flFour.visibility=View.VISIBLE
                    }else{
                        binding.flFour.visibility=View.GONE
                    }
                    binding.instagramAccount.setText(it.instagramAccount)
                    binding.twitterAccount.setText(it.twitterAccount)
                    binding.websiteId.setText(it.webUrl)
                    binding.linkedId.setText(it.LinkedInAccount)
                    binding.facebookId.setText(it.facebookAccount)
                    if (it.isFav) {
                        binding.addToFav.setImageResource(R.drawable.ic_favourite_selected)
                    } else {
                        binding.addToFav.setImageResource(R.drawable.ic_favourite)
                    }
                    if (it.profile.isNullOrBlank()) {
                        binding.circleTextView.visibility = View.VISIBLE
                        binding.callerAvatar.visibility = View.GONE
                    } else {
                        binding.circleTextView.visibility = View.GONE
                        binding.callerAvatar.visibility = View.VISIBLE
                        val imageArray=convertStringToByteArray(it.profile)
                        val bitmap= BitmapFactory.decodeByteArray(imageArray,0,imageArray.size)
                        Glide.with(this@UserProfileActivity).load(bitmap)
                            .into(binding.callerAvatar)
                    }
                    binding.saveContact.setOnClickListener { view ->
                        it.isFav = isFavNow
                        viewmodel.insertContact(it)
                        showCustomAlert(CONTACT_SAVED, binding.root)
                    }
                    binding.richCallNow.setOnClickListener {view->
                        if(isNetworkAvailable(this@UserProfileActivity)){
                            if(it.isAvailableRichCall){
                                var data =it
                                data.profile=""
                                val intent = Intent(this@UserProfileActivity, SelectScreenActivity::class.java).apply {
                                    putExtra("FAVDATA", data)
                                }
                                startActivity(intent)
                            }else{
                                showCustomAlert(ErrorMessage.NOT_AVAILABLE__RICH_CALL_DATA, binding.root)
                            }

                        }else{
                            showCustomAlert(ErrorMessage.INTERNET_CONNECTION_ERROR, binding.root)
                        }

                    }
                    binding.callNow.setOnClickListener { view->
                        selectSim(it.phoneNumber)
                    }

                    showLoadingDialog("Please Wait ...")!!.dismiss()
                } else {
                    showLoadingDialog("Please Wait ...")!!.dismiss()
                }

            }
        }
    }

    private fun selectSim(number:String) {
        Dexter.withContext(this@UserProfileActivity)
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    if (number.isNullOrBlank()){
                        Toast.makeText(this@UserProfileActivity,"First select number", Toast.LENGTH_LONG).show()
                    }
                    else{
                        val richCallFragment= RichCallFragment.newInstance(0, viewModel!!,number)!!
                        richCallFragment.show(manager, "add_richcall_dialog_fragment")
                    }
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Calling  Permission", binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {
                }
            }).check()

    }
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}
