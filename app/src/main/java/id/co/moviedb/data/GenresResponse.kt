package id.co.moviedb.data

import com.google.gson.annotations.SerializedName


/**
 * Created by pertadima on 24,March,2019
 */

data class GenresResponse(
    @SerializedName("genres") val genres: List<GenreModel>?
)

data class GenreModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
)