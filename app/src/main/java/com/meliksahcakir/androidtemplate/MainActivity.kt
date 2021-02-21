package com.meliksahcakir.androidtemplate

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.meliksahcakir.androidutils.NotificationUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val notificationUtil: NotificationUtil by lazy { NotificationUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_compute.setOnClickListener {
            val input = edit_text_factorial.text.toString().toInt()
            val result = factorial(input)

            text_result.text = result.toString()
            text_result.visibility = View.VISIBLE

            notificationUtil.showNotification(
                context = this,
                title = getString(R.string.notification_title),
                message = result.toString(),
                icon = R.drawable.ic_alarm
            )
        }
    }

    private fun factorial(number: Int): Long {
        return when {
            number < 0 -> 0
            number <= 1 -> 1
            else -> number.toLong() * factorial(number - 1)
        }
    }
}
