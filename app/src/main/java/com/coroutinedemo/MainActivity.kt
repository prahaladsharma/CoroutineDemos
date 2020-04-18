package com.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result_1"
    private val RESULT_2 = "Result_2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // default:- CPU intensive work
        // IO:-  Input/output. ex: network or disk transactions
        // Main:- UI Interactions
        button.setOnClickListener{
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private fun setText(input: String){
        val newText = text.text.toString() + "\n $input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done

        if ( result1.equals(RESULT_1)) {

            setTextOnMainThread("Got $result1")

            val result2 = getResult2FromApi() // wait until job is done

            if (result2.equals(RESULT_2)) {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }
    }

    private suspend fun getResult1FromApi(): String{
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String){
        Log.v("CoroutineDemo" , methodName + Thread.currentThread().name)
    }
}
