package com.example.beekeeb.model

import java.io.Serializable

data class Post(val title: String, val thread: String, val tag: String, val path: String, val authorName: String, val authorPicture: String, val like: Int): Serializable {}