package uz.pixyz.rentcar.dp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.pixyz.rentcar.models.registration.CURRENT_USER_ID
import uz.pixyz.rentcar.models.registration.User

@Dao
interface UserDao {

    @Insert
    fun setUser(user: User)

    @Query("SELECT * FROM user WHERE id = $CURRENT_USER_ID")
    fun getUser():User

    @Query("SELECT EXISTS (SELECT 1 FROM user WHERE id = :id)")
    fun exists(id: Int): Boolean

}