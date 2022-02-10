package com.cartrack.userlocation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cartrack.userlocation.Constants
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.LoginLayoutBinding
import com.cartrack.userlocation.ui.UserLocationFragment
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object Utility {
    const val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
    const val salt = "QWlGNHNhMTJTQWZ2bGhpV3U=" // base64 decode => AiF4sa12SAfvlhiWu
    const val iv = "bVQzNFNhRkQ1Njc4UUFaWA==" // base64 decode => mT34SaFD5678QAZX

    private const val TAG = "DataCheck"

    fun validation(binding: LoginLayoutBinding?): Constants.UserValidation {
        val result: Constants.UserValidation =
            if (binding!!.txtUserName.text.isNullOrEmpty() || !binding.txtUserName.text.isValidUserName()) {
                Constants.UserValidation.INVALID_USERNAME //todo: remove NOT from above line and below line
            } else if (binding.txtPassword.text.isNullOrEmpty() || !binding.txtPassword.text.isValidPassword()) {
                Constants.UserValidation.INVALID_PASSWORD
            } else {
                Constants.UserValidation.SUCCESS
            }
        return result
    }


    fun CharSequence.isValidPassword(): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{7,20}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(this)
//        return matcher.matches()
        return true //todo : uncomment above check
    }

    fun CharSequence.isValidUserName(): Boolean {
        val passwordPattern =
            "^[A-Za-z]\\\\w{7,29}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(this)
//        return matcher.matches()
        return true
        //todo : uncomment above check
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun encryptionFunction(strToEncrypt: String): String? {
        try {
            val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =
                PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.encodeToString(
                cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)),
                Base64.DEFAULT
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error while encrypting: $e")
        }
        return null
    }

    fun decryptionFunction(strToDecrypt: String): String? {
        try {

            val ivParameter = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =
                PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec);
            val secretKey = SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);
            return String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        } catch (e: Exception) {
            Log.e(TAG, "Error while decrypting: $e");
        }
        return null
    }

    fun getCountries(list: List<UserInfoRepositoryList>): MutableList<String> {
        val totalList: MutableList<String> = mutableListOf()
        list.forEach {
            totalList.add(it.address?.city!!)
        }
        return totalList
    }

    // function to check if GPS is on
    fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}