package infra.ktor

import io.ktor.server.routing.*

interface KtorRoute {
    fun Routing.install()
}

context(Routing)
fun KtorRoute.installRoute() {
    with(this) {
        install()
    }
}
