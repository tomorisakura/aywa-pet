package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/api/pet/get")
    suspend fun getPet() : Response<PetResponse>

    @GET("/api/types/get")
    suspend fun getAnimal() : Response<AnimalResponse>

    @GET("/api/pet/get-pet/{id}")
    suspend fun getPetId(@Path("id") id : String) : Response<PetDetailResponse>

    @FormUrlEncoded
    @POST("/api/keep/post")
    suspend fun postKeep(@Field("pet_id") petId : String, @Field("users_id") usersId : String) : Response<KeepPostResponse>

    @GET("/api/users/auth/verify")
    suspend fun getEmailVerify(@Query("email") email : String) : Response<VerifyResponse>

    @FormUrlEncoded
    @POST("/api/users/post")
    suspend fun createUser(@Field("name") name : String,
                           @Field("no_hp") phone : String,
                           @Field("password") password : String,
                           @Field("alamat") alamat : String,
                           @Field("email") email: String,
                           @Field("uid") uid : String
    ) : Response<PostUserResponse>

    @GET("/api/keep/find/{id}")
    suspend fun getKeepPet(@Path("id") idUser : String) : Response<GetKeepResponse>
}