package com.example.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.sqrt

class CalculatorViewModel : ViewModel() {
    val expression = mutableStateOf("")
    fun clear() {
        expression.value = ""
    }

    fun append(char: String) {
        if (char in "0123456789") {
            expression.value += char
        } else if (char in "+-×÷") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()

                if (lastChar in "+-×÷") {
                    expression.value = expression.value.dropLast(1)
                }
            }
            expression.value += char
        } else if (char == ".") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar != '.') {
                    if (lastChar in "+-×÷") {
                        expression.value += "0"
                    }
                    expression.value += char
                }
            }

        } else if (char == "(") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar !in "+-×÷") {
                    expression.value += "×"
                }
            }
            expression.value += char
        } else if (char == "%") {
            if (expression.value.isNotEmpty()) {
                expression.value += "%"
            } else {
                expression.value = expression.value.dropLast(1)
            }
        } else if (char == "√") {
            if (expression.value.isNotEmpty()) {
                expression.value = sqrt(expression.value.toDouble()).toString().take(5)
            } else {
                expression.value = expression.value.dropLast(1)
            }
        } else if (char == "π") {
            if (expression.value.isNotEmpty()) {
                expression.value = (expression.value.toDouble() * 3.14159265).toString().take(6)
            } else {
                expression.value = Math.PI.toString().take(6)
            }
        } else if (char == "x²") {
            if (expression.value.isNotEmpty()) {
                expression.value =
                    (expression.value.toDouble() * expression.value.toDouble()).toString()
            } else {
                expression.value = expression.value.dropLast(1)
            }
        } else if (char == "e") {
            if (expression.value.isNotEmpty()) {
                expression.value += "x" + "e^"
            } else {
                expression.value = "e^"
            }
        } else if (char == ")") {
            expression.value += char
        }
    }

    fun delete() {
        if (expression.value.isNotEmpty()) {
            expression.value = expression.value.dropLast(1)
        }
    }

    fun evaluate() {
        expression.value = try {
            val result = evaluate(expression.value)
            result.toString()
        } catch (e: Exception) {
            e.toString()
        }
    }
}