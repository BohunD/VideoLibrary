package com.apps.videolibrary.data.models

import com.google.gson.annotations.SerializedName


data class Tiny (

  @SerializedName("url"       ) var url       : String? = null,
  @SerializedName("width"     ) var width     : Int?    = null,
  @SerializedName("height"    ) var height    : Int?    = null,
  @SerializedName("size"      ) var size      : Int?    = null,
  @SerializedName("thumbnail" ) var thumbnail : String? = null

)