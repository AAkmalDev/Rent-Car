package uz.pixyz.rentcar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.databinding.FragmentFilterBinding
import uz.pixyz.rentcar.models.FilterData

class FilterFragment : Fragment() {

    private lateinit var filterBinding: FragmentFilterBinding
    private var start: Double? = null
    private var end: Double? = null
    private var priceCarStart: String? = null
    private var priceCarEnd: String? = null
    private var turbo = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        filterBinding = FragmentFilterBinding.inflate(inflater)

        val turboEngine = filterBinding.turboEngine

        turboEngine.setOnCheckedChangeListener { _, isChecked -> turbo = isChecked }

        filterBinding.filterButton.setOnClickListener {
            priceCarStart = filterBinding.priceCarStart.text.toString()
            priceCarEnd = filterBinding.priceCarEnd.text.toString()
            start = if (priceCarStart == "") 0.0 else priceCarStart!!.toDouble()
            end = if (priceCarEnd == "") 0.0 else priceCarEnd!!.toDouble()

            val filterData = FilterData(start, end, turbo)

            findNavController().previousBackStackEntry?.savedStateHandle?.set("key", filterData)
            findNavController().popBackStack()
        }

        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)

        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        return filterBinding.root
    }
}