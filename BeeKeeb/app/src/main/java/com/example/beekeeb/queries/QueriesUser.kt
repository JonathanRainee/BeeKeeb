package com.example.beekeeb.queries

import com.example.beekeeb.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QueriesUser {
    companion object{
        fun getUser(userID: String): User {
            val db = Firebase.firestore
            var user = User("","","","","");
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnSuccessListener { doc ->
                if(doc != null){
                    val username = doc.data?.get("user_name").toString()
                    val email = doc.data?.get("user_email").toString()
                    val phoneNum = doc.data?.get("user_phone").toString()
                    val birthdate = doc.data?.get("user_birthdate").toString()
                    val profilePic = doc.data?.get("user_profile_picture").toString()
                    user = User(username,email,phoneNum,birthdate,profilePic);
                }
            }
            return user;
        }
    }
}