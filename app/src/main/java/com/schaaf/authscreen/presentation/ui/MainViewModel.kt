package com.schaaf.authscreen.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schaaf.authscreen.data.user.model.User
import com.schaaf.authscreen.data.weather.model.CurrentWeatherResponse
import com.schaaf.authscreen.domain.user.UserUseCase
import com.schaaf.authscreen.domain.weather.WeatherUseCase
import com.schaaf.authscreen.presentation.AuthScreenState
import com.schaaf.authscreen.presentation.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.schaaf.authscreen.domain.parse_exception.ParseExceptionUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val parseExceptionUseCase: ParseExceptionUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(AuthScreenState.AUTHORIZATION)
    val screenState = _screenState.asStateFlow()

    private val _isUserSuccessfullyRegistered = MutableSharedFlow<Response<Long>>()
    val isUserSuccessfullyRegistered = _isUserSuccessfullyRegistered.asSharedFlow()

    private val _userData = MutableSharedFlow<Response<User?>>()
    val userData = _userData.asSharedFlow()

    private val _weatherData = MutableStateFlow<Response<CurrentWeatherResponse?>?>(null)
    val weatherData = _weatherData.asStateFlow()

    fun registerUser(login: String, password: String) = viewModelScope.launch(IO){
        try {
            userUseCase.addUser(User(login, password)).let {
                _isUserSuccessfullyRegistered.emit(Response.Success(it))
            }
        }catch (e: Exception){
            e.printStackTrace()
            _isUserSuccessfullyRegistered.emit(Response.Error(parseExceptionUseCase.parseException(e)))
        }
    }

    fun getUserInfo(login: String, password: String) = viewModelScope.launch(IO){
        try {
            userUseCase.getUser(login, password).let {
                _userData.emit(Response.Success(it))
            }
        }catch (e: Exception){
            e.printStackTrace()
            _userData.emit(Response.Error(parseExceptionUseCase.parseException(e)))
        }
    }

    fun loadWeather(cityId: Int)= viewModelScope.launch {

        _weatherData.emit(Response.Loading)

        weatherUseCase.getCurrentWeather(cityId)
            .flowOn(IO)
            .catch {
                it.printStackTrace()
                _weatherData.emit(Response.Error(parseExceptionUseCase.parseException(it)))
            }
            .collect {
                _weatherData.emit(Response.Success(it))
            }
    }

    fun changeScreenState(newState: AuthScreenState) = viewModelScope.launch {
        _screenState.emit(newState)
    }

    fun clearWeatherResponse() = viewModelScope.launch {
        _weatherData.emit(null)
    }
}