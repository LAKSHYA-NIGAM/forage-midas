package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionProcessingTest {

    @Test
    public void testSuccessfulTransferWithIncentive() {
        TransactionProcessingService service = new TransactionProcessingService();

        UserRecord sender = new UserRecord("Alice", 500.0f);
        UserRecord recipient = new UserRecord("Bob", 100.0f);
        Transaction tx = new Transaction(1L, 2L, 200.0f);

        // $200 transfer with $15 incentive bonus
        TransactionProcessingService.ProcessingResult result = service.processTransaction(sender, recipient, tx, 15.0f);

        assertTrue(result.isSuccessful());
        assertEquals(300.0f, result.getSenderBalanceAfter());
        assertEquals(315.0f, result.getRecipientBalanceAfter()); // 100 + 200 + 15
        assertEquals(15.0f, result.getIncentiveApplied());
        assertTrue(result.getMessage().contains("completed successfully"));
    }

    @Test
    public void testInsufficientFundsRejection() {
        TransactionProcessingService service = new TransactionProcessingService();

        UserRecord sender = new UserRecord("Charlie", 50.0f);
        UserRecord recipient = new UserRecord("Dave", 100.0f);
        Transaction tx = new Transaction(3L, 4L, 200.0f);

        TransactionProcessingService.ProcessingResult result = service.processTransaction(sender, recipient, tx, 0.0f);

        assertFalse(result.isSuccessful());
        assertEquals(50.0f, result.getSenderBalanceAfter()); // Unchanged
        assertEquals(100.0f, result.getRecipientBalanceAfter()); // Unchanged
        assertTrue(result.getMessage().contains("Insufficient funds"));
    }

    @Test
    public void testNegativeAmountRejection() {
        TransactionProcessingService service = new TransactionProcessingService();

        UserRecord sender = new UserRecord("Eve", 100.0f);
        UserRecord recipient = new UserRecord("Frank", 100.0f);
        Transaction tx = new Transaction(5L, 6L, -50.0f);

        TransactionProcessingService.ProcessingResult result = service.processTransaction(sender, recipient, tx, 0.0f);

        assertFalse(result.isSuccessful());
        assertTrue(result.getMessage().contains("strictly positive"));
    }
}
