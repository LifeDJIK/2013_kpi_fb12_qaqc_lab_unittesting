/*
 * Copyright (C) 2013 Ivan Krakhmaliuk (LifeDJIK)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package myatm;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.InOrder;

public class ATMTest {
    public ATMTest() {}

    @Test
    public void getMoneyInATM_test() {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double result = atm.getMoneyInATM();
        assertEquals(moneyInATM, result, 0.0);
    }

    @Test(expected = NoCardInserted.class)
    public void validateCard_testNoCardInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = null;
        int pinCode = 0;
        atm.validateCard(card, pinCode);
    }

    @Test
    public void validateCard_testValidCardAndPin() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        int pinCode = 1234;
        boolean result = atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        verify(card).isBlocked();
        assertTrue(result);
    }

    @Test
    public void validateCard_testValidCardInvalidPin() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        int pinCode = 4321;
        boolean result = atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        assertFalse(result);
    }

    @Test
    public void validateCard_testInvalidCardValidPin() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        int pinCode = 1234;
        boolean result = atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        verify(card).isBlocked();
        assertFalse(result);
    }

    @Test
    public void validateCard_testInvalidCardInvalidPin() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        int pinCode = 4321;
        boolean result = atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        assertFalse(result);
    }

    @Test(expected = NoCardInserted.class)
    public void checkBalance_testNoCardEverInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        atm.checkBalance();
    }

    @Test(expected = NoCardInserted.class)
    public void checkBalance_testInvalidCardInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        int pinCode = 1234;
        atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        verify(card).isBlocked();
        atm.checkBalance();
    }

    @Test
    public void checkBalance_testValidCardInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double balance = 555;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(account);
        int pinCode = 1234;
        atm.validateCard(card, pinCode);
        double result = atm.checkBalance();
        verify(card).isBlocked();
        InOrder inOrder = inOrder(card, account);
        inOrder.verify(card).checkPin(pinCode);
        inOrder.verify(card).getAccount();
        inOrder.verify(account).getBalance();
        assertEquals(balance, result, 0.0);
    }

    @Test(expected = NoCardInserted.class)
    public void getCash_testNoCardEverInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double amount = 500;
        atm.getCash(amount);
    }

    @Test(expected = NoCardInserted.class)
    public void getCash_testInvalidCardInserted() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        int pinCode = 4321;
        atm.validateCard(card, pinCode);
        verify(card).checkPin(pinCode);
        double amount = 500;
        atm.getCash(amount);
    }

    @Test(expected = NotEnoughMoneyInAccount.class)
    public void getCash_testNotEnoughMoneyInAccount() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double balance = 555;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(account);
        int pinCode = 1234;
        atm.validateCard(card, pinCode);
        verify(card).isBlocked();
        InOrder inOrder = inOrder(card, account);
        inOrder.verify(card).checkPin(pinCode);
        inOrder.verify(card).getAccount();
        double amount = 777;
        atm.getCash(amount);
        inOrder.verify(account).getBalance();
    }

    @Test(expected = NotEnoughMoneyInATM.class)
    public void getCash_testNotEnoughMoneyInATM() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double balance = 5555;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(account);
        int pinCode = 1234;
        atm.validateCard(card, pinCode);
        verify(card).isBlocked();
        InOrder inOrder = inOrder(card, account);
        inOrder.verify(card).checkPin(pinCode);
        inOrder.verify(card).getAccount();
        double amount = 5000;
        atm.getCash(amount);
        inOrder.verify(account).getBalance();
    }
    
    @Test
    public void getCash_testValidCardAndEnoughMoney() throws Exception {
        double moneyInATM = 1000;
        ATM atm = new ATM(moneyInATM);
        double balance = 555;
        Account account = mock(Account.class);
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.checkPin(1234)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getAccount()).thenReturn(account);
        int pinCode = 1234;
        atm.validateCard(card, pinCode);
        verify(card).isBlocked();
        InOrder inOrder = inOrder(card, account);
        inOrder.verify(card).checkPin(pinCode);
        inOrder.verify(card).getAccount();
        double amount = 500;
        /*
         * Our mocked Account cannot change state -- I have no idea how to do that without dirty tricks in mockito.
         * So Account.getBalance() always first returns initial balance
         * and on all consequent invocations returns (balance - amount)
         */
        when(account.getBalance()).thenReturn(balance).thenReturn(balance - amount);
        when(account.withdrow(amount)).thenReturn(amount);
        assertEquals(balance - amount, atm.getCash(amount), 0.0);
        inOrder.verify(account).getBalance();
        inOrder.verify(account).withdrow(amount);
        assertEquals(moneyInATM - amount, atm.getMoneyInATM(), 0.0);
    }
}
