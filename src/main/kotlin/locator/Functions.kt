package locator

fun startLocator(block: Locator.() -> Unit) {
    startLocator(listOf(block))
}

fun startLocator(vararg blocks: Locator.() -> Unit) {
    LocatorService.startLocator(blocks.asList())
}

fun startLocator(blocks: List<Locator.() -> Unit>) {
    LocatorService.startLocator(blocks)
}

fun stopLocator() = LocatorService.stopLocator()

/**
 * Lazily injects a dependency of type [T] from the current Locator.
 *
 * The default thread-safety mode for lazy is LazyThreadSafetyMode.SYNCHRONIZED.
 *
 * This is typically used for property injection using Kotlin's `by` syntax.
 * The dependency is resolved on first access.
 *
 * Example:
 * ```
 * val myService by inject<MyService>()
 * ```
 *
 * @param T the type of the dependency to inject
 * @return a lazy-initialized instance of [T]
 * @throws IllegalStateException if the Locator has not been started
 * @throws Exception if the bean is not registered or a circular dependency is detected
 */
inline fun <reified T : Any> inject(): Lazy<T> = lazy { with(LocatorService.getLocator()) { get() } }

/**
 * Immediately retrieves a dependency of type [T] from the current Locator.
 *
 * Use this when you need the instance directly, such as during initialization
 * or inside functions or tests.
 *
 * Example:
 * ```
 * val myService = get<MyService>()
 * ```
 *
 * @param T the type of the dependency to retrieve
 * @return an instance of [T]
 * @throws IllegalStateException if the Locator has not been started
 * @throws Exception if the bean is not registered or a circular dependency is detected
 */
inline fun <reified T : Any> get(): T = with(LocatorService.getLocator()) { get() }
