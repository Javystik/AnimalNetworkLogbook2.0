package com.zoi4erom.animalnetworkbook.persistence.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * An abstract base class representing an entity in the Animal Network Book system.
 * Entities have a unique identifier (UUID) and provide methods for equality and hash code calculation.
 */
public abstract class Entity {
	/**
	 * The unique identifier of the entity.
	 */
	protected final UUID id;

	/**
	 * Constructs an instance of the Entity class with the specified identifier.
	 *
	 * @param id The unique identifier of the entity.
	 */
	protected Entity(UUID id) {
		this.id = id;
	}

	/**
	 * Gets the unique identifier of the entity.
	 *
	 * @return The unique identifier of the entity.
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Checks if this entity is equal to another object.
	 *
	 * @param o The object to compare with this entity.
	 * @return {@code true} if the objects are equal, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Entity entity = (Entity) o;
		return Objects.equals(id, entity.id);
	}

	/**
	 * Calculates the hash code for this entity.
	 *
	 * @return The hash code of the entity.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
