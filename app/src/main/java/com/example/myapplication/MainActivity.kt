package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {// 전역 변수로 바인딩 객체 선언

    private var auth: FirebaseAuth? = null // 파이어베이스 인증

    override fun onCreate(savedInstanceState: Bundle?) { // 앱이 최초 실행 시 수행.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //xml 화면 뷰를 연결 한다

        auth = FirebaseAuth.getInstance()

        val next: Button =findViewById(R.id.btn_next)

        next.setOnClickListener{
            //var: 변수 값이 언제든지 변할 수 있음
            //val: 변수 값 불변
            val intent = Intent(this,MainActivity1::class.java ) //다음 화면으로 이동하기 위한 인텐트 객체 생성.
//            intent.putExtra("msg",)
            startActivity(intent)
            finish()
        }
    }
    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }
    }
}
