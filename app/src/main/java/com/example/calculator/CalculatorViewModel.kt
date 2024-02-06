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
                if (lastChar != '.' && lastChar.isDigit()) {
                    if (lastChar in "+-×÷") {
                        expression.value += "0"
                    }
                    expression.value += char
                }
            }
        } else if (char == "(") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit()) {
                    expression.value += "×("
                }else{
                    expression.value += "("
                }

            }else{
                expression.value = char
            }
        } else if (char == "mod") {
            if (expression.value.isNotEmpty()) {
                expression.value += "mod"
            } else {
                expression.value = expression.value.dropLast(1)
            }
        } else if (char == ",") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit()) {
                    expression.value += ","
                }
            }
        } else if (char == "√") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit()) {
                    expression.value = sqrt(expression.value.toDouble()).toString().take(5)
                }
            }
        } else if (char == "π") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    expression.value += "×π"
                } else {
                    expression.value += "π"
                }
            } else {
                expression.value = "π"
            }
        } else if (char == "x²") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit()) {
                    expression.value =
                        (expression.value.toDouble() * expression.value.toDouble()).toString()
                }
            }
        } else if (char == "^") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    expression.value += "^"
                }
            }
        } else if (char == "e") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    expression.value += "×" + "(e^"
                } else {
                    expression.value += "(e^"
                }
            } else {
                expression.value = "e^"
            }
        }
        else if (char == "log") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    expression.value += "×log("
                } else {
                    expression.value += "log("
                }
            } else {
                expression.value = "log("
            }
        }
        else if (char == ")") {
            expression.value += char
        } else if (char in "tscu") {
            if (expression.value.isNotEmpty()) {
                val lastChar = expression.value.last()
                if (lastChar.isDigit() || lastChar == ')') {
                    when (char) {
                        "t" -> expression.value += "×tan("
                        "c" -> expression.value += "×cot("
                        "s" -> expression.value += "×sin("
                        "u" -> expression.value += "×cos("
                    }
                } else {
                    when (char) {
                        "t" -> expression.value += "tan("
                        "c" -> expression.value += "cot("
                        "s" -> expression.value += "sin("
                        "u" -> expression.value += "cos("
                    }
                }
            } else {
                when (char) {
                    "t" -> expression.value += "tan("
                    "c" -> expression.value += "cot("
                    "s" -> expression.value += "sin("
                    "u" -> expression.value += "cos("
                }
            }
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
