package expense.management.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import expense.management.HistoryTypes

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val count: String,
    val type: HistoryTypes
)