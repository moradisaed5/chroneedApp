package com.chroneed.chroneedapp.ui.social

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.databinding.ActivitySocialNetworkBinding
import com.chroneed.chroneedapp.ui.main.MainActivity

class SocialNetworkActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySocialNetworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialNetworkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firstInitMain()
        setHomeFabClick()
        setNavigationItemSelect()

    }

    private fun setHomeFabClick() {
        binding.mainMenuFabHome.setOnClickListener() {
            val fragment = SocialNewPostFragment()
            makeCurrentNavView(fragment)
        }
    }

    private fun setNavigationItemSelect() {
        val newPostFragment = SocialNewPostFragment()
        val profileFragment = SocialUserProfileFragment()
        val homeFragment = SocialHomeFragment()
        val settingsFragment = SocialSettingFragment()
        val socialRequestsFragment = SocialRequestsFragment()

        binding.mainMenuNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_home -> makeCurrentNavView(homeFragment)
                R.id.bottom_menu_profile -> makeCurrentNavView(profileFragment)
                R.id.bottom_menu_back ->{
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                R.id.bottom_menu_requests -> makeCurrentNavView(socialRequestsFragment)
                else -> makeCurrentNavView(homeFragment)
            }
            true
        }
    }

    private fun firstInitMain() {
        val fragment = SocialHomeFragment()
        binding.mainMenuNavigationView.background = null
        makeCurrentNavView(fragment)
    }

    private fun makeCurrentNavView(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.social_layout_fragment, fragment)
            commit()
        }
    }
}