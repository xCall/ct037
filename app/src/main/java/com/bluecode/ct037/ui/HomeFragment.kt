package com.bluecode.ct037.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentHomeBinding
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

enum class Button(val value: Int) {
    BtnStart(0),
    BtnPause(1),
    BtnReset(2)
}

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!

    private var setUptime: Int = 0
    private var setRestTime: Int = 0

    private var timeRest: Boolean = false

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 0

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var updatedTimeRunnable: Runnable
    private lateinit var tvUtcClockText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDate()
        defaultCountdown()

        tvUtcClockText = view.findViewById(R.id.tvUtcClockText)
        with(binding) {

            updatedTimeRunnable = object : Runnable {
                override fun run() {
                    val currentTime = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"))
                    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
                    val minute = currentTime.get(Calendar.MINUTE)
                    val second = currentTime.get(Calendar.SECOND)

                    val timeFormatted = String.format("%02d:%02d:%02d", hour,minute,second)
                    tvUtcClockText.text = timeFormatted
                    handler.postDelayed(this,1000)
                }

            }
            handler.post(updatedTimeRunnable)

            disabledButtons(Button.BtnStart.value,true)
            disabledButtons(Button.BtnPause.value,false)
            btnOpenSettings.setOnClickListener{
                parentFragmentManager.beginTransaction().replace(R.id.home, SettingsActivityFragment()).addToBackStack(null).commit()
            }

            llBtnHelpers.getChildAt(Button.BtnStart.value).setOnClickListener{
                if(!isTimerRunning && !timeRest){
//                    if(timeLeftInMillis == 0L) {
//                        binding.tvClockText.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow_400))
//                        binding.tvClockText.text = "Lutar"
//                        Handler(Looper.getMainLooper()).postDelayed({
//
//                            binding.tvClockText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
//                            startCountdown()
//                        },2000)
//
//                    } else {
                    startCountdown()
//
//                    }
                    disabledButtons(Button.BtnStart.value,false)
                    disabledButtons(Button.BtnPause.value,true)

                } else {
                    restCountdown()
                    disabledButtons(Button.BtnStart.value,false)
                    disabledButtons(Button.BtnPause.value,true)
                }

            }

            llBtnHelpers.getChildAt(Button.BtnPause.value).setOnClickListener{
                if(isTimerRunning){
                    stopCountdown()
                    disabledButtons(Button.BtnPause.value,false)
                    disabledButtons(Button.BtnStart.value,true)
                }
            }

            llBtnHelpers.getChildAt(Button.BtnReset.value).setOnClickListener{
                resetCountdown()
                disabledButtons(Button.BtnStart.value,true)
                disabledButtons(Button.BtnPause.value,false)
            }
        }
    }

    private fun loadDate() {
        try {
            val prefs = requireContext().getSharedPreferences("settings_app", Context.MODE_PRIVATE)

            setRestTime = prefs.getInt("restTime", 0)
            setUptime = prefs.getInt("uptime", 0)

            if(setUptime == 0) setUptime = 5
            if(setRestTime == 0) setRestTime = 1

            Toast.makeText(requireContext(), "Tempo Padrão: $setUptime", Toast.LENGTH_SHORT).show()

            defaultCountdown()


        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao carregar dados: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun disabledButtons(indexBtn: Int, status: Boolean) {
            binding.llBtnHelpers.getChildAt(indexBtn).isEnabled = status
            binding.llBtnHelpers.getChildAt(indexBtn).isClickable = status
    }

    private fun stopCountdown() {
        isTimerRunning = false
        countDownTimer?.cancel()
    }

    private fun resetCountdown() {
        timeLeftInMillis = 0L
        stopCountdown()
        defaultCountdown()
    }

    private fun defaultCountdown(){
        val minutes = TimeUnit.MILLISECONDS.toMinutes(setUptime.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(setUptime.toLong()) % 60
        binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun startCountdown() {
        timeRest = false
        val initialTimeInMinutes = if (setUptime == 0) 5 else setUptime
        if (timeLeftInMillis == 0L) {
            message("Lutar")

            timeLeftInMillis = initialTimeInMinutes * 60 * 1000L

            Handler(Looper.getMainLooper()).postDelayed({
                setBackgroundColor()
                countDownTimer(timeLeftInMillis, ::restCountdown)
            }, 2000)

        } else {
            countDownTimer(timeLeftInMillis, ::restCountdown)
        }

    }

    private fun setBackgroundColor() {
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun message(message: String){
        binding.tvClockText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.tvClockText.text = message
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(0xFFF7C400.toInt(), 0xFFFFD600.toInt())
        )

        binding.root.background = gradient
    }

    private fun restCountdown() {


        val initialRestTimeInMinutes = if (setRestTime == 0) 1 else setRestTime
        if (timeLeftInMillis == 0L) {
            timeRest = true
            message("Descansar")

            Handler(Looper.getMainLooper()).postDelayed({
                setBackgroundColor()
                countDownTimer(timeLeftInMillis, ::startCountdown)
            }, 2000)

            timeLeftInMillis = initialRestTimeInMinutes * 60 * 1000L
        } else {
            countDownTimer(timeLeftInMillis, ::startCountdown)
        }

    }

    fun countDownTimer(initialRestTimeInMinutes: Long,countDown: () -> Unit) {
        countDownTimer?.cancel()

        binding.tvClockText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                isTimerRunning = false
                timeLeftInMillis = 0L
                Handler(Looper.getMainLooper()).postDelayed({

                }, 2000) // 300
                countDown() // Volta para o trabalho
            }
        }.start()

        isTimerRunning = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        handler.removeCallbacks(updatedTimeRunnable)

        _binding = null
    }

}
