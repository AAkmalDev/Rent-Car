package uz.pixyz.rentcar.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.pixyz.rentcar.databinding.CarsDescItemBinding
import uz.pixyz.rentcar.models.cars.Car

class DetailsRvAdapter(var carList: ArrayList<Car>, var listener: OnDetailClickListener) :
    RecyclerView.Adapter<DetailsRvAdapter.VhDetails>() {

    lateinit var detailsBinding: CarsDescItemBinding

    inner class VhDetails(detailsBinding: CarsDescItemBinding) :
        RecyclerView.ViewHolder(detailsBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(car: Car) {
            detailsBinding.carName1.text = car.name
            detailsBinding.textCarPrice.text = car.price_day
            detailsBinding.maxspeedValue.text = "${car.max_speed} km/h"
            detailsBinding.textSpeedToSec.text = "${car.speed_to_100} sec"
            detailsBinding.textEngineValue.text = "${car.engine_volume}"
            detailsBinding.textHorsePower.text = car.horsepower.toString()
            Glide.with(detailsBinding.root).load(car.images[0].image).into(detailsBinding.imageCar)
        }

        fun onDetailClick(car: Car, position: Int) {
            detailsBinding.detalLinearLayout.setOnClickListener {
                listener.onItemDetailClick(car, position)
            }
        }

        fun onRentNowClick(car: Car) {
            detailsBinding.detalRentNow.setOnClickListener {
                listener.onItemRentNowClick(car)
            }
        }
    }

    fun addData(cars: List<Car>) {
        carList.addAll(cars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhDetails {
        detailsBinding =
            CarsDescItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VhDetails(detailsBinding)
    }

    override fun onBindViewHolder(holder: VhDetails, position: Int) {
        holder.onBind(carList[position])
        holder.onDetailClick(carList[position], position)
        holder.onRentNowClick(carList[position])

    }

    override fun getItemCount(): Int {
        return carList.size
    }

    interface OnDetailClickListener {
        fun onItemDetailClick(car: Car, position: Int)
        fun onItemRentNowClick(car: Car)
    }

}