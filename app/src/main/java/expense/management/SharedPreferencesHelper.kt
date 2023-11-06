package expense.management

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val PREF_AMOUNT = "app_money"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setMoney(context: Context, amount: Int) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putInt(PREF_AMOUNT, amount).apply()
    }

    fun getMoney(context: Context): Int {
        val prefs = getSharedPreferences(context)
        return prefs.getInt(PREF_AMOUNT, 0)
    }

}
