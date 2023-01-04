package com.z0o0a.fbtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.fbtest.databinding.UserPageBinding

class UserPage : AppCompatActivity() {
    private lateinit var binding: UserPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}