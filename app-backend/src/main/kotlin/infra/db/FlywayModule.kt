package infra.db

import io.heapy.komok.tech.di.lib.Module
import org.flywaydb.core.Flyway

@Module
open class FlywayModule(
    private val jdbcModule: JdbcModule,
) {
    open val flyway: Flyway by lazy {
        Flyway.configure()
            .locations("classpath:db/migration/main")
            .dataSource(jdbcModule.dataSource)
            .load()
    }
}
