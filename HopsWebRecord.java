import java.net.*;

public class HopsWebRecord 
{
  public URL url;
  public int index;

  public HopsWebRecord(URL url,int numberOf)
  {
     this.url = url;
     this.index = numberOf;
  }
//=========================================================
  public void setURL(URL urlOfSite)
  {
    this.url = urlOfSite;
  }
//=========================================================
  public void setIndex(int number)
  {
    this.index = number;
  }
//=========================================================
  public URL getURL()
  {
    return url;
  }
//=========================================================
  public int getIndex()
  {
    return index;
  }
//=========================================================
}