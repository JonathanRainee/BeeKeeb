package com.example.beekeeb.model

import java.io.Serializable


class User (val username: String, val about: String, val email: String, val phoneNumber: String, val birthDate: String, val profilePicture: String, val likedBy: List<String>): Serializable {}