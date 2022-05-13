package com.artixtise.richdialer.presentation.ui.activity.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.ActivityWelcomeBinding
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        val viewPager = binding.vpPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.dotsIndicator.attachTo(viewPager)

        binding.tvStarted.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

}