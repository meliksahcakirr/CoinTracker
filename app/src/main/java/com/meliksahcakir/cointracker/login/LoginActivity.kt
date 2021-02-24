package com.meliksahcakir.cointracker.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.meliksahcakir.androidutils.EventObserver
import com.meliksahcakir.androidutils.Result
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.databinding.ActivityLoginBinding
import com.meliksahcakir.cointracker.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val GOOGLE_SIGN_IN = 1000

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loginResult.observe(
            this,
            Observer {
                if (it is Result.Error) {
                    Toast.makeText(applicationContext, it.exception.message, Toast.LENGTH_SHORT).show()
                } else {
                    navigateToMainActivity()
                }
            }
        )

        viewModel.navigateToMainActivity.observe(
            this,
            EventObserver {
                navigateToMainActivity()
            }
        )

        viewModel.busy.observe(
            this,
            Observer {
                binding.progressBar.isVisible = it == true
            }
        )

        initializeGoogleSignIn()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun initializeGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignInButton.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            data?.let {
                viewModel.login(data)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUser()
    }
}
