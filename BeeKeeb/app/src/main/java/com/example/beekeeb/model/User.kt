package com.example.beekeeb.model

import java.io.Serializable


class User (val username: String, val email: String, val phoneNumber: String, val birthDate: String, val profilePicture: String ): Serializable {}