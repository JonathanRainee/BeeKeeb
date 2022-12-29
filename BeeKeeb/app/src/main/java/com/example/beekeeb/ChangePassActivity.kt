package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityChangePassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var oldPass: String
    private lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityChangePassBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        user = auth.currentUser!!
        val reference = db.collection("users").document(auth.uid.toString())

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        reference.addSnapshotListener{snapshot, e ->
            if (e != null){
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
                if (snapshot != null){
                    oldPass = snapshot.data?.get("user_password").toString()
                }
            }else{
                Log.d("snapshot null", "Current data: null")
            }
        }

        binding.btnCancel.setOnClickListener {
            this.finish()
        }

        binding.btnUpdate.setOnClickListener {
            val newPass = binding.etPass.text.toString()
            val newConfpass = binding.etConfpass.text.toString()

            val errors = arrayOf<Int>(
                R.string.password_confirm_error,
                R.string.password_old_error,
                R.string.fields_error,
            )
            val successMsg = R.string.success_change_pass

            if(newPass != "" && newConfpass != ""){
                if (newPass != newConfpass){
                    Toast.makeText(this, errors[0], Toast.LENGTH_SHORT).show()

                } else if(newPass == oldPass){
                    Toast.makeText(this, errors[1], Toast.LENGTH_SHORT).show()
                }
                else{
                    user.updatePassword(newPass).addOnCompleteListener{
                        reference.update(mapOf(
                            "user_password" to newPass
                        ))
                        Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show()
                        this.finish()
                    }
                }
            }else{
                Toast.makeText(this, errors[2], Toast.LENGTH_SHORT).show()
            }
        }

    }
}