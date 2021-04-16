package uz.pixyz.rentcar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.models.cars.Mark
import uz.pixyz.rentcar.retrofit.ApiHelper
import uz.pixyz.rentcar.utils.Resource
import java.io.IOException
import java.lang.Exception

class HomeViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private val carList = MutableLiveData<Resource<List<Car>>>()
    private val markList = MutableLiveData<Resource<List<Mark>>>()

    init {
        loadCarData()
    }

    init {
        loadMarkData()
    }

    private fun loadMarkData() {
        GlobalScope.launch {
            markList.postValue(Resource.loading(null))
            try {
                val marks = apiHelper.getMarks()
                if (marks.isSuccessful){
                    markList.postValue(Resource.success(marks.body()))
                }
            } catch (e: Exception) {
                markList.postValue(Resource.error(null, e.message!!))
            } catch (e: IOException) {
                markList.postValue(Resource.error(null, e.message!!))
            }
        }
    }

    private fun loadCarData() {
        GlobalScope.launch {
            carList.postValue(Resource.loading(null))
            try {
                val cars = apiHelper.getCars()
                if (cars.isSuccessful) {
                    carList.postValue(Resource.success(cars.body()))
                }
            } catch (e: Exception) {
                carList.postValue(Resource.error(null, e.message!!))
            }
        }
    }

    fun getCarData(): LiveData<Resource<List<Car>>> {
        return carList
    }

    fun getMarkData(): LiveData<Resource<List<Mark>>> {
        return markList
    }


}