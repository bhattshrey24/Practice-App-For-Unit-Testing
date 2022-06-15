package com.example.practiceappforunittesting.other


// This is a very usefull class that help us to manage network calls efficiently , Since its generic therefore it will act as a wrapping class for any class and by wrapping it over to object or another class we an add these functionalities of checking network status to it
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> { // these functions return the data wrapped with their status and error message if there is any
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}