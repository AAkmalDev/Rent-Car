package uz.pixyz.rentcar.ui.chosecar

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.adapters.DetailsRvAdapter
import uz.pixyz.rentcar.databinding.FragmentChoseCarBinding
import uz.pixyz.rentcar.models.FilterData
import uz.pixyz.rentcar.models.cars.Car
import uz.pixyz.rentcar.retrofit.ApiClient.getRetrofit
import uz.pixyz.rentcar.retrofit.ApiHelperImpl
import uz.pixyz.rentcar.retrofit.ApiService
import uz.pixyz.rentcar.utils.MySharedPreferences
import uz.pixyz.rentcar.utils.Status
import uz.pixyz.rentcar.utils.ViewModelFactory

class ChoseCarFragment : Fragment(), View.OnClickListener {
    private lateinit var choseCarBinding: FragmentChoseCarBinding
    private var viewModel: ChoseCarViewModel? = null
    private var detailsRvAdapter: DetailsRvAdapter? = null
    private var carList: List<Car>? = null
    private var filterData: FilterData? = null
    private var startPrice: Int? = null
    private var endPrice: Int? = null
    private var turboMotor: Boolean? = null
    private var chip: Chip? = null
    private var slug: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        choseCarBinding = FragmentChoseCarBinding.inflate(inflater)


        MySharedPreferences.init(requireContext())

        slug = arguments?.getString("markslug")
        setupUi()
        setupViewModel()
        setupObserver()
        filterData =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<FilterData>("key")

        startPrice = filterData?.startPrice?.toInt()
        endPrice = filterData?.endPrice?.toInt()
        turboMotor = filterData?.turboMotor
        if (turboMotor == true && turboMotor != null) {
            chipsGroup("Turbo Motor")
        }
        if (startPrice != null || endPrice != null) {
            if (startPrice!! > 0 || endPrice!! > 0) {
                chipsGroup("${startPrice}-${endPrice}").setOnCloseIconClickListener {
                    startPrice = null
                    endPrice = null
                }
            }
        }

        choseCarBinding.filterText.setOnClickListener {
            Navigation.findNavController(choseCarBinding.root)
                .navigate(R.id.action_choseCarFragment_to_filterFragment)
        }

        val choseCar = SpannableString("Chose a car")
        choseCar.setSpan(StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        choseCar.setSpan(StyleSpan(Typeface.NORMAL), 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        choseCarBinding.textChoseACar.text = choseCar

        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)

        backArrow.setOnClickListener {
            findNavController().popBackStack()
            val toolbarText = (activity as MainActivity).findViewById<TextView>(R.id.toolbar_text_1)
            toolbarText.visibility = View.VISIBLE
            val location = (activity as MainActivity).findViewById<LinearLayout>(R.id.location)
            val dataToolbar =
                (activity as MainActivity).findViewById<LinearLayout>(R.id.data_toolbar)
            location.visibility = View.GONE
            dataToolbar.visibility = View.GONE
            backArrow.visibility = View.GONE
        }

        return choseCarBinding.root
    }

    private fun setupUi() {

        detailsRvAdapter =
            DetailsRvAdapter(arrayListOf(), object : DetailsRvAdapter.OnDetailClickListener {
                override fun onItemDetailClick(car: Car, position: Int) {
                    val bundle = Bundle()
                    bundle.putSerializable("choseCar", car)
                    findNavController().navigate(R.id.action_choseCarFragment_to_detalisFragment, bundle)
                }

                override fun onItemRentNowClick(car: Car) {
                    if (MySharedPreferences.phone != "") {
                        findNavController().navigate(R.id.action_choseCarFragment_to_orderFragment)
                    } else {
                        findNavController().navigate(R.id.action_choseCarFragment_to_loginFragment)
                    }
                }
            })
        choseCarBinding.recycler2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        choseCarBinding.recycler2.adapter = detailsRvAdapter
    }

    private fun setupObserver() {
        viewModel!!.loadData(slug!!)
        viewModel!!.getCars().observe(viewLifecycleOwner, {
            if (filterData == null) {
                when (it.status) {
                    Status.SUCCESS -> {
                        carList = ArrayList()
                        choseCarBinding.progressBarChose.visibility = View.GONE
                        it.data.let { cars ->
                            renderList(cars!!)
                            carList = cars
                        }
                        choseCarBinding.recycler2.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        choseCarBinding.progressBarChose.visibility = View.VISIBLE
                        choseCarBinding.recycler2.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        choseCarBinding.progressBarChose.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "XAXAXAXAXAX O'xshamadimi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                renderList(
                    filterData(
                        startPrice!!.toDouble(), endPrice!!.toDouble(), turboMotor!!,
                        carList as ArrayList<Car>
                    )
                )
            }
        })
    }

    private fun renderList(users: List<Car>) {
        detailsRvAdapter!!.addData(users)
        detailsRvAdapter!!.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        val apiService: ApiService = getRetrofit(requireContext()).create(ApiService::class.java)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(ApiHelperImpl(apiService)))
                .get(ChoseCarViewModel::class.java)
    }


    private fun chipsGroup(str: String): Chip {
        chip = Chip(requireContext())
        chip!!.text = str
        chip!!.isCloseIconVisible = true
        chip!!.isCheckable = false
        chip!!.isClickable = false
        chip!!.setOnCloseIconClickListener(this)
        choseCarBinding.chipgroup.addView(chip)
        return chip!!
    }

    private fun filterData(
        start: Double?,
        end: Double?,
        turbo: Boolean,
        carList: List<Car>
    ): ArrayList<Car> {
        val filterList = ArrayList<Car>()
        for (a in carList) {
            if (a.price_day.toDouble() <= end!!) {
                if (a.price_day.toDouble() > start!!) {
                    if (turbo) {
                        if (a.is_turbo_engine) {
                            filterList.add(a)
                        }
                    } else {
                        filterList.add(a)
                    }
                }
            } else if (end == 0.0) {
                if (a.price_day.toDouble() > start!!) {
                    if (turbo) {
                        if (a.is_turbo_engine) {
                            filterList.add(a)
                        }
                    } else {
                        filterList.add(a)
                    }
                }
            }
        }
        return filterList
    }

    override fun onDestroy() {
        super.onDestroy()
        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)
        val toolbarText = (activity as MainActivity).findViewById<TextView>(R.id.toolbar_text_1)
        toolbarText.visibility = View.VISIBLE
        val location = (activity as MainActivity).findViewById<LinearLayout>(R.id.location)
        val dataToolbar = (activity as MainActivity).findViewById<LinearLayout>(R.id.data_toolbar)
        location.visibility = View.GONE
        dataToolbar.visibility = View.GONE
        backArrow.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        val chip = v as Chip
        choseCarBinding.chipgroup.removeView(chip)


    }

}