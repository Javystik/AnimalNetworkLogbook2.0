package com.zoi4erom.animalnetworkbook.aui;

import java.io.IOException;

/**
 * The Renderable interface represents an object that can be rendered, typically in a UI or display context.
 */
public interface Renderable {

	/**
	 * Renders the object, displaying or presenting its content.
	 *
	 * @throws IOException If an I/O error occurs during the rendering process.
	 */
	default void render() throws IOException {
		// Default implementation does nothing.
	}
}
