package Bank;

import java.io.Serializable;

public class CreateBankAccountMessage implements Serializable
{
  private final String name;
  private final int initialBalance;

  public CreateBankAccountMessage(String name, int initialBalance)
  {
    this.name = name;
    this.initialBalance = initialBalance;
  }

  public int getInitialBalance()
  {
    return initialBalance;
  }

  public String getName()
  {
    return name;
  }
}
