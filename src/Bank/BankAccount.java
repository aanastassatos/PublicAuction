package Bank;

/**
 * Represents the state of an account created with the bank holing the accounts fund, as well as the name of the
 * agent and the account number. The secret key is already related to the account through the bank's map
 */
public class BankAccount
{
  final private Fund fund;
  final private int accountNumber;
  final private String name;

  public BankAccount(final Fund fund, final int accountNumber, final String name)
  {
    this.fund = fund;
    this.accountNumber = accountNumber;
    this.name = name;
  }

  public Fund getFund()
  {
    return fund;
  }

  public int getAccountNumber()
  {
    return accountNumber;
  }

  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    return name + "'s account";
  }
}
