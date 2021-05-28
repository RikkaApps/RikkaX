package rikka.lifecycle

import androidx.lifecycle.MutableLiveData

private val noError = Throwable("No error")

open class Resource<out T>(val status: Status, val data: T?, val error: Throwable) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, noError)
        }

        fun <T> error(error: Throwable, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, error)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, noError)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (status != other.status) return false
        if (data != other.data) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + error.hashCode()
        return result
    }

    override fun toString(): String {
        return "Resource(status=$status, data=$data, error=$error)"
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

open class ResourceLiveData<T> : MutableLiveData<Resource<T>> {

    constructor() : super()

    constructor(status: Status, value: T? = null, error: Throwable = noError) : super(Resource(status, value, error))

    fun setLoading(data: T? = null) {
        value = Resource.loading(data)
    }

    fun setSuccess(data: T? = null) {
        value = Resource.success(data)
    }

    fun setError(error: Throwable, data: T? = null) {
        value = Resource.error(error, data)
    }

    fun postLoading(data: T? = null) = postValue(Resource.loading(data))

    fun postSuccess(data: T? = null) = postValue(Resource.success(data))

    fun postError(error: Throwable, data: T? = null) = postValue(Resource.error(error, data))
}

open class NullableResourceLiveData<T> : MutableLiveData<Resource<T>?> {

    constructor() : super()

    constructor(status: Status, value: T? = null, error: Throwable = noError) : super(Resource(status, value, error))

    fun setLoading(data: T? = null) {
        value = Resource.loading(data)
    }

    fun setSuccess(data: T? = null) {
        value = Resource.success(data)
    }

    fun setError(error: Throwable, data: T? = null) {
        value = Resource.error(error, data)
    }

    fun postLoading(data: T? = null) = postValue(Resource.loading(data))

    fun postSuccess(data: T? = null) = postValue(Resource.success(data))

    fun postError(error: Throwable, data: T? = null) = postValue(Resource.error(error, data))
}
