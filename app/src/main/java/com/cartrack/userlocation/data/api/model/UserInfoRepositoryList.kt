package com.cartrack.userlocation.data.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.cartrack.userlocation.Constants
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = Constants.USER_DETAIL_TABLE)
data class UserInfoRepositoryList(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("email")
    var email: String? = null,

    var address: UserDetailsAddress? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("website")
    var website: String? = null,

    var company: UserDetailsCompany? = null,

    var password: String? = null
)

data class UserDetailsCompany(
    val name: String?,
    val catchPhrase: String?,
    val bs: String
)

data class UserDetailsGeo(
    val lat: String?,
    val lng: String?
)

data class UserDetailsAddress(
    val street: String? = null,
    val suite: String? = null,
    val city: String? = null,
    val zipcode: String? = null,
    val geo: UserDetailsGeo? = null
)

class TypeConverterAddress {
    val gson: Gson = Gson()

    @TypeConverter
    fun stringToObjectList(data: String?): UserDetailsAddress? {
        if (data == null) return null
        val listType: Type = object : TypeToken<UserDetailsAddress?>() {}.type
        return gson.fromJson<UserDetailsAddress?>(data, listType)
    }

    @TypeConverter
    fun objectToString(value: UserDetailsAddress?): String? {
        return gson.toJson(value)
    }
}

class TypeConverterCompany {
    val gson: Gson = Gson()

    @TypeConverter
    fun stringToObjectList(data: String?): UserDetailsCompany? {
        if (data == null) return null
        val listType: Type = object : TypeToken<UserDetailsCompany?>() {}.type
        return gson.fromJson<UserDetailsCompany?>(data, listType)
    }

    @TypeConverter
    fun objectToString(value: UserDetailsCompany?): String? {
        return gson.toJson(value)
    }
}

class TypeConverterLocation {
    val gson: Gson = Gson()

    @TypeConverter
    fun stringToObjectList(data: String?): UserDetailsGeo? {
        if (data == null) return null
        val listType: Type = object : TypeToken<UserDetailsGeo?>() {}.type
        return gson.fromJson<UserDetailsGeo?>(data, listType)
    }

    @TypeConverter
    fun objectToString(value: UserDetailsGeo?): String? {
        return gson.toJson(value)
    }
}
