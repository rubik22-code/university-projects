package uk.ac.soton.app.model.DataBase;

import uk.ac.soton.app.enums.Privilege;

/**
 * Record used to represent a user
 * @param username username of this user
 * @param accessLevel access level of this user
 */
public record User(String username, Privilege accessLevel) { }
