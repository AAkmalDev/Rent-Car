package uz.pixyz.rentcar.adapters.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.databinding.ItemCarLogoBinding
import uz.pixyz.rentcar.models.cars.Mark

class LogoRvAdapter(val markList: ArrayList<Mark>, private val listener: OnClickLogoListener) :
    RecyclerView.Adapter<LogoRvAdapter.VHLogo>() {


    private lateinit var itemCarLogoBinding: ItemCarLogoBinding
    private var lastPosition = -1

    inner class VHLogo(itemCarLogoBinding: ItemCarLogoBinding) :
        RecyclerView.ViewHolder(itemCarLogoBinding.root) {
        fun onBind(mark: Mark) {
            Glide.with(itemCarLogoBinding.root).load(mark.icon)
                .into(itemCarLogoBinding.imageLogoCar)
        }


        fun onClick(mark: Mark, position: Int) {
//            itemCarLogoBinding.imageLogoCar.setBackgroundResource(R.drawable.logo_color_back_not_clicked)
            itemCarLogoBinding.root.setOnClickListener {
                listener.onItemClick(mark, position)
//                itemCarLogoBinding.imageLogoCar.setBackgroundResource(R.drawable.logo_color_back)
//                setItemChanged(position)
//                notifyItemChanged(position)
            }
        }
    }

    fun setItemChanged(position: Int) {
        if (lastPosition != -1) {
            notifyItemChanged(lastPosition)
        }
        lastPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHLogo {
        itemCarLogoBinding =
            ItemCarLogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHLogo(itemCarLogoBinding)
    }

    override fun onBindViewHolder(holder: VHLogo, position: Int) {
        holder.onBind(markList[position])
        holder.onClick(markList[position], position)

        lastPosition = holder.adapterPosition

    }

    override fun getItemCount(): Int {
        return markList.size
    }

    interface OnClickLogoListener {
        fun onItemClick(mark: Mark, position: Int)
    }

    fun addData(list: List<Mark>) {
        markList.addAll(list)
    }

}