package edu.bluejack22_1.BeeKeeb.frag

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.IntegerWidth
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.*
import com.example.beekeeb.adapter.postAdapter
import com.example.beekeeb.databinding.FragmentAddBinding
import com.example.beekeeb.databinding.FragmentProfileUserBinding
import com.example.beekeeb.model.Post
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import edu.bluejack22_1.BeeKeeb.util.Util
import java.io.File
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!


    private val userInstance = FirebaseAuth.getInstance()
    private lateinit var path: Uri

    private lateinit var usernameTV: TextView
    private lateinit var profileImg: ImageView
    private lateinit var settingIV: ImageView
    private lateinit var logoutBtn: Button
    private lateinit var postQuery: Query

    private lateinit var currUser: User

    private lateinit var postData: ArrayList<Post>
    private lateinit var adapterPost: postAdapter
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
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)

        postQuery = db.collection("posts").whereEqualTo("author", userInstance.uid.toString())

        postData = ArrayList()
        recyclerView = binding.postRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        postQuery.get().addOnSuccessListener { posts ->
            Log.d("result", "success")
            for (post in posts){
                val title = post.get("title").toString()
                val author = post.get("author").toString()
                val thread = post.get("thread").toString()
                val tag = post.get("tag").toString()
                val path = post.get("thread").toString()
                val like = Integer.parseInt(post.get("like").toString())
                Log.d("post", title+" "+thread+" "+tag+" "+path+" "+author+" "+like)
                postData.add(Post(title, thread, tag, path, author, like))
            }
        }

        adapterPost = postAdapter(postData)
        recyclerView.adapter = adapterPost

        val reference = db.collection("users").document(userInstance.uid.toString())
        profileImg = binding.profileImv
        usernameTV = binding.usernameTv
        settingIV = binding.settingProfileID
        logoutBtn = binding.logoutBtn

        profileImg.setOnClickListener{
            pickImg()
        }


        logoutBtn.setOnClickListener{
            userInstance.signOut()
            activity?.finish()
            val intent = Intent(context, LoginEmailActivity::class.java)
            startActivity(intent)
        }



        settingIV.setOnClickListener{
//            changeFragment(ProfileFragment())
            val intent = Intent(context, UpdateProfileActivity::class.java)
            startActivity(intent)
        }


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
                    usernameTV.setText("Hello, ${currUser.username}")
                    if(currUser.profilePicture != ""){
                        Picasso.get().load(currUser.profilePicture).fit().centerCrop().into(profileImg)
                    }

                }
            } else {
                Log.d("snapshot null", "Current data: null")
            }
        }

        return binding.root
    }

    private fun pickImg(){
        var intent = Intent()
        intent.type = "image/* video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        pickImgFromGallery.launch(intent)
    }

    private var pickImgFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null){
            path = result.data!!.data!!
            val fileName = Util.getImageName(path.toString());
            val storagePath = currUser.email+"/profilePicture/$fileName"
            val ref = storage.getReference(storagePath)
            ref.putFile(path).addOnSuccessListener {
                storage.reference.child(storagePath).downloadUrl.addOnSuccessListener {
                    updateProfilePic(userInstance.uid.toString(), it.toString())
                }.addOnFailureListener {
                    Log.d("error", "error while getting download url ")
                }
            }.addOnFailureListener{
                Log.d("error", "error while adding to storage")
            }
        }
    }

    private fun updateProfilePic(userID: String, imgUrl: String){
        db.collection("users").document(userID).update("user_profile_picture", imgUrl)
    }

    private fun changeFragment(frag : Fragment){
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, frag)
        fragmentTransaction?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}