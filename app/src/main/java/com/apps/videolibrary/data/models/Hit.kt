package com.apps.videolibrary.data.models

import com.apps.videolibrary.data.db.HitsEntity
import com.google.gson.annotations.SerializedName


data class Hit (

  @SerializedName("id"           ) var id           : Int?    = null,
  @SerializedName("pageURL"      ) var pageURL      : String? = null,
  @SerializedName("type"         ) var type         : String? = null,
  @SerializedName("tags"         ) var tags         : String? = null,
  @SerializedName("duration"     ) var duration     : Int?    = null,
  @SerializedName("videos"       ) var videos       : Videos? = Videos(),
  @SerializedName("views"        ) var views        : Int?    = null,
  @SerializedName("downloads"    ) var downloads    : Int?    = null,
  @SerializedName("likes"        ) var likes        : Int?    = null,
  @SerializedName("comments"     ) var comments     : Int?    = null,
  @SerializedName("user_id"      ) var userId       : Int?    = null,
  @SerializedName("user"         ) var user         : String? = null,
  @SerializedName("userImageURL" ) var userImageURL : String? = null

){
  fun modelToEntity(id: Int): HitsEntity {
    return HitsEntity(
      id = id,
      apiId = this.id,
      pageURL = this.pageURL,
      type = this.type,
      tags = this.tags,
      url = this.videos?.medium?.url,
      views = this.views,
      downloads = this.downloads,
      likes = this.likes,
      comments = this.comments,
      userId = this.userId,
      user = this.user,
      userImageURL = this.userImageURL
    )
  }
}