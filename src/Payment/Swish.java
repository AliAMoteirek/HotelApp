package Payment;

public class Swish implements Payment{

    String name ;
    String phoneNumber;

    public Swish(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid by Swish.");
    }
}
