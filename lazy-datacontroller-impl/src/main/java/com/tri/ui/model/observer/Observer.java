package com.tri.ui.model.observer;

/**
 * The observer interface for receiving change events.
 * 
 * @author khennnig@pobox.com
 */
public interface Observer {

	/**
	 * Invoked when a change event occurs.
	 */
	void update(ChangeEvent event);

}
