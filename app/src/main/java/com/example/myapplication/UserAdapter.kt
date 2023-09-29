package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter (val context: Context, val UserList: ArrayList<User>) : BaseAdapter()
{
    override fun getCount(): Int { //리스트 개수
        return UserList.size
    }

    override fun getItem(position: Int): Any {
        return UserList[position]
    }

    override fun getItemId(position: Int): Long { //이 실습에서는 사용 안해서 0으로 줌
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_user, null)
        //뷰를 붙일 때 LayoutInflater 사용한다.
        val profile = view.findViewById<ImageView>(R.id.iv_lift)
        val name = view.findViewById<TextView>(R.id.tv_name)
        val time = view.findViewById<TextView>(R.id.tv_time)
        val count = view.findViewById<TextView>(R.id.tv_count)

        val user = UserList[position]  //position은 0부터 센다.

        profile.setImageResource(user.profile)

        name.text = user.name
        time.text = user.time.toString() + " min"
        count.text = user.count.toString() + " set"

        return view //마지막에 꼭 넣어야함
    }

}