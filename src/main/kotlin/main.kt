import java.math.BigInteger
import java.util.*

const val ERROR_CODE = -1
const val DELIMITER = " "
const val MAX_ALLOWED_NUMBER = 1048575
const val MIN_ALLOWED_NUMBER = 0
val MAX_ALLOWED_INT: BigInteger = BigInteger.valueOf(1048575)
val stack = Stack<Int>()
fun main(args: Array<String>) {
    println(solution("13 DUP 4 POP 5 DUP + DUP + - DUP + + 9 -"))
}

fun solution(S: String?): Int {
    if (S.isNullOrBlank()) {
        return ERROR_CODE
    }
    val commands = S.split(DELIMITER)
    if (commands.size > 2000) {
        throw IllegalArgumentException("Command to long")
    }
    return commands.map { runCommand(it) }.last()
}

fun runCommand(input: String): Int = when {
    Commands.POP.commandValue == input -> stack.pop()
    Commands.DUP.commandValue == input -> stack.push(stack.peek())
    Commands.ADD_TWO_LAST.commandValue == input -> performTwoLastAdd()
    Commands.SUBTRACT_TWO_LAST.commandValue == input -> performTwoLastSubtract()
    isPositiveNumber(input) -> tryAddToStack(input)
    else -> throw IllegalArgumentException("Only allowed values are [POP, DUP,+, -] and any number between 0 and 1048575");
}

fun tryAddToStack(value: String): Int {
    val fullValue = BigInteger(value)
    if (fullValue > MAX_ALLOWED_INT) {
        throw IllegalArgumentException("Only allowed numbers are between $MIN_ALLOWED_NUMBER and $MAX_ALLOWED_NUMBER");
    }
    return stack.push(fullValue.toInt())
}

fun isPositiveNumber(S: String): Boolean {
    return S.chars().allMatch { codePoint: Int -> Character.isDigit(codePoint) }
}

fun performTwoLastAdd(): Int {
    val firstElement = stack.pop()
    val secondElement = stack.pop()
    val sum = firstElement + secondElement
    return sum.let {
        if (it > MAX_ALLOWED_NUMBER) {
            stack.push(secondElement)
            stack.push(firstElement)
            throw ArithmeticException("Max allowed value is $MAX_ALLOWED_NUMBER");
        }
        stack.push(it)
    }
}

fun performTwoLastSubtract(): Int {
    val firstElement = stack.pop()
    val secondElement = stack.pop()
    val difference = firstElement - secondElement

    return difference.let {
        if (it < MIN_ALLOWED_NUMBER) {
            stack.push(secondElement)
            stack.push(firstElement)
            throw ArithmeticException("Min allowed value is $MIN_ALLOWED_NUMBER");
        }
        stack.push(it)
    }
}