package expense.management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import expense.management.adapter.HistoryAdapter
import expense.management.databinding.ActivityMainBinding
import expense.management.db.HistoryItemDao
import expense.management.entities.HistoryItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "debugTag"
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private var dataList: MutableList<HistoryItem> = mutableListOf()
    private lateinit var database: AppDatabase
    private lateinit var incomeTV: TextView
    private lateinit var expenseTV: TextView
    private lateinit var currentMoneyTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)
        init()
        setHistoryRecycler()
        GlobalScope.launch {
            // Добавление при запуске всех элементов в историю
            val historyItems = database.historyItemDao().getAll()

            runOnUiThread {
                dataList.clear()
                dataList.addAll(historyItems)
                adapter.notifyDataSetChanged()
                Log.v(TAG, "Restore")
            }
        }
        addIncome()
        addExpense()
    }

    private fun init() {
        incomeTV = binding.addInc
        expenseTV = binding.addExp
        currentMoneyTV = binding.currentMoney

        currentMoneyTV.text = SharedPreferencesHelper.getMoney(this).toString() // Восстановление денег
    }

    private fun addIncome() {
        val addIncButton = binding.addIncButton
        addIncButton.isActivated = false

        incomeTV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim() // Удаляем пробелы и проверяем наличие текста

                addIncButton.isEnabled = text.isNotEmpty()
                addIncButton.setOnClickListener {
                    addHistoryItem("Income", incomeTV.text.toString(), HistoryTypes.INCOME)
                    incomeTV.text = ""
                    addIncButton.isActivated = false
                    calcIncomeMoney(text.toInt())
                }
            }
        })
    }
    private fun calcIncomeMoney(enterValue: Int) {
        if (enterValue.toString().isNotEmpty()) {
            var currentMoneyCount = currentMoneyTV.text.toString().toInt()
            val calcMoney = currentMoneyCount + enterValue
            currentMoneyTV.text = calcMoney.toString()
            SharedPreferencesHelper.setMoney(this, calcMoney)
        }
    }

    private fun addExpense() {
        val addExpButton = binding.addExpButton
        addExpButton.isActivated = false

        expenseTV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()

                addExpButton.isEnabled = text.isNotEmpty()
                addExpButton.setOnClickListener {
                    addHistoryItem("Expense", expenseTV.text.toString(), HistoryTypes.EXPENSE)
                    expenseTV.text = ""
                    addExpButton.isActivated = false
                    calcExpenseMoney(text.toInt())
                }
            }
        })
    }
    private fun calcExpenseMoney(enterValue: Int) {
        if (enterValue.toString().isNotEmpty()) {
            var currentMoneyCount = currentMoneyTV.text.toString().toInt()
            val calcMoney = currentMoneyCount - enterValue
            currentMoneyTV.text = calcMoney.toString()
            SharedPreferencesHelper.setMoney(this, calcMoney)
        }
    }

    private fun addHistoryItem(name: String, count: String, types: HistoryTypes) {
        val historyItem = HistoryItem(name = name, count = count, type = types)
        // Сохранение элемента в базе данных
        GlobalScope.launch {
            database.historyItemDao().insert(historyItem)
        }

        dataList.add(0, historyItem)
        adapter.notifyItemInserted(0)
    }

    private fun setHistoryRecycler() {
        recyclerView = binding.recycleHistory
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HistoryAdapter(dataList)
        recyclerView.adapter = adapter
    }
}