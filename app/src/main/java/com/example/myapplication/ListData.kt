package com.example.myapplication

data class ListData(
    var userKey: String,
    var listProfile: Int,
    var listActName: String,
    var listTime: String,
    var listCount: String
){
    constructor(): this("",R.drawable.pngtreecharacter_rope_skipping_motion_silhouette_3856250,"","","")
}
