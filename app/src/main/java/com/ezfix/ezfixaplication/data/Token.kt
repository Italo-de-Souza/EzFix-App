package com.ezfix.ezfixaplication.data

import java.io.Serializable

data class Token (
    val tipo : String,
    val token : String) : Serializable;
