package Bank;

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
}
