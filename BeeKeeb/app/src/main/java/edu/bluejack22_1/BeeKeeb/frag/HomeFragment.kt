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
import com.example.beekeeb.MainPageActivity
import com.example.beekeeb.PostDetailActivity
import com.example.beekeeb.R
import com.example.beekeeb.SearchActivity
import com.example.beekeeb.adapter.postAdapter
import com.example.beekeeb.databinding.FragmentHomeBinding
import com.example.beekeeb.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.BeeKeeb.util.Util

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var postData: ArrayList<Post>
    private lateinit var adapterPost: postAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: FirebaseUser
    private lateinit var following: List<String>

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        currUser = FirebaseAuth.getInstance().currentUser!!
        context?.let { Util.loadingDialog(it) }

        recyclerView = binding.postRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        postData = ArrayList()
        val path = db.collection("users").document(currUser.uid)

        val docs = path.get()
        docs.addOnSuccessListener { doc ->
            following = doc.data?.get("following") as List<String>


            for (i in following){
                Log.d("following", i)
                val currUserPost = db.collection("posts").orderBy("author").startAt(i).endAt(i+"\uf8ff")
                val documents = currUserPost.get()
                documents.addOnSuccessListener { docs ->

                    for(d in docs){

                        val title = d.get("title").toString();
                        val thread = d.get("thread").toString();
                        val path = d.get("path").toString();
//                    val like = doc.get("like").toString().toInt();
                        val tag = d.get("tag").toString();
                        val author = d.get("author").toString();
                        val uid = d.get("uid").toString()
                        val docRef = db.collection("users").document(author)
                        docRef.get().addOnSuccessListener {
                            if (doc != null){
                                val authorID = it.id
                                val username = it.data?.get("user_name").toString()
                                val profilePic = it.data?.get("user_profile_picture").toString()
                                postData.add(Post(title, thread, tag, path, username, authorID, profilePic, 0, uid))
                                adapterPost = postAdapter(postData)
                                recyclerView.adapter = adapterPost
                                Util.dismissLoadingDialog()
                                adapterPost.onItemClicked = {
                                    val intent = Intent(context, PostDetailActivity::class.java)
                                    intent.putExtra("uid", it.uid)
                                    intent.putExtra("authorUID", it.authorUID)
                                    startActivity(intent)
                                }

                            }else{
                                Log.d("doc not found", "No such document")
                            }
                        }.addOnFailureListener{ e ->
                            Log.d("Error Get User", e.toString())
                        }
                    }


                }
            }
            Util.dismissLoadingDialog()
        }


        binding.searchFAB.setOnClickListener{
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

//        return inflater.inflate(R.layout.fragment_home, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}