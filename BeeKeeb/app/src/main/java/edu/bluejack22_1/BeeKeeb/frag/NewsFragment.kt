package edu.bluejack22_1.BeeKeeb.frag

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.PostDetailActivity
import com.example.beekeeb.R
import com.example.beekeeb.adapter.NewsAdapter
import com.example.beekeeb.databinding.FragmentNewsBinding
import com.example.beekeeb.model.News
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack22_1.BeeKeeb.util.Util

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsData: ArrayList<News>
    private lateinit var adapterNews: NewsAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: FirebaseUser

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

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        currUser = FirebaseAuth.getInstance().currentUser!!

        context?.let { Util.loadingDialog(it) }

        recyclerView = binding.newsRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        newsData = ArrayList()
        val path = db.collection("users").document(currUser.uid).collection("news")
        val docs = path.get()
        docs.addOnSuccessListener { documents ->
            for (d in documents){
                val sender = d.get("sender").toString()
                val receiver = d.get("receiver").toString()
                val news = d.get("news").toString()
                val postID = d.get("postID").toString()

                val userRef = db.collection("users").document(sender)
                val userData = userRef.get()
                userData.addOnSuccessListener { dataUser ->
                    val username = dataUser.data?.get("user_name").toString()
                    val senderImgProfile = dataUser.data?.get("user_profile_picture").toString()
                    val newsFinal = username+" " +news
                    newsData.add(News(sender, senderImgProfile,receiver, newsFinal, postID))
                    adapterNews = NewsAdapter(newsData)
                    recyclerView.adapter = adapterNews
//                    Util.dismissLoadingDialog()
                    adapterNews.onItemClicked = {
                        val intent = Intent(context, PostDetailActivity::class.java)
                        val postPath = db.collection("posts").document(it.postID)
                        val postData = postPath.get()
                        postData.addOnSuccessListener { docx ->
                            val authorUID = docx.data?.get("author").toString()
                            intent.putExtra("uid", it.postID)
                            intent.putExtra("authorUID", authorUID)
                            startActivity(intent)
                        }
                    }
                }
            }
            Util.dismissLoadingDialog()
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}