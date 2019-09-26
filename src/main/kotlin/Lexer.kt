sealed class Token {
    override fun toString(): String {
        return javaClass.simpleName
    }
    data class Number(val n: Int) : Token()
    data class Variable(val v: String) : Token()
    object Lambthere : Token()
    object Arrow : Token()
    object KlammerLinks : Token()
    object KlammerRechts : Token()
    object EndOfFile : Token()
}

class Lexer(val input: String) : Iterator<Token> {
    val iterator = PeakableIterator(input.iterator())
    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): Token {
        if (!iterator.hasNext()) {
            return Token.EndOfFile
        }
        return when (val lame = iterator.next()) {
            '\\' -> Token.Lambthere
            '-' -> {
                if (iterator.next() == '>') Token.Arrow else throw Exception("brauwr")
            }
            '(' -> Token.KlammerLinks
            ')' -> Token.KlammerRechts
            else -> {
                if (lame.isDigit()){
                    var str = lame.toString()
                    while (iterator.hasNext() && iterator.peek().isDigit()) {
                        str += iterator.next()
                    }
                    Token.Number(str.toInt())
                } else if (lame.isJavaIdentifierStart()){
                    var str: String = lame.toString()
                    while (iterator.hasNext() && iterator.peek().isJavaIdentifierPart()){
                        str += iterator.next()
                    }
                    Token.Variable(str)
                } else {
                    Token.EndOfFile
                }
            }
        }.also { consumeWhiteSpace() }
    }

    fun consumeWhiteSpace(){
        while (iterator.hasNext() && iterator.peek().isWhitespace()){
            iterator.next()
        }
    }
}

class PeakableIterator<T>(val iterator: Iterator<T>) : Iterator<T> {

    var lookAhead: T? = null

    override fun hasNext(): Boolean = (lookAhead != null || iterator.hasNext())

    override fun next(): T {
        if (lookAhead != null) {
            val value = lookAhead
            lookAhead = null
            return value!!
        } else {
            return iterator.next()
        }
    }

    fun peek(): T {
        if (lookAhead == null) {
            lookAhead = iterator.next()
        }
        return lookAhead!!
    }

}

fun main() {
    val interartor = PeakableIterator(listOf(1, 2, 3, 4, 5, 6).listIterator())
    println(interartor.next())
    println(interartor.peek())
    println(interartor.next())

    val lexxxxer: Lexer= Lexer("293356    egdjhedg   \\ -> ((gggg))")
    var token: Token
    do {
        token = lexxxxer.next()
        println(token)
    }while (token != Token.EndOfFile)
}