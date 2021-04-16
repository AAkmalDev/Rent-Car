package uz.pixyz.rentcar.models

import java.io.Serializable

data class FilterData(
    var startPrice: Double? = null,
    var endPrice: Double? = null,
    var turboMotor: Boolean? = null,
    var engineVolume: Double? = null,
    var avtomatmanual: Boolean? = null
):Serializable
