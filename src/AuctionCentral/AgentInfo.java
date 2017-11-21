package AuctionCentral;

import java.io.Serializable;

public class AgentInfo implements Serializable
{
  private final String name;
  private final int bankKey;
  
  AgentInfo(final String name, final int bankKey)
  {
    this.name = name;
    this.bankKey = bankKey;
  }
  
  String getName()
  {
    return name;
  }
  
  int getBankKey()
  {
    return bankKey;
  }
}
