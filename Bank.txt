class Bank {

    private long[] balance;

    public Bank(long[] balance) {
        this.balance = balance;
    }
    
    public boolean transfer(int account1, int account2, long money) {
        if(account1-1 >= balance.length || account2-1 >= balance.length) return false;
            
        if(withdraw(account1, money)) {
            return deposit(account2, money);
        }

        return false;
    }
    
    public boolean deposit(int account, long money) {
        if(account-1 < balance.length) {
            this.balance[account-1] += money;
            return true;
        }

        return false;
    }
    
    public boolean withdraw(int account, long money) {
        if(account-1 >= this.balance.length) return false;

        if(this.balance[account-1] < money) return false;

        this.balance[account-1] -= money;
        return true;
    }
}

/**
 * Your Bank object will be instantiated and called as such:
 * Bank obj = new Bank(balance);
 * boolean param_1 = obj.transfer(account1,account2,money);
 * boolean param_2 = obj.deposit(account,money);
 * boolean param_3 = obj.withdraw(account,money);
 */

 /**
 [10,100,20,50,30]

 
  */