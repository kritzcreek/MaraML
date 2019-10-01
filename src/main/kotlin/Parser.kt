class Parser(lexxxxer: Lexer) {

    val iterator = PeakableIterator(lexxxxer)

    fun parseExpression(): Expression {
        return when (iterator.peek()) {
            is Token.Number -> parseIntLiteral()
            is Token.Variable -> parseVariable()
            is Token.Lambthere -> parseLambthere()
            is Token.Let -> parseLet()
            else -> throw Exception("Ich kann so nicht arbeiten!!!")
        }
    }

    fun parseLambthere(): Expression.Lambthere {
        expectNext<Token.Lambthere>("Booooooooooooooooooooring!")
        val binder = expectNext<Token.Variable>("Du Spaßt! Alter Regen ist nass!").v
        expectNext<Token.Arrow>("Füttere mich lieber (mit Arrows -> Mrauwrs)!")
        val body = parseExpression()
        return Expression.Lambthere(binder, body)
    }

    fun parseVariable(): Expression.Var {
        val name = expectNext<Token.Variable>("Bist du Ire???")
        return Expression.Var(name.v)
    }

    fun parseIntLiteral(): Expression.IntLiteral {
        val number = expectNext<Token.Number>("Hast du Lack gesoffen???")
        return Expression.IntLiteral(number.n)
    }

    fun parseLet(): Expression.Let {
        expectNext<Token.Let>("Let")
        val binder = expectNext<Token.Variable>("Binder").v
        expectNext<Token.Equal>("Equal")
        val expression = parseExpression()
        expectNext<Token.In>("In")
        val body = parseExpression()
        return Expression.Let(binder, expression, body)
    }

    inline fun <reified T> expectNext(text: String): T {
        val token = iterator.next()
        if (token is T) {
            return token
        } else {
            throw Exception("Was soll $token? $text")
        }
    }
}

sealed class Expression {
    data class IntLiteral(val int: Int) : Expression()
    data class Var(val name: String) : Expression()
    data class App(val function: Expression, val argument: Expression) : Expression()
    data class Lambthere(val binder: String, val body: Expression) : Expression()
    data class Let(val binder: String, val expression: Expression, val body: Expression) : Expression()
}

fun main() {
    val parser: Parser = Parser(Lexer("name"))
    println(parser.parseExpression())
    val parser2: Parser = Parser(Lexer("43"))
    println(parser2.parseExpression())
    val parser3: Parser = Parser(Lexer("\\ gnampf -> 42"))
    println(parser3.parseExpression())
    val parser4: Parser = Parser(Lexer("let id = \\ x -> x in id"))
    println(parser4.parseExpression())
}

