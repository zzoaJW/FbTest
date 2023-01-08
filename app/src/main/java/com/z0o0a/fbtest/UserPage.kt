package com.z0o0a.fbtest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.z0o0a.fbtest.databinding.UserPageBinding

class UserPage : AppCompatActivity() {
    private lateinit var binding: UserPageBinding
    private var db : FirebaseFirestore? = null
    private var user : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Firebase.firestore
        user = Firebase.auth.currentUser

        loadUserData()
        loadData()

        binding.btnSaveDocument.setOnClickListener {
            val input1 = binding.edittextName.text.toString()
            val input2 = binding.edittextYear.text.toString().toInt()
            val input3 = binding.edittextDesc.text.toString()

            saveDocument(input1, input2, input3)
        }

        binding.btnSaveCollection.setOnClickListener {
            val input1 = binding.edittextName.text.toString()
            val input2 = binding.edittextYear.text.toString().toInt()
            val input3 = binding.edittextDesc.text.toString()

            saveCollection(input1, input2, input3)
            loadData()
        }

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveDocument(input1:String, input2:Int, input3:String){
        val userData = hashMapOf(
            "name" to input1,
            "age" to input2,
            "desc" to input3
        )

        // users(컬렉션)에 user!!.uid(문서) 저장
        // user!!.uid(문서)의 필드에 userData 저장
        db!!.collection("users").document(user!!.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "데이터 저장 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveCollection(input1:String, input2:Int, input3:String){
        val temp = hashMapOf(
            "ripening period" to input2,
            "taste desc" to input3
        )


        db!!.collection("users").document(user!!.uid)
            .collection("tasting note").document(input1)
            .set(temp)
            .addOnSuccessListener {
                Toast.makeText(this, "데이터 저장 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "데이터 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserData(){
        binding.txtUserInfo.text = user!!.email
    }

    private fun loadData(){
        binding.loadingAnim.visibility = View.VISIBLE

        db!!.collection("users").document(user!!.uid)
            .collection("tasting note")
            .get()
            .addOnSuccessListener { result ->
                var allDocu = ""

                for (document in result) {
                    allDocu += "[ ${document.id} ]\n" +
                            "${document.data["ripening period"]}\n" +
                            "${document.data["taste desc"]}\n\n"
                }
                binding.txtUserDb.text = allDocu
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "데이터 불러오기 실패 : ${exception}", Toast.LENGTH_LONG).show()
            }

        binding.loadingAnim.visibility = View.GONE
    }
}