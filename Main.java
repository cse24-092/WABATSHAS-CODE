package com.elitebank;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank("Elite Bank", "Gaborone");
        Address addr = new Address("12 College Rd", "Gaborone", "BW");
        Customer c1 = new Customer("CUST001", "Stevie", "Pansiri", "stevie", "1234", addr);
        bank.addCustomer(c1);

        Account acc1 = bank.openAccount("ACC001", "savings", 1000.0, c1);
        Account acc2 = bank.openAccount("ACC002", "cheque", 500.0, c1);

        System.out.println("Initial balances:");
        System.out.println(acc1.getAccountNumber() + " = " + acc1.getBalance());
        System.out.println(acc2.getAccountNumber() + " = " + acc2.getBalance());

        acc1.deposit(200);
        acc2.withdraw(100);

        System.out.println("After transactions:");
        System.out.println(acc1.getAccountNumber() + " = " + acc1.getBalance());
        System.out.println(acc2.getAccountNumber() + " = " + acc2.getBalance());

        bank.applyInterest();
        System.out.println("After applying interest:");
        System.out.println(acc1.getAccountNumber() + " = " + acc1.getBalance());
        System.out.println(acc2.getAccountNumber() + " = " + acc2.getBalance());
    }
}
