package uz.pdp.bankcard.exception;

public class MoneyInsufficientException extends Exception{
    public MoneyInsufficientException(String errorMessage){
        super(errorMessage);
    }
}
