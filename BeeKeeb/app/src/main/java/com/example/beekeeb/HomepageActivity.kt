package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.beekeeb.databinding.ActivityHomepageBinding
import com.example.beekeeb.databinding.HomeLayoutBinding
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var intnt: Intent
    private lateinit var userData: User
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phoneNumber: String
    private lateinit var birthdate: String
    private lateinit var UserID: String

    fun addUserToDB(hashMap: HashMap<String, String>, userID: String){
        db.collection("user").document(userID).set(hashMap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        intnt = getIntent()
        userData = intnt.getSerializableExtra("data") as User
        UserID = firebaseAuth.currentUser?.uid.toString()

        if(userData == null){
            Log.d("debug", "gad data")
        }else if(userData != null){
            name = userData.username
            email = userData.email
            phoneNumber = userData.phoneNumber
            birthdate = userData.birthDate

            val user = hashMapOf(
                "name" to name,
                "email" to email,
                "phoneNumber" to phoneNumber,
                "birthdate" to birthdate
            )
            addUserToDB(user, UserID)
        }
    }
}