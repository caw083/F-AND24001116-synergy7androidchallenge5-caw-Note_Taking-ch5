package com.example.challenge4.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.challenge4.R
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Log



import com.example.challenge4.data.UserData
import com.example.challenge4.databinding.FragmentLoginBinding
import com.example.challenge4.data.DataStoreManager

import androidx.fragment.app.viewModels

class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var dataStoreManager: DataStoreManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Initialize dataStoreManager
        dataStoreManager = DataStoreManager(requireContext())

        lifecycleScope.launch {
            // Launch coroutine to set default user data if necessary
            Log.d("Coroutine", "Coroutine is running")


            val currentUserData = dataStoreManager.userData.first()
            if (currentUserData == null) {
                val defaultUser = UserData(
                    "Christopher",
                    "christopher083@gmail.com",
                    "christo083",
                    "88216323520",
                    false
                )
                dataStoreManager.storeUserData(defaultUser)
                showToast("Data created")
            } else {
                if (currentUserData.isLogin == true){
                    findNavController().navigate(R.id.action_login_to_list2)
                }else{
                    showToast("user not login")
                }
            }
            showToast("Hello world")

            // Second coroutine
            try {
                val updatedUserData = dataStoreManager.userData.first()
                if (updatedUserData != null) {
                    val loginBinding = LoginBinding().apply {
                        userEmailt = updatedUserData.email
                        userPassword = updatedUserData.password
                        loginText = "Login"
                        alreadyHaveAccount = "Already \n have an \n Account?"
                        newAccountText = "New User ? Register Now"
                    }
                    binding.loginDataBinding = loginBinding
                    showToast("User Data: $updatedUserData")
                } else {
                    showToast("No user data found")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }



        // Set up the toRegister TextView to navigate to the Register fragment
        binding.toRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        // Set up the login button to check credentials and navigate to Home if successful
        binding.cirLoginButton.setOnClickListener {
            lifecycleScope.launch {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                // Retrieve user data from DataStore
                val currentUserData = dataStoreManager.userData.first()

                // Perform the login check with the retrieved user data
                val isLoginValid = currentUserData?.let { userData ->
                    userData.email == email && userData.password == password
                } ?: false

                // Update isLogin field in DataStore if login is valid
                if (isLoginValid) {
                    currentUserData?.let {
                        val updatedUserData = it.copy(isLogin = true)
                        dataStoreManager.storeUserData(updatedUserData)
                    }
                }

                // Navigate to the next screen if login is valid, otherwise show a toast
                if (isLoginValid) {
                    findNavController().navigate(R.id.action_login_to_list2)
                } else {
                    showToast("Invalid email or password")
                }
            }
        }
        return binding.root
    }




    fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }
}
