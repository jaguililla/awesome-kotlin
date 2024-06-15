/*
 * This file is generated by jOOQ.
 */
package jooq.main.tables.daos


import java.time.OffsetDateTime

import jooq.main.tables.LibraryVersion
import jooq.main.tables.records.LibraryVersionRecord

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class LibraryVersionDao(configuration: Configuration?) : DAOImpl<LibraryVersionRecord, jooq.main.tables.pojos.LibraryVersion, Long>(LibraryVersion.LIBRARY_VERSION, jooq.main.tables.pojos.LibraryVersion::class.java, configuration) {

    /**
     * Create a new LibraryVersionDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: jooq.main.tables.pojos.LibraryVersion): Long? = o.id

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfId(lowerInclusive: Long?, upperInclusive: Long?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    fun fetchById(vararg values: Long): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.ID, *values.toTypedArray())

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    fun fetchOneById(value: Long): jooq.main.tables.pojos.LibraryVersion? = fetchOne(LibraryVersion.LIBRARY_VERSION.ID, value)

    /**
     * Fetch records that have <code>library_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfLibraryId(lowerInclusive: Long?, upperInclusive: Long?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.LIBRARY_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>library_id IN (values)</code>
     */
    fun fetchByLibraryId(vararg values: Long): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.LIBRARY_ID, *values.toTypedArray())

    /**
     * Fetch records that have <code>group_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfGroupId(lowerInclusive: String?, upperInclusive: String?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.GROUP_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>group_id IN (values)</code>
     */
    fun fetchByGroupId(vararg values: String): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.GROUP_ID, *values)

    /**
     * Fetch records that have <code>artifact_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfArtifactId(lowerInclusive: String?, upperInclusive: String?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.ARTIFACT_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>artifact_id IN (values)</code>
     */
    fun fetchByArtifactId(vararg values: String): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.ARTIFACT_ID, *values)

    /**
     * Fetch records that have <code>version BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfVersion(lowerInclusive: String?, upperInclusive: String?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.VERSION, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>version IN (values)</code>
     */
    fun fetchByVersion(vararg values: String): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.VERSION, *values)

    /**
     * Fetch records that have <code>created BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfCreated(lowerInclusive: OffsetDateTime?, upperInclusive: OffsetDateTime?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.CREATED, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>created IN (values)</code>
     */
    fun fetchByCreated(vararg values: OffsetDateTime): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.CREATED, *values)

    /**
     * Fetch records that have <code>updated BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfUpdated(lowerInclusive: OffsetDateTime?, upperInclusive: OffsetDateTime?): List<jooq.main.tables.pojos.LibraryVersion> = fetchRange(LibraryVersion.LIBRARY_VERSION.UPDATED, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>updated IN (values)</code>
     */
    fun fetchByUpdated(vararg values: OffsetDateTime): List<jooq.main.tables.pojos.LibraryVersion> = fetch(LibraryVersion.LIBRARY_VERSION.UPDATED, *values)
}