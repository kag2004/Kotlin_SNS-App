package com.example.my_test0

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            createAndLoginEmail()
        }
    }

    //아이디 생성  (아이디 있을 경우와 없을 경우의 로직을 만들어 둠)
    fun createAndLoginEmail() {
        auth?.createUserWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(auth?.currentUser)
//                    Toast.makeText(this, "아이디 생성 완료 되었습니다.", Toast.LENGTH_LONG).show()
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                } else {
                    signinEmail()
                }
            }
    }

    //로그인 성공 했을때 (sign 로그인)
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(email_edittext.text.toString(), password_edittext.text.toString())
            ?.addOnCompleteListener { task ->
                //로그인 성공 했을때 task성공하면 로그인이 성공 됐다 생각 하면 됨
                if (task.isSuccessful) {
                    moveMainPage(auth?.currentUser)
//                    Toast.makeText(this, "로그인이 성공 했습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

    }
    fun moveMainPage(user :FirebaseUser?){
        if (user != null) {
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
    }
}
