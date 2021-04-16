package uz.pixyz.rentcar.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.pixyz.rentcar.models.registration.User
import uz.pixyz.rentcar.retrofit.ApiHelper
import uz.pixyz.rentcar.utils.Resource
import java.lang.Exception

class LoginViewModel(private val apiHelper: ApiHelper) : ViewModel() {


    private val userLogin = MutableLiveData<Resource<User>>()

    fun loadData(user: User) {
        GlobalScope.launch {
            userLogin.postValue(Resource.loading(null))
            try {
                val login = apiHelper.getLogin(user)
                if (login.isSuccessful) {
                    userLogin.postValue(Resource.success(login.body()))
                } else if (login.code() == 401) {
                    userLogin.postValue(Resource.error(null,"Parol yoki raqam xato"))
                }
            } catch (e: Exception) {
                userLogin.postValue(Resource.error(null, e.message!!))
            }
        }
    }

    fun getLoginUser(): MutableLiveData<Resource<User>> {
        return userLogin
    }

}