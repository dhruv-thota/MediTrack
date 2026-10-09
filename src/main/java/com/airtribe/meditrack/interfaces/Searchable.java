package com.airtribe.meditrack.interfaces;

import java.util.List;

/**
 * Generic search capability contract.
 *
 * @param <T> The entity type to search.
 */
public interface Searchable<T> {
    
    /**
     * Search for an entity by its unique identifier.
     *
     * @param id The entity ID to search for.
     * @return The matching entity, or null if not found.
     */
    T searchById(String id);

    /**
     * Search for entities matching a given name.
     *
     * @param name The name query string.
     * @return A list of matching entities.
     */
    List<T> searchByName(String name);
}
