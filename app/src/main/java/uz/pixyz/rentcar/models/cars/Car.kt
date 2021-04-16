package uz.pixyz.rentcar.models.cars

import java.io.Serializable

data class Car(
    val air_conditioner: Boolean,
    val color: String,
    val condition: String,
    val engine_volume: Double,
    val fuel_consumption: Double,
    val horsepower: Int,
    val id: Int,
    val images: List<Image>,
    val is_active: Boolean,
    val is_turbo_engine: Boolean,
    val mark: Mark,
    val max_speed: Int,
    val name: String,
    val passenger: Int,
    val price_day: String,
    val price_hour: String,
    val slug: String,
    val speed_to_100: Double,
    val transmission: String,
    val type: String,
    val year_of_issue: Int
):Serializable