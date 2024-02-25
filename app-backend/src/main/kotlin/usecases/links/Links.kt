package usecases.links

import io.heapy.komok.tech.di.lib.Module
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import infra.ktor.KtorRoute

@Module
open class LinksModule {
    open val linkSource by lazy {
        LinkSource().get()
    }

    open val route by lazy {
        LinksRoute(
            links = linkSource,
        )
    }
}

class LinksRoute(
    private val links: List<CategoryV1>,
) : KtorRoute {
    override fun Routing.install() {
        get("/api/links") {
            val isAwesome = call.request.queryParameters["awesome"]?.toBoolean() ?: false
            val isKugs = call.request.queryParameters["kugs"]?.toBoolean() ?: false
            val query = call.request.queryParameters["query"]

            val filteredLinks = if (isAwesome) {
                if (query != null) {
                    links.filterByQuery(query)
                } else {
                    links.filterByAwesome()
                }
            } else if (isKugs) {
                links.filterByKug().filterByQuery(query)
            } else {
                links.filterByQuery(query)
            }

            call.respond(filteredLinks)
        }
    }
}

fun List<CategoryV1>.filterByAwesome(): List<CategoryV1> {
    return this.mapNotNull { category ->
        val subcategories = category.subcategories.mapNotNull { subcategory ->
            val links = subcategory.links.filter { linkV1 ->
                linkV1.state == LinkStateV1.AWESOME
            }

            if (links.isNotEmpty()) subcategory.copy(links = links) else null
        }

        if (subcategories.isNotEmpty()) category.copy(subcategories = subcategories) else null
    }
}
fun List<CategoryV1>.filterByKug(): List<CategoryV1> {
    return this.filter { categoryV1 ->
        categoryV1.name == "Kotlin User Groups"
    }
}

fun List<CategoryV1>.filterByQuery(query: String?): List<CategoryV1> {
    return if (query != null) {
        val searchTerm = query.lowercase()

        this.mapNotNull { category ->
            val subcategories = category.subcategories.mapNotNull { subcategory ->
                val links = subcategory.links.filter { link ->
                    link.name.contains(searchTerm, ignoreCase = true) ||
                        link.desc?.contains(searchTerm, ignoreCase = true) == true ||
                        link.tags.any { tag -> tag.contains(searchTerm, ignoreCase = true) }
                }
                if (links.isNotEmpty()) subcategory.copy(links = links) else null
            }
            if (subcategories.isNotEmpty()) category.copy(subcategories = subcategories) else null
        }
    } else {
        this
    }
}

class LinkSource {
    fun get(): List<CategoryV1> {
        val json = LinkSource::class.java
            .classLoader
            .getResource("links/links.json")
            .readText()
        return Json.decodeFromString(json)
    }
}

@Serializable
data class LinkV1(
    val name: String,
    val href: String? = null,
    val desc: String? = null,
    val platforms: List<PlatformTypeV1> = emptyList(),
    val tags: List<String> = emptyList(),
    val star: Int? = null,
    val update: String? = null,
    val state: LinkStateV1,
)

@Serializable
enum class PlatformTypeV1 {
    ANDROID,
    COMMON,
    IOS,
    JS,
    JVM,
    NATIVE,
    WASM
}

@Serializable
enum class LinkStateV1 {
    AWESOME,
    UNSUPPORTED,
    ARCHIVED,
    DEFAULT,
}

@Serializable
data class SubcategoryV1(
    val name: String,
    val links: List<LinkV1>
)

@Serializable
data class CategoryV1(
    val name: String,
    val subcategories: List<SubcategoryV1>
)
