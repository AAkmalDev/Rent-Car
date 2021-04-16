package uz.pixyz.rentcar.models.cars

data class Mark(
    val icon: String,
    val name: String,
    val slug: String,
    var isClick: Boolean = false
)