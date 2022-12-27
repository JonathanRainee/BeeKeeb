package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityUpdateprofileBinding
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateprofileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpBtn: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: User
    private lateinit var usernameParts: List<String>

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etBirthdate: DatePicker
    private lateinit var etABout: EditText
    private val userInstance = FirebaseAuth.getInstance()

    private lateinit var username: String
    private lateinit var phoneNum: String
    private lateinit var email: String
    private lateinit var birthdate: String
    private lateinit var bio: String
    private lateinit var following: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        etFirstName = binding.etFirstName
        etLastName = binding.etLastName
        etPhone = binding.etPhone
        etBirthdate = binding.etBirthdate
        etABout = binding.etAbout

        val reference = db.collection("users").document(userInstance.uid.toString())

        reference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                if(snapshot != null){
                    username = snapshot.data?.get("user_name").toString()
                    email = snapshot.data?.get("user_email").toString()
                    phoneNum = snapshot.data?.get("user_phone").toString()
                    birthdate = snapshot.data?.get("user_birthdate").toString()
                    val profilePic = snapshot.data?.get("user_profile_picture").toString()
                    bio = snapshot.data?.get("user_about").toString()
                    following = snapshot.data?.get("following") as List<String>
                    currUser = User(username, bio, email, phoneNum, birthdate, profilePic, following)
                    usernameParts = currUser.username.split(" ")
                    Log.d("firstname", usernameParts[0])
                    Log.d("lastname", usernameParts[0])
                }
            } else {
                Log.d("snapshot null", "Current data: null")
            }
        }

        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassActivity::class.java)
            startActivity(intent)
        }

        binding.btnUpdate.setOnClickListener {
            Log.d("kntl", "salkdfna")
            val firstname = etFirstName.text.toString()
            val lastname = etLastName.text.toString()
            val usernameUpd = firstname + " " + lastname
            val day = etBirthdate.dayOfMonth.toString()
            val month = (etBirthdate.month+1).toString()
            val year = etBirthdate.year.toString()
            val birthdateUpd = month + "-" + day + "-" + year
            val phoneUpd = etPhone.text.toString()
            val about = etABout.text.toString()

            if(firstname != "" && lastname != "" && about != "" && phoneUpd != ""){
                Log.d("dbg username", usernameUpd+" "+username)
                Log.d("dbg birth", birthdateUpd+" "+birthdate)
                Log.d("dbg phone", phoneUpd+" "+phoneNum)
                Log.d("dbg about", about+" "+bio)
                if(usernameUpd == username && birthdateUpd == birthdate && phoneUpd == phoneNum && about == bio){
                    Toast.makeText(this, "Please input a new profile data", Toast.LENGTH_SHORT).show()
                }
                else{
                    reference.update(mapOf(
                        "user_name" to usernameUpd,
                        "user_birthdate" to birthdateUpd,
                        "user_phone" to phoneUpd,
                        "user_about" to about
                    ))
                    this.finish()
                    val intent = Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                }
            }else{
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }




    }
}