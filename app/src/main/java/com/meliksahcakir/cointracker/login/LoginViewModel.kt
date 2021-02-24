package com.meliksahcakir.cointracker.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<FirebaseUser>>()
    val loginResult: LiveData<Result<FirebaseUser>> = _loginResult

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    private val _busy = MutableLiveData<Boolean>(false)
    val busy: LiveData<Boolean> = _busy

    fun login(data: Intent) {
        viewModelScope.launch {
            _busy.value = true
            val result = internalLogin(data)
            _loginResult.value = result
            _busy.value = false
        }
    }

    private suspend fun internalLogin(data: Intent): Result<FirebaseUser> {
        return try {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
            }
            val user = firebaseAuth.currentUser
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun checkUser(): FirebaseUser? {
        val user = firebaseAuth.currentUser
        if (user != null) {
            _navigateToMainActivity.value = Event(Unit)
        }
        return user
    }
}
