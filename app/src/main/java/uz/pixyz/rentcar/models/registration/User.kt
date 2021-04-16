package uz.pixyz.rentcar.models.registration

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    val phone: String,
    val password: String,
    val password2: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = CURRENT_USER_ID
}

