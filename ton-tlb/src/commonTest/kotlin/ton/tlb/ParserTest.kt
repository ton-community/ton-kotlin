//package ton.tlb
//
//import ton.tlb.old.TlbLexer
//import kotlin.test.Test
//
//class ParserTest {
//    @Test
//    fun test() {
//        val tlbLexer = TlbLexer()
//        tlbLexer.reset("hello$0101 test:# = Foo;")
//        while (true) {
//            val token = tlbLexer.advance() ?: break
//            println(token)
//        }
//    }
//}