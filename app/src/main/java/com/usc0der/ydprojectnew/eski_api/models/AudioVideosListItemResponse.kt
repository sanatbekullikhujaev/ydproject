package com.usc0der.ydprojectnew.connection.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "audiosList")
class AudiosListItemResponse : Serializable, BaseMedia() {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int = 0

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String? = null

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String? = null

    @ColumnInfo(name = "image_file")
    @SerializedName("image_file")
    var image_file: String? = null

}

@Entity(tableName = "videosList")
class VideosListItemResponse : Serializable, BaseMedia() {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int = 0

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String? = null

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String? = null

    @ColumnInfo(name = "image_file")
    @SerializedName("image_file")
    var image_file: String? = null
    override fun toString(): String {
        return "VideosListItemResponse(id=$id, title=$title, description=$description, image_file=$image_file)"
    }
}
open class BaseMedia