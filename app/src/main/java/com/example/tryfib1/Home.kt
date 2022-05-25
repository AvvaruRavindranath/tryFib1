package com.example.tryfib1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val regbtn : Button = findViewById(R.id.HOME_registerbtn)
        val brbtn : Button = findViewById(R.id.HOME_requestblood)
        val gohome : TextView = findViewById(R.id.gohome)


        gohome.setOnClickListener{
            startActivity(Intent(this,MainActivity2::class.java))
        }
        regbtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        brbtn.setOnClickListener {
            startActivity(Intent(this,userDetailView::class.java))
        }
    }

    fun showRegistration(hl :LinearLayout,rl :LinearLayout,ll :LinearLayout){
        hl.visibility = View.GONE
        rl.visibility = View.VISIBLE
        ll.visibility = View.GONE
    }

    fun showLogin(hl :LinearLayout,rl :LinearLayout,ll :LinearLayout){
        hl.visibility = View.GONE
        rl.visibility = View.GONE
        ll.visibility = View.VISIBLE
    }

    fun showHome(hl :LinearLayout,rl :LinearLayout,ll :LinearLayout){
        hl.visibility = View.VISIBLE
        rl.visibility = View.GONE
        ll.visibility = View.GONE
    }
}