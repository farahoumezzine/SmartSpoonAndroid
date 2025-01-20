package com.example.smartspoon.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R
import com.example.smartspoon.utils.VoiceCommandManager

class AIRecipeGeneratorActivity : AppCompatActivity() {
    private lateinit var voiceManager: VoiceCommandManager
    private var isListening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_recipe_generator)

        val ingredientsInput = findViewById<EditText>(R.id.et_ingredients)
        val generateButton = findViewById<Button>(R.id.btn_generate)
        val resultText = findViewById<TextView>(R.id.tv_generated_recipe)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        // Get scanned ingredients if any
        intent.getStringExtra("ingredients")?.let {
            ingredientsInput.setText(it)
            // Automatically generate recipe for scanned ingredients
            generateRecipe(it) { recipe ->
                progressBar.visibility = View.GONE
                resultText.text = recipe
            }
        }

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        generateButton.setOnClickListener {
            val ingredients = ingredientsInput.text.toString()
            if (ingredients.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                generateRecipe(ingredients) { recipe ->
                    progressBar.visibility = View.GONE
                    resultText.text = recipe
                }
            }
        }

        voiceManager = VoiceCommandManager(this)

        // Add voice command button
        findViewById<ImageButton>(R.id.btn_voice).setOnClickListener {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                if (!isListening) {
                    startVoiceCommands()
                } else {
                    stopVoiceCommands()
                }
            } else {
                checkAndRequestPermission()
            }
        }
    }

    private fun generateRecipe(ingredients: String, callback: (String) -> Unit) {
        // Simulate AI generation (replace with actual API call)
        Handler(Looper.getMainLooper()).postDelayed({
            val recipe = """
                Based on your ingredients: $ingredients
                
                Suggested Pizza Recipe:
                
                Ingredients needed:
                • ${ingredients.split(",").joinToString("\n• ")}
                • Additional suggested ingredients
                
                Instructions:
                1. Preheat oven to 450°F
                2. Prepare pizza dough
                3. Add your ingredients
                4. Bake for 12-15 minutes
                
                Note: This is a basic suggestion. Adjust ingredients and instructions based on your preferences.
            """.trimIndent()
            
            callback(recipe)
        }, 2000) // Simulated 2-second delay
    }

    private fun startVoiceCommands() {
        isListening = true
        findViewById<ImageButton>(R.id.btn_voice).setImageResource(R.drawable.ic_mic_active)
        voiceManager.startListening { command ->
            when {
                command.contains("generate") -> {
                    val ingredients = findViewById<EditText>(R.id.et_ingredients).text.toString()
                    if (ingredients.isNotEmpty()) {
                        findViewById<Button>(R.id.btn_generate).performClick()
                    }
                }
                command.contains("add") -> {
                    val ingredient = command.replace("add", "").trim()
                    val currentText = findViewById<EditText>(R.id.et_ingredients).text.toString()
                    val newText = if (currentText.isEmpty()) ingredient else "$currentText, $ingredient"
                    findViewById<EditText>(R.id.et_ingredients).setText(newText)
                }
                command.contains("clear") -> {
                    findViewById<EditText>(R.id.et_ingredients).text.clear()
                }
                command.contains("back") -> {
                    finish()
                }
            }
        }
    }

    private fun stopVoiceCommands() {
        isListening = false
        findViewById<ImageButton>(R.id.btn_voice).setImageResource(R.drawable.ic_mic)
        voiceManager.stopListening()
    }

    private fun checkAndRequestPermission() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 200)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startVoiceCommands()
        } else {
            Toast.makeText(this, "Microphone permission is required for voice commands", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceManager.destroy()
    }
} 