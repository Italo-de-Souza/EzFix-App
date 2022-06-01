package com.ezfix.ezfixaplication.configuration

import com.ezfix.ezfixaplication.model.NovoUsuario
import com.ezfix.ezfixaplication.data.*
import com.ezfix.ezfixaplication.model.PerfilAssistencia
import okhttp3.MultipartBody
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.net.URI
import java.util.ArrayList

interface Backend {

    @POST("/auth")
    fun logar(@Body formLogin: FormLogin): Call<Token>

    @POST("/auth/novoSolicitante")
    fun criarUsuario(@Body novoUsuario: NovoUsuario): Call<Void>;

    @GET("/assistencia/card-assistencia")
    fun getCardsAssistencias(@Query("page") page : Int): Call<CardAssistencia>;

    @GET("/assistencia/perfil/{id}")
    fun getImagem(@Path("id") id : Long) : Call<ResponseBody>;

    @GET("/assistencia/perfil-assistencia/{id}")
    fun getAssistencia(@Path("id") id: Long) : Call<PerfilAssistencia>

    @GET("/solicitante/perfil")
    fun getLogado(@Header("Authorization") token: String) : Call<UserLogado>

    @GET("/solicitante/perfil/{id}")
    fun getFotoPerfil(@Path("id") cpf : String) : Call<ResponseBody>

    @Multipart
    @POST("/solicitante/perfil")
    fun pathFotoPerfil( @Part img : MultipartBody.Part,
                        @Header("Authorization") token: String) : Call<ResponseBody>

    @GET("/orcamentos/pedidos")
    fun getPedidos(@Header("Authorization") token: String) : Call<ArrayList<CardPedido>>

    @GET("/produtos")
    fun getProdutos() : Call<ArrayList<Produtos>>

    @GET("/produtos/tipos")
    fun getProdTipos() : Call<ArrayList<InfoProduto>>

    @GET("/produtos/marcas/{id}")
    fun getProdMarcas(@Path ("id") id : Long) : Call<ArrayList<InfoProduto>>

    @GET("/produtos/modelos/{idTipo}/{idMarca}")
    fun getProdModelos(@Path ("idTipo") idTipo : Long,
                       @Path ("idMarca") idMarca : Long) : Call<ArrayList<InfoProduto>>

    @POST("orcamentos/novo/{idAssistencia}")
    fun sendOrcamento(@Body orcamento: ArrayList<Orcamento>,
                      @Header("Authorization") token: String,
                      @Path("idAssistencia") idAssistencia : Long) : Call<ResponseBody>;



}
