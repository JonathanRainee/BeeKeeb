package edu.bluejack22_1.BeeKeeb.frag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.beekeeb.R
import com.example.beekeeb.databinding.FragmentProfileBinding
import com.example.beekeeb.databinding.FragmentProfileUserBinding
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var updateBtn: AppCompatButton
    private val db = Firebase.firestore
    private val userInstance = FirebaseAuth.getInstance()

    private lateinit var currUser: User
    private lateinit var usernameParts: List<String>

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etBirthdate: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        etFirstName = binding.etFirstName
        etLastName = binding.etLastName
        etPhone = binding.etPhone
        etBirthdate = binding.etBirthdate
        updateBtn = binding.btnSignUp

        val reference = db.collection("users").document(userInstance.uid.toString())
        reference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                if(snapshot != null){
                    val username = snapshot.data?.get("user_name").toString()
                    val email = snapshot.data?.get("user_email").toString()
                    val phoneNum = snapshot.data?.get("user_phone").toString()
                    val birthdate = snapshot.data?.get("user_birthdate").toString()
                    val profilePic = snapshot.data?.get("user_profile_picture").toString()
                    currUser = User(username, email, phoneNum, birthdate, profilePic)
                    usernameParts = currUser.username.split(" ")
                    Log.d("firstname", usernameParts[0])
                    Log.d("lastname", usernameParts[0])
                    etFirstName.hint = usernameParts[0]
                    etLastName.setHint(usernameParts[1])

                }
            } else {
                Log.d("snapshot null", "Current data: null")
            }
        }

        updateBtn.setOnClickListener{
            val firstname = etFirstName.text.toString()
            val lastname = etLastName.text.toString()
            val username = firstname + " " + lastname
            val day = etBirthdate.dayOfMonth.toString()
            val month = etBirthdate.month.toString()
            val year = etBirthdate.year.toString()
            val birthdate = month + "-" + day + "-" + year
            val phone = etPhone.text.toString()
            if(firstname != ""){
                reference.update(mapOf(
                    "user_name" to username,
                    "user_birthdate" to birthdate,
                    "user_phone" to phone
                )).addOnSuccessListener {
                    Toast.makeText(context, "Success update profile!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to update profile!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Firstname cannot be empty!", Toast.LENGTH_SHORT).show()
            }

        }




        return inflater.inflate(R.layout.fragment_profile, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}