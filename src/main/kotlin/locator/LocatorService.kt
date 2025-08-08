package locator

object LocatorService {

    private var locator: Locator? = null

    fun getLocator(): Locator = locator ?: error("Locator has not been created")

    fun startLocator(blocks: List<Locator.() -> Unit>): Locator {
        return locator ?: Locator.create().apply {
            initialize(this)
            blocks.forEach { it() }
        }
    }

    fun stopLocator() {
        locator?.apply { clear() }
        locator = null
    }

    private fun initialize(instance: Locator) {
        locator = instance
    }
}
