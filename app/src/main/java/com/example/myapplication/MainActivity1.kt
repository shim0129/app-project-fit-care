package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity1 : AppCompatActivity() {

    private var auth: FirebaseAuth? = null // 파이어베이스 인증

    override fun onCreate(savedInstanceState: Bundle?) { // 앱이 최초 실행 시 수행.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1) // xml 화면 뷰를 연결 한다

        auth = FirebaseAuth.getInstance()

        val signin: Button = findViewById(R.id.login)

        // 회원가입 창으로
        val signup: Button = findViewById(R.id.btn_apply)

        signup.setOnClickListener {
            val intent1 = Intent(this, SignUp::class.java)
            startActivity(intent1)
        }

        // 로그인 버튼
        signin.setOnClickListener {
            val getemail = findViewById<EditText>(R.id.username).text.toString()
            val getpassword = findViewById<EditText>(R.id.password).text.toString()
            signIn(getemail, getpassword)
        }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    // 로그인
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "로그인에 성공 하였습니다.", Toast.LENGTH_LONG).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(baseContext, "로그인에 실패 하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }
    }
}

