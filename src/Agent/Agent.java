package Agent;

public class Agent extends Thread
{

  public Agent()
  {
    AgentBankAccount bankAccount = new AgentBankAccount();
  }

  public static void main(String[] args) throws Exception
  {
    Agent agent = new Agent();
  }
}
