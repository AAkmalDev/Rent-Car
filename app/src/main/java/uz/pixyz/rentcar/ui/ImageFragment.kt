package uz.pixyz.rentcar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import uz.pixyz.rentcar.databinding.FragmentImageBinding

private const val ARG_PARAM1 = "param1"
class ImageFragment : Fragment() {

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    private lateinit var imageBinding: FragmentImageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imageBinding = FragmentImageBinding.inflate(inflater)
        Glide.with(imageBinding.root).load(param1).into(imageBinding.viewPagerImage)
        return imageBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}