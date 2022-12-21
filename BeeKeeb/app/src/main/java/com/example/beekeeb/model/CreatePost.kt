package com.example.beekeeb.model

import java.io.Serializable

data class CreatePost(val title: String, val thread: String, val tag: String, val path: String, val author: String, val like: Int): Serializable {}