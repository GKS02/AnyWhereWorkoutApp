package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.flStart?.setOnClickListener {

          //Toast.makeText(this,"Button clicked",Toast.LENGTH_SHORT).show()

           startActivity(Intent(this,ExerciseActivity::class.java))

        }

        binding?.flBMI?.setOnClickListener {

           startActivity(Intent(this,BMIActivity::class.java))

        }

        binding?.flHistory?.setOnClickListener {

            startActivity(Intent(this,HistoryActivity::class.java))

        }




    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

}