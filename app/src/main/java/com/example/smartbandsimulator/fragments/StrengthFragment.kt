package com.example.smartbandsimulator.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartbandsimulator.databinding.FragmentStrengthBinding

class StrengthFragment : Fragment() {

    private var _binding: FragmentStrengthBinding? = null
    private val binding get() = _binding!!
    private var running = false
    private val handler = Handler(Looper.getMainLooper())
    private val bpmRunnable = object : Runnable {
        override fun run() {
            val bpm = (60..140).random()
            binding.tvBpm.text = "BPM: $bpm"
            if (running) handler.postDelayed(this, 1000)
        }
    }

    private val workouts = mapOf(
        1 to listOf(
            "Împins cu haltera la bancă orizontală",
            "Fluturări cu gantere pe bancă înclinată",
            "Dips (paralele) pentru piept",
            "Extensii triceps la scripete (rope)"
        ),
        2 to listOf(
            "Tracțiuni (pull-ups) — priză largă",
            "Ramat cu bara T-bar",
            "Ramat cu gantera (unilateral), sprijinit pe bancă",
            "Flexii biceps cu bara EZ"
        ),
        3 to listOf(
            "Genuflexiuni cu bara (back squat)",
            "Fandări înainte cu gantere (walking lunges)",
            "Îndreptări picioare drepte (Romanian deadlift)",
            "Ridicări pe vârfuri (standing calf raises)"
        ),
        4 to listOf(
            "Pres militar cu gantere (seated dumbbell press)",
            "Ridicări laterale cu gantere",
            "Ridicări frontale cu disc/gantere",
            "Planșă (plank) – menține 3×30–60 s"
        ),
        5 to listOf(
            "Burpees (exploziv, 3×10–12)",
            "Kettlebell swings (3×15)",
            "Mountain climbers (3×30 s)",
            "Jump squats (3×12)"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStrengthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        for (i in 1..5) {
            val btn = android.widget.Button(requireContext()).apply {
                text = "Ziua $i"
                setOnClickListener { showExercises(i) }
            }
            binding.daysContainer.addView(btn)
        }

        binding.btnStartStop.setOnClickListener {
            if (running) stopSession() else startSession()
        }
    }

    private fun showExercises(day: Int) {
        val list = workouts[day] ?: emptyList()
        binding.tvExercises.text = list.joinToString("\n") { "• $it" }
    }

    private fun startSession() {
        binding.chronometer.base = android.os.SystemClock.elapsedRealtime()
        binding.chronometer.start()
        running = true
        binding.btnStartStop.text = "Stop"
        handler.post(bpmRunnable)
    }

    private fun stopSession() {
        binding.chronometer.stop()
        running = false
        binding.btnStartStop.text = "Start"
        handler.removeCallbacks(bpmRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(bpmRunnable)
        _binding = null
    }
}
