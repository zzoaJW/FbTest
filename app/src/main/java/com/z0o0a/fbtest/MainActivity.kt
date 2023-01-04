package com.z0o0a.fbtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.z0o0a.fbtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val inputId = binding.edittextId.text.toString()
            val inputPwd = binding.edittextPwd.text.toString()

            val loginResult = loginAccount(inputId, inputPwd)
        }

        binding.btnRegister.setOnClickListener {
            // 입력한 id, pwd로 회원가입
            val inputId = binding.edittextId.text.toString()
            val inputPwd = binding.edittextPwd.text.toString()

            // Firebase에 회원가입 정보 보내기
            registerAccount(inputId, inputPwd)
        }

    }

    private fun loginAccount(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                    loginAccount(email, password)

                    intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}