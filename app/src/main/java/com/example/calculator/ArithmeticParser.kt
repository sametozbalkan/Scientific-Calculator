package com.example.calculator

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

class Tokenizer(val input: String) {
    var pos = 0
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
    while (tokenizer.token in listOf("×", "÷", "%", "e", "^")) {
        val op = tokenizer.token!!
        tokenizer.nextToken()
        val factor = parseFactor(tokenizer)
        result = when (op) {
            "×" -> result * factor
            "÷" -> result / factor
            "%" -> result % factor
            "e" -> Math.E.pow(factor).toString().take(9).toDouble()
            "^" -> result.pow(factor)
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
        if (tokenizer.token == "(" && tokenizer.input.indexOf(")", tokenizer.pos) != -1) {
            tokenizer.nextToken()
            val angle = parseExpression(tokenizer)
            return if (tokenizer.token == ")") {
                tokenizer.nextToken()
                when (trigFunc) {
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
    if (tokenizer.token == "(" && tokenizer.input.indexOf(
            ")",
            tokenizer.pos
        ) != -1
    ) {
        tokenizer.nextToken()
        val value = parseExpression(tokenizer)
        if (tokenizer.token == ")") {
            tokenizer.nextToken()
            return value
        } else {
            throw IllegalArgumentException("Missing closing parenthesis")
        }
    } else {
        tokenizer.nextToken()
        throw IllegalArgumentException("Invalid factor: ${tokenizer.token}")
    }
}