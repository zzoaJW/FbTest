package com.z0o0a.fbtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.z0o0a.fbtest.databinding.UserPageBinding

class UserPage : AppCompatActivity() {
    private lateinit var binding: UserPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val user = Firebase.auth.currentUser

        binding.txtUserInfo.text = user!!.email

        binding.btnSaveDb.setOnClickListener {
            val input1 = binding.edittextName.text
            val input2 = binding.edittextAge.text
            val input3 = binding.edittextDesc.text
        }

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}