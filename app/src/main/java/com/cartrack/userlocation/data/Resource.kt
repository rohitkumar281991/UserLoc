package com.cartrack.userlocation.data

data class Resource<T, E>(val status: ResourceStatus, val data: T?, val error: E?) {
    companion object {
        fun <T, E> loading(data: T? = null): Resource<T, E> {
            return Resource(ResourceStatus.LOADING, data, null)
        }

        fun <T, E> success(data: T? = null): Resource<T, E> {
            return Resource(ResourceStatus.SUCCESS, data, null)
        }

        fun <T, E> error(error: E, data: T? = null): Resource<T, E> {
            return Resource(ResourceStatus.ERROR, data, error)
        }
    }
}