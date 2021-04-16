package uz.pixyz.rentcar.adapters.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.pixyz.rentcar.databinding.ItemCarTypeBinding
import uz.pixyz.rentcar.models.cars.Car

class InRvAdapter(var carList: ArrayList<Car>) : RecyclerView.Adapter<InRvAdapter.VH>() {
    private lateinit var itemCarType: ItemCarTypeBinding

    inner class VH(itemCarTypeBinding: ItemCarTypeBinding) : RecyclerView.ViewHolder(itemCarTypeBinding.root) {
        fun onBind(car: Car) {
            Glide.with(itemCarType.root).load(car.images[0].image).into(itemCarType.carImage)
            itemCarType.textCarName.text = car.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        itemCarType = ItemCarTypeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(itemCarType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(carList[position])
    }

    override fun getItemCount(): Int {
        return carList.size
    }
}