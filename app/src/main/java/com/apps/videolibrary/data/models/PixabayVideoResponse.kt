package com.apps.videolibrary.data.models

import com.google.gson.annotations.SerializedName


data class PixabayVideoResponse (

  @SerializedName("total"     ) var total     : Int?            = null,
  @SerializedName("totalHits" ) var totalHits : Int?            = null,
  @SerializedName("hits"      ) var hits      : ArrayList<Hit> = arrayListOf()

)