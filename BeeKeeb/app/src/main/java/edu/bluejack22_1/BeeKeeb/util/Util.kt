package edu.bluejack22_1.BeeKeeb.util

import android.content.Context
import android.net.Uri
import com.example.beekeeb.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Util {

    companion object{
        fun getImageName(path: String): String{
            return path.substring(path.lastIndexOf("/") + 1)
        }

        fun uploadImage(email: String, filePath: Uri, context : Context ,callback: (imageUrl: String) -> Unit){
            val mediaName = Util.getImageName(filePath.path.toString())
            val storageRef = FirebaseStorage.getInstance().getReference(email + "/media/"+mediaName)

            storageRef.putFile(filePath).addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    callback.invoke(imageUrl.toString())
                }
            }.addOnFailureListener{
                return@addOnFailureListener
            }
        }

        fun uploadPost(currID: String, post: Post){
            val post = hashMapOf(
                "author" to currID,
                "title" to post.title,
                "thread" to post.thread,
                "tag" to post.tag,
                "path" to post.path,
                "like" to 0
            )
            val path = "users/"+currID+"/posts"
            val postPath = "posts"
            FirebaseFirestore.getInstance().collection(postPath).add(post)
            FirebaseFirestore.getInstance().collection(path).add(post)
        }
    }

}