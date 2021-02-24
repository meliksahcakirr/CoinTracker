package com.meliksahcakir.cointracker.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meliksahcakir.androidutils.EventObserver
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.login.LoginActivity
import com.meliksahcakir.cointracker.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.navigateToNextScreen.observe(
            this,
            EventObserver {
                if (it == SplashActivityDirections.LOGIN_ACTIVITY) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else if (it == SplashActivityDirections.MAIN_ACTIVITY) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        )
    }
}
