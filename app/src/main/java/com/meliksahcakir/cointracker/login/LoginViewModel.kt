package com.meliksahcakir.cointracker.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.meliksahcakir.androidutils.Event
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.data.UserRemote
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(private val userRemote: UserRemote) : ViewModel() {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    private val _warningEvent = MutableLiveData<Event<String>>()
    val warningEvent: LiveData<Event<String>> = _warningEvent

    private val _busy = MutableLiveData<Boolean>(false)
    val busy: LiveData<Boolean> = _busy

    fun login(data: Intent) {
        viewModelScope.launch {
            _busy.value = true
            val result = internalLogin(data)
            if (result is Result.Success) {
                userRemote.setup()
                _navigateToMainActivity.value = Event(Unit)
            } else if (result is Result.Error) {
                _warningEvent.value = Event(result.exception.message ?: "")
            }
            _busy.value = false
        }
    }

    private suspend fun internalLogin(data: Intent): Result<FirebaseUser> {
        return try {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                userRemote.firebaseAuth.signInWithCredential(credential).await()
            }
            val user = userRemote.firebaseAuth.currentUser
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
        val user = userRemote.firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {
                userRemote.setup()
                _navigateToMainActivity.value = Event(Unit)
            }
        }
        return user
    }
}
