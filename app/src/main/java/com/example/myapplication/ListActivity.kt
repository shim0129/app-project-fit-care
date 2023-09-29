package com.example.myapplication

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.myapplication.databinding.ActivityListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
class ListActivity: AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    var UserList = arrayListOf<User> (
        User("",R.drawable.training,"Dead Lift","3","5"),
        User("",R.drawable.lunges,"Lunges","3","5"),
        User("",R.drawable.exercise, "Cycle","20","2"),
        User("",R.drawable.weight_lifting,"Squat","5","5"),
        User("",R.drawable.gym,"Shoulder press","5","5")
    )

    private lateinit var binding: ActivityListBinding
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var dao: ListDao

    private val doubleClickDelay: Long = 300 // 더블 클릭 간격을 설정합니다. 여기서는 300ms로 설정했습니다.
    private var lastClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        binding = ActivityListBinding.inflate(layoutInflater)

        auth = Firebase.auth


        val list : ListView = findViewById(R.id.lv_list)

        //dao초기화
        dao = ListDao()

        // User 데이터를 저장할 ArrayList를 초기화하고 기존 데이터를 추가합니다.
        userList = ArrayList()
        userList.addAll(UserList) // 기존 리스트에 있는 데이터를 추가합니다.

        // userList를 사용하여 ListView 등의 뷰를 업데이트합니다.
        adapter = UserAdapter(this, userList)
        list.adapter = adapter


        //사용자 정보 가져오기
        getUserList()

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastClickTime

            if (elapsedTime <= doubleClickDelay) {
                deleteUser(position)
            }
            lastClickTime = currentTime
            true
        }

        list.onItemClickListener= AdapterView.OnItemClickListener{
                parent,v,position,id->
            val intent = Intent(applicationContext, TimerActivity::class.java)

            intent.putExtra("profile", userList[position].profile.toString())
            intent.putExtra("name", userList[position].name)
            intent.putExtra("time", userList[position].time)
            intent.putExtra("count", userList[position].count)

            startActivity(intent)
        }

        val listup = findViewById<Button>(R.id.bt_listreg)

        listup.setOnClickListener{
            val intent = Intent(this, ListUp::class.java)
            startActivity(intent)
        }

        val logout = findViewById<Button>(R.id.bt_out)

        logout.setOnLongClickListener{
            val intent =Intent(this,MainActivity1::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            auth?.signOut()
            true
        }

        list.setOnItemLongClickListener(AdapterView.OnItemLongClickListener { parent, view, position, id ->
            if (position >= 5) {
                val intent = Intent(this, ListReset::class.java)
                intent.putExtra("key", userList[position].key)
                intent.putExtra("name", userList[position].name)
                intent.putExtra("time", userList[position].time)
                intent.putExtra("count", userList[position].count)
                startActivity(intent)
                true
            } else {
                false
            }
        })

    } //onCreate

    private fun getUserList(){
        dao.getUserList()?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 데이터 변경 시 호출되는 콜백 메서드입니다.
                for (dataSnapshot in snapshot.children) {
                    val listData = dataSnapshot.getValue(ListData::class.java)
                    val key = dataSnapshot.key
                    listData?.userKey = key.toString()
                    if(listData != null){
                        val user = User(
                            listData.userKey,
                            listData.listProfile,
                            listData.listActName,
                            listData.listTime,
                            listData.listCount
                        )
                        userList.add(user)
                    }
                }

                // userList가 업데이트되었으므로 ListView 등의 뷰를 다시 업데이트합니다.
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기가 취소된 경우 호출되는 콜백 메서드입니다.
                Log.e("ListActivity", "Firebase 데이터베이스에서 데이터를 읽을 수 없습니다: ${error.message}")
            }
        })
    }

    private fun deleteUser(position: Int) {
        if (position >= 5) {
            val key = userList[position].key

            dao.userDelete(key)
                .addOnSuccessListener {
                    // 성공 이벤트
                    Toast.makeText(
                        this@ListActivity,
                        "삭제 성공",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    // 삭제 이벤트
                    Toast.makeText(
                        this@ListActivity,
                        "삭제 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}
