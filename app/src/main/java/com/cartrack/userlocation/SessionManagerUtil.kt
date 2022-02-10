package com.cartrack.userlocation

import android.content.Context
import android.util.Log

object SessionManagerUtil {
    private const val TAG = "SessionManagerUtil"
    private const val SESSION_PREFERENCES = "com.cartrack.userlocation.SESSION_PREFERENCES"
    private const val LOGIN_ACTIVE = "login_active"

    fun startUserSession(context: Context) {
        Log.d(TAG,"startSession")
        val editor = context.applicationContext.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.putBoolean(LOGIN_ACTIVE, true)
        editor.apply()
    }

    fun isSessionActive(context: Context) : Boolean {
        Log.d(TAG,"isSessionActive "+context.getSharedPreferences(SESSION_PREFERENCES, 0).getBoolean(LOGIN_ACTIVE,false))
        return context.applicationContext.getSharedPreferences(SESSION_PREFERENCES, 0).getBoolean(LOGIN_ACTIVE,false)
    }

    fun endUserSession(context: Context) {
        Log.d(TAG,"EndSession")
        val editor = context.applicationContext.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.clear()
        editor.apply()
    }

}