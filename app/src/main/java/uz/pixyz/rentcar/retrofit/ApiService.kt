package uz.pixyz.rentcar.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.models.cars.Mark
import uz.pixyz.rentcar.models.registration.User

interface ApiService {

    @GET("cars/")
    suspend fun getCars(): Response<List<Car>>

    @GET("marks/")
    suspend fun getMarks(): Response<List<Mark>>

    @GET("marks/{slug}/")
    suspend fun getInMarkCar(@Path("slug") slug: String): Response<List<Car>>

    @POST("register/")
    suspend fun getRegister(@Body user: User): Response<User>

    @POST("token/")
    suspend fun getLogin(@Body user: User):Response<User>

}