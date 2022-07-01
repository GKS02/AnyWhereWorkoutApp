package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.ActivityExerciseBinding
import com.example.workoutapp.databinding.DialogCustomBackConfirmationBinding
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding?=null

    private var restTimer : CountDownTimer?=null
    private var restProgress=0;
    private var restTimerDuration : Long = 10
    private var exerciseTimer : CountDownTimer?=null
    private var exerciseProgress=0;

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currExercisePosition: Int=-1
    private var exerciseTimerDuration : Long = 30
    private var tts : TextToSpeech?=null
    private var player:MediaPlayer?=null

    private var exerciseAdapter : ExerciseStatusAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

       setSupportActionBar(toolbarExerxise)
        if(supportActionBar !=null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this,this)
        toolbarExerxise.setNavigationOnClickListener {
            customDialogForBackButton()

        }

        setRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onBackPressed() {
        customDialogForBackButton()
        super.onBackPressed()
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = com.example.workoutapp.databinding.DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter=exerciseAdapter

    }

    private fun setRestView() {
        try {
            val soundUri = Uri.parse(
                "android.resource://com.example.workoutapp/"+R.raw.app_src_main_res_raw_press_start)
            player=MediaPlayer.create(applicationContext,soundUri)
            player?.isLooping=false
            player?.start()
        }catch (e:Exception) {
            e.printStackTrace()
        }

        flRestView.visibility=View.VISIBLE
        flExerciseProgresBar.visibility=View.INVISIBLE

        tvTitle.visibility=View.VISIBLE
        tvExerciseName.visibility=View.INVISIBLE
        ivImage.visibility=View.INVISIBLE


            tvUpcomingExercise.visibility=View.VISIBLE
            UpcomeExerciseName.visibility=View.VISIBLE
            UpcomeExerciseName.text = exerciseList!![currExercisePosition+1].getName()

        if(restTimer !=null) {
            restTimer?.cancel()
            restProgress=0
        }

        setRestProgressBar()
    }
    private fun setExerciseRestView() {
        speakOut(exerciseList!![currExercisePosition].getName())
        flRestView.visibility=View.INVISIBLE
        flExerciseProgresBar.visibility=View.VISIBLE

        tvTitle.visibility=View.INVISIBLE
        tvExerciseName.visibility=View.VISIBLE
        ivImage.visibility=View.VISIBLE
        tvUpcomingExercise.visibility=View.INVISIBLE
        UpcomeExerciseName.visibility=View.INVISIBLE

        if(exerciseTimer !=null) {
            exerciseTimer?.cancel()
            exerciseProgress=0
        }

         ivImage.setImageResource(exerciseList!![currExercisePosition].getImage())
        tvExerciseName.text=exerciseList!![currExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        progresBar.progress=restProgress

        restTimer = object : CountDownTimer(restTimerDuration*1000,1000) {
            override fun onTick(p0: Long) {
                restProgress++;
                progresBar.progress= restTimerDuration.toInt()-restProgress
                tvTimer.text=(restTimerDuration.toInt()-restProgress).toString()
            }

            override fun onFinish() {
//                Toast.makeText(
//                    this@ExerciseActivity,
//                    "Lets start the Exercise",
//                    Toast.LENGTH_SHORT).show()


                currExercisePosition++
                exerciseList!![currExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setExerciseRestView()
            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        progresExerciseBar.progress=exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000,1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++;
                progresExerciseBar.progress= exerciseTimerDuration.toInt()-exerciseProgress
                tvExerciseTimer.text=(exerciseTimerDuration.toInt()-exerciseProgress).toString()
            }

            override fun onFinish() {
//                Toast.makeText(
//                    this@ExerciseActivity,
//                    "Time up!!",
//                    Toast.LENGTH_SHORT).show()
            if(currExercisePosition < exerciseList?.size!!-1) {
                exerciseList!![currExercisePosition].setIsSelected(false)
                exerciseList!![currExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setRestView()
            }else {
//                           Toast.makeText(
//                    this@ExerciseActivity,
//                    "Congrats!! Workout Finished",
//                    Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                startActivity(intent)
             }

            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer !=null) {
            restTimer?.cancel()
            restProgress=0
        }

        if(exerciseTimer !=null) {
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        if(tts!=null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player!=null) {
            player?.stop()
        }
        binding=null
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language Specified is not supported" )
            }
        }else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(text:String) {
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}