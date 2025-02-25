package com.grevi.aywapet.datasource.services

import com.grevi.aywapet.datasource.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/api/pet/get")
    suspend fun getPet(@Header("x-access-token") token : String) : Response<PetResponse>

    @GET("/api/types/get")
    suspend fun getAnimal(@Header("x-access-token") token : String) : Response<AnimalResponse>

    @GET("/api/pet/get-pet/{id}")
    suspend fun getPetId(@Header("x-access-token") token : String, @Path("id") idPet : String) : Response<PetDetailResponse>

    @FormUrlEncoded
    @POST("/api/keep/post")
    suspend fun postKeep(@Header("x-access-token") token : String, @Field("pet_id") petId : String, @Field("users_id") usersId : String) : Response<KeepPostResponse>

    @GET("/api/users/auth/verify")
    suspend fun getEmailVerify(@Query("email") email : String) : Response<VerifyResponse>

    @FormUrlEncoded
    @POST("/api/users/post")
    suspend fun createUser(@Field("name") name : String,
                           @Field("no_hp") phone : String,
                           @Field("alamat") alamat : String,
                           @Field("email") email: String,
                           @Field("uid") uid : String
    ) : Response<PostUserResponse>

    @GET("/api/keep/find/{id}")
    suspend fun getKeepPet(@Header("x-access-token") token : String, @Path("id") idUser : String) : Response<GetKeepResponse>
    
    @GET("/api/keep/success/user/{id}")
    suspend fun getSuccessKeep(@Header("x-access-token") token : String, @Path("id") idUser : String) : Response<GetKeepSuccessResponse>

    @GET("/api/pet/get-type")
    suspend fun getPetByType(@Header("x-access-token") token : String, @Query("id") idType : String) : Response<PetResponse>

    @GET("/api/province")
    suspend fun getProvince() : Response<ProvinceResponse>

    @GET("/api/kabupaten/{id}")
    suspend fun getKabupaten(@Path("id") id : String) : Response<KabupatenResponse>

    @GET("/api/kecamatan/{id}")
    suspend fun getKecamatan(@Path("id") id : String) : Response<KecamatanResponse>

    @GET("/api/pet/search")
    suspend fun getSearchPet(@Header("x-access-token") token: String, @Query("ras") ras : String) : Response<PetResponse>
}