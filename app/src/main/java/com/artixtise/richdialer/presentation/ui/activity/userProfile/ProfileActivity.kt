package com.artixtise.richdialer.presentation.ui.activity.userProfile


import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.BuildConfig
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.databinding.ActivityProfileBinding
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.mapper.UserAccessToProfile
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel
import com.artixtise.richdialer.repositories.util.FileAccess
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewmodel: ProfileViewmodel by viewModels()
    private var data = UserAccessData()
    private var profile=""
    private var finalFile=File("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.backNow.setOnClickListener {
            finish()
        }
        binding.callerAvatar.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        var pictureActionIntent: Intent? = null
                        pictureActionIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pictureActionIntent, 100)

                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        showCustomAlert("Need Camera Permission", binding.root)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest, permissionToken: PermissionToken
                    ) {
                    }
                }).check()
        }
        fetchData()
        initViews()
    }

    private fun initViews() {
        binding.update.setOnClickListener {

            data = UserAccessData(
                binding.etName.text.toString(),
                "",
                binding.etEmail.text.toString(),
                sharedPre.userId ?: "",
                binding.etPhone1.text.toString(),
                binding.etPhone2.text.toString(),
                binding.etAddress.text.toString(),
                System.currentTimeMillis(),
                profile,
                binding.instagramId.text.toString(),
                binding.linkedId.text.toString(),
                binding.facebookId.text.toString(),
                binding.websiteId.text.toString(),
                binding.twitterId.text.toString(),
                deviceToken = sharedPre.firebaseDeviceToken ?: "",
                binding.instaCheckbox.isChecked,
                binding.linkedinCheckbox.isChecked,
                binding.facebookCheckBox.isChecked,
                binding.webCheckbox0.isChecked,
                binding.twitterCheckbox.isChecked,
            )
            updateData(data)

        }
    }

    private fun updateData(data: UserAccessData) {
        viewmodel.updateUserData(data)
    }

    fun fetchData() {
        viewmodel.getMyProfile().observe(this) {
            if (it != null) {
                setData(UserAccessToProfile.reverce(it))
                profile= it.profilePicture
                val imageArray=convertStringToByteArray(profile)
                val bitmap= BitmapFactory.decodeByteArray(imageArray,0,imageArray.size)
                Glide.with(this).load(bitmap)
                    .placeholder(R.drawable.avtar_profile)
                    .into(binding.callerAvatar)

                binding.emptyData.visibility = View.GONE
            } else {
                getOnlineProfile()
                binding.emptyData.visibility = View.VISIBLE
            }
        }
        lifecycleScope.launchWhenCreated {
            viewmodel.registerState.collect {
                when (it) {
                    is LoginSealed.RegisterUserState.Loading -> {
                        showLoadingDialog(ErrorMessage.PLEASE_WAIT)!!
                            .show()
                    }
                    is LoginSealed.RegisterUserState.Success -> {
                        showLoadingDialog(it.response)!!
                            .dismiss()
                        showCustomAlert(it.response, binding.root)
                    }
                    is LoginSealed.RegisterUserState.Error -> {
                        showLoadingDialog(it.response)!!.dismiss()
                        showCustomAlert(it.response, binding.root)
                    }
                }
            }
        }
    }

    fun getOnlineProfile() {
        lifecycleScope.launchWhenCreated {
            viewmodel.getOtherUserId(sharedPre.userMobile!!).observe(this@ProfileActivity) { user ->
                if (user != null) {
                    viewmodel.insertMyProfile(UserAccessToProfile.convert(user))
                    binding.emptyData.visibility = View.GONE
                } else {
                    binding.emptyData.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun setData(userAccessData: UserAccessData) {
        binding.data = userAccessData
    }
    fun showImgDialog(isCancelable: Boolean): Dialog? {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (isCancelable) {
            dialog.setCancelable(true)
        } else {
            dialog.setCancelable(false)
        }
        dialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation
        dialog.setContentView(R.layout.upload_img_lay)
        val camera: ImageView = dialog.findViewById<ImageView>(R.id.img_camera)
        val gallery: ImageView = dialog.findViewById<ImageView>(R.id.img_gallery)
        val close: ImageView = dialog.findViewById<ImageView>(R.id.closeAttach)
        close.setOnClickListener { v: View? -> dialog.dismiss() }
        camera.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        dialog.dismiss()
                        captureFromCamera()

                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        showCustomAlert("Need Camera Permission", binding.root)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest, permissionToken: PermissionToken
                    ) {
                    }
                }).check()
        }
        gallery.setOnClickListener {

        }

        dialog.setCancelable(isCancelable)
        return dialog
    }

    private fun captureFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    photoFile
                )
                finalFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, 101)
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "RichCall" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val file = File.createTempFile(mFileName, ".jpg", storageDir)
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            when (requestCode) {
                100 -> {
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            try {
                                val selectedImage = data.data
                                val file: String =
                                    FileAccess.getPath(this, selectedImage)
                                finalFile = File(file)
                                Glide.with(this).load(selectedImage)
                                    .placeholder(R.drawable.avtar_profile)
                                    .into(binding.callerAvatar)
                                profile= convertByteArraytoString(converBitmap(finalFile))

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                    }
                }
                101 -> {
                    if (resultCode == RESULT_OK) {
                        finalFile = File(finalFile.absolutePath)
                        if (!finalFile.exists()) {
                            finalFile.mkdir()
                        }
                        Glide.with(this).load(finalFile)
                            .placeholder(R.drawable.avtar_profile)
                            .into(binding.callerAvatar)
                        profile= convertByteArraytoString(converBitmap(finalFile))
                    } else if (resultCode == RESULT_CANCELED) {
                        //  Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                }
                else -> {}
            }
        } catch (e: Exception) {
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}