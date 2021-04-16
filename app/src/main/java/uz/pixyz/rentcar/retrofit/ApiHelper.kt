package uz.pixyz.rentcar.retrofit

import retrofit2.Response
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.models.cars.Mark
import uz.pixyz.rentcar.models.registration.User

interface ApiHelper {

    suspend fun getCars(): Response<List<Car>>

    suspend fun getMarks(): Response<List<Mark>>

    suspend fun getInMarkCar(slug: String): Response<List<Car>>

    suspend fun getRegister(user: User):Response<User>

    suspend fun getLogin(user: User):Response<User>

}