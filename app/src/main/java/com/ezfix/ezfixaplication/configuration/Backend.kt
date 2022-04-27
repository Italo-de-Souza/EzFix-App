package com.ezfix.ezfixaplication.configuration

import com.ezfix.ezfixaplication.model.NovoUsuario
import com.ezfix.ezfixaplication.data.*
import com.ezfix.ezfixaplication.model.PerfilAssistencia
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

interface Backend {

    @POST("/auth")
    fun logar(@Body formLogin: FormLogin): Call<Token>

    @POST("/auth/novoSolicitante")
    fun criarUsuario(@Body novoUsuario: NovoUsuario): Call<Void>;

    @GET("/assistencia/card-assistencia")
    fun getCardsAssistencias(@Query("page") page : Int): Call<CardAssistencia>;

    @GET("/assistencia/perfil/{id}")
    fun getImagem(@Path("id") id : Int) : Call<ResponseBody>;

    @GET("/assistencia/{id}")
    fun getAssistencia(@Path("id") id: Int) : Call<PerfilAssistencia>
}
