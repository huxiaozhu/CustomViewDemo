package com.liuxiaozhu.circularprogressbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
    }

    private fun setData() {
        var progress = 0
        button.setOnClickListener {
            Thread(Runnable {
                while (progress <= 100) {
                    progress += 2
                    progressbar.setProgress(progress)
                    progressbar1.setProgress(progress)
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }).start()
        }
    }
}
