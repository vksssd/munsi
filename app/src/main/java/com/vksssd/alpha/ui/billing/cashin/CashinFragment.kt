package com.vksssd.alpha.ui.billing.cashin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentCashinBinding

class CashinFragment : Fragment() {

    private var _binding: FragmentCashinBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCashinBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cashinTitlebar.toolbarTitle.text = "Cash In"
        binding.cashinTitlebar.searchButton.visibility = View.INVISIBLE
        binding.cashinTitlebar.backButton.visibility = View.VISIBLE
        binding.cashinNumpad.amountDisplay.text = "0"

        setupNumPad()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupNumPad() {
        binding.cashinNumpad.apply {
            // Set up digit buttons
            val numberButtons = listOf(
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9,
                button00, buttonDecimal
            )

            // Set up operator buttons
            val operatorButtons = listOf(
                addition, subtraction, multiply, division
            )

            // Handle number and decimal input
            numberButtons.forEach { button ->
                button.setOnClickListener {
                    val currentText = amountDisplay.text.toString()
                    val buttonText = button.text.toString()

                    amountDisplay.text = when {
                        // Handle multiple leading zeros
                        currentText == "0" && buttonText == "0" -> "0"
                        currentText == "0" && buttonText == "00" -> "0"
                        // Replace leading zero with digit (not decimal point)
                        currentText == "0" && buttonText != "." -> buttonText
                        // Check if decimal already exists
                        buttonText == "." && currentText.contains(".") -> currentText
                        // Append zero when starting with a decimal point
                        currentText == "0" && buttonText == "." -> "0."
                        else -> currentText + buttonText
                    }
                }
            }

            // Handle operator input with automatic evaluation
            operatorButtons.forEach { button ->
                button.setOnClickListener {
                    val currentText = amountDisplay.text.toString()
                    val buttonText = button.text.toString()

                    // Handle expression ending with an operator (replace it)
                    if (currentText.isNotEmpty()) {
                        val lastChar = currentText.last()
                        if (lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == 'X' || lastChar == '/' || lastChar == '*') {
                            // Just replace the last operator with the new one and exit
                            val newText = currentText.substring(0, currentText.length - 1) + buttonText
                            amountDisplay.text = newText
                            return@setOnClickListener
                        }
                    }

                    // Don't allow operators at the beginning of empty expression
                    if (currentText == "0") {
                        amountDisplay.text = "0"
                        return@setOnClickListener
                    }

                    // Check if expression already contains an operator and needs evaluation
                    if (containsOperator(currentText)) {
                        // Evaluate the current expression first
                        try {
                            calculateExpression(currentText).fold(
                                onSuccess = { result ->
                                    // Display result and append the new operator
                                    amountDisplay.text = formatResult(result) + buttonText
                                },
                                onFailure = { error ->
                                    amountDisplay.text = when (error) {
                                        is ArithmeticException -> "Math Error"
                                        else -> "Error"
                                    }
                                }
                            )
                        } catch (e: Exception) {
                            amountDisplay.text = "Error"
                        }
                    } else {
                        // Expression doesn't contain an operator yet, just append it
                        amountDisplay.text = currentText + buttonText
                    }
                }
            }

            buttonBackspace.setOnClickListener {
                val currentText = amountDisplay.text.toString()
                amountDisplay.text = when {
                    currentText.length == 1 -> "0"
                    currentText.isNotEmpty() -> currentText.substring(0, currentText.length - 1)
                    else -> "0"
                }
            }

            clearAll.setOnClickListener {
                amountDisplay.text = "0"
            }

            calculate.setOnClickListener {
                val currentText = amountDisplay.text.toString()
                // Don't calculate if expression ends with an operator
                if (currentText.endsWith("+") || currentText.endsWith("-") ||
                    currentText.endsWith("*") ||currentText.endsWith("x") || currentText.endsWith("/")) {
                    // Remove the trailing operator
                    val cleanedText = currentText.substring(0, currentText.length - 1)
                    evaluateAndDisplay(cleanedText)
                } else {
                    evaluateAndDisplay(currentText)
                }
            }
        }
    }



    // Helper function to evaluate and display result
    private fun evaluateAndDisplay(expression: String) {
        try {
            calculateExpression(expression).fold(
                onSuccess = { result ->
                    binding.cashinNumpad.amountDisplay.text = formatResult(result)
                },
                onFailure = { error ->
                    binding.cashinNumpad.amountDisplay.text = when (error) {
                        is ArithmeticException -> "Math Error"
                        else -> "Error"
                    }
                }
            )
        } catch (e: Exception) {
            binding.cashinNumpad.amountDisplay.text = "Error"
        }
    }

    // Helper function to check if expression contains an operator
    private fun containsOperator(expression: String): Boolean {
        // Skip the last character when checking to prevent matching a trailing operator
        val textToCheck = if (expression.length > 1) expression.substring(0, expression.length - 1) else ""
        return textToCheck.contains("+") || textToCheck.contains("-") ||
                textToCheck.contains("*") || textToCheck.contains("/") ||
                textToCheck.contains("x") || textToCheck.contains("X")
    }

    private fun setupClickListeners() {
        binding.apply {
            cashinTitlebar.backButton.setOnClickListener {
                navController.navigate(R.id.action_cashinFragment_to_homeFragment)
            }

            reviewOrderTextview.setOnClickListener {
                val currentText = cashinNumpad.amountDisplay.text.toString()
                val cleanedText: String
                // Don't calculate if expression ends with an operator
                if (currentText.endsWith("+") || currentText.endsWith("-") ||
                    currentText.endsWith("*") ||currentText.endsWith("x") || currentText.endsWith("/")) {
                    // Remove the trailing operator
                    cleanedText = currentText.substring(0, currentText.length - 1)
                }else{
                    cleanedText = currentText
                }

                if (cleanedText != "0" && cleanedText != "0.0") {
                        val bundle = Bundle().apply {
                            putString("amount_cashin", cleanedText)
                        }
                        setFragmentResult("requestKey", bundle)
                        navController.navigate(R.id.action_cashinFragment_to_billCreatedFragment)
                    }
            }
        }
    }

    private fun setupRecyclerView() {
        // Set up RecyclerView
    }

    private fun observeViewModel() {
        // Observe LiveData from ViewModel
    }

    override fun onStart() {
        super.onStart()
        // Perform actions when the fragment is visible
    }

    override fun onResume() {
        super.onResume()
        // Perform actions when the fragment is in the foreground
    }

    override fun onPause() {
        super.onPause()
        // Perform actions when the fragment is going into the background
    }

    override fun onStop() {
        super.onStop()
        // Perform actions when the fragment is no longer visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any non-view related resources
    }

    // Renamed to avoid confusion with the onClick handler
    private fun calculateExpression(expression: String): Result<Double> {
        return try {
            // Remove all whitespace
            val cleanExpression = expression.replace("\\s+".toRegex(), "")
                .replace("x", "*")
                .replace("X", "*")
            if (cleanExpression.isEmpty()) {
                Result.failure(IllegalArgumentException("Expression cannot be empty"))
            } else {
                val result = evaluateExpression(cleanExpression)
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Format the result for display, handling potential floating point precision issues
     */
    private fun formatResult(result: Double): String {
        // Check if result is essentially an integer
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            // Limit decimal places for display but avoid scientific notation
            String.format("%.3f", result).trimEnd('0').trimEnd('.')
        }
    }

    // Main evaluation function
    private fun evaluateExpression(expression: String): Double {
        return parseAdditionSubtraction(expression, 0).first
    }

    // Handle addition and subtraction
    private fun parseAdditionSubtraction(expression: String, startIndex: Int): Pair<Double, Int> {
        var result = parseMultiplicationDivision(expression, startIndex)
        var value = result.first
        var currentIndex = result.second

        while (currentIndex < expression.length) {
            when (expression[currentIndex]) {
                '+' -> {
                    result = parseMultiplicationDivision(expression, currentIndex + 1)
                    value += result.first
                    currentIndex = result.second
                }
                '-' -> {
                    result = parseMultiplicationDivision(expression, currentIndex + 1)
                    value -= result.first
                    currentIndex = result.second
                }
                else -> break
            }
        }

        return Pair(value, currentIndex)
    }

    // Handle multiplication and division
    private fun parseMultiplicationDivision(expression: String, startIndex: Int): Pair<Double, Int> {
        var result = parsePrimary(expression, startIndex)
        var value = result.first
        var currentIndex = result.second

        while (currentIndex < expression.length) {
            when (expression[currentIndex]) {
                '*' -> {
                    result = parsePrimary(expression, currentIndex + 1)
                    value *= result.first
                    currentIndex = result.second
                }
                '/' -> {
                    result = parsePrimary(expression, currentIndex + 1)
                    if (result.first == 0.0) {
                        throw ArithmeticException("Division by zero")
                    }
                    value /= result.first
                    currentIndex = result.second
                }
                else -> break
            }
        }

        return Pair(value, currentIndex)
    }

    // Handle numbers, parentheses, and negative numbers
    private fun parsePrimary(expression: String, startIndex: Int): Pair<Double, Int> {
        var currentIndex = startIndex

        // Skip whitespace
        while (currentIndex < expression.length && expression[currentIndex].isWhitespace()) {
            currentIndex++
        }

        if (currentIndex >= expression.length) {
            throw IllegalArgumentException("Unexpected end of expression")
        }

        // Handle parentheses
        if (expression[currentIndex] == '(') {
            val result = parseAdditionSubtraction(expression, currentIndex + 1)
            val nextIndex = result.second

            if (nextIndex >= expression.length || expression[nextIndex] != ')') {
                throw IllegalArgumentException("Missing closing parenthesis")
            }

            return Pair(result.first, nextIndex + 1)
        }

        // Handle negative numbers
        if (expression[currentIndex] == '-') {
            val result = parsePrimary(expression, currentIndex + 1)
            return Pair(-result.first, result.second)
        }

        // Parse the number
        val numberBuilder = StringBuilder()
        var hasDecimalPoint = false

        while (currentIndex < expression.length) {
            val c = expression[currentIndex]
            if (c.isDigit()) {
                numberBuilder.append(c)
                currentIndex++
            } else if (c == '.' && !hasDecimalPoint) {
                numberBuilder.append(c)
                hasDecimalPoint = true
                currentIndex++
            } else {
                break
            }
        }

        if (numberBuilder.isEmpty()) {
            throw IllegalArgumentException("Expected number at position $currentIndex")
        }

        return Pair(numberBuilder.toString().toDouble(), currentIndex)
    }
}