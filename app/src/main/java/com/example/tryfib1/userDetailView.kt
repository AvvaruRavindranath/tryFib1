package com.example.tryfib1

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Math.cos
import java.lang.Math.sin

class userDetailView : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var bloodgroups: Array<String>
    private lateinit var db : DatabaseReference

    var LastDetail: String = " "

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latitude: String = ""
    var longitude: String = ""
    lateinit var getlocation_of_Recipient: Button


    fun gettingUserData() {
        db = FirebaseDatabase.getInstance().getReference("Users")

        db.addValueEventListener(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) { //want to add in new level

                        val a = userSnapshot.getValue(User::class.java)
                        //Toast.makeText(this@userDetailView," This $a",Toast.LENGTH_LONG).show()
                        if (a?.bloodgroup in bloodgroups && a?.eligibility == true) {
                            //  var i:Double = 0.1
                            // while(i<=1.0) {
                            //  if(LastDetail == a.phone){continue}
                            if (distance(
                                    longitude.toDouble(),
                                    latitude.toDouble(),
                                    a.longitude!!.toDouble(),
                                    a.latitude!!.toDouble(),
                                    'K'
                                ) <= 5.0
                            ) {
                                userArrayList.add(a)
                            }


                        }
                        //i+=0.1
                        // LastDetail = a.phone.toString()
                        //  }
                    }

                }
                userRecyclerView.adapter = MyAdapter(userArrayList)
                // Toast.makeText(this@userDetailView," Done",Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getData() {
        db = FirebaseDatabase.getInstance().getReference("Users")
        Toast.makeText(this, "$bloodgroups", Toast.LENGTH_SHORT).show()
        db.child("Ravindra9959273686").get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value
                val age = it.child("age").value
                val gender = it.child("gender").value
                val blood = it.child("bloodgroup").value
                Toast.makeText(this, "$name  $age \n $gender  $blood", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No Bloogroup match", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchLocation() {
        Toast.makeText(this, "location", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Location Successfully Fetched", Toast.LENGTH_SHORT).show()
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()

                getlocation_of_Recipient.setBackgroundColor(Color.GREEN)

            }
        }
    }


    /**
     * Function to find distance between our [lat,lon] and other node [lat,lon] units as K,N
     */

    fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double, unit: Char): Double {
        val pi = Math.PI
        var radlat1 = pi * lat1 / 180
        var radlat2 = pi * lat2 / 180
        var theta = lng1 - lng2
        var radtheta = pi * theta / 180
        var dist = sin(radlat1) * sin(radlat2) + cos(radlat1) * cos(radlat2) * cos(radtheta)
        if (dist > 1) {
            dist = 1.0
        }
        dist = Math.acos(dist)
        dist = dist * 180 / pi
        dist = dist * 60 * 1.1515
        if (unit == 'K') {
            dist = dist * 1.609344
        }
        if (unit == 'N') {
            dist = dist * 0.8684
        }

        return dist
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail_view)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        /**
         * Getting View's {Layouts}
         */

        val bloodRequestView: LinearLayout = findViewById(R.id.bloodRequestView)
        val Recipient_Details: CardView = findViewById(R.id.Recipient_Details)



        userRecyclerView = findViewById(R.id.userRV)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()

        val bloodgroupI: RadioGroup = findViewById(R.id.userbloodgroup)


        val getdonarbtn: Button = findViewById(R.id.getdonorbtn)


        var bloodgroup: String = ""

        /**
         * Getting Recipient Details
         */

        val recipientName: EditText = findViewById(R.id.recipientName)
        val recipientAge: EditText = findViewById(R.id.recipientAge)
        val recipientPhoneNumber: EditText = findViewById(R.id.recipientPhoneNumber)
        val recipientHospitalName: EditText = findViewById(R.id.recipientHospitalName)
        getlocation_of_Recipient = findViewById(R.id.getlocation_of_Recipient)
        getlocation_of_Recipient.setBackgroundColor(Color.RED)
        fetchLocation()

        getlocation_of_Recipient.setOnClickListener {
            fetchLocation()
        }


        var Recipient_Name_textArea: TextView = findViewById(R.id.Recipient_Name_textArea)
        var Recipient_Bloodgroup_textArea: TextView =
            findViewById(R.id.Recipient_Bloodgroup_textArea)
        var Recipient_Phone_textArea: TextView = findViewById(R.id.Recipient_Phone_textArea)
        var Recipient_Hospital_textArea: TextView = findViewById(R.id.Recipient_Hospital_textArea)
        var Recipient_Address_textArea: TextView = findViewById(R.id.Recipient_Address_textArea)




        getdonarbtn.setOnClickListener {


            try {
                val bloodID = bloodgroupI.checkedRadioButtonId
                val bloodrb: RadioButton = findViewById(bloodID)
                bloodgroup = bloodrb.text.toString()

                bloodRequestView.visibility = View.GONE
                Recipient_Details.visibility = View.VISIBLE

            } catch (e: NullPointerException) {
                Toast.makeText(this, "Select Blood Group", Toast.LENGTH_LONG).show()
            }


            /**
             * Assigning Recipient details to card view of Recipient_Details
             */
            Recipient_Name_textArea.text = recipientName.text
            Recipient_Bloodgroup_textArea.text = bloodgroup
            Recipient_Phone_textArea.text = recipientPhoneNumber.text
            Recipient_Hospital_textArea.text = recipientHospitalName.text
            Recipient_Address_textArea.text = " lat : $latitude \n lng : $longitude"



            userArrayList.clear()


            when (bloodgroup) {
                "AB+" -> bloodgroups = arrayOf("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-")
                "AB-" -> bloodgroups = arrayOf("AB-", "A-", "B-", "O-")
                "A+" -> bloodgroups = arrayOf("A+", "A-", "O+", "O-")
                "A-" -> bloodgroups = arrayOf("A-", "O-")
                "B+" -> bloodgroups = arrayOf("B+", "B-", "O+", "O-")
                "B-" -> bloodgroups = arrayOf("B-", "O-")
                "O+" -> bloodgroups = arrayOf("O+", "O-")
                else -> bloodgroups = arrayOf("O-")

            }

            if(recipientName.text.toString() != "" && bloodgroup != "" && latitude != ""){
                gettingUserData()
            }
            else{
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
            }
            }
        }

    }
