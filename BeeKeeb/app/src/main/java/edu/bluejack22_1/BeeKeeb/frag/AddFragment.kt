package edu.bluejack22_1.BeeKeeb.frag

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.beekeeb.R
import com.example.beekeeb.databinding.FragmentAddBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageView: ImageView
    private lateinit var addbtn: Button
    private lateinit var addImgBtn: Button
    private lateinit var path: Uri

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
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        addbtn = binding.btnAdd
        imageView = binding.ivImg
        addImgBtn = binding.addImgBtn

        val tag = binding.spinTag.selectedItem.toString()

        imageView.visibility = View.GONE

        addImgBtn.setOnClickListener{
            Log.d("start", "debug")
            imageView.visibility = View.GONE
            pickImg()
            Log.d("end", "debug")
        }

        addbtn.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val thread = binding.etThread.text.toString()
            Log.d("start btn", title + " " + thread);
            if(title.isEmpty() || thread.isEmpty()){
                Log.d("if debugs", "debug if")
                Toast.makeText(context, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
            }else{
                Log.d("else if debugs", "debug elseif")
                Log.d("post", title)
                Log.d("post", thread)
                Log.d("post", tag)
                Log.d("else if debugs", "debug end")
                Toast.makeText(context, "New post added", Toast.LENGTH_SHORT).show()
            }
//            Log.d("start btn", "end")
        }

        return binding.root

    }

    private fun pickImg() {
        val intent = Intent()
        intent.type = "image/* video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        pickImgFromGallery.launch(intent)
    }

    private var pickImgFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null){
            path = result.data!!.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, path)
//            imageView.setImageBitmap(bitmap)
            binding.ivImg.visibility =View.VISIBLE
            binding.ivImg.setImageBitmap(bitmap)
            Log.d("imageview", path.toString())

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}