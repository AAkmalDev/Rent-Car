package uz.pixyz.rentcar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.pixyz.rentcar.databinding.PickUpItemBinding
import uz.pixyz.rentcar.models.Location

class LocationRvAdapter(private var locations: ArrayList<Location>) :
    RecyclerView.Adapter<LocationRvAdapter.LocationVH>() {

    private lateinit var locationBinding: PickUpItemBinding

    inner class LocationVH(locationBinding: PickUpItemBinding) :
        RecyclerView.ViewHolder(locationBinding.root) {
        fun onBind(location: Location) {
            locationBinding.pickUpName.text = location.locName
            locationBinding.pickUpDesc.text = location.locDesc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        locationBinding =
            PickUpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationVH(locationBinding)
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        holder.onBind(locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }
}