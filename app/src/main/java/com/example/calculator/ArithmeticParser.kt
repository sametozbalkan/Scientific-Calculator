package com.example.calculator

import kotlin.math.PI
import kotlin.math.ln
import kotlin.math.pow
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
        if ("+-×÷()%^eπ".contains(input[pos])) {
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
        throw IllegalArgumentException("Geçersiz karakter: ${input[pos]}")
    }
}

fun evaluate(expression: String): Double {
    val expressionWithMod = expression.replace("mod", "%")
    val tokenizer = Tokenizer(expressionWithMod)
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
            else -> throw IllegalStateException("Geçersiz operatör: $op")
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
            "log" -> parseLogFunction(tokenizer)
            else -> throw IllegalStateException("Geçersiz operatör: $op")
        }
    }
    return result
}

fun parseLogFunction(tokenizer: Tokenizer): Double {
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
                    throw IllegalArgumentException("Log fonksiyonunda geçersiz kullanım")
                }
            } else {
                throw IllegalArgumentException("Log fonksiyonunda eksik parantez")
            }
        } else {
            throw IllegalArgumentException("Log fonksiyonunda eksik virgül")
        }
    } else {
        throw IllegalArgumentException("Geçersiz log fonksiyonu kullanımı")
    }
}

fun parseFactor(tokenizer: Tokenizer): Double {
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
            else -> throw IllegalStateException("Geçersiz operatör: $op")
        }
    }
    if (tokenizer.token == "e") {
        tokenizer.nextToken()
        return Math.E
    }
    if (tokenizer.token == "π") {
        tokenizer.nextToken()
        return PI
    }
    val trigFunctions = mapOf(
        "tan" to Math::tan,
        "cot" to { angle: Double -> 1.0 / tan(angle) },
        "sin" to Math::sin,
        "cos" to Math::cos
    )
    if (tokenizer.token in trigFunctions.keys) {
        val trigFunc = tokenizer.token!!
        tokenizer.nextToken()
        if (tokenizer.token == "(") {
            tokenizer.nextToken()
            val angle = parseExpression(tokenizer)
            if (tokenizer.token == ")") {
                tokenizer.nextToken()
                val angleInRadians = Math.toRadians(angle)
                return trigFunctions[trigFunc]!!(angleInRadians)
            } else {
                throw IllegalArgumentException("Trigonometrik fonksiyonda eksik parantez")
            }
        } else {
            throw IllegalArgumentException("Geçersiz trigonometrik fonksiyon kullanımı: $trigFunc")
        }
    }
    if (tokenizer.token == "log") {
        return parseLogFunction(tokenizer)
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
            throw IllegalArgumentException("Eksik parantez")
        }
    }
    throw IllegalArgumentException("Geçersiz faktör: ${tokenizer.token}")
}