package com.example.beekeeb

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.beekeeb.databinding.AcitivityLoginoptionBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.BeeKeeb.util.AlarmReceiver

class LoginOptionActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: AcitivityLoginoptionBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcitivityLoginoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                  .requestIdToken(getString(R.string.default_web_client_id))
                  .requestEmail().build()
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("600993970348-d1krkh9ctsvme7oardr6fvp3j1e3b174.apps.googleusercontent.com").requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnEmail.setOnClickListener(){
            val intentEmail = Intent(this, LoginEmailActivity::class.java)
            startActivityForResult(intentEmail, 2)
        }

        binding.googleBtn.setOnClickListener{
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent

        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                Log.w(ContentValues.TAG, "google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){task->
            if(task.isSuccessful){

            }
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                signInAttempt(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInAttempt(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val failMsg = R.string.google_auth_error

        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val uid = auth.currentUser?.uid.toString()
                val data = hashMapOf(
                    "user_id" to uid,
                    "user_email" to account.email,
                    "user_password" to "",
                    "user_name" to account.displayName,
                    "user_birthdate" to "",
                    "user_profile_picture" to "",
                    "user_about" to ""
                )

                db.collection("users").document(uid).set(data).addOnSuccessListener {
                    this.finish()
                    val intent = Intent(this, MainPageActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener{
                        e-> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
                AlarmReceiver.sendNotification(this)
            }else{
                Toast.makeText(this, failMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}