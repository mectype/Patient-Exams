package com.example.odtpatients

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetAvatars {

    @Headers("Authorization: xQoHeaHThmJYlRwVKweDz4EocrpgAdzufWVLIPkdSRFtvIcyYqgBIK2F")
    @GET("v1/search")
    fun getPexelsImages(@Query("query") queryParam: String): Single<PexelsWrapper>

}

data class PexelsWrapper(val photos: List<PhotoResponse>)
data class PhotoResponse(val url: String, val src: ImageFile)
data class ImageFile(val small: String)