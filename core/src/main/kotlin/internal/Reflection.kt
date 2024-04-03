package lamarque.loic.catest.core.internal

import kotlin.reflect.KClass

internal inline fun <reified T : Any> simpleNameOf(): String {
    val kClass: KClass<T> = T::class
    return kClass.simpleName
        ?: error("Getting simple name of '$kClass' shouldn't fail.")
}
