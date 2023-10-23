package Auditoriski.a4t1;

//to-do
class InsufficientFundsException extends Exception{
    public InsufficientFundsException() {
        super("Insufficient amount of funds!");
    }
}

interface InterestBearingAccount{
    void addInterest();
}

class InterestCheckingAccount extends Account implements InterestBearingAccount{

    static double INTEREST=0.03;
    public InterestCheckingAccount(String ownerName, int currentAmount) {
        super(ownerName, currentAmount);
    }

    @Override
    public void addInterest() {

    }
}

class PlatinumCheckingAccount extends Account{

    public PlatinumCheckingAccount(String ownerName, int currentAmount) {
        super(ownerName, currentAmount);
    }
}

class NonInterestCheckingAccount extends Account{

    public NonInterestCheckingAccount(String ownerName, int currentAmount) {
        super(ownerName, currentAmount);
    }
}

public abstract class Account {
    private String ownerName;
    private int id;
    private static int counter=1;
    private int currentAmount;

    public Account(String ownerName, int currentAmount) {
        this.ownerName = ownerName;
        this.currentAmount = currentAmount;
        this.id=counter++;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }
    public void withdrawAmount(int amount) throws InsufficientFundsException {
       if(currentAmount<amount){
           throw new InsufficientFundsException();
       }
       currentAmount-=amount;
    }
    public void addAmount(int amount){
        currentAmount+=amount;
    }
}
