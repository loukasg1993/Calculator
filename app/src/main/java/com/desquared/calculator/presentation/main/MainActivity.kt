package com.desquared.calculator.presentation.main

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.desquared.calculator.R
import com.desquared.calculator.databinding.ActivityMainBinding
import com.desquared.calculator.databinding.CurrencyPopUpBinding
import org.mariuszgromada.math.mxparser.Expression
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var popupWindow: PopupWindow
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var calculationList: MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.black))

        subscribeObservers()
        initializeHistoryAdapter()
        setUpListeners()

    }

    private fun setUpListeners(){
        binding.output.movementMethod = ScrollingMovementMethod()

        binding.historyButton.setOnClickListener {
            if (calculationList.isNotEmpty())
                binding.drawerLayout.openDrawer(binding.navigationView)
        }

        binding.buttonClear.setOnClickListener {
            binding.input.text = ""
            binding.output.text = ""
        }

        binding.buttonBracket.setOnClickListener {

            binding.input.text = addToInputText("(")

        }
        binding.buttonBracketR.setOnClickListener {

            binding.input.text = addToInputText(")")

        }

        binding.buttonErase.setOnClickListener {
            val removedLast = binding.input.text.toString().dropLast(1)
            binding.input.text = removedLast
        }

        binding.button0.setOnClickListener {
            binding.input.text = addToInputText("0")
        }
        binding.button1.setOnClickListener {
            binding.input.text = addToInputText("1")
        }
        binding.button2.setOnClickListener {
            binding.input.text = addToInputText("2")
        }
        binding.button3.setOnClickListener {
            binding.input.text = addToInputText("3")
        }
        binding.button4.setOnClickListener {
            binding.input.text = addToInputText("4")
        }
        binding.button5.setOnClickListener {
            binding.input.text = addToInputText("5")
        }
        binding.button6.setOnClickListener {
            binding.input.text = addToInputText("6")
        }
        binding.button7.setOnClickListener {
            binding.input.text = addToInputText("7")
        }
        binding.button8.setOnClickListener {
            binding.input.text = addToInputText("8")
        }
        binding.button9.setOnClickListener {
            binding.input.text = addToInputText("9")
        }
        binding.buttonDot.setOnClickListener {
            binding.input.text = addToInputText(".")
        }
        binding.buttonDivision.setOnClickListener {
            binding.input.text = addToInputText("÷")
        }
        binding.buttonMultiply.setOnClickListener {
            binding.input.text = addToInputText("×")
        }

        binding.buttonSubtraction.setOnClickListener {
            binding.input.text = addToInputText("-")
        }
        binding.buttonAddition.setOnClickListener {
            binding.input.text = addToInputText("+")
        }

        binding.buttonEquals.setOnClickListener {
            showResult()
        }

        binding.exchangePopup.setOnClickListener { view ->
            showPopupWindow(view)
        }
    }

    private fun initializeHistoryAdapter(){
        sharedPreferences = getSharedPreferences("CalculatorPrefs", Context.MODE_PRIVATE)
        calculationList =
            sharedPreferences.getStringSet("calculations", null)?.toMutableList() ?: mutableListOf()

        val adapter = ArrayAdapter(this, R.layout.history_list_item,R.id.text1, calculationList)
        binding.historyListView.adapter = adapter
        binding.historyListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = calculationList[position]
            binding.input.text = selectedItem.split("\n")[0]
            binding.output.text = selectedItem.split("\n")[1]
            binding.output.setTextColor(ContextCompat.getColor(this, R.color.orange))
        }
        binding.clearHistoryButton.setOnClickListener {
            calculationList.clear()
            val sharedPreferences = getSharedPreferences("CalculatorPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("calculations").apply()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show()
            binding.drawerLayout.closeDrawer(binding.navigationView)
        }
    }

    private fun addHistoryItem(item: String) {

        if (calculationList.isEmpty()) {
            calculationList.add(0, item)
        } else {
            if(!calculationList.contains(item))
                calculationList.add(calculationList.size, item)
        }
        if (calculationList.size > 6) {
            calculationList.removeAt(0)
        }

        sharedPreferences.edit().putStringSet("calculations", HashSet(calculationList)).apply()
        (binding.historyListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    private fun showPopupWindow(anchorView: View) {
        var bindingPopUp = CurrencyPopUpBinding.inflate(layoutInflater)

        popupWindow = PopupWindow(
            bindingPopUp.root.rootView,
            400,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        bindingPopUp.usdCurrency.setOnClickListener {
            if (binding.output.text.isNotEmpty()) {
                viewModel.fetchConversionResult("USD")
                popupWindow.dismiss()
            }
        }
        bindingPopUp.jpyCurrency.setOnClickListener {
            if (binding.output.text.isNotEmpty()) {
                viewModel.fetchConversionResult("JPY")
                popupWindow.dismiss()
            }
        }
        bindingPopUp.gbpCurrency.setOnClickListener {
            if (binding.output.text.isNotEmpty()) {
                viewModel.fetchConversionResult("GBP")
                popupWindow.dismiss()
            }
        }
        bindingPopUp.audCurrency.setOnClickListener {
            if (binding.output.text.isNotEmpty()) {
                viewModel.fetchConversionResult("AUD")
                popupWindow.dismiss()
            }
        }
        bindingPopUp.cadCurrency.setOnClickListener {
            if (binding.output.text.isNotEmpty()) {
                viewModel.fetchConversionResult("CAD")
                popupWindow.dismiss()
            }
        }



        popupWindow.showAsDropDown(anchorView)
    }

    private fun subscribeObservers() {

        viewModel.conversionResult.observe(this) { conversionResult ->
            val currencyRate = conversionResult.rates[conversionResult.currency]
            val outputValue = if (currencyRate != null) {
                currencyRate * binding.output.text.toString().toDouble()
            } else {
                0
            }
            binding.output.text = DecimalFormat("0.###").format(outputValue).toString()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showErrorDialog(errorMessage)
            }
        }


    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun addToInputText(buttonValue: String): String {

        return binding.input.text.toString() + "" + buttonValue
    }

    private fun getInputExpression(): String {
        var expression = binding.input.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }

    private fun showResult() {
        try {
            val expression = getInputExpression()
            val result = Expression(expression).calculate()
            if (result.isNaN()) {
                binding.output.text = ""
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else {
                binding.output.text = DecimalFormat("0.###").format(result).toString()
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.orange))
                addHistoryItem(binding.input.text.toString() + "\n" + binding.output.text.toString())
            }
        } catch (e: Exception) {
            binding.output.text = ""
            binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }
}