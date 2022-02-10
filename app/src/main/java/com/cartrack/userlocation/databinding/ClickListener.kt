package com.cartrack.userlocation.databinding

import android.view.View
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList

interface ClickListener {
    fun onClick(view: View?)
    fun onRecyclerViewClick(view:UserInfoRepositoryList?)
    fun getLocation()
}