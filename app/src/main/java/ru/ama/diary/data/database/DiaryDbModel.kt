package ru.ama.diary.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ama.diary.data.database.DiaryDBModel.Companion.mTableName

@Entity(tableName = mTableName)
data class DiaryDBModel(
    val idFromJson: Int,
    @ColumnInfo(name = "date_start")
    val dateStart: Long,
    @ColumnInfo(name = "date_finish")
    val dateFinish: Long,
    val name: String,
    val description: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    companion object {
        const val mTableName = "todo_table"
    }
}