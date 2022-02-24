package com.example.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var UsingDot = true
    private var UsingOperation = false
    private var tvOperation: TextView? = null
    private var tvResult: TextView? = null

    private val TAG = "MainActivity"
    private val TEXTOperation = "TEXT_CONTENTOP"
    private val TEXTResult = "TEXT_CONTENTRES"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnClear: Button = findViewById<Button>(R.id.button)
        var btnDivision: Button = findViewById<Button>(R.id.button2)
        var btnProduct: Button = findViewById<Button>(R.id.button3)
        var btnEraseToTheLeft: Button = findViewById<Button>(R.id.button4)
        var btn7: Button = findViewById<Button>(R.id.button5)
        var btn8: Button = findViewById<Button>(R.id.button6)
        var btn9: Button = findViewById<Button>(R.id.button7)
        var btnMinus: Button = findViewById<Button>(R.id.button8)
        var btn4: Button = findViewById<Button>(R.id.button9)
        var btn5: Button = findViewById<Button>(R.id.button10)
        var btn6: Button = findViewById<Button>(R.id.button11)
        var btnSum: Button = findViewById<Button>(R.id.button12)
        var btn1: Button = findViewById<Button>(R.id.button13)
        var btn2: Button = findViewById<Button>(R.id.button14)
        var btn3: Button = findViewById<Button>(R.id.button15)
        var btnResult: Button = findViewById<Button>(R.id.button16)
        var btn0: Button = findViewById<Button>(R.id.button18)
        var btnDot: Button = findViewById<Button>(R.id.button19)
        tvResult = findViewById<TextView>(R.id.textView2)
        tvOperation = findViewById<TextView>(R.id.textView)

        //Clear button erasing content in Text view fields
        btnClear?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                tvOperation?.text = ""
                tvResult?.text = ""
            }
        })

        //Making a string subsequence when EraseToTheLeft button is clicked
        btnEraseToTheLeft?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val length = tvOperation?.length()
                if(length ?: 0 > 0){
                    tvOperation?.text = tvOperation?.text?.subSequence(0, length?.minus(1)?:0)
                    Log.d(TAG, length.toString())
                }
            }
        })

        //Number buttons adding its respective digit.
        btn0?.setOnClickListener(numberviewListener)
        btn1?.setOnClickListener(numberviewListener)
        btn2?.setOnClickListener(numberviewListener)
        btn3?.setOnClickListener(numberviewListener)
        btn4?.setOnClickListener(numberviewListener)
        btn5?.setOnClickListener(numberviewListener)
        btn6?.setOnClickListener(numberviewListener)
        btn7?.setOnClickListener(numberviewListener)
        btn8?.setOnClickListener(numberviewListener)
        btn9?.setOnClickListener(numberviewListener)
        btnDot?.setOnClickListener(numberviewListener)

        //Operation buttons adding its respective symbol
        btnDivision?.setOnClickListener(operationviewListener)
        btnProduct?.setOnClickListener(operationviewListener)
        btnMinus?.setOnClickListener(operationviewListener)
        btnSum?.setOnClickListener(operationviewListener)

        //Equal button function
        btnResult?.setOnClickListener(resultViewListener)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXTOperation, tvOperation?.text.toString())
        outState.putString(TEXTResult, tvResult?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tvOperation?.text = savedInstanceState.getString(TEXTOperation, "")
        tvResult?.text = savedInstanceState.getString(TEXTResult, "")
    }

    //Setting OnClick listener to their respective view
    private val numberviewListener = View.OnClickListener {
        addNumbers(findViewById(it.id))
    }

    private val operationviewListener = View.OnClickListener {
        addOperation(findViewById(it.id))
    }

    private val resultViewListener = View.OnClickListener {
        result(findViewById(it.id))
    }

    //Adding numbers to the Operation text view when clicking it
    private fun addNumbers(view: View){
        if(view is Button){
            if(view.text == "."){
                if(UsingDot){
                    tvOperation?.append(view.text)
                    UsingDot = false
                }
            }
            else{
                tvOperation?.append(view.text)
                UsingOperation = true
            }
        }
    }

    //Adding operator to the text view and setting to false the operator append.
    private fun addOperation(view: View){
        if (view is Button && UsingOperation){
            tvOperation?.append(view.text)
            UsingOperation = false
            UsingDot = true
        }
    }

    //Displaying the result in Result text view
    private fun result(view: View){
        tvResult?.text = makemaths()
    }


    private fun makemaths(): String {
        //Making a MutableList for the string in Text View
        val getTVInput = getTVInput()
        if(getTVInput.isEmpty()) return ""

        //Passing the MutableList to make product and division operations
        val productAndDivision = productAndDivision(getTVInput)
        if(productAndDivision.isEmpty()) return ""

        //Passing a resulting MutableList of product and division calcs now to make adding and substraction.
        val result = sumAndSubstraction(productAndDivision)
        return result.toString()
    }


    private fun sumAndSubstraction(list: MutableList<Any>): Float {
        //Assign the first value since its a digit to result
        var result = list[0] as Float
        for (i in list.indices){
            //Log.d(TAG,productAndDivision[i].toString())
            if (list[i] is Char && i != list.lastIndex){
                val op = list[i]
                //Log.d(TAG, op.toString())
                val nextop = list[i+1] as Float
                if(op == '+') result += nextop
                    Log.d(TAG, result.toString())

                if(op == '-') result -= nextop
                    Log.d(TAG, result.toString())
            }
        }
        return result
    }

    //Checking if the MutableList named list has product or division operators
    private fun productAndDivision(list: MutableList<Any>): MutableList<Any> {
        var templist = list
        while (templist.contains('X') || list.contains('/')){
            templist = prodDivCalcs(templist)
            //Log.d(TAG, templist.toString())
        }
        return templist
    }

    //Making product and division operations
    private fun prodDivCalcs(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = list.size

        for (i in list.indices){
            if(list[i] is Char && i != list.lastIndex && i < restartIndex){
                val op = list[i]
                val nextOp = list[i + 1] as Float
                val prevOp = list[i - 1] as Float
                //Log.d(TAG, prevOp.toString())
                //Log.d(TAG, op.toString())
                //Log.d(TAG, nextOp.toString())
                when(op){
                    'X' ->{
                        newList.add(prevOp * nextOp)
                        restartIndex = i + 1
                    }
                    '/'->{
                        newList.add(prevOp / nextOp)
                        restartIndex = i + 1
                    }
                    else ->{
                        newList.add(prevOp)
                        newList.add(nextOp)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(list[i])
        }
        return newList
    }

    private fun getTVInput(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        tvOperation?.text?.forEach {
            if(it.isDigit() || it == '.'){
                currentDigit += it

            }
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(it)
            }
        }

        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }
        Log.d(TAG, list.toString())
        return list
    }

}