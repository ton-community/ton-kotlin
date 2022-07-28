# Ton-Kotlin documentation

Ton-Kotlin is a cross-platform Software Development Kit for [The Open Network](https://ton.org) (TON) written from
scratch in Kotlin. It aims to provide developers with an easy way to interact with the network, the blockchain and
its services while fully embracing concepts outlined in [TON whitepaper](https://ton-blockchain.github.io/docs/ton.pdf)
and supporting documentation, namely [TON Blockchain](https://ton-blockchain.github.io/docs/tblkch.pdf) and
[TON Virtual Machine](https://ton-blockchain.github.io/docs/tvm.pdf).

## ton-adnl

## ton-api

## ton-bigint

## ton-bitstring

TON Virtual Machine operates directly on individual binary digits (bits): 0 (false) and 1 (true). A finite string of
such values is called a bitstring, and is represented by the `BitString` interface in ton-kotlin. This interface is
heavily used by more high-level abstractions, such as cells and bags-of-cells.

```kotlin
BitString(true, false, true) // Create from boolean values
BitString.binary("101")      // Create from string representation of zeroes and ones
```

Bitstrings also have standard hex string representation. It correctly handles cases where number of bits is not
divisible by 8, unlike for example [ton-crypto's](#ton-crypto) `hex()`. In TON's documentation they are denoted by `x`
prefix or being wrapped in `x{` and `}`.

```kotlin
BitString("B_").joinToStringBits() // "101"
BitString.binary("101")            // "B_"
```

TVM sets maximum number of bits in one cell to *1023*. Because of that bitstrings are also limited as it would be
useless to have bitstrings bigger than what can fit in a cell.

```kotlin
BitString(List(1024) { false }) // throws BitStringOverflowException
```

It is important to note that bitstrings, as well as many other primitives are immutable, meaning that in order to modify
its value it is necessary to first convert it to a `MutableBitString`, for example using the
`BitString.toMutableBitString()` method.

```kotlin
BitString("B_")
    .toMutableBitString()
    .apply { // this: MutableBitString
        add(false)                  // Append to the end
        addAll(listOf(false, true)) // Same but for multiple values
        set(1, true)                // Set second element to 1
    }                               // "E6_"
```

Avoid conversion to byte arrays wherever possible, as forgetting to specify correct number of bits may result in extra
zeroes appended to the end of the new bitstring. Conversion to and from byte arrays is not portable, serialization to
Bags-of-Cells should be used instead. See an entry on [ton-boc](#ton-boc).

```kotlin
val a = BitString("B_")             // "B_" - original
val b = BitString(a.toByteArray())  // "A0" - size was not specified, extra zeroes appended to the end

require(a == b) // Fail, they're different bitstrings

val c = BitString(a.toByteArray(), a.size) // "B_" - only `a.size` bits were taken, rest was ignored

require(a == c) // Success
```

## ton-block

## ton-boc

In order to store and transfer cells in a portable way, TON defines a collection of cells as bag-of-cells (BoC),
alongside an algorithm to serialize (write) and deserialize (read) it to/from a series of bytes. In ton-kotlin this
functionality is provided by a `BagOfCells` interface.

## ton-cell

TON Blockchain and Virtual Machine represent all data as so-called cells, where each cell consists of up to 1023 bits
and can contain up to 4 references to other cells, circular references are not allowed. In ton-kotlin, they are
represented by the `Cell` interface.

Once again, cells themselves are immutable - in order to manipulate with them, two extra interfaces are provided:

- `CellBuilder`, that contains helpful definitions to construct new cells
- `CellSlice`, providing functionality to read cells

## ton-crypto

The Open Network extensively relies on strong cryptography algorithms and data conversion methods. **Ton-crypto** module
is aimed to put all the interfaces to these algorithms in one convenient package.

> Note: no safety-critical algorithms are implemented as a part of this module, it simply serves as an interface to
> secure implementations provided by well-trusted libraries, such as [Bouncy Castle](https://www.bouncycastle.org/).

### base64(), base64url(), hex()

These are basic helper functions for easy conversion of binary data (`ByteArray`) into strings and back. Some examples
of their common uses in TON:

- base64 as defined in [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
    - addresses, such as `EQCD39VS5jcptHL8vMjEXrzGaRcCVYto7HUn4bpAOg8xqB2N`
        - liteservers' public keys, for example `VrBrkkxiB/EDNju0FpxMavfESFvtSk1uqZeNNhMT4rs=`
- base64url, as defined in [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
    - addresses, used more often than the plain base64 due to it being url-safe
- hex, a simple conversion of bytes to their hexadecimal string representation
    - addresses, in their raw form: `-1:dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5`
    - bags-of-cells, it is common to store compiled smart-contract code as a hex string, such
      as `B5EE9C72410101010044...`

> Note: `hex()` is only suitable for conversion of whole bytes. Conversion of arbitrary long bit sequences is done
> using **ton-bitstring** and is discussed further in this document.

### CRC16, CRC32, CRC32c

Cyclic Redundancy Check algorithms used in TON blockchain to ensure validity of potentially corrupted data being read
from file system or network. One of their prominent uses is in user-friendly addresses (base64/base64url-encoded), which
prevents users from losing their funds in case of a typo.

### SHA256

### HMAC, PBKDF2

### SecureRandom

A cryptographically-secure source of randomness used in various places, for example in generation of new wallet private
keys. It is advised to use this object over those provided by the language as they might default to unsafe algorithms.

### Ed25519, X25519

## ton-hashmap

This module contains interfaces for working with dictionaries that are defined in
[hashmap.tlb](https://github.com/ton-blockchain/ton/blob/9191be1546444aad7300fd7b5fdde5ff7cf10ca5/crypto/tl/hashmap.tlb)
.

Since ton-kotlin closely follows original schema, `HashMapE` is actually represented as a tree rather than being
flattened to language-defined `Map` on parsing. For ease of use `HashMapE.toMap()` method is provided, it walks through
each node and assembles simpler key-value representation.

## ton-lite-client

Implementing interfaces outlined in **ton-lite-api**, this module provides a way to interact with lite-servers over ADNL
protocol, with many convenience functions added for most common scenarios.

## ton-lite-api

## ton-mnemonic

## ton-smartcontract

## ton-tl

## ton-tlb
