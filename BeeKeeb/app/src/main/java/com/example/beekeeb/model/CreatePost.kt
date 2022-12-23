package com.example.beekeeb.model

import com.google.firebase.firestore.FieldValue
import java.io.Serializable

data class CreatePost(val title: String, val thread: String, val tag: String, val path: String, val author: String, val like: Int, val uid: String, val likedBy: List<String>): Serializable {}