/**
 * Created by Alex Anastassatos
 *
 * Stores information needed to connect to auction houses
 */

package AuctionCentral;

public class AuctionHouseConnectionInfo
{
  private final String address;
  private final int port;
  
  /**
   * Stores the given information.
   * @param address
   * @param port
   */
  AuctionHouseConnectionInfo(final String address, final int port)
  {
    this.address = address;
    this.port = port;
  }
  
  /**
   * Gets the stored address.
   * @return
   */
  public String getAddress()
  {
    return address;
  }
  
  /**
   * Gets the stored port.
   * @return
   */
  public int getPort()
  {
    return port;
  }
}
