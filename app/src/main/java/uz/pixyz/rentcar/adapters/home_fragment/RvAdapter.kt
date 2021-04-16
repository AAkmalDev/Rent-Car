package uz.pixyz.rentcar.adapters.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import uz.pixyz.rentcar.databinding.ItemCarBinding
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.models.cars.CarType

class RvAdapter(private var carTypeList: ArrayList<CarType>):RecyclerView.Adapter<RvAdapter.VH>() {

    private lateinit var itemCarBinding: ItemCarBinding
    inner class VH(itemCarBinding: ItemCarBinding):RecyclerView.ViewHolder(itemCarBinding.root){
        fun onBind(carType: CarType){
            itemCarBinding.textCarType.text = carType.typeName
            itemCarBinding.recyclerCarType.layoutManager = LinearLayoutManager(itemCarBinding.root.context,LinearLayoutManager.HORIZONTAL,false)
            val snapHelper = LinearSnapHelper()
            itemCarBinding.recyclerCarType.onFlingListener = null
            snapHelper.attachToRecyclerView(itemCarBinding.recyclerCarType)
            val inRvAdapter = InRvAdapter(carType.carList!! as ArrayList<Car>)
            itemCarBinding.recyclerCarType.adapter = inRvAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        itemCarBinding = ItemCarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(itemCarBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(carTypeList[position])
    }

    override fun getItemCount(): Int {
        return carTypeList.size
    }
    fun addData(carType: ArrayList<CarType>){
        carTypeList.addAll(carType)
    }
}