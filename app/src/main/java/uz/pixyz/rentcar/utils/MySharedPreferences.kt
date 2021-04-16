package uz.pixyz.rentcar.utils

import android.content.Context
import android.content.SharedPreferences
import uz.pixyz.rentcar.models.registration.User
import uz.pixyz.rentcar.utils.MySharedPreferences.sharedPreferences

object MySharedPreferences {

    private const val NAME = "rentcar"
    private const val MODE = Context.MODE_PRIVATE
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(NAME, MODE)

    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }


    var markslug: String?
        get() = sharedPreferences?.getString("markslug", "chevrolet")
        set(value) = sharedPreferences!!.edit() {
            if (value != null) {
                it.putString("markslug", value)
            }
        }

    var phone: String?
        get() = sharedPreferences?.getString("phone", "")
        set(value) = sharedPreferences!!.edit() {
            if (value != null) {
                it.putString("phone", value)
            }
        }

    var password: String?
        get() = sharedPreferences?.getString("password", "")
        set(value) = sharedPreferences!!.edit() {
            if (value != null) {
                it.putString("password", value)
            }
        }


}