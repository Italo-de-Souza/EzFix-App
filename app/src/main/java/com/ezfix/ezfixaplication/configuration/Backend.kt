package com.ezfix.ezfixaplication.configuration

import android.graphics.Bitmap
import com.ezfix.ezfixaplication.cadastro.model.NovoUsuario
import com.ezfix.ezfixaplication.data.*
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Call

interface Backend {

    @POST("/auth")
    fun logar(@Body formLogin: FormLogin): Call<Token>

    @POST("/auth/novoSolicitante")
    fun criarUsuario(@Body novoUsuario: NovoUsuario): Call<Void>;

    @GET("/assistencia/card-assistencia")
    fun getCardsAssistencias(): Call<CardAssistencia>;

    @GET("/assistencia/perfil/{id}")
    fun getImagem(@Path("id") id : Int) : Call<ResponseBody>;
}
