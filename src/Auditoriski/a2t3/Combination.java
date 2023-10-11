package Auditoriski.a2t3;

public class Combination {
    private int password;
    private boolean isOpen;

    public Combination(int password) {
        this.password = password;
        this.isOpen = false;
    }
    public boolean open(int password){
        this.isOpen=(this.password==password);
        return isOpen;
    }
    public boolean changePassword(int oldPassword, int newPassword){
        if(password==oldPassword){
            password=newPassword;
            return true;
        }
        return false;
    }
}
