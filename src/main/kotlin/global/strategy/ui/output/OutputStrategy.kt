package global.strategy.ui.output

fun interface OutputStrategy {
    fun execute(message: String)
}