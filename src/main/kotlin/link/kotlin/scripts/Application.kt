@file:JvmName("Application")

package link.kotlin.scripts

import link.kotlin.scripts.model.ApplicationConfiguration
import link.kotlin.scripts.model.DataApplicationConfiguration
import link.kotlin.scripts.scripting.CachingScriptEvaluator
import link.kotlin.scripts.scripting.ScriptEvaluator
import link.kotlin.scripts.scripting.ScriptingScriptEvaluator
import link.kotlin.scripts.utils.*
import org.apache.http.impl.cookie.IgnoreSpecProvider
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.system.exitProcess

open class ApplicationFactory {
    open val objectMapper by lazy {
        KotlinObjectMapper()
    }

    open val httpClient: HttpClient by lazy {
        val ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:129.0) Gecko/20100101 Firefox/129.0"

        val asyncClient = HttpAsyncClients.custom()
            .setUserAgent(ua)
            .setMaxConnPerRoute(10)
            .setMaxConnTotal(100)
            .setDefaultCookieSpecRegistry { IgnoreSpecProvider() }
            .build()

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            logger<HttpClient>().info("HttpClient Shutdown called...")
            asyncClient.close()
            logger<HttpClient>().info("HttpClient Shutdown done...")
        })

        asyncClient.start()

        DefaultHttpClient(
            client = asyncClient
        )
    }

    open val configuration: ApplicationConfiguration by lazy {
        val configuration = DataApplicationConfiguration()

        if (configuration.ghToken.isEmpty()) {
            logger<DataApplicationConfiguration>().info("GH_TOKEN is not defined, dry run...")
            configuration.copy(dryRun = true)
        } else configuration
    }

    open val markdownRenderer: MarkdownRenderer by lazy {
        val extensions = listOf(TablesExtension.create())
        val parser = Parser.builder().extensions(extensions).build()
        val renderer = HtmlRenderer.builder().extensions(extensions).build()

        CommonMarkMarkdownRenderer(
            parser = parser,
            renderer = renderer
        )
    }

    open val cache: Cache by lazy {
        val folder = Paths.get(System.getProperty("user.home"), ".cache", "awesome-kotlin")
        Files.createDirectories(folder)

        val fileCache = FileCache(
            folder = folder,
            mapper = objectMapper
        )

        DisableCache(
            cache = fileCache,
            configuration = configuration
        )
    }

    open val scriptingHost by lazy {
        BasicJvmScriptingHost()
    }

    open val scriptEvaluator: ScriptEvaluator by lazy {
        val scriptingScriptEvaluator = ScriptingScriptEvaluator(
            scriptingHost = scriptingHost
        )

        CachingScriptEvaluator(
            cache = cache,
            scriptEvaluator = scriptingScriptEvaluator
        )
    }

    open val sitemapGenerator: SitemapGenerator by lazy {
        DefaultSitemapGenerator(
            configuration = configuration
        )
    }

    open val pagesGenerator: PagesGenerator by lazy {
        DefaultPagesGenerator()
    }

    open val rssGenerator: RssGenerator by lazy {
        DefaultRssGenerator()
    }

    open val siteGenerator by lazy {
        val instance = DefaultSiteGenerator(
            mapper = objectMapper,
            sitemapGenerator = sitemapGenerator,
            pagesGenerator = pagesGenerator,
            rssGenerator = rssGenerator,
        )

        callLogger<SiteGenerator>(instance)
    }

    open val linksChecker: LinksChecker by lazy {
        DefaultLinksChecker(
            httpClient = httpClient
        )
    }

    open val generator: AwesomeKotlinGenerator by lazy {
        val linksProcessor = LinksProcessor.default(
            mapper = objectMapper,
            httpClient = httpClient,
            linksChecker = linksChecker,
            configuration = configuration,
            markdownRenderer = markdownRenderer
        )

        val categoryProcessor = CategoryProcessor.default(
            linksProcessor = linksProcessor
        )

        val githubTrending = GithubTrending.default(
            cache = cache
        )

        val implementation = DefaultAwesomeKotlinGenerator(
            linksSource = LinksSource.default(
                scriptEvaluator = scriptEvaluator,
                githubTrending = githubTrending,
                categoryProcessor = categoryProcessor
            ),
            articlesSource = ArticlesSource.default(
                scriptEvaluator = scriptEvaluator,
                articlesProcessor = ArticlesProcessor.default(
                    markdownRenderer = markdownRenderer
                )
            ),
            readmeGenerator = ReadmeGenerator.default(),
            siteGenerator = siteGenerator
        )

        callLogger<AwesomeKotlinGenerator>(implementation)
    }
}

fun main() {
    try {
        val generator = ApplicationFactory().generator

        // Load data
        val articles = generator.getArticles()
        val links = generator.getLinks()

        // Create README.md
        generator.generateReadme(links)

        // Generate resources for site
        generator.generateSiteResources(links, articles)

        LOGGER.info("Done, exit.")
        exitProcess(0)
    } catch (e: Exception) {
        LOGGER.error("Failed, exit.", e)
        exitProcess(1)
    }
}

private val LOGGER = logger {}
