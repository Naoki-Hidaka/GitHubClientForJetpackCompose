package jp.dosukoi.githubclient.domain.entity.auth

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Auth(
    @SerialName("access_token")
    val accessToken: String
) : Parcelable

@Entity
data class AuthEntity(
    @PrimaryKey
    val id: Long,
    val accessToken: String
)

fun main() {
    val result = RakutenAreaResult(Area(listOf()))
    val hoge =
        result.areaClasses.largeAreas.find { it.largeClass[0].largeClassCode == "japan" }?.let {
            it.largeClass.drop(1).flatMap {
                it.middleAreas!!.map { it.middleClass[0].middleClassName }
            }
        }

    val selectedMiddleClassName = ""
    val foo =
        result.areaClasses.largeAreas.find { it.largeClass[0].largeClassCode == "japan" }?.let {
            it.largeClass.drop(1).flatMap {
                it.middleAreas?.find { it.middleClass[0].middleClassName == selectedMiddleClassName }
                    ?.let {
                        it.middleClass.drop(0).flatMap {
                            it.smallAreas!!.map { it.smallClass[0].smallClassName }
                        }
                    } ?: emptyList()
            }
        }
    println(result)
}

data class RakutenAreaResult(
    val areaClasses: Area
)

data class Area(

    val largeAreas: List<LargeArea>
)


data class LargeArea(
    val largeClass: List<LargeClass>
)

data class LargeClass(
    val largeClassCode: String? = null,
    val largeClassName: String? = null,

    val middleAreas: List<MiddleArea>? = null,
)


data class MiddleArea(
    val middleClass: List<MiddleClass>
)


data class MiddleClass(
    val middleClassCode: String? = null,
    val middleClassName: String? = null,

    val smallAreas: List<SmallArea>? = null
)


data class SmallArea(
    val smallClass: List<SmallClass>
)


data class SmallClass(
    val smallClassCode: String? = null,
    val smallClassName: String? = null
)
