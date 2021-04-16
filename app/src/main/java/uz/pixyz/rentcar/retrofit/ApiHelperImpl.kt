package uz.pixyz.rentcar.retrofit

import retrofit2.Response
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.models.cars.Mark
import uz.pixyz.rentcar.models.registration.User

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getCars(): Response<List<Car>> = apiService.getCars()

    override suspend fun getMarks(): Response<List<Mark>> = apiService.getMarks()

    override suspend fun getInMarkCar(slug: String): Response<List<Car>> = apiService.getInMarkCar(slug)

    override suspend fun getRegister(user: User): Response<User> = apiService.getRegister(user)

    override suspend fun getLogin(user: User): Response<User> = apiService.getLogin(user)
}