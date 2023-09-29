package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ListReset : AppCompatActivity() {

    private lateinit var sKey: String
    private lateinit var sName: String
    private lateinit var sTime: String
    private lateinit var sCount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_reset)

        //데이터베이스 객체
        val dao = ListDao()

        //데이터 null 체크
        if(intent.hasExtra("key")&&intent.hasExtra("name")&&
            intent.hasExtra("time")&&intent.hasExtra("count")){

            //데이터 담기
            sKey = intent.getStringExtra("key")!!
            sName = intent.getStringExtra("name")!!
            sTime = intent.getStringExtra("time")!!
            sCount = intent.getStringExtra("count")!!

            //데이터 보여주기
            val upName =findViewById<EditText>(R.id.et_rename)
            val upTime =findViewById<EditText>(R.id.et_retime)
            val upCount =findViewById<EditText>(R.id.et_set)

            upName.setText(sName)
            upTime.setText(sTime)
            upCount.setText(sCount)

            val btrereg = findViewById<Button>(R.id.bt_datareg)

            btrereg.setOnClickListener{

                val uName = upName.text.toString()
                val uTime = upTime.text.toString()
                val uCount = upCount.text.toString()

                //파라미터 셋팅
                val hasMap: HashMap<String, Any> = HashMap()
                hasMap["listProfile"] = R.drawable.pngtreecharacter_rope_skipping_motion_silhouette_3856250
                hasMap["listActName"] = uName
                hasMap["listTime"]=uTime
                hasMap["listCount"]=uCount

                //사용자 업데이트 이벤트
                dao.userUpdate(sKey,hasMap).addOnSuccessListener {

                    Toast.makeText(applicationContext,"수정 완료", Toast.LENGTH_SHORT).show()

                    //목록으로 이동
                    val intent = Intent(this@ListReset,ListActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext,"수정 실패",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}