package locator

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentHashMap.KeySetView
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class Locator private constructor() {

    val anyLock = Any()

    val hash: ConcurrentHashMap<KType, Any> = ConcurrentHashMap()
    val blocksHash: ConcurrentHashMap<KType, () -> Any> = ConcurrentHashMap()
    val factories: ConcurrentHashMap<KType, () -> Any> = ConcurrentHashMap()
    val resolving: KeySetView<KType, Boolean> = ConcurrentHashMap.newKeySet()
    var errors: AtomicReference<StringBuilder> = AtomicReference(StringBuilder())

    fun clear() {
        hash.clear()
        blocksHash.clear()
        factories.clear()
        resolving.clear()
        errors.get().clear()
    }

    inline fun <reified T : Any> singleton(noinline func: () -> T) {
        blocksHash[typeOf<T>()] = func
    }

    inline fun <reified T : Any> provider(noinline func: () -> T) {
        factories[typeOf<T>()] = func
    }

    inline fun <reified T> get(): T {
        synchronized(anyLock) {
            val typeOf = typeOf<T>()

            val bean = hash[typeOf] as? T
            if (bean != null) {
                return bean
            }

            if (!resolving.add(typeOf)) {
                error("Circular dependency detected for $typeOf")
                throw Exception(errors.toString())
            }

            val loaded: T? = load(typeOf)
            if (loaded != null) {
                return loaded
            }

            val created = factories[typeOf]?.invoke() as? T
            if (created != null) {
                resolving.remove(typeOf)
                return created
            }

            error("Bean $typeOf not defined")
            throw Exception(errors.toString())
        }
    }

    inline fun <reified T> load(typeOf: KType): T? {
        return try {
            blocksHash[typeOf]?.let {
                blocksHash.remove(typeOf)
                (it() as? T)?.also { t -> hash[typeOf] = t }
            }
        } catch (_: Exception) {
            null
        }
    }

    fun error(message: String) {
        errors.updateAndGet {
            if (it.isNotBlank()) it.append(", ")
            it.append(message)
        }
    }

    companion object {

        fun create(): Locator {
            return Locator()
        }
    }
}
