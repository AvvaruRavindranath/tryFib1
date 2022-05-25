package com.example.tryfib1

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/***************************************************************
 *
 * Unwanted class
 *
 * class F
 * to get date from CalenderView
 * in DD/MM/YYYY format
 * converted java code to kotlin and used in main activity
 */

class F : AppCompatActivity() {
    var calendarView: CalendarView? = null
    var myBate: TextView? = null
    var myDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById<View>(R.id.calendarView) as CalendarView
        calendarView!!.setOnDateChangeListener { calendarView, i, i1, i2 ->
            myDate = i2.toString() + "/" + (i1 + 1) + "/" + i
        }
    }


}



