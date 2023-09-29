package com.example.myapplication

import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class ListDao {
    private  var databaseReference: DatabaseReference?=null

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("ListData")
    }//경로 지정

    //등록
    fun add(user: ListData): Task<Void>{
        return databaseReference!!.push().setValue(user)
    }
    //조회
    fun getUserList(): Query?{
        return databaseReference
    }
    //수정
    fun userUpdate(key: String, hashMap: HashMap<String,Any>): Task<Void>{
        return databaseReference!!.child(key)!!.updateChildren(hashMap)
    }
    //삭제
    fun userDelete(key: String): Task<Void>{
        return databaseReference!!.child(key).removeValue()
    }
}