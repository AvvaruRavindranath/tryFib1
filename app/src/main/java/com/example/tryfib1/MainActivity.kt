package com.example.tryfib1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tryfib1.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.installations.FirebaseInstallations

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseReference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latitude: String = ""
    var longitude: String = ""
    lateinit var registerbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        registerbutton = findViewById(R.id.registerbutton)
        registerbutton.setBackgroundColor(Color.RED)

        /**
         * Kotlin code to get Date DD/MM/YYYY
         */

        val calendarView: CalendarView = findViewById(R.id.calendarView)
        var lbdd: String = ""

        calendarView.setOnDateChangeListener { calendarView, i, i1, i2 ->
            lbdd = i2.toString() + "/" + (i1 + 1) + "/" + i
            Toast.makeText(this, "date is $lbdd", Toast.LENGTH_SHORT).show()
        }

        val username: EditText = findViewById(R.id.username)
        val userage: EditText = findViewById(R.id.userage)
        val usergender: RadioGroup = findViewById(R.id.usergender)
        val userbloodgroup: RadioGroup = findViewById(R.id.userbloodgroup)
        val getlocation: Button = findViewById(R.id.getlocation)
        val userphonenumber: EditText = findViewById(R.id.userphonenumber)
        val donatedOrNot: CheckBox = findViewById(R.id.donated_or_not)
        val usereligibility: CheckBox = findViewById(R.id.usereligibility)


        registerbutton.setOnClickListener {
            try {
                val name = username.text.toString()
                val agee = userage.text.toString()
                var age: Int = 0
                if (agee != "") {
                    age = agee.toInt()
                }

                //Getting Gender from Radio Type
                var gender: String? = null

                val genderID = usergender.checkedRadioButtonId
                val genderrb: RadioButton = findViewById(genderID)
                gender = genderrb.text.toString()


                //Getting BloodGroup from Radio Type
                var bloodgroup: String? = null

                val bloodID = userbloodgroup.checkedRadioButtonId
                val bloodrb: RadioButton = findViewById(bloodID)
                bloodgroup = bloodrb.text.toString()

                getlocation.setOnClickListener {
                    fetchLocation()
                }


                val final_latitude = latitude.toDouble()
                val final_longitude = longitude.toDouble()


                val phone: String = userphonenumber.text.toString()

                val donated_or_not = donatedOrNot.isChecked
                if (donated_or_not == true) {
                    // binding.cvLL.visibility = View.GONE
                    //calendarView.visibility = View.GONE
                    lbdd = ""
                }


                // val lbdd = binding.userlastblooddonationdate.text.toString()
                val eligibility = usereligibility.isChecked

                db = FirebaseDatabase.getInstance().getReference("Users")

                val userToken = FirebaseInstallations.getInstance().id.toString()


                if (name != "" && age > 18 && age < 65 && gender != "" && bloodgroup != "" && latitude != "") {
                    val user = User(
                        name,
                        age,
                        gender,
                        bloodgroup,
                        phone,
                        final_latitude,
                        final_longitude,
                        lbdd,
                        eligibility,
                        userToken
                    )
                    val userName = name + phone
                    db.child(userName).setValue(user).addOnSuccessListener {
                        username.text.clear()
                        userage.text.clear()
                        usergender.clearCheck()
                        userbloodgroup.clearCheck()
                        userphonenumber.text.clear()
                        usereligibility.isChecked = false

                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Home::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Enter Valid Data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NullPointerException) {
                Toast.makeText(this, "Enter Valid Data", Toast.LENGTH_SHORT).show()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this,
                    "Enter Correct Phone Number \n Check Location Again",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun fetchLocation() {
        //Toast.makeText(this,"getting location",Toast.LENGTH_SHORT).show()
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                //Toast.makeText(applicationContext, "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Check Register Now", Toast.LENGTH_SHORT).show()
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()

                registerbutton.setBackgroundColor(Color.GREEN)

            }
        }
    }

}