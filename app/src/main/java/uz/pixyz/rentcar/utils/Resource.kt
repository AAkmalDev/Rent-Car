package uz.pixyz.rentcar.utils

data class Resource<T>(val status: Status, val data: T?, val error: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> error(data: T?, message: String): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }
    }
}