package root.iv.ivandroidacademy.data.database.converter

import androidx.room.TypeConverter

class ListStringConverter {

    @TypeConverter
    fun toDb(list: List<String>): String = list.joinToString(separator = ",")

    @TypeConverter
    fun toEntity(string: String): List<String> = string.split(",")
}