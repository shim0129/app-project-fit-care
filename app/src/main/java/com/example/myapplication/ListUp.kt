package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ListUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_up)

        //데이터베이스 클래스 객체 생성
        val dao = ListDao()

        val regi = findViewById<Button>(R.id.bt_datareg)

        regi.setOnClickListener{

            val name = findViewById<EditText>(R.id.et_rename).text.toString() //운동 이름
            val time = findViewById<EditText>(R.id.et_retime).text.toString() //운동 시간
            val set = findViewById<EditText>(R.id.et_set).text.toString() //운동 셋트

            // 데이터 셋팅
            if (name.isEmpty() || time.isEmpty() || set.isEmpty()) {
                Toast.makeText(this, "등록할 정보가 부족합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val list = ListData("",R.drawable.pngtreecharacter_rope_skipping_motion_silhouette_3856250,name, time, set)

                dao.add(list)?.addOnSuccessListener {
                    Toast.makeText(this, "등록 성공", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "등록 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            val intent = Intent(applicationContext,ListActivity::class.java)
            startActivity(intent)
        }

    }
}