package uz.pixyz.rentcar.ui.chosecar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.IOException
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.retrofit.ApiHelper
import uz.pixyz.rentcar.utils.Resource

class ChoseCarViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private val carList = MutableLiveData<Resource<List<Car>>>()

    fun loadData(slug: String) {
        GlobalScope.launch {
            carList.postValue(Resource.loading(null))
            try {
                val inMarkCar = apiHelper.getInMarkCar(slug)
                if (inMarkCar.isSuccessful) {
                    carList.postValue(Resource.success(inMarkCar.body()))
                }
            } catch (e: Exception) {
                carList.postValue(Resource.error(null, e.message!!))
            } catch (e: IOException) {
                carList.postValue(Resource.error(null,e.message!!))
            }
        }
    }

    fun getCars(): LiveData<Resource<List<Car>>> {
        return carList
    }
}