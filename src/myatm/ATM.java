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

public class ATM
{
    private double __moneyInATM;
    private Account __account;

    /*
     * Можно задавать количество денег в банкомате
     */
    ATM(double moneyInATM) {
        __moneyInATM = moneyInATM;
        __account = null;
    }

    public double getMoneyInATM() {
         return __moneyInATM;
    }

    /*
     * С вызова данного метода начинается работа с картой
     * Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
     * Если неправильный пин-код или карточка заблокирована, возвращаем false.
     * При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
     */
    public boolean validateCard(Card card, int pinCode) throws NoCardInserted {
        if (card == null) { throw new NoCardInserted(); }
        if (card.checkPin(pinCode) && !card.isBlocked()) {
            __account = card.getAccount();
            return true;
        }
        __account = null;
        return false;
    }

    /*
     * Возвращает сколько денег есть на счету
     */
    public double checkBalance() throws NoCardInserted {
        if (__account == null) { throw new NoCardInserted(); }
        return __account.getBalance();
    }

    /*
     * Метод для снятия указанной суммы
     * Метод возвращает сумму, которая у клиента осталась на счету после снятия
     * Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
     * Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount
     * Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM
     * При успешном снятии денег, указанная сумма должна списываться со счета,
     * и в банкомате должно уменьшаться количество денег
     */
    public double getCash(double amount) throws NoCardInserted, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        if (__account == null) { throw new NoCardInserted(); }
        if (__moneyInATM < amount) { throw new NotEnoughMoneyInATM(); }
        if (__account.getBalance() < amount) { throw new NotEnoughMoneyInAccount(); }
        __moneyInATM -= __account.withdrow(amount);
        return __account.getBalance();
    }
}
