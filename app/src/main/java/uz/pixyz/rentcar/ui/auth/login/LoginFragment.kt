package uz.pixyz.rentcar.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import uz.pixyz.rentcar.MainActivity
import uz.pixyz.rentcar.R
import uz.pixyz.rentcar.databinding.FragmentLoginBinding
import uz.pixyz.rentcar.models.registration.User
import uz.pixyz.rentcar.retrofit.ApiClient.getRetrofit
import uz.pixyz.rentcar.retrofit.ApiHelperImpl
import uz.pixyz.rentcar.retrofit.ApiService
import uz.pixyz.rentcar.ui.auth.register.RegisterActivity
import uz.pixyz.rentcar.utils.MySharedPreferences
import uz.pixyz.rentcar.utils.Status
import uz.pixyz.rentcar.utils.ViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(inflater)

        MySharedPreferences.init(requireContext())
        loginBinding.loginRegister.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }

        setupViewModel()

        loginBinding.loginButtonOk.setOnClickListener {
            setupObserve()
        }

        val backArrow = (activity as MainActivity).findViewById<ImageView>(R.id.back_arrow)

        backArrow.setOnClickListener {
            Navigation.findNavController(loginBinding.root).popBackStack()
        }

        return loginBinding.root
    }

    private fun setupViewModel() {
        val apiService: ApiService = getRetrofit(requireContext()).create(ApiService::class.java)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelperImpl(apiService)))
            .get(LoginViewModel::class.java)
    }

    private fun setupObserve() {
        val loginPhone = loginBinding.loginPhone
        val text = loginBinding.loginPhoneText.text
        val phone = loginBinding.loginPhone.unMasked
        val password = loginBinding.loginPassword.text.toString()

        if (!loginPhone.isDone) {
            loginBinding.loginTextPhone.visibility = View.VISIBLE
        } else {
            loginBinding.loginTextPhone.visibility = View.GONE
            if (password.isEmpty()) {
                loginBinding.loginTextPassword.visibility = View.VISIBLE

            } else {
                loginBinding.loginTextPassword.visibility = View.GONE
                if (password.length >= 6) {
                    val phone1 = "$text" + phone
                    val user = User(phone1, password)

                    viewModel.loadData(user)
                    viewModel.getLoginUser().observe(viewLifecycleOwner, {
                        when (it.status) {
                            Status.LOADING -> {
                                loginBinding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                loginBinding.progressBar.visibility = View.GONE
                                MySharedPreferences.phone = phone1
                                MySharedPreferences.password = password
                                Navigation.findNavController(loginBinding.root)
                                    .navigate(R.id.action_loginFragment_to_orderFragment2)
                                Log.d("LoginUser", it.data.toString())
                                Toast.makeText(
                                    requireContext(),
                                    it.data.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Status.ERROR -> {
                                loginBinding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(requireContext(), "parol kam", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}