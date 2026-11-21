package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;

/**
 * JPMorgan Chase Midas Core Transaction Processing Service
 * Validates account balances, enforces non-negative account rules,
 * and credits incentive bonuses.
 */
public class TransactionProcessingService {

    public static class ProcessingResult {
        private final boolean isSuccessful;
        private final float senderBalanceAfter;
        private final float recipientBalanceAfter;
        private final float incentiveApplied;
        private final String message;

        public ProcessingResult(boolean success, float senderBal, float recipientBal, float incentive, String msg) {
            this.isSuccessful = success;
            this.senderBalanceAfter = senderBal;
            this.recipientBalanceAfter = recipientBal;
            this.incentiveApplied = incentive;
            this.message = msg;
        }

        public boolean isSuccessful() { return isSuccessful; }
        public float getSenderBalanceAfter() { return senderBalanceAfter; }
        public float getRecipientBalanceAfter() { return recipientBalanceAfter; }
        public float getIncentiveApplied() { return incentiveApplied; }
        public String getMessage() { return message; }
    }

    /**
     * Processes transaction between sender and recipient with optional incentive.
     */
    public ProcessingResult processTransaction(UserRecord sender, UserRecord recipient, Transaction tx, float incentiveAmount) {
        if (sender == null || recipient == null) {
            return new ProcessingResult(false, 0f, 0f, 0f, "Invalid sender or recipient account");
        }

        if (tx == null || tx.getAmount() <= 0f) {
            return new ProcessingResult(false, sender.getBalance(), recipient.getBalance(), 0f, "Transaction amount must be strictly positive");
        }

        float amount = tx.getAmount();
        float incentive = Math.max(0f, incentiveAmount);

        // Check if sender has sufficient funds
        if (sender.getBalance() < amount) {
            return new ProcessingResult(false, sender.getBalance(), recipient.getBalance(), 0f,
                    "Insufficient funds: required " + amount + ", available " + sender.getBalance());
        }

        // Execute atomic balance update
        float newSenderBalance = sender.getBalance() - amount;
        float newRecipientBalance = recipient.getBalance() + amount + incentive;

        sender.setBalance(newSenderBalance);
        recipient.setBalance(newRecipientBalance);

        return new ProcessingResult(true, newSenderBalance, newRecipientBalance, incentive,
                "Transaction completed successfully. Incentive of " + incentive + " applied.");
    }
}
