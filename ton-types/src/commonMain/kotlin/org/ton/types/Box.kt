package org.ton.types

import kotlin.reflect.KProperty

data class Box(
    var value: Any = Unit,
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Any = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
        this.value = value
    }
}