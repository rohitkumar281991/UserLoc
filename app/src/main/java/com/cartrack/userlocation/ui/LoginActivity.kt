package com.cartrack.userlocation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cartrack.userlocation.Constants
import com.cartrack.userlocation.R
import com.cartrack.userlocation.SessionManagerUtil
import com.cartrack.userlocation.data.ResourceStatus
import com.cartrack.userlocation.data.api.model.UserDetailsAddress
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.ClickListener
import com.cartrack.userlocation.databinding.LoginLayoutBinding
import com.cartrack.userlocation.utils.Utility
import com.cartrack.userlocation.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity(), ClickListener {
    companion object {
        private const val TAG = "Ro_LoginActivity"
        fun createIntent(
            context: Context
        ): Intent {
            return Intent().apply {
                setClass(context, LoginActivity::class.java)
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }
        }
    }

    private lateinit var binding: LoginLayoutBinding
    private val viewModel: LoginViewModel by viewModels()
    private var selectedCountry : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_layout)
        binding.listener = this

        checkSessionState()
        textViewClickListeners()
        fetchAllUsers()

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        })

        viewModel.usersListFromServer.observe(this, Observer {
            if (it != null) {
                populateCountries(Utility.getCountries(it))
            }
        })

        viewModel.errorMessage.observe(this) {

            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }

    }

    private fun fetchAllUsers() {
        viewModel.getAllUsersFromServerUsingCall()
    }

    private fun checkSessionState() {
        val sessionIsActive = SessionManagerUtil.isSessionActive(this)
        if (sessionIsActive) launchUserDetailActivity()
    }

    private fun textViewClickListeners() {
        binding.txtPassword.setOnClickListener {
            if (binding.txtValidateHint.visibility == View.VISIBLE) {
                binding.txtValidateHint.visibility = View.GONE
            }
        }
        binding.txtUserName.setOnClickListener {
            if (binding.txtValidateHint.visibility == View.VISIBLE) {
                binding.txtValidateHint.visibility = View.GONE
            }
        }
    }
    private fun populateCountries(list: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, list)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedCountry = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    override fun onClick(view: View?) {
        Log.d(
            TAG, "onClick $selectedCountry" + ":" + binding.txtUserName.text.toString() +
                    ":+" + binding.txtPassword.text.toString()
        )
        viewModel.hideKeyboard(this)
        if (viewModel.validateUserInput(binding) == Constants.UserValidation.SUCCESS) {

            val user = UserInfoRepositoryList(
                name = binding.txtUserName.text.toString(),
                password = Utility.encryptionFunction(binding.txtPassword.text.toString()),
                address = UserDetailsAddress(
                    city = selectedCountry
                )
            )
            viewModel.insertSingleUserData(user)
            viewModel.insertUserDataStatus.observe(this, Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        viewModel.startUserSession(this, binding)
                        binding.txtValidateHint.visibility = View.GONE
                        launchUserDetailActivity()
                    }
                    ResourceStatus.ERROR -> {
                        binding.txtValidateHint.visibility = View.GONE
                        Log.e(TAG, "it.message = ${it.error}")
                        Snackbar.make(
                            binding.root,
                            it.error.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
        } else {
            if (binding.txtValidateHint.visibility == View.VISIBLE) {
                binding.txtValidateHint.visibility = View.GONE
            }
            if (viewModel.validateUserInput(binding) == Constants.UserValidation.INVALID_USERNAME) {
                binding.txtValidateHint.text = getString(R.string.username_validation_text)
            } else {
                binding.txtValidateHint.text = getString(R.string.password_validation_text)
            }
            binding.txtValidateHint.visibility = View.VISIBLE
        }
    }

    override fun onRecyclerViewClick(view: UserInfoRepositoryList?) {

    }

    override fun getLocation() {
        TODO("Not yet implemented")
    }

    private fun launchUserDetailActivity() {
        Log.d(TAG, "Start UserDetailActivity")
        startActivity(UserDetailActivity.createIntent(this))
    }
}