package uz.pixyz.rentcar.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import uz.pixyz.rentcar.databinding.ActivityRegisterBinding
import uz.pixyz.rentcar.models.registration.User
import uz.pixyz.rentcar.retrofit.ApiClient.getRegister
import uz.pixyz.rentcar.retrofit.ApiHelperImpl
import uz.pixyz.rentcar.retrofit.ApiService
import uz.pixyz.rentcar.utils.Status
import uz.pixyz.rentcar.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var registerBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        setupViewModel()
        setupObserve()
        registerBinding.buttonOk.setOnClickListener { setupObserve() }
    }

    private fun setupObserve() {
        val phone = registerBinding.phone.unMasked
        val phone1 = registerBinding.phone
        val password = registerBinding.password.text.toString()
        val password2 = registerBinding.password2.text.toString()
        val textPhone = registerBinding.registerTextPhone
        val textPassword = registerBinding.registerTextPassword
        val textPassword2 = registerBinding.registerTextPassword2

        if (phone.isEmpty() && !phone1.isDone) {
            textPhone.visibility = View.VISIBLE
        } else {
            textPhone.visibility = View.GONE
            if (password.isEmpty()) {
                textPassword.visibility = View.VISIBLE
            } else {
                textPassword.visibility = View.GONE
                if (password.length >= 6) {
                    if (password2.isEmpty()) {
                        textPassword2.visibility = View.VISIBLE
                    } else {
                        textPassword2.visibility = View.GONE
                        if (password2.length >= 6) {
                            if (password == password2) {
                                val phone2 = "+998$phone"
                                val user = User(phone2, password, password2)
                                viewModel.loadData(user)
                                viewModel.getRegister().observe(this, {
                                    when (it.status) {
                                        Status.LOADING -> {
                                            registerBinding.progressBar.visibility =
                                                View.VISIBLE
                                        }
                                        Status.SUCCESS -> {
                                            registerBinding.progressBar.visibility = View.GONE
                                            Toast.makeText(
                                                this,
                                                it.data.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d("Register", it.data.toString())
                                        }
                                        Status.ERROR -> {
                                            registerBinding.progressBar.visibility = View.GONE
                                            Log.d("Register1", it.data.toString())
                                            Log.d("Register2", it.error.toString())
                                        }
                                    }
                                })
                            } else {
                                Toast.makeText(this, "parol kam", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "parol kam", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "parol kam", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewModel() {
        val apiService: ApiService = getRegister().create(ApiService::class.java)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelperImpl(apiService)))
            .get(RegisterViewModel::class.java)
    }
}