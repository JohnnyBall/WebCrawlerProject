import java.net.*;

public class WebDomainRecord
{
  private URL url;
  private int numOfLinksOff;

  public WebDomainRecord(URL url,int numberOf)
  {
     this.url = url;
     this.numOfLinksOff = numberOf;
  }
//=========================================================
  public void setURL(URL urlOfSite)
  {
    this.url = urlOfSite;
  }
//=========================================================
  public void setNumLinks(int number)
  {
    this.numOfLinksOff = number;
  }
//=========================================================
  public URL getURL()
  {
    return url;
  }
//=========================================================
  public int getNumLinks()
  {
    return numOfLinksOff;
  }
//=========================================================
}