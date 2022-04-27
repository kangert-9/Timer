package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var flowOne: Flow<String>
    lateinit var flowTwo: Flow<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFlows()
        zipFlows()
    }

    private fun getFlow(): Flow<Int> = flow {
        Log.d("TAG", "Start flow")
        (0..10).forEach {
            delay(500)
            Log.d("TAG", "Emitting $it")
            emit(it)
        }
    }.map { it * 2 }
        .flowOn(Dispatchers.Default)

    private fun startFlow() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                getFlow().collect {
                    Log.d("TAG", it.toString())
                }
            }
        }
    }

    private fun setupFlows() {
        flowOne = flowOf("Юрий", "Александр", "Иван").flowOn(Dispatchers.Default)
        flowTwo = flowOf("Гагарин", "Пушкин", "Грозный").flowOn(Dispatchers.Default)
    }
    private fun zipFlows() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flowOne.zip(flowTwo)
                { firstString, secondString ->
                    "$firstString $secondString"
                }.collect {
                    Log.d("TAG", it)
                }
            }
        }
    }
}