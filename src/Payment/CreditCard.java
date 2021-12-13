package Payment;

public class CreditCard implements Payment{

    String name ;
    String cardNumber ;

    public CreditCard(String name, String cardNumber) {
        this.name = name;
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid with Card.");
    }
}
