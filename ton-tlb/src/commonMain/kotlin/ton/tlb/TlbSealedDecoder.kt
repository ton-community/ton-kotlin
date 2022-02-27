package ton.tlb

//class TlbSealedDecoder(
//    tlb: Tlb,
//    bitStringReader: BitStringReader,
//) : TlbDecoder(tlb, bitStringReader) {
//    override fun decodeString(): String = ""
//
//    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>, previousValue: T?): T {
//
//        TODO("DECODE SEALED")
//
//        val (type, value) = deserializer.descriptor.elementDescriptors.toList()
//        val constructors = value.elementDescriptors.asSequence().mapNotNull {
//            val tlbConstructorPrefix =
//                it.annotations.asSequence().filterIsInstance<TlbConstructorPrefix>().firstOrNull()
//            if (tlbConstructorPrefix != null) {
//                tlbConstructorPrefix.bitPrefix to it
//            } else null
//        }.sortedBy { it.first.size }
//
//        val prefixConstructor = ArrayList<Boolean>()
//        val (_, constructorDescriptor) = constructors.find { (prefix, constructorDescriptor) ->
//            while (prefixConstructor.size < prefix.size) {
//                prefixConstructor.add(bitStringReader.readBit())
//            }
//            prefix.contentEquals(prefixConstructor.toBooleanArray())
//        } ?: throw SerializationException("Can't find constructor for $deserializer")
//
//        return beginStructure(constructorDescriptor).decodeSerializableElement(constructorDescriptor, 1, deserializer)
//    }
//}