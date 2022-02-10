package com.cartrack.userlocation

object Constants {
    const val USER_LOCATION_DB = "user_location_db"
    const val USER_DETAIL_TABLE = "USER_DETAIL_TABLE"
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    enum class UserValidation {
        INVALID_USERNAME,
        INVALID_PASSWORD,
        SUCCESS
    }
}