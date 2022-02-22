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
        var btnPercentage: Button = findViewById<Button>(R.id.button17)
        var btn0: Button = findViewById<Button>(R.id.button18)
        var btnDot: Button = findViewById<Button>(R.id.button19)
        tvResult = findViewById<TextView>(R.id.textView2)
        tvOperation = findViewById<TextView>(R.id.textView)


        btnClear?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                tvOperation?.text = ""
                tvResult?.text = ""
            }
        })

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
        btn0?.setOnClickListener(NumberviewListener)
        btn1?.setOnClickListener(NumberviewListener)
        btn2?.setOnClickListener(NumberviewListener)
        btn3?.setOnClickListener(NumberviewListener)
        btn4?.setOnClickListener(NumberviewListener)
        btn5?.setOnClickListener(NumberviewListener)
        btn6?.setOnClickListener(NumberviewListener)
        btn7?.setOnClickListener(NumberviewListener)
        btn8?.setOnClickListener(NumberviewListener)
        btn9?.setOnClickListener(NumberviewListener)
        btnDot?.setOnClickListener(NumberviewListener)

        //Operation buttons adding its respective symbol
        btnDivision?.setOnClickListener(OperationviewListener)
        btnProduct?.setOnClickListener(OperationviewListener)
        btnMinus?.setOnClickListener(OperationviewListener)
        btnSum?.setOnClickListener(OperationviewListener)



    }

    private val NumberviewListener = View.OnClickListener {
        addNumbers(findViewById(it.id))
    }

    private val OperationviewListener = View.OnClickListener {
        addOperation(findViewById(it.id))
    }

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

    private fun addOperation(view: View){
        if (view is Button && UsingOperation){
            tvOperation?.append(view.text)
            UsingOperation = false
            UsingDot = true
        }
    }

    private fun result(view: View){
        tvResult?.text = makemaths()
    }

    private fun makemaths(): CharSequence? {
        val digitsOperator = digitsOperators()
        if(digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperator)
        if(timesDivision.isEmpty()) return ""

        //val result = addSubstractCalculate(timesDivision)
        return ""
    }

    private fun timesDivisionCalculate(list: MutableList<Any>): MutableList<Any> {
        var templist = list
        while (templist.contains('X') || list.contains('/')){
            templist = calcTimesDiv(templist)
        }
        return list
    }

    private fun calcTimesDiv(templist: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = templist.size

        for (i in templist.indices){
            if(templist[i] is Char && i != templist.lastIndex && i < restartIndex){
                val op = templist[i]
                val nextOp = templist[i+1] as Float
                val prevOp = templist[i-1] as Float
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
                newList.add(templist[i])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
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
        return list
    }

}