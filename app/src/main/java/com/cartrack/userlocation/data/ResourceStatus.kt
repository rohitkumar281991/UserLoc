package com.cartrack.userlocation.data

/**
 * This is for Repository classes where they return LiveData<Resource<T>> to pass back latest
 * data to UI with its fetch status.
 */
enum class ResourceStatus {
    SUCCESS,
    ERROR,
    LOADING
}