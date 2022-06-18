package com.tiooooo.core.model

data class UserModel(
    val name: String,
    val email: String,
    val accessToken: String,
    val isLogin: Boolean
)