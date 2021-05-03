package com.schaaf.authscreen.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.schaaf.authscreen.R
import com.schaaf.authscreen.databinding.ActivityMainBinding
import com.schaaf.authscreen.presentation.AuthScreenState
import com.schaaf.authscreen.presentation.AuthScreenState.*
import com.schaaf.authscreen.presentation.Response
import com.schaaf.authscreen.presentation.utils.hideKeyboard
import com.schaaf.authscreen.presentation.utils.validateEmail
import com.schaaf.authscreen.presentation.utils.validatePassword
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this)).also {
            with(it){
                initUi()
                observeViewModel()
            }
        }
        setContentView(binding.root)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }


    private fun ActivityMainBinding.initUi(){

        editTextEmail.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!layoutEmail.error.isNullOrEmpty()) layoutEmail.error = null
            }
        })

        layoutPassword.apply {

            setEndIconOnClickListener { helperText = getString(R.string.suggestion_password) }

            editText?.apply {

                addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                    override fun afterTextChanged(s: Editable?) = Unit

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (!helperText.isNullOrEmpty()) helperText = null
                        if (!layoutPassword.error.isNullOrEmpty()) layoutPassword.error = null
                    }
                })

                setOnFocusChangeListener { _, hasFocus ->
                    if(!hasFocus && !helperText.isNullOrEmpty()) helperText = null
                }
            }
        }

        toolbar.apply {

            setOnMenuItemClickListener {
                if(it.itemId == R.id.create){
                    changeScreenState(REGISTRATION)
                    true
                }else{
                    false
                }
            }

            setNavigationOnClickListener {
                changeScreenState(AUTHORIZATION)
            }
        }
    }



    private fun ActivityMainBinding.observeViewModel() = lifecycleScope.launch {
        awaitAll(
            async {
                viewModel.screenState.flowWithLifecycle(lifecycle).collect {

                    when (it) {

                        AUTHORIZATION -> {
                            buttonSubmit.apply {
                                text = getString(R.string.btn_sign_in)
                                setOnClickListener {
                                    signInUser()
                                }
                            }
                            toolbar.apply {
                                title = getString(R.string.title_auth)
                                menu.getItem(0)?.isVisible = true
                                navigationIcon = null
                            }
                            editTextEmail.clearFocus()
                            editTextPassword.clearFocus()
                        }

                        REGISTRATION -> {
                            buttonSubmit.apply {
                                text = getString(R.string.btn_register)
                                setOnClickListener {
                                    registerUser()
                                }
                            }
                            toolbar.apply {
                                menu.getItem(0)?.isVisible = false
                                title = getString(R.string.title_registration)
                                navigationIcon = ContextCompat.getDrawable(
                                    this@MainActivity,
                                    R.drawable.ic_back
                                )
                            }
                        }
                    }

                    layoutEmail.apply {
                        if (!error.isNullOrEmpty()) error = null
                        if (!helperText.isNullOrEmpty()) helperText = null
                    }


                    layoutPassword.apply {
                        if (!error.isNullOrEmpty()) error = null
                    }
                }
            },

            async {
                viewModel.isUserSuccessfullyRegistered.flowWithLifecycle(lifecycle).collect {
                    when (it) {

                        is Response.Success ->
                            if (it.data != -1L) {

                                editTextEmail.text?.clear()
                                editTextPassword.text?.clear()

                                Snackbar.make(
                                    root,
                                    getString(R.string.user_created),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                changeScreenState(AUTHORIZATION)

                            } else {
                                layoutEmail.error = getString(
                                    R.string.error_registration_user_already_exists
                                )
                            }

                        is Response.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                it.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                        }
                    }
                }
            },

            async {
                viewModel.userData.flowWithLifecycle(lifecycle).collect {
                    when (it) {

                        is Response.Success -> {
                            if (it.data != null) {

                                val isEmailMatch = it.data.login == editTextEmail.text.toString()
                                val isPasswordMatch = it.data.password == editTextPassword.text.toString()

                                when {
                                    !isEmailMatch -> {
                                        layoutEmail.error =
                                            getString(R.string.error_sign_in_wrong_email)
                                    }

                                    !isPasswordMatch -> {
                                        layoutPassword.error =
                                            getString(R.string.error_sign_in_wrong_password)
                                    }

                                    isEmailMatch && isPasswordMatch -> {
                                        editTextEmail.text?.clear()
                                        editTextPassword.text?.clear()

                                        loadWeather()
                                    }
                                }

                            } else {
                                layoutEmail.error = getString(R.string.error_sign_in_user_not_found)
                            }
                        }


                        is Response.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                it.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                        }
                    }
                }
            },

            async {
                viewModel.weatherData.flowWithLifecycle(lifecycle).filterNotNull().collect {
                    when (it) {

                        is Response.Success -> {

                            if (it.data != null) {
                                Snackbar.make(
                                    root,
                                    getString(R.string.temp_now, it.data.main?.temp?.roundToInt()),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.unknown_error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            setButtonsEnabled(true)
                            progressBar.isVisible = false
                        }


                        is Response.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                it.exception,
                                Toast.LENGTH_SHORT
                            ).show()

                            setButtonsEnabled(true)
                            progressBar.isVisible = false
                        }

                        else -> {
                        }
                    }
                    viewModel.clearWeatherResponse()
                }
            }
        )
    }

    override fun onBackPressed() {
        if (viewModel.screenState.value == REGISTRATION){
            changeScreenState(AUTHORIZATION)
        }else{
            super.onBackPressed()
        }
    }

    private fun ActivityMainBinding.registerUser(){
       if (isInputDataValid()){
           buttonSubmit.hideKeyboard()

           viewModel.registerUser(
               layoutEmail.editText!!.text.toString().trim(),
               layoutPassword.editText!!.text.toString().trim()
           )
       }
    }

    private fun ActivityMainBinding.signInUser(){
        if (isInputDataValid()){
            buttonSubmit.hideKeyboard()

            viewModel.getUserInfo(
                layoutEmail.editText!!.text.toString().trim(),
                layoutPassword.editText!!.text.toString().trim()
            )
        }
    }

    private fun loadWeather(){
        setButtonsEnabled(false)
        binding.progressBar.isVisible = true
        viewModel.loadWeather(cityId = 498817)//Saint Petersburg
    }

    private fun setButtonsEnabled(isEnabled: Boolean) {
        binding.apply {
            layoutEmail.apply {
                this.isEnabled = isEnabled
                if(isEnabled) editTextEmail.clearFocus()
            }
            layoutPassword.apply {
                this.isEnabled = isEnabled
                if(isEnabled) editTextPassword.clearFocus()
            }
            buttonSubmit.isEnabled = isEnabled
            toolbar.menu.getItem(0)?.isEnabled = isEnabled
        }
    }

    private fun changeScreenState(newState: AuthScreenState){
        binding.apply {
            editTextPassword.text?.clear()
            editTextEmail.text?.clear()
        }
        viewModel.changeScreenState(newState)
    }

    private fun isInputDataValid(): Boolean{
        val isEmailValid = binding.layoutEmail.validateEmail()
        val isPasswordValid = binding.layoutPassword.validatePassword()
        return isEmailValid && isPasswordValid
    }
}