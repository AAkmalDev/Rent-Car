package uz.pixyz.rentcar.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.pixyz.rentcar.retrofit.ApiHelper
import uz.pixyz.rentcar.ui.auth.login.LoginViewModel
import uz.pixyz.rentcar.ui.auth.register.RegisterViewModel
import uz.pixyz.rentcar.ui.chosecar.ChoseCarViewModel
import uz.pixyz.rentcar.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoseCarViewModel::class.java)) {
            return ChoseCarViewModel(apiHelper) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(apiHelper) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(apiHelper) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(apiHelper) as T
        }
        throw IllegalArgumentException("String")
    }
}