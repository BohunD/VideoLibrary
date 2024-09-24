package com.apps.videolibrary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apps.videolibrary.data.models.Hit
import com.apps.videolibrary.data.models.Medium
import com.apps.videolibrary.data.models.Videos
import kotlin.time.Duration

@Entity(tableName = "Hits")

data class HitsEntity(
    @PrimaryKey var id: Int?=null,
    var apiId: Int? = null,
    var pageURL: String? = null,
    var type: String? = null,
    var tags: String? = null,
    var duration: Int? = null,
    var url: String? = null,
    var views: Int? = null,
    var downloads: Int? = null,
    var likes: Int? = null,
    var comments: Int? = null,
    var userId: Int? = null,
    var user: String? = null,
    var userImageURL: String? = null,
)
{

    fun entityToModel(): Hit {
        return Hit(
            id = this.apiId,
            pageURL = this.pageURL,
            type = this.type,
            tags = this.tags,
            duration = this.duration,
            videos = Videos(medium = Medium(url=this.url)),
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