package edu.bluejack22_1.BeeKeeb.frag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.PostDetailActivity
import com.example.beekeeb.R
import com.example.beekeeb.adapter.postAdapter
import com.example.beekeeb.databinding.FragmentPostsearchBinding
import com.example.beekeeb.model.Post
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack22_1.BeeKeeb.util.Util

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostsearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostsearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentPostsearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

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
        // Inflate the layout for this fragment
        _binding = FragmentPostsearchBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()

        recyclerView = binding.postRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        postData = ArrayList()

        binding.spinTag.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                postData.clear()
                val item = parent?.getItemAtPosition(position) as String
                val path = db.collection("posts").orderBy("tag").startAt(item).endAt(item+"\uf8ff")
                val docs = path.get()
                docs.addOnSuccessListener { docs ->
                    if(docs != null) {
                        for (doc in docs){
                            val title = doc.get("title").toString();
                            val thread = doc.get("thread").toString();
                            val path = doc.get("path").toString();
                            val like = doc.get("like").toString().toInt();
                            val tag = doc.get("tag").toString();
                            val author = doc.get("author").toString();
                            val uid = doc.get("uid").toString()
                            val docRef = db.collection("users").document(author)
                            docRef.get().addOnSuccessListener { doc ->
                                if (doc != null){
                                    val authorID = doc.id
                                    val username = doc.data?.get("user_name").toString()
                                    val profilePic = doc.data?.get("user_profile_picture").toString()
                                    Log.d("ini post", title+" "+thread+" "+tag+" "+author+" "+username)
                                    postData.add(Post(title, thread, tag, path, username, authorID, profilePic, like, uid))
                                    adapterPost = postAdapter(postData)
                                    recyclerView.adapter = adapterPost
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
                }.addOnFailureListener{
                    Util.dismissLoadingDialog()
                    Toast.makeText(context, R.string.postNotFound, Toast.LENGTH_LONG).show()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        binding.sendBtn.setOnClickListener{
            context?.let { Util.loadingDialog(it) }
            postData.clear()
            var keyword = binding.etKeyword.text.toString()
            val regex = Regex("^"+keyword+".*")
            Log.d("keyword", keyword)
//            val path = db.collection("posts").where(FieldPath.of("user_name"), "==", regex)

            val path2 = db.collection("posts").orderBy("title").startAt(keyword).endAt(keyword+"\uf8ff")

            val docs = path2.get()
            docs.addOnSuccessListener { docs ->
                for (doc in docs){
                    val title = doc.get("title").toString();
                    val thread = doc.get("thread").toString();
                    val path = doc.get("path").toString();
                    val like = doc.get("like").toString().toInt();
                    val tag = doc.get("tag").toString();
                    val author = doc.get("author").toString();
                    val uid = doc.get("uid").toString()
                    val docRef = db.collection("users").document(author)
                    docRef.get().addOnSuccessListener { doc ->
                        if (doc != null){
                            val authorID = doc.id
                            val username = doc.data?.get("user_name").toString()
                            val profilePic = doc.data?.get("user_profile_picture").toString()
                            Log.d("ini post", title+" "+thread+" "+tag+" "+author+" "+username)
                            postData.add(Post(title, thread, tag, path, username, authorID, profilePic, like, uid))
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



//        return inflater.inflate(R.layout.fragment_postsearch, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostsearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostsearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}