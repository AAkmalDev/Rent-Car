package uz.pixyz.rentcar.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.pixyz.rentcar.models.cars.Image
import uz.pixyz.rentcar.ui.ImageFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,val imageList: List<Image>): FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(imageList[position].image)
    }
}