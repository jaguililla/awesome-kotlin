package usecases.rss

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import infra.ktor.KtorRoute

class RssRoute : KtorRoute {
    override fun Routing.install() {
        get("/rss.xml") {
            call.respondText("<xml></xml>", ContentType.Application.Atom.withCharset(Charsets.UTF_8))
        }
    }
}

class FullRssRoute : KtorRoute {
    override fun Routing.install() {
        get("/rss-full.xml") {
            call.response.headers.append(HttpHeaders.ContentType, ContentType.Application.Atom.withCharset(Charsets.UTF_8).toString())
            call.respond(message = "<xml></xml>")
        }
    }
}
