package com.jpmc.midascore.foundation;

import com.jpmc.midascore.entity.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPMC Midas Core Foundation & Entity Tests")
class TransactionModelTest {

    @Test
    @DisplayName("UserRecord should store name and balance accurately")
    void testUserRecord() {
        UserRecord user = new UserRecord("Alice", 1500.50f);
        assertEquals("Alice", user.getName());
        assertEquals(1500.50f, user.getBalance(), 0.001f);
        assertEquals(0L, user.getId());

        user.setBalance(2000.00f);
        assertEquals(2000.00f, user.getBalance(), 0.001f);
        assertTrue(user.toString().contains("Alice"));
    }

    @Test
    @DisplayName("Transaction should initialize with sender, recipient and amount")
    void testTransaction() {
        Transaction tx = new Transaction(101L, 202L, 75.25f);
        assertEquals(101L, tx.getSenderId());
        assertEquals(202L, tx.getRecipientId());
        assertEquals(75.25f, tx.getAmount(), 0.001f);

        tx.setAmount(100.0f);
        assertEquals(100.0f, tx.getAmount(), 0.001f);
        assertTrue(tx.toString().contains("101"));
        assertTrue(tx.toString().contains("202"));
    }

    @Test
    @DisplayName("Balance should correctly hold numeric balance amount")
    void testBalance() {
        Balance b = new Balance(525.75f);
        assertEquals(525.75f, b.getAmount(), 0.001f);

        b.setAmount(600.0f);
        assertEquals(600.0f, b.getAmount(), 0.001f);
        assertTrue(b.toString().contains("600"));
    }

    @Test
    @DisplayName("Transactions between distinct accounts should be valid")
    void testTransactionDistinctAccounts() {
        Transaction tx = new Transaction(1L, 2L, 50.0f);
        assertNotEquals(tx.getSenderId(), tx.getRecipientId(), "Sender and recipient must be distinct");
        assertTrue(tx.getAmount() > 0, "Transaction amount must be strictly positive");
    }
}
