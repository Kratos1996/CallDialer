package com.artixtise.richdialer.presentation.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.animation.MyBounceInterpolator
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.databinding.ActivitySplashBinding
import com.artixtise.richdialer.presentation.ui.activity.home.HomeActivity
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var datastore: DataStoreBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivitySplashBinding=DataBindingUtil.setContentView(this,R.layout.activity_splash)
        val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_button)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        binding.logo.startAnimation(myAnim)
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (isActive) {
                    swapActivty()
                }

            }
        }
    }
    suspend fun swapActivty() {
        delay(3000L)
        datastore.isLoggedIn().collect{
            if(it){
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }

    }
}