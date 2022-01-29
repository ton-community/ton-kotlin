package ton.fift

import kotlin.test.Test

val fiftLib = "{ bl word 1 2 ' (create) } \"::\" 1 (create)\n" +
        "{ bl word 0 2 ' (create) } :: :\n" +
        "{ bl word 2 2 ' (create) } :: :_\n" +
        "{ bl word 3 2 ' (create) } :: ::_\n" +
        "{ bl word 0 (create) } : create\n" +
        "{ bl word (forget) } : forget\n" +
        "{ bl word 1 ' (forget) } :: [forget]\n" +
        "{ 0 word drop 0 'nop } :: //\n" +
        "{ char \" word 1 { swap { abort } if drop } } ::_ abort\"\n" +
        "{ { bl word dup \"\" \$= abort\"comment extends after end of file\" \"*/\" \$= } until 0 'nop } :: /*\n" +
        "{ char \" word 1 ' type } ::_ .\"\n" +
        "{ char } word x>B 1 'nop } ::_ B{\n" +
        "{ swap ({) over 2+ -roll swap (compile) (}) } : does\n" +
        "{ 1 'nop does create } : constant\n" +
        "{ 2 'nop does create } : 2constant\n" +
        "{ hole constant } : variable\n" +
        "10 constant ten\n" +
        "{ bl word 1 { find 0= abort\"word not found\" } } :: (')\n" +
        "{ bl word find not abort\"-?\" 0 swap } :: [compile]\n" +
        "{ bl word 1 { \n" +
        "  dup find { \" -?\" \$+ abort } ifnot nip execute\n" +
        "} } :: @'\n" +
        "{ bl word 1 { swap 1 'nop does swap 0 (create) }\n" +
        "} :: =:\n" +
        "{ bl word 1 { -rot 2 'nop does swap 0 (create) }\n" +
        "} :: 2=:\n" +
        "{ <b swap s, b> } : s>c\n" +
        "{ s>c hashB } : shash\n" +
        "{ dup 0< ' negate if } : abs\n" +
        "{ 2dup > ' swap if } : minmax\n" +
        "{ minmax drop } : min\n" +
        "{ minmax nip } : max\n" +
        "\"\" constant <#\n" +
        "' \$reverse : #>\n" +
        "{ swap 10 /mod char 0 + rot swap hold } : #\n" +
        "{ { # over 0<= } until } : #s\n" +
        "{ 0< { char - hold } if } : sign\n" +
        "{ dup abs <# #s rot sign #> nip } : (.)\n" +
        "{ (.) type } : ._\n" +
        "{ ._ space } : .\n" +
        "{ dup 10 < { 48 } { 55 } cond + } : Digit\n" +
        "{ dup 10 < { 48 } { 87 } cond + } : digit\n" +
        "{ rot swap /mod Digit rot swap hold } : B#\n" +
        "{ rot swap /mod digit rot swap hold } : b#\n" +
        "{ 16 B# } : X#\n" +
        "{ 16 b# } : x#\n" +
        "{ -rot { 2 pick B# over 0<= } until rot drop } : B#s\n" +
        "{ -rot { 2 pick b# over 0<= } until rot drop } : b#s\n" +
        "{ 16 B#s } : X#s\n" +
        "{ 16 b#s } : x#s\n" +
        "variable base\n" +
        "{ 10 base ! } : decimal\n" +
        "{ 16 base ! } : hex\n" +
        "{ 8 base ! } : octal\n" +
        "{ 2 base ! } : binary\n" +
        "{ base @ B# } : Base#\n" +
        "{ base @ b# } : base#\n" +
        "{ base @ B#s } : Base#s\n" +
        "{ base @ b#s } : base#s\n" +
        "{ over abs <# rot 1- ' X# swap times X#s rot sign #> nip } : (0X.)\n" +
        "{ over abs <# rot 1- ' x# swap times x#s rot sign #> nip } : (0x.)\n" +
        "{ (0X.) type } : 0X._\n" +
        "{ 0X._ space } : 0X.\n" +
        "{ (0x.) type } : 0x._\n" +
        "{ 0x._ space } : 0x.\n" +
        "{ bl (-trailing) } : -trailing\n" +
        "{ char 0 (-trailing) } : -trailing0\n" +
        "{ char \" word 1 ' \$+ } ::_ +\"\n" +
        "{ find 0<> dup ' nip if } : (def?)\n" +
        "{ bl word 1 ' (def?) } :: def?\n" +
        "{ bl word 1 { (def?) not } } :: undef?\n" +
        "{ def? ' skip-to-eof if } : skip-ifdef\n" +
        "{ bl word dup (def?) { drop skip-to-eof } { 'nop swap 0 (create) } cond } : library\n" +
        "{ bl word dup (def?) { 2drop skip-to-eof } { swap 1 'nop does swap 0 (create) } cond } : library-version\n" +
        "{ char ) word \"\$\" swap \$+ 1 { find 0= abort\"undefined parameter\" execute } } ::_ \$(\n" +
        "{ sbitrefs rot brembitrefs rot >= -rot <= and } : s-fits?\n" +
        "{ swap sbitrefs -rot + rot brembitrefs -rot <= -rot <= and } : s-fits-with?\n" +
        "{ 0 swap ! } : 0!\n" +
        "{ tuck @ + swap ! } : +!\n" +
        "{ tuck @ swap - swap ! } : -!\n" +
        "{ 1 swap +! } : 1+!\n" +
        "{ -1 swap +! } : 1-!\n" +
        "{ null swap ! } : null!\n" +
        "{ not 2 pick @ and xor swap ! } : ~!\n" +
        "0 tuple constant nil\n" +
        "{ 1 tuple } : single\n" +
        "{ 2 tuple } : pair\n" +
        "{ 3 tuple } : triple\n" +
        "{ 1 untuple } : unsingle\n" +
        "{ 2 untuple } : unpair\n" +
        "{ 3 untuple } : untriple\n" +
        "{ over tuple? { swap count = } { 2drop false } cond } : tuple-len?\n" +
        "{ 0 tuple-len? } : nil?\n" +
        "{ 1 tuple-len? } : single?\n" +
        "{ 2 tuple-len? } : pair?\n" +
        "{ 3 tuple-len? } : triple?\n" +
        "{ 0 [] } : first\n" +
        "{ 1 [] } : second\n" +
        "{ 2 [] } : third\n" +
        "' pair : cons\n" +
        "' unpair : uncons\n" +
        "{ 0 [] } : car\n" +
        "{ 1 [] } : cdr\n" +
        "{ cdr car } : cadr\n" +
        "{ cdr cdr } : cddr\n" +
        "{ cdr cdr car } : caddr\n" +
        "{ null ' cons rot times } : list\n" +
        "{ -rot pair swap ! } : 2!\n" +
        "{ @ unpair } : 2@\n" +
        "{ true (atom) drop } : atom\n" +
        "{ bl word atom 1 'nop } ::_ `\n" +
        "{ hole dup 1 { @ execute } does create } : recursive\n" +
        "{ 0 { 1+ dup 1 ' \$() does over (.) \"\$\" swap \$+ 0 (create) } rot times drop } : :\$1..n\n" +
        "{ 10 hold } : +cr\n" +
        "{ 9 hold } : +tab\n" +
        "{ \"\" swap { 0 word 2dup \$cmp } { rot swap \$+ +cr swap } while 2drop } : scan-until-word\n" +
        "{ 0 word -trailing scan-until-word 1 'nop } ::_ \$<<\n" +
        "{ 0x40 runvmx } : runvmcode\n" +
        "{ 0x48 runvmx } : gasrunvmcode\n" +
        "{ 0xc8 runvmx } : gas2runvmcode\n" +
        "{ 0x43 runvmx } : runvmdict\n" +
        "{ 0x4b runvmx } : gasrunvmdict\n" +
        "{ 0xcb runvmx } : gas2runvmdict\n" +
        "{ 0x45 runvmx } : runvm\n" +
        "{ 0x4d runvmx } : gasrunvm\n" +
        "{ 0xcd runvmx } : gas2runvm\n" +
        "{ 0x55 runvmx } : runvmctx\n" +
        "{ 0x5d runvmx } : gasrunvmctx\n" +
        "{ 0xdd runvmx } : gas2runvmctx\n" +
        "{ 0x75 runvmx } : runvmctxact\n" +
        "{ 0x7d runvmx } : gasrunvmctxact\n" +
        "{ 0xfd runvmx } : gas2runvmctxact\n" +
        "{ 0x35 runvmx } : runvmctxactq\n" +
        "{ 0x3d runvmx } : gasrunvmctxactq\n"

class FiftLibTest {
    @Test
    fun libTest() = testFift {
        val lines = fiftLib.split("\n")
        var line = 0
        try {
            lines.forEachIndexed { index, s ->
                line = index + 1
                interpret(s)
            }
        } catch (e: Exception) {
            throw RuntimeException("Error while interpretation on $line:$charPos - `$currentLine`", e)
        }
    }
}