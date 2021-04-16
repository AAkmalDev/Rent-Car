package uz.pixyz.rentcar.ui.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uz.pixyz.rentcar.models.registration.User
import uz.pixyz.rentcar.retrofit.ApiHelper
import uz.pixyz.rentcar.utils.Resource
import java.lang.Exception

class RegisterViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private val registerUser = MutableLiveData<Resource<User>>()

    fun loadData(user: User) {
        GlobalScope.launch {
            registerUser.postValue(Resource.loading(null))
            try {
                val register = apiHelper.getRegister(user)
                if (register.isSuccessful) {
                    registerUser.postValue(Resource.success(register.body()))
                }
            } catch (e: Exception) {
                registerUser.postValue(Resource.error(null, e.message!!))
            }
        }
    }

    fun getRegister(): MutableLiveData<Resource<User>> {
        return registerUser
    }

}