package usecases.rss

import io.heapy.komok.tech.di.lib.Module

@Module
open class RssModule {
    open val rssRoute by lazy {
        RssRoute()
    }

    open val fullRssRoute by lazy {
        FullRssRoute()
    }
}
