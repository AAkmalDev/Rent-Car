package uz.pixyz.rentcar.models.cars

import uz.pixyz.rentcar.models.cars.Car

data class CarType( var typeName: String? = null,
                    var carList: List<Car>? = null) {
}