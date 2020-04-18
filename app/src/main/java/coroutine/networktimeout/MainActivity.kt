package coroutine.networktimeout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coroutinedemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result_1"
    private val RESULT_2 = "Result_2"
    private val JOB_TIMEOUT = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            setText("Click!")
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }


    private suspend fun fakeApiRequest(){
        withContext(IO){

            val job = withTimeoutOrNull(JOB_TIMEOUT){
                val result1 = getResult1FromApi()  // wait until job is finish
                setTextOnMainThread(result1)

                val  result2 = getResult2FromApi()  // wait until job is finish
                setTextOnMainThread(result2)
            }

            if(job == null){
                val cancelMessage = "Cancelling job...Job took longer than $JOB_TIMEOUT ms"
                println("debug: ${cancelMessage}")
                setTextOnMainThread(cancelMessage)
            }

        }
    }

    private fun setText(input: String){
        val newText = text.text.toString() + "\n $input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        withContext(Dispatchers.Main){
            setText(input)
        }
    }


    private suspend fun getResult1FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }




}