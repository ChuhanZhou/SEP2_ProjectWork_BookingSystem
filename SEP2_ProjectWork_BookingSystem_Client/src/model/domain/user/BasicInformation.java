package model.domain.user;

import model.domain.order.OrderInterface;

public class BasicInformation
{
  private String firstName;
  private String lastName;
  private String gender;
  private String nationality;
  private String phoneNumber;
  private String email;

  public BasicInformation(String firstName,String lastName,String gender, String nationality, String email, String phoneNumber)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.email = email;
    this.nationality = nationality;
    this.phoneNumber = phoneNumber;
  }

  public BasicInformation(String email)
  {
    this("","","","",email,"");
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public void setGender(String gender)
  {
    this.gender = gender;
  }

  public void setNationality(String nationality)
  {
    this.nationality = nationality;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getGender()
  {
    return gender;
  }

  public String getNationality()
  {
    return nationality;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public boolean hasNull()
  {
    return firstName.equals("")||lastName.equals("")||gender.equals("")|| nationality.equals("")|| phoneNumber.equals("")|| firstName==null||lastName==null||gender==null|| nationality==null|| phoneNumber==null;
  }

  public BasicInformation copy()
  {
    BasicInformation other = new BasicInformation(firstName,lastName,gender,nationality,email,phoneNumber);
    return other;
  }

  @Override public String toString()
  {
    return "Email: " + email + "\nName: " + firstName + " " + lastName + "\nGender: " + gender + "\nNationality: " + nationality +"\nPhone Number: " + phoneNumber;
  }
}
