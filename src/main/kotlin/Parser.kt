class Parser(lexxxxer: Lexer) {

    val iterator = PeakableIterator(lexxxxer)

    fun parseExpression(): Expression {
        val expressions = mutableListOf<Expression>()
        while (true) {
            val atomi = parseAtom()
            if (atomi == null) {
                break
            } else {
                expressions += atomi
            }
        }
        if (expressions.isEmpty()){
            throw Exception("Ich kann so nicht arbeiten!!!")
        } else {
            return expressions.drop(1).fold(expressions.first(),{accAkut, expri -> Expression.App(accAkut, expri)})
        }
    }

    fun parseAtom(): Expression? {
        return when (iterator.peek()) {
            is Token.Number -> parseIntLiteral()
            is Token.Variable -> parseVariable()
            is Token.Lambthere -> parseLambthere()
            is Token.Let -> parseLet()
            else -> null
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
        expectNext<Token.Let>("Let it Be, let it beeee, let it be oh let it be!!!")
        val binder = expectNext<Token.Variable>("Du bis'se so still, biste am murksen??? (Binder is incorrect)").v
        expectNext<Token.Equal>("Because not all lambs are createt Equal!")
        val expression = parseExpression()
        expectNext<Token.In>("There is no Inn in here!")
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
    val parser5: Parser = Parser(Lexer("f g y"))
    println(parser5.parseExpression())
}

