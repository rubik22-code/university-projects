package uk.ac.soton.app.enums;

import uk.ac.soton.app.Application;

/**
 * Represents different themes for the user interface
 */
public enum Style {
    DEFAULT;

    public String asString()
    {
        return ( "default" );
    }

    public String asPath()
    {
        return ( Application.class.getResource(this.asString() + ".css" ).toExternalForm() );
    }
}
