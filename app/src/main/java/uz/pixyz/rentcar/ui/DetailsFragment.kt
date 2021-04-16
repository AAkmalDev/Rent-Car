package uz.pixyz.rentcar.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.adapters.ViewPagerAdapter
import uz.pixyz.rentcar.databinding.FragmentDetalisBinding
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.utils.MySharedPreferences

class DetailsFragment : Fragment() {

    private lateinit var detailsBinding: FragmentDetalisBinding
    private var viewPagerAdapter: ViewPagerAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentDetalisBinding.inflate(inflater)

        MySharedPreferences.init(requireContext())

        val car = arguments?.getSerializable("choseCar") as Car

        detailsBinding.detalCarName.text = car.name
        detailsBinding.detalCarPrice.text = car.price_day
        detailsBinding.detalCarEngine.text = "${car.engine_volume}"
        detailsBinding.detalCarPower.text = "${car.horsepower} hp"
        detailsBinding.detalCarSec.text = "${car.speed_to_100} sec"
        detailsBinding.detalCarMaxspeed.text = "${car.max_speed} km/s"
        val viewPager = detailsBinding.viewPager
        val tabLayout = detailsBinding.tabLayout

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, car.images)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)

        backArrow.setOnClickListener {
            Navigation.findNavController(detailsBinding.root).popBackStack()
        }

        detailsBinding.rentCarLiner.setOnClickListener {
            if (MySharedPreferences.phone != "") {
                findNavController().navigate(R.id.action_detalisFragment_to_orderFragment)
            }else{
                findNavController().navigate(R.id.action_detalisFragment_to_loginFragment)
            }
        }

        return detailsBinding.root
    }
}