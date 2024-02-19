package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    private String age;
    //using this for age

    private String hobby;
    //using this for hobby



    public Person(){
        
    }

    public Person(String firstName, String lastName, String address, String telephone,String age, String hobby){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.age = age;
        this.hobby = hobby;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAge(){return this.age; }

    public void setAge(String age){this.age = age; }

    public String getHobby(){return this.hobby; }

    public void setHobby(String hobby){this.hobby = hobby; }

    @Override
    public String toString() {
         return firstName   + " "
                + lastName  + " "
                + address   + " "
                + telephone + " "
                + age       + " "
                 + hobby;
    }
}
