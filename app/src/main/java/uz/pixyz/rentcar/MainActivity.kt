package uz.pixyz.rentcar

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.pixyz.rentcar.databinding.ActivityMainBinding
import uz.pixyz.rentcar.retrofit.NetworkHelper

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        val toolbarString = SpannableString("Browse cars")
        toolbarString.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        toolbarString.setSpan(StyleSpan(Typeface.NORMAL), 6, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mainBinding.toolbarText1.text = toolbarString

        if(NetworkHelper(this).isNetworkConnected()){
            Toast.makeText(this, "internet bor", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "internet yoq", Toast.LENGTH_SHORT).show()
        }

     //   val networkHelper = NetworkHelper(this)
//        setSupportActionBar(toolbar_drawer)
//
//        val navController = Navigation.findNavController(this, R.id.fragment_nav)
//        NavigationUI.setupWithNavController(nav_view, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)


    }

    

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(
//            Navigation.findNavController(this, R.id.fragment_nav), drawer_layout
//        )
//    }
}