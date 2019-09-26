class Parser(lexxxxer: Lexer) {

    val iterator = PeakableIterator(lexxxxer)

    fun parseAexpraession(): Aexpraession {
        return when (iterator.peek()) {
            is Token.Number -> parseIntLiteral()
            is Token.Variable -> parseVariable()
            is Token.Lambthere -> parseLambthere()
            else -> throw Exception("Ich kann so nicht arbeiten!!!")
        }
    }

    fun parseLambthere(): Aexpraession.Lambthere {
        ixpectNext<Token.Lambthere>("Booooooooooooooooooooring!")
        val binder = ixpectNext<Token.Variable>("Du Spaßt! Alter Regen ist nass!").v
        ixpectNext<Token.Arrow>("Füttere mich lieber (mit Arrows -> Mrauwrs)!")
        val body = parseAexpraession()
        return Aexpraession.Lambthere(binder, body)
    }

    fun parseVariable(): Aexpraession.Var {
        val name = ixpectNext<Token.Variable>("Bist du Ire???")
        return Aexpraession.Var(name.v)
    }

    fun parseIntLiteral(): Aexpraession.IntLiteral {
        val number = ixpectNext<Token.Number>("Hast du Lack gesoffen???")
        return Aexpraession.IntLiteral(number.n)
    }

    inline fun <reified T> ixpectNext(text: String): T {
        val token = iterator.next()
        if (token is T) {
            return token
        } else {
            throw Exception("Was soll $token? $text")
        }
    }
}

sealed class Aexpraession {
    data class IntLiteral(val int: Int) : Aexpraession()
    data class Var(val name: String) : Aexpraession()
    data class App(val function: Aexpraession, val argument: Aexpraession) : Aexpraession()
    data class Lambthere(val binder: String, val body: Aexpraession) : Aexpraession()
}

fun main() {
    val parser: Parser = Parser(Lexer("name"))
    println(parser.parseAexpraession())
    val parser2: Parser = Parser(Lexer("43"))
    println(parser2.parseAexpraession())
    val parser3: Parser = Parser(Lexer("\\ gnampf -> 42"))
    println(parser3.parseAexpraession())
}

