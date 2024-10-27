package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sourceAmount: EditText
    private lateinit var targetAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var targetCurrency: Spinner

    private val exchangeRates = mapOf(
        "USD" to 1.0, // Base currency (USD)
        "EUR" to 0.85,
        "JPY" to 110.0,
        "VND" to 23000.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmount = findViewById(R.id.sourceAmount)
        targetAmount = findViewById(R.id.targetAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        targetCurrency = findViewById(R.id.targetCurrency)

        val currencyList = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceCurrency.adapter = adapter
        targetCurrency.adapter = adapter

        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sourceCurrency.onItemSelectedListener = listener
        targetCurrency.onItemSelectedListener = listener
    }

    private fun convertCurrency() {
        val sourceCurrencyCode = sourceCurrency.selectedItem.toString()
        val targetCurrencyCode = targetCurrency.selectedItem.toString()
        val sourceAmountValue = sourceAmount.text.toString().toDoubleOrNull()

        if (sourceAmountValue != null) {
            val sourceRate = exchangeRates[sourceCurrencyCode] ?: 1.0
            val targetRate = exchangeRates[targetCurrencyCode] ?: 1.0
            val convertedAmount = sourceAmountValue * (targetRate / sourceRate)
            targetAmount.setText(String.format("%.2f", convertedAmount))
        } else {
            targetAmount.setText("")
        }
    }
}
