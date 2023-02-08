@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.CellRef
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
public interface HashmapAugE<X, Y> : TlbObject {
    /**
     * ```tl-b
     * ahme_empty$0 {n:#} {X:Type} {Y:Type} extra:Y = HashmapAugE n X Y;
     */
    public interface AhmeEmpty<X, Y> : HashmapAugE<X, Y> {
        public val n: Int
        public val extra: Y

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ahme_empty") {
                field("extra", extra)
            }
        }
    }

    /**
     * ```tl-b
     * ahme_root$1 {n:#} {X:Type} {Y:Type} root:^(HashmapAug n X Y) extra:Y = HashmapAugE n X Y;
     */
    public interface AhmeRoot<X, Y> : HashmapAugE<X, Y> {
        public val n: Int
        public val root: CellRef<HashmapAug<X, Y>>
        public val extra: Y

        public fun loadRoot(): HashmapAug<X, Y> = root.value

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ame_root") {
                field("root", root)
                field("extra", extra)
            }
        }
    }

    public companion object {
        @JvmStatic
        public fun <X, Y> empty(n: Int, extra: Y): AhmeEmpty<X, Y> =
            AhmeEmptyImpl(n, extra)

        @JvmStatic
        public fun <X, Y> root(n: Int, root: CellRef<HashmapAug<X, Y>>, extra: Y): AhmeRoot<X, Y> =
            AhmeRootImpl(n, root, extra)
    }
}

private data class AhmeEmptyImpl<X, Y>(
    override val n: Int,
    override val extra: Y,
) : HashmapAugE.AhmeEmpty<X, Y> {
    override fun toString(): String = print().toString()
}

private data class AhmeRootImpl<X, Y>(
    override val n: Int,
    override val root: CellRef<HashmapAug<X, Y>>,
    override val extra: Y,
) : HashmapAugE.AhmeRoot<X, Y> {
    override fun toString(): String = print().toString()
}
