package uz.pixyz.rentcar.models.order

import java.io.Serializable

data class Order(
    val car: Int? = null,
    val day_or_hour: Int? = null,
    val type_lifetime: String? = null,
    val type_order: String? = null
):Serializable