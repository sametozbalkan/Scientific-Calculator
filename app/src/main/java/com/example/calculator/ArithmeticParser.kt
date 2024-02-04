package com.example.calculator

import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

class Tokenizer(private val input: String) {
    private var pos = 0
    var token: String? = null
    fun nextToken() {
        while (pos < input.length && input[pos].isWhitespace()) {
            pos++
        }
        if (pos == input.length) {
            token = null
            return
        }
        if (input[pos].isDigit() || input[pos] == '.') {
            val sb = StringBuilder()
            while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) {
                sb.append(input[pos])
                pos++
            }
            token = sb.toString()
            return
        }
        if ("+-×÷()%^e".contains(input[pos])) {
            token = input[pos].toString()
            pos++
            return
        }
        if (input.startsWith("log", pos)) {
            token = "log"
            pos += 3
            return
        }
        if (input[pos] == ',') {
            token = ","
            pos++
            return
        }
        val trigFunctions = listOf("tan", "cot", "sin", "cos")
        for (func in trigFunctions) {
            if (input.startsWith(func, pos)) {
                token = func
                pos += func.length
                return
            }
        }
        throw IllegalArgumentException("Invalid character: ${input[pos]}")
    }
}

fun evaluate(expression: String): Double {
    val tokenizer = Tokenizer(expression)
    tokenizer.nextToken()
    return parseExpression(tokenizer)
}

fun parseExpression(tokenizer: Tokenizer): Double {
    var result = parseTerm(tokenizer)
    while (tokenizer.token in listOf("+", "-")) {
        val op = tokenizer.token!!
        tokenizer.nextToken()
        val term = parseTerm(tokenizer)
        result = when (op) {
            "+" -> result + term
            "-" -> result - term
            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }
    return result
}

fun parseTerm(tokenizer: Tokenizer): Double {
    var result = parseFactor(tokenizer)
    while (tokenizer.token in listOf("×", "÷", "%", "e", "^", "log")) {
        val op = tokenizer.token!!
        tokenizer.nextToken()
        val factor = parseFactor(tokenizer)
        result = when (op) {
            "×" -> result * factor
            "÷" -> result / factor
            "%" -> result % factor
            "e" -> Math.E.pow(factor).toString().take(9).toDouble()
            "^" -> result.pow(factor)
            "log" -> {
                if (tokenizer.token == "(") {
                    tokenizer.nextToken()
                    val base = parseExpression(tokenizer)
                    val argument = parseExpression(tokenizer)
                    if (tokenizer.token == ")") {
                        tokenizer.nextToken()
                        if (base > 0 && base != 1.0 && argument > 0) {
                            ln(argument) / ln(base)
                        } else {
                            throw IllegalArgumentException("Invalid arguments for logarithmic function")
                        }
                    } else {
                        throw IllegalArgumentException("Missing closing parenthesis after log function")
                    }
                } else {
                    throw IllegalArgumentException("Invalid usage of log function")
                }
            }

            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }
    return result
}

fun parseFactor(tokenizer: Tokenizer): Double {
    if (tokenizer.token == null) {
        throw IllegalArgumentException("Missing factor")
    }
    if (tokenizer.token!!.toDoubleOrNull() != null) {
        val value = tokenizer.token!!.toDouble()
        tokenizer.nextToken()
        return value
    }
    if (tokenizer.token in listOf("+", "-")) {
        val op = tokenizer.token!!
        tokenizer.nextToken()
        val factor = parseFactor(tokenizer)
        return when (op) {
            "+" -> +factor
            "-" -> -factor
            else -> throw IllegalStateException("Invalid operator: $op")
        }
    }
    if (tokenizer.token == "e") {
        tokenizer.nextToken()
        return Math.E
    }
    if (tokenizer.token in listOf("tan", "cot", "sin", "cos")) {
        val trigFunc = tokenizer.token!!
        tokenizer.nextToken()
        if (tokenizer.token == "(") {
            tokenizer.nextToken()
            val angle = parseExpression(tokenizer)
            if (tokenizer.token == ")") {
                tokenizer.nextToken()
                return when (trigFunc) {
                    "tan" -> tan(angle)
                    "cot" -> 1 / tan(angle)
                    "sin" -> sin(angle)
                    "cos" -> cos(angle)
                    else -> throw IllegalStateException("Invalid trigonometric function: $trigFunc")
                }
            } else {
                throw IllegalArgumentException("Missing closing parenthesis after trigonometric function")
            }
        } else {
            throw IllegalArgumentException("Invalid usage of trigonometric function: $trigFunc")
        }
    }
    if (tokenizer.token == "log") {
        tokenizer.nextToken()
        if (tokenizer.token == "(") {
            tokenizer.nextToken()
            val base = parseExpression(tokenizer)
            if (tokenizer.token == ",") {
                tokenizer.nextToken()
                val argument = parseExpression(tokenizer)
                if (tokenizer.token == ")") {
                    tokenizer.nextToken()
                    if (base > 0 && base != 1.0 && argument > 0) {
                        return (ln(argument) / ln(base)).toString().take(8).toDouble()
                    } else {
                        throw IllegalArgumentException("Invalid arguments for logarithmic function")
                    }
                } else {
                    throw IllegalArgumentException("Missing closing parenthesis after log function")
                }
            } else {
                throw IllegalArgumentException("Missing comma in log function")
            }
        } else {
            throw IllegalArgumentException("Invalid usage of log function")
        }
    }
    if (tokenizer.token == ",") {
        tokenizer.nextToken()
        return 0.0
    }
    if (tokenizer.token == "(") {
        tokenizer.nextToken()
        val value = parseExpression(tokenizer)
        if (tokenizer.token == ")") {
            tokenizer.nextToken()
            return value
        } else {
            throw IllegalArgumentException("Missing closing parenthesis")
        }
    } else {
        throw IllegalArgumentException("Invalid factor: ${tokenizer.token}")
    }
}