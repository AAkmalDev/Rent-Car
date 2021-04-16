package uz.pixyz.rentcar.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.MapsActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.adapters.LocationRvAdapter
import uz.pixyz.rentcar.adapters.home_fragment.LogoRvAdapter
import uz.pixyz.rentcar.adapters.home_fragment.RvAdapter
import uz.pixyz.rentcar.databinding.BottomSheetLayoutBinding
import uz.pixyz.rentcar.databinding.FragmentHomeBinding
import uz.pixyz.rentcar.databinding.SpinnerDateItemBinding
import uz.pixyz.rentcar.models.Location
import uz.pixyz.rentcar.models.cars.CarType
import uz.pixyz.rentcar.models.cars.Mark
import uz.pixyz.rentcar.retrofit.ApiClient
import uz.pixyz.rentcar.retrofit.ApiHelperImpl
import uz.pixyz.rentcar.retrofit.ApiService
import uz.pixyz.rentcar.utils.Status
import uz.pixyz.rentcar.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private var rvAdapter: RvAdapter? = null
    private var logoRvAdapter: LogoRvAdapter? = null
    private var locationList: ArrayList<Location>? = null
    private var locationRvAdapter: LocationRvAdapter? = null
    private var check = false
    private var startTime: String? = null
    private var endTime: String? = null
    private lateinit var carTypeList: ArrayList<CarType>
    private var viewModel: HomeViewModel? = null
    private var slug: String? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var dateFormat: SimpleDateFormat? = null
    private var day: Int? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater)
        setupUI()
        setupViewModel()
        setupCarObserve()
        setupMarkObserve()

        loadData()

        homeBinding.card3.setOnClickListener { calendarData(2) }
        homeBinding.card4.setOnClickListener { calendarData(1) }

        homeBinding.spinnerSheet.setOnClickListener {
            val bottomSheet = BottomSheetDialog(requireContext())
            val bottomSheetLayoutBinding: BottomSheetLayoutBinding =
                BottomSheetLayoutBinding
                    .inflate(LayoutInflater.from(requireContext()))
            bottomSheet.setContentView(bottomSheetLayoutBinding.root)
            locationRvAdapter = LocationRvAdapter(locationList!!)
            bottomSheetLayoutBinding.recyclerSheet.adapter = locationRvAdapter
            bottomSheet.show()
        }

        homeBinding.locationMark.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }


        val string = activity?.intent
        val stringExtra = string?.getStringExtra("maps")
        if (stringExtra == null) {
            homeBinding.textMapsLocation.text = "Pick up place"
        } else {
            homeBinding.textMapsLocation.text = stringExtra
        }

        homeBinding.switchOn.setOnCheckedChangeListener { _, isChecked ->
            if (check) {
                homeBinding.innerCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.logo_color_back)
                homeBinding.locationMark.isClickable = false
                homeBinding.spinnerSheet.isClickable = false
            } else {
                homeBinding.innerCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.logo_white_color_back)
                homeBinding.locationMark.isClickable = true
                homeBinding.spinnerSheet.isClickable = true

            }
            check = isChecked
        }

        homeBinding.buttonFound.setOnClickListener {
            if (slug != null) {
                val bundle = Bundle()
                bundle.putString("markslug", slug)
                Navigation.findNavController(homeBinding.root)
                    .navigate(R.id.action_homeFragment_to_choseCarFragment, bundle)
                bundle.clear()
                slug = null
                val toolbarText =
                    (activity as MainActivity).findViewById<TextView>(R.id.toolbar_text_1)
                toolbarText.visibility = View.GONE
                val location = (activity as MainActivity).findViewById<LinearLayout>(R.id.location)
                val dataToolbar =
                    (activity as MainActivity).findViewById<LinearLayout>(R.id.data_toolbar)
                val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)
                location.visibility = View.VISIBLE
                dataToolbar.visibility = View.VISIBLE
                backArrow.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Logoni tanlang", Toast.LENGTH_SHORT).show()
            }
        }
        return homeBinding.root
    }

    private fun setupMarkObserve() {
        viewModel!!.getMarkData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    homeBinding.progressBar.visibility = View.GONE
                    renderMarkList(it.data!!)
                    homeBinding.recycler.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    homeBinding.progressBar.visibility = View.VISIBLE
                    homeBinding.recycler.visibility = View.GONE
                }
                Status.ERROR -> {
                    homeBinding.recycler.visibility = View.GONE
                    homeBinding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun setupUI() {
        logoRvAdapter = LogoRvAdapter(arrayListOf(), object : LogoRvAdapter.OnClickLogoListener {
            override fun onItemClick(mark: Mark, position: Int) {
                slug = mark.slug
                Toast.makeText(requireContext(), slug, Toast.LENGTH_SHORT).show()
            }
        })
        homeBinding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recycler.adapter = logoRvAdapter

        rvAdapter = RvAdapter(arrayListOf())
        homeBinding.recycler1.adapter = rvAdapter
    }

    private fun setupCarObserve() {
        viewModel!!.getCarData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    homeBinding.recycler1.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    carTypeList = ArrayList()
                    it.let { data ->
                        carTypeList.add(CarType(data.data!![0].type, it.data))
                        renderCarList(carTypeList)
                    }
                    homeBinding.recycler1.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    homeBinding.recycler1.visibility = View.GONE
                }
            }
        })
    }

    private fun setupViewModel() {
        val apiService: ApiService =
            ApiClient.getRetrofit(requireContext()).create(ApiService::class.java)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelperImpl(apiService))).get(
            HomeViewModel::class.java
        )
    }

    private fun renderCarList(list: List<CarType>) {
        rvAdapter!!.addData(list as ArrayList<CarType>)
        rvAdapter!!.notifyDataSetChanged()
    }

    private fun renderMarkList(list: List<Mark>) {
        logoRvAdapter!!.addData(list)
        logoRvAdapter!!.notifyDataSetChanged()
    }

    private fun calendarData(type: Int) {
        val spinnerDateItemBinding: SpinnerDateItemBinding = SpinnerDateItemBinding
            .inflate(LayoutInflater.from(requireContext()))
        val datePickerDialog = AlertDialog.Builder(requireContext())

        datePickerDialog.setView(spinnerDateItemBinding.root)
        datePickerDialog.setTitle("Sanani tanla")

        val datePicker = spinnerDateItemBinding.datePicker
        datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.setPositiveButton("Ok") { _, _ ->
            dateFormat = SimpleDateFormat("dd MMM ", Locale.ENGLISH)
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val format = dateFormat!!.format(calendar.time)
            if (type == 1) {
                startDate = calendar.time
            } else if (type == 2) {
                if (startDate != null) {
                    endDate = calendar.time
                } else {
                    Toast.makeText(
                        requireContext(),
                        "boshlanish sanani kirit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            if (type == 1) {
                startTime = format
                homeBinding.pickUpDate.text = format
            } else if (type == 2) {
                endTime = format
                if (startDate != null && endDate != null) {
                    if (startDate!! <= endDate!!) {
                        homeBinding.dropDate.text = format
                        calendar.timeInMillis = endDate!!.time - startDate!!.time
                        day = calendar.get(Calendar.DAY_OF_MONTH)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Boshqa sana kiriting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        datePicker.findViewById<View>(resources.getIdentifier("year", "id", "android")).visibility =
            View.GONE
        datePickerDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        datePickerDialog.show()
    }

    override fun onStart() {
        super.onStart()
        homeBinding.innerCard.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.logo_color_back)
        homeBinding.locationMark.isClickable = false
        homeBinding.spinnerSheet.isClickable = false
    }

    private fun loadData() {


        locationList = ArrayList()
        locationList!!.add(Location("Mirzo Ulug'bek tumani ", "Buyuk ipak yo'li metrosi"))
        locationList!!.add(Location("Chilonzor tumani", "Chilonzor metrosi"))
        locationList!!.add(Location("Mirzo Ulug'bek tumani ", "Buyuk ipak yo'li metrosi"))
        locationList!!.add(Location("Chilonzor tumani", "Chilonzor metrosi"))
        locationList!!.add(Location("Mirzo Ulug'bek tumani ", "Buyuk ipak yo'li metrosi"))
        locationList!!.add(Location("Chilonzor tumani", "Chilonzor metrosi"))
        locationList!!.add(Location("Mirzo Ulug'bek tumani ", "Buyuk ipak yo'li metrosi"))
        locationList!!.add(Location("Chilonzor tumani", "Chilonzor metrosi"))
        locationList!!.add(Location("Mirzo Ulug'bek tumani ", "Buyuk ipak yo'li metrosi"))

    }

}