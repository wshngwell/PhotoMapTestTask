package com.example.map.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.map.R
import com.example.map.databinding.ActivityAutorizationBinding

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAutorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.loginAndRegisterFragmentsContainer.id, LoginFragment())
            .commit()

        binding.loginLabel.setOnClickListener {
            val loginResId = R.color.white
            val registerResId = android.R.color.holo_green_light

            binding.loginLine.background = ContextCompat.getDrawable(this, loginResId)
            binding.registerLine.background = ContextCompat.getDrawable(this, registerResId)
            supportFragmentManager.beginTransaction()
                .replace(binding.loginAndRegisterFragmentsContainer.id, LoginFragment())
                .commit()
        }
        binding.registerLabel.setOnClickListener {
            val loginResId = android.R.color.holo_green_light
            val registerResId = R.color.white

            binding.loginLine.background = ContextCompat.getDrawable(this, loginResId)
            binding.registerLine.background = ContextCompat.getDrawable(this, registerResId)

            supportFragmentManager.beginTransaction()
                .replace(binding.loginAndRegisterFragmentsContainer.id, RegisterFragment())
                .commit()
        }

    }
}