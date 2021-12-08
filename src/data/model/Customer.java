package data.model;

public class Customer {

    private String name ;
    private String socialSecurityId ;
    private String emailAddress ;

    public Customer(String name, String socialSecurityId, String emailAddress) {
        this.name = name;
        this.socialSecurityId = socialSecurityId;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocialSecurityId() {
        return socialSecurityId;
    }

    public void setSocialSecurityId(String socialSecurityId) {
        this.socialSecurityId = socialSecurityId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Name " + getName() + ", Social security number " + getSocialSecurityId() +
                " email address " + getEmailAddress();
    }
}
