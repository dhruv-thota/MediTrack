package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Generic in-memory storage repository that maintains synchronized
 * {@link ArrayList} and {@link HashMap} collections for fast $O(1)$ ID lookups
 * and ordered list iteration.
 *
 * @param <T> The entity type stored in this data store.
 */
public class DataStore<T> {

    private final Map<String, T> map;
    private final List<T> list;
    private final Function<T, String> idExtractor;

    /**
     * Constructs a DataStore using the provided strategy function to extract entity IDs.
     *
     * @param idExtractor A function mapping an entity instance to its String ID.
     */
    public DataStore(Function<T, String> idExtractor) {
        this.idExtractor = Objects.requireNonNull(idExtractor, "idExtractor cannot be null");
        this.map = new HashMap<>();
        this.list = new ArrayList<>();
    }

    /**
     * Helper method to validate that an ID is neither null nor blank.
     *
     * @param id The ID string to validate.
     * @return true if ID is valid, false if null or blank.
     */
    private boolean isValidId(String id) {
        return id != null && !id.isBlank();
    }

    /**
     * Saves a new entity to the data store, keeping map and list synchronized.
     * Rejects null entities, invalid IDs, and duplicate IDs.
     *
     * @param entity The entity to save.
     * @return true if successfully saved as a new record, false otherwise.
     */
    public boolean save(T entity) {
        if (entity == null) {
            return false;
        }
        String id = idExtractor.apply(entity);
        if (!isValidId(id) || map.containsKey(id)) {
            return false;
        }
        map.put(id, entity);
        list.add(entity);
        return true;
    }

    /**
     * Retrieves an entity by its unique ID.
     *
     * @param id The entity ID.
     * @return Optional containing the entity if found, or empty Optional if missing or invalid ID.
     */
    public Optional<T> getById(String id) {
        if (!isValidId(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Returns an unmodifiable view of all stored entities in insertion order.
     *
     * @return List of all entities.
     */
    public List<T> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    /**
     * Updates an existing entity, synchronizing both map and list.
     * Rejects null entities, invalid IDs, or IDs that do not exist.
     *
     * @param entity The updated entity instance.
     * @return true if updated, false if entity ID did not exist or was invalid.
     */
    public boolean update(T entity) {
        if (entity == null) {
            return false;
        }
        String id = idExtractor.apply(entity);
        if (!isValidId(id) || !map.containsKey(id)) {
            return false;
        }
        map.put(id, entity);
        for (int i = 0; i < list.size(); i++) {
            if (id.equals(idExtractor.apply(list.get(i)))) {
                list.set(i, entity);
                break;
            }
        }
        return true;
    }

    /**
     * Deletes an entity by its unique ID, removing it from both map and list.
     *
     * @param id The entity ID to delete.
     * @return true if deleted, false if ID was invalid or not present.
     */
    public boolean deleteById(String id) {
        if (!isValidId(id) || !map.containsKey(id)) {
            return false;
        }
        map.remove(id);
        list.removeIf(item -> id.equals(idExtractor.apply(item)));
        return true;
    }

    /**
     * Checks whether an entity with the specified ID exists.
     *
     * @param id The entity ID.
     * @return true if exists and valid, false otherwise.
     */
    public boolean existsById(String id) {
        return isValidId(id) && map.containsKey(id);
    }

    /**
     * Returns the total count of stored items.
     *
     * @return The item count.
     */
    public int size() {
        return list.size();
    }

    /**
     * Clears all items from the data store.
     */
    public void clear() {
        map.clear();
        list.clear();
    }
}

