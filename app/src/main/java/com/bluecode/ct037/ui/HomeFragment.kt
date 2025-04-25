package com.bluecode.ct037.ui

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentHomeBinding
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

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 0

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

        with(binding) {
            disabledButtons(Button.BtnStart.value,true)
            disabledButtons(Button.BtnPause.value,false)
            btnOpenSettings.setOnClickListener{
                parentFragmentManager.beginTransaction().replace(R.id.home, SettingsActivityFragment()).addToBackStack(null).commit()
            }

            llBtnHelpers.getChildAt(Button.BtnStart.value).setOnClickListener{
                if(!isTimerRunning){
                    startCountdown()
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
        stopCountdown()
        defaultCountdown()
    }

    private fun defaultCountdown(){
        val minutes = TimeUnit.MILLISECONDS.toMinutes(setUptime.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(setUptime.toLong()) % 60
        binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
    }

//    private fun startCountdown() {
//
//        val initialTimeInMinutes = if (setUptime == 0) 5 else setUptime!!
//        val startTime = if (timeLeftInMillis > 0) timeLeftInMillis else initialTimeInMinutes * 60 * 1000L
//
//        countDownTimer = object : CountDownTimer(startTime,1000) {
//            override fun onTick(millisUntilFinished:Long) {
//                timeLeftInMillis = millisUntilFinished // Atualiza o tempo restante
//                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
//                binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
//            }
//
//            override fun onFinish() {
//                binding.tvClockText.text = "04:00"
//                isTimerRunning = true
//
//                restCountdown()
//            }
//        }.start()
//
//        isTimerRunning = true
//    }
//
//    private fun restCountdown() {
//
//        val initialTimeInMinutes = if (setRestTime == 0) 5 else setRestTime!!
//        val startTime = if (timeLeftInMillis > 0) timeLeftInMillis else initialTimeInMinutes * 60 * 1000L
//
//        countDownTimer = object : CountDownTimer(startTime,1000) {
//            override fun onTick(millisUntilFinished:Long) {
//                timeLeftInMillis = millisUntilFinished // Atualiza o tempo restante
//                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
//                binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
//            }
//
//            override fun onFinish() {
//                binding.tvClockText.text = "04:00"
//                isTimerRunning = true
//
//                startCountdown()
//            }
//        }.start()
//
//        isTimerRunning = true
//    }

    private fun startCountdown() {
        val initialTimeInMinutes = if (setUptime == 0) 5 else setUptime
        timeLeftInMillis = initialTimeInMinutes * 60 * 1000L

        countDownTimer?.cancel()
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
                restCountdown() // Vai para o descanso
            }
        }.start()

        isTimerRunning = true
    }

    private fun restCountdown() {
        val initialRestTimeInMinutes = if (setRestTime == 0) 1 else setRestTime
        timeLeftInMillis = initialRestTimeInMinutes * 60 * 1000L

        countDownTimer?.cancel()
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
                startCountdown() // Volta para o trabalho
            }
        }.start()

        isTimerRunning = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()

        _binding = null
    }

}
