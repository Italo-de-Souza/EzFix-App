package com.ezfix.ezfixaplication.configuration

import com.ezfix.ezfixaplication.data.Token
import com.ezfix.ezfixaplication.data.UserLogado

object Constants {
    val IP_BACKEND = "http://192.168.15.12:8080"
//    val IP_BACKEND = "https://api.ezfix.com.br/"
    lateinit var userLogado : UserLogado;
    lateinit var token : Token;
}