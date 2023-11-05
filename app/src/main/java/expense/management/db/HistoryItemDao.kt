package expense.management.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import expense.management.entities.HistoryItem

@Dao
interface HistoryItemDao {
    @Insert
    suspend fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM history_items ORDER BY id DESC")
    suspend fun getAll(): List<HistoryItem>

    @Query("DELETE FROM history_items")
    suspend fun deleteAll()
}
