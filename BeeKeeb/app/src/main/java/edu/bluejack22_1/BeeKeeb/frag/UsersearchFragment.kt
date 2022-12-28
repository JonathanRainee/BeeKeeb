package edu.bluejack22_1.BeeKeeb.frag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.AnotherProfileActivity
import com.example.beekeeb.R
import com.example.beekeeb.adapter.ProfileAdapter
import com.example.beekeeb.databinding.FragmentUsersearchBinding
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsersearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentUsersearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val userInstance = FirebaseAuth.getInstance()

    private lateinit var profileData: ArrayList<User>
    private lateinit var adapterProfile: ProfileAdapter
    private lateinit var recyclerView: RecyclerView

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
        // Inflate the layout for this fragment
        _binding = FragmentUsersearchBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()

        recyclerView = binding.profileRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        profileData = ArrayList()

        binding.searchBtn.setOnClickListener{
            profileData.clear()
            var keyword = binding.etSearch.text.toString()
            val currUserID = userInstance.currentUser?.uid.toString()
            val path = db.collection("users").orderBy("user_name").startAt(keyword).endAt(keyword+"\uf8ff")

            val docs = path.get()
            docs.addOnSuccessListener { docs ->
                for(doc in docs){
                    val username = doc.get("user_name").toString()
                    val about = doc.get("user_about").toString()
                    val email = doc.get("user_email").toString()
                    val phone = doc.get("user_phone").toString()
                    val birthdate = doc.get("user_birthdate").toString()
                    val following = doc.get("following") as List<String>
                    val uid = doc.get("user_id").toString()
                    val profilePic = doc.get("user_profile_picture").toString()
                    if(uid != currUserID){
                        profileData.add(User(username, about, email, phone, birthdate, profilePic, following, uid))
                        adapterProfile = ProfileAdapter(profileData)
                        recyclerView.adapter = adapterProfile
                        adapterProfile.onItemClicked = {
                            val intent = Intent(context, AnotherProfileActivity::class.java)
                            intent.putExtra("profileUID", it.id)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

//        return inflater.inflate(R.layout.fragment_usersearch, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UsersearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsersearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}