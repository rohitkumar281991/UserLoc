package com.cartrack.userlocation.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.cartrack.userlocation.R
import com.cartrack.userlocation.Status

object DialogManager {
    private const val TAG = "Ro_DialogManager"
    private val errorMessageMap = HashMap<Status, ErrorMessage>()

    private class ErrorMessage(
        private val title: Int,
        private val message: Int,
        private val arg: Int? = null
    ) {
        fun getTitle(context: Context?): String? {
            return if (title > 0) {
                context?.getString(title)
            } else {
                null
            }
        }

        fun getMessage(context: Context?): String? {
            return when {
                arg != null -> {
                    context?.getString(message, context.getString(arg))
                }
                message > 0 -> {
                    context?.getString(message)
                }
                else -> {
                    null
                }
            }
        }
    }

    init {
        errorMessageMap[Status.ERROR] = ErrorMessage(0, R.string.error_unknown)
        errorMessageMap[Status.ERROR_NO_NETWORK] =
            ErrorMessage(R.string.mobile_date_off, R.string.connect_to_wifi)
        errorMessageMap[Status.ERROR_NETWORK_TIMEOUT] =
            ErrorMessage(R.string.cannot_connect, R.string.something_went_wrong)
        errorMessageMap[Status.ERROR_INTERNAL_SERVER] =
            ErrorMessage(R.string.cannot_connect, R.string.something_went_wrong)
    }

    fun showErrorDialog(activity: Activity?, status: Status) {
        val errorMessage = errorMessageMap[status] ?: errorMessageMap[Status.ERROR]
        val title = errorMessage!!.getTitle(activity)
        val content = errorMessage.getMessage(activity)

        AlertDialog.Builder(activity)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .setMessage(content)
            .setTitle(title)
            .show()
    }

    fun showErrorDialog(activity: Activity?, status: String) {
        AlertDialog.Builder(activity)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .setMessage(status)
            .show()
    }
}














