package com.cognizant.hams.constant;

import com.cognizant.hams.exception.InsufficientFundsException;
import com.cognizant.hams.exception.InvalidPaymentTypeException;
import com.cognizant.hams.exception.InvalidPaymentTypePinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

record CreditCard(String cardNumber, int pin, float balance) {}
record DebitCard(String cardNumber, int pin, float balance) {}
record Upi(String upiId, int pin, float balance) {}

public final class PaymentConstants {

    private static final List<CreditCard> CREDIT_CARDS = new ArrayList<>();
    private static final List<DebitCard> DEBIT_CARDS = new ArrayList<>();
    private static final List<Upi> UPI_ACCOUNTS = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(PaymentConstants.class);

    private PaymentConstants() {}

    static {
        // Credit Cards
        CREDIT_CARDS.add(new CreditCard("4111-2222-3333-4444", 1234, 25000.0f));
        CREDIT_CARDS.add(new CreditCard("4555-6666-7777-8888", 5678, 150000.0f));
        CREDIT_CARDS.add(new CreditCard("4999-8888-7777-6666", 9012, 7500.50f));

        // Debit Cards
        DEBIT_CARDS.add(new DebitCard("5011-2233-4455-6677", 9876, 12345.67f));
        DEBIT_CARDS.add(new DebitCard("5088-7766-5544-3322", 5432, 8500.00f));
        DEBIT_CARDS.add(new DebitCard("5099-1111-0000-2222", 1122, 500.25f));

        // UPI Accounts
        UPI_ACCOUNTS.add(new Upi("john.doe@okbank", 1111, 987.0f));
        UPI_ACCOUNTS.add(new Upi("jane.smith@ybl", 2222, 10500.75f));
        UPI_ACCOUNTS.add(new Upi("testuser@payapp", 9999, 1200.0f));
    }

    /**
     * Validates a payment based on type, identifier, PIN, and amount.
     * Throws specific exceptions for various validation failures.
     * Returns true if validation is successful.
     */
    public static boolean validatePayment(String type, String identifier, int pin, float amount) {
        logger.info("--- \nAttempting validation for {} payment of ${}...", type.toUpperCase(Locale.getDefault()), amount);

        switch (type.toLowerCase()) {
            case "credit":
                for (CreditCard card : CREDIT_CARDS) {
                    if (card.cardNumber().equals(identifier)) {
                        validatePinAndBalance(card.pin(), pin, card.balance(), amount, "credit card");
                        logger.info("Credit Card payment validated.");
                        return true;
                    }
                }
                break;
            case "debit":
                for (DebitCard card : DEBIT_CARDS) {
                    if (card.cardNumber().equals(identifier)) {
                        validatePinAndBalance(card.pin(), pin, card.balance(), amount, "debit card");
                        logger.info("Debit Card payment validated.");
                        return true;
                    }
                }
                break;
            case "upi":
                for (Upi account : UPI_ACCOUNTS) {
                    if (account.upiId().equals(identifier)) {
                        validatePinAndBalance(account.pin(), pin, account.balance(), amount, "UPI ID");
                        logger.info("UPI payment validated.");
                        return true;
                    }
                }
                break;
            default:
                throw new InvalidPaymentTypeException("Invalid payment type specified.");
        }

        // This line is only reached if no identifier was found after checking all loops
        logger.error("Payment identifier not found.");
        return false;
    }

    private static void validatePinAndBalance(int storedPin, int providedPin, float balance, float amount, String type) {
        if (storedPin != providedPin) {
            throw new InvalidPaymentTypePinException("Invalid PIN for " + type);
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds in " + type);
        }
    }
}