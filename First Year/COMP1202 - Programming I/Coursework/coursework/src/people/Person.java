package people;

/**
 * This class represents a person, that musicians will extend from.
 */

public class Person {

  private String name;
  public Person() {

  }

  /**
   * This constructor creates a person with a name.
   * @param name The name of the person.
   */

  public Person(String name) {
    this.name = name;
  }

  /**
   * This method returns the name of the person.
   * @return The name of the person.
   */

  public String getName() {
    return name;
  }
}
