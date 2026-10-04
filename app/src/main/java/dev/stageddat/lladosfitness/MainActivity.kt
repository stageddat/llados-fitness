package dev.stageddat.lladosfitness

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // variables
    var weight = 70
    var height = 170

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // views
        val btnCalculate: Button = findViewById(R.id.btn_calculate)
        val tvResultBmi: TextView = findViewById(R.id.tv_result_bmi)
        val tvResultComment: TextView = findViewById(R.id.tv_result_comment)
        val cvResult: CardView = findViewById(R.id.cv_result)

        // name
        val etName: EditText = findViewById(R.id.t_name_input)

        // peso
        val tvWeightValue: TextView = findViewById(R.id.tv_weight_value)
        val btnWeightMinus: Button = findViewById(R.id.btn_weight_minus)
        val btnWeightPlus: Button = findViewById(R.id.btn_weight_plus)
        tvWeightValue.text = weight.toString()

        // altura
        val tvHeightValue: TextView = findViewById(R.id.tv_height_value)
        val sbHeightInput: SeekBar = findViewById(R.id.sb_height_input)

        // listeners
        // peso listeners
        btnWeightPlus.setOnClickListener {
            weight += 1
            tvWeightValue.text = weight.toString()
        }
        btnWeightMinus.setOnClickListener {
            if (weight > 1) {
                weight -= 1
                tvWeightValue.text = weight.toString()
            }
        }

        // seekbar listener
        sbHeightInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress < 1) {
                    seekBar?.progress = 1
                    return
                }
                height = progress
                tvHeightValue.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // calcular button listener
        btnCalculate.setOnClickListener {
            val name = etName.text.toString().ifBlank { getString(R.string.default_name) }

            val bmi = calculateBMI(weight, height)
            tvResultBmi.text = "%.2f".format(bmi)
            tvResultComment.text = getString(R.string.result_comment, name, getBmiStatus(bmi))
            cvResult.visibility = View.VISIBLE
        }
    }

    private fun getBmiStatus(bmi: Double): String {
        return when {
            bmi < 18.5 -> getString(R.string.bmi_underweight)
            bmi < 25 -> getString(R.string.bmi_normal)
            bmi < 30 -> getString(R.string.bmi_overweight)
            else -> getString(R.string.bmi_obesity)
        }
    }

    private fun calculateBMI(weight: Int, height: Int): Double {
        if (weight < 1 || height < 1) {
            return 0.0
        }
        val heightM = height / 100.0

        val bmi: Double = (weight.toDouble() / (heightM*heightM))
        return bmi
    }


}