package com.lyhorng.common.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Base service interface providing common CRUD operations
 * Extend this in your services for consistent behavior
 */
public interface BaseService<T, ID> {

    /**
     * Find all entities
     */
    @Transactional(readOnly = true)
    List<T> findAll();

    /**
     * Find entity by ID
     */
    @Transactional(readOnly = true)
    T findById(ID id);

    /**
     * Create new entity
     */
    @Transactional
    T create(T entity);

    /**
     * Update existing entity
     */
    @Transactional
    T update(ID id, T entity);

    /**
     * Delete entity by ID
     */
    @Transactional
    void delete(ID id);

    /**
     * Check if entity exists
     */
    @Transactional(readOnly = true)
    boolean existsById(ID id);

    /**
     * Count all entities
     */
    @Transactional(readOnly = true)
    long count();
}

