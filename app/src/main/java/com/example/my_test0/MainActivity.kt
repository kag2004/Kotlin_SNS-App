package com.example.my_test0

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null

    var googleSignInClient : GoogleSignInClient? = null

    var GOOGLE_LOGIN_CODE = 9001
    var callbackManager: CallbackManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            createAndLoginEmail()
    }

    google_sign_in_button.setOnClickListener {
        googleLogin()
    }

    facebook_login_button.setOnClickListener {
        facebookLogin()
    }

    //      default_web_client_id => 구글 로그인 접근 할수 있는 키 토큰 키 생각하면 됨
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    googleSignInClient = GoogleSignIn.getClient(this,gso)
    callbackManager = CallbackManager.Factory.create()  // 초기화 부분

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

    //로그인시 펑션을 받는 쪽도 필요함
    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }

    // 파이어 베이스는 다른 플래폼이라 파이어 베이스에 넘겨주는 코드를 넣기 위한 곳
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
         var credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth?.signInWithCredential(credential)
    }

    fun facebookLogin(){
        LoginManager
            .getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))
        LoginManager
            .getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                // 로그인 완료시
                override fun onSuccess(result: LoginResult?) {
                    handleFacebookAccessToken(result?.accessToken)
                }
                //로그인 실패시
                override fun onCancel() {

                }
                override fun onError(error: FacebookException?) {

                }
            })
    }
    //페이스북 로그인 하고 난 후 뭐가 로그인 됐는지 롤백 하기 위한 장소
    fun handleFacebookAccessToken(token: AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                moveMainPage(auth?.currentUser)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        moveMainPage(auth?.currentUser)
    }

    //페이스북 , 구글 등 여러개 펑션이 가져 와야해서 구분 해줘야 한다 그건 fun XXX에서 넘겨 줄때 적어서 보냄
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                var account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
        }
    }
}
