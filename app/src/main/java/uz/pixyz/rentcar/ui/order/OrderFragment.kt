package uz.pixyz.rentcar.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var orderBinding: FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        orderBinding = FragmentOrderBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)

        backArrow.setOnClickListener {
            Navigation.findNavController(orderBinding.root).popBackStack()
        }

        return orderBinding.root
    }
}