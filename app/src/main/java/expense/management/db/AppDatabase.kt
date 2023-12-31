package expense.management

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import expense.management.db.HistoryItemDao
import expense.management.entities.HistoryItem

@Database(entities = [HistoryItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyItemDao(): HistoryItemDao

    companion object {
        private const val DATABASE_NAME = "expense_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}
