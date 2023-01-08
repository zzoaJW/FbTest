package com.z0o0a.fbtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
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

            binding.loadingLayout.visibility = View.VISIBLE
            this.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            loginAccount(inputId, inputPwd)
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
                    this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.loadingLayout.visibility = View.GONE

                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    intent = Intent(this, UserPage::class.java)
                    startActivity(intent)
                } else {
                    this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.loadingLayout.visibility = View.GONE

                    if(task.exception is FirebaseNetworkException){
                        Toast.makeText(this, "네트워크를 확인해주세요.", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else{
            Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
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
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> { // 이미 가입된 이메일인 경우 (이메일이 식별자임)
                            Toast.makeText(this, "이미 가입된 이메일입니다.", Toast.LENGTH_LONG).show()
                        }
                        is FirebaseAuthWeakPasswordException -> { // 비밀번호가 6자 미만인 경우
                            Toast.makeText(this, "비밀번호를 6자 이상으로 설정해주세요.", Toast.LENGTH_LONG).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> { // 이메일 형식이 올바르지 않은 경우
                            Toast.makeText(this, "이메일 형식을 확인해주세요.", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else{
            Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
        }
    }

}