package Messages;

import java.io.Serializable;

public class RegisterAgentMessage implements Serializable
{
  private final String name;
  private final int bankKey;
  
  public RegisterAgentMessage(String name, int bankKey)
  {
    this.name = name;
    this.bankKey = bankKey;
  }
  
  public String getName()
  {
    return name;
  }
  
  public int getBankKey()
  {
    return bankKey;
  }
}
