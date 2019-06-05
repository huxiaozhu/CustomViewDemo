package com.liuxiaozhu.circularprogressbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mProgressbar: CircularProgressbarView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgressbar = findViewById(R.id.progressbar)
        setData()
    }

    private fun setData() {
        var progress = 0
        mProgressbar!!.setOnClickListener {
            Thread(Runnable {
                while (progress <= 100) {
                    progress += 2
                    mProgressbar!!.setProgress(progress)

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
