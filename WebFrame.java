import java.io.*;
import java.awt.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.table.*;
import java.util.LinkedList;
import javax.swing.text.html.*;
import java.awt.datatransfer.*;
import javax.swing.text.html.parser.*;

public class WebFrame extends JFrame
{
Container               cp;
JTable                  jTable;
JScrollPane             scrollPane;
WebTableModel           webTableModel;
DefaultTableColumnModel tableColMod;

HistoryDLM histDLM;

TableColumn                    colURL;
TableColumn                    colNUMB;
LinkedList<HopsWebRecord>  wdrLinkList;

public WebFrame()
{
  try
  {
    histDLM     = new HistoryDLM();
    wdrLinkList = new LinkedList<HopsWebRecord> ();

    startCrawler(new URL("http://www.thumpertalk.com"));//CALLhttp://www.thumpertalk.com http://www.newegg.com

    webTableModel = new WebTableModel(histDLM);
    jTable        = new JTable(webTableModel);
    scrollPane    = new JScrollPane(jTable);

    jTable.setFont(new Font("ARIAL",Font.PLAIN,11));
    jTable.setMinimumSize(new Dimension());

    //#TABLE COLUMN MODEL SECTION ########################################################
    tableColMod = new DefaultTableColumnModel();
    colURL      = new TableColumn(0);
    colURL.setPreferredWidth(150);
    colURL.setMinWidth(1);
    colURL.setResizable(true);
    colURL.setHeaderValue("URL'S");
    colURL.setIdentifier("URL'S");
    tableColMod.addColumn(colURL);

    colNUMB = new TableColumn(1);
    colNUMB.setPreferredWidth(4);
    colNUMB.setMinWidth(1);
    colURL.setResizable(true);
    colNUMB.setHeaderValue("# of Links");
    tableColMod.addColumn(colNUMB);

    jTable.setColumnModel(tableColMod);

    //######################################################################################

    cp = getContentPane();
    cp.add(scrollPane, BorderLayout.CENTER);
    setupMainFrame();
  }
  catch(MalformedURLException mue)
  {
    System.out.println("Looks as if the URL you are attempting to enter is incorrect!");
  }
}
//***************************************************************************************************************************************************************************************************************
  //Section Below used for setting up the window.
public void setupMainFrame()
{
  Toolkit   tk  = Toolkit.getDefaultToolkit();
  Dimension d = tk.getScreenSize();
  setSize(d.width/2, d.height/2);
  setLocation(d.width/4, d.height/4);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setTitle("WebCrawler Frame");
  setVisible(true);
}
//***************************************************************************************************************************************************************************************************************
public void startCrawler(URL startURL)
{
  int               numberOfHops;
  int               hopsAwayFromRoot;
  Boolean           hopsLimitReached;
  Boolean           rootProcessed;
  HopsWebRecord     tmpWebRec;

  URL                         url = null;
  URLConnection               urlCon;
  InputStreamReader           isr;
  WCParserCallbackTagHandler  tagHandler;

  hopsAwayFromRoot  = 0;
  numberOfHops      = 3;  //NUMBER OF SITES AWAY FROM ORIGINAL LINK
  hopsLimitReached  = false;
  rootProcessed     = false;

  wdrLinkList.add(new HopsWebRecord(startURL,0));

  while(wdrLinkList.size() != 0 && !hopsLimitReached)
  {
    try
      {
        tmpWebRec        = (HopsWebRecord)wdrLinkList.pop();//getFirst() also works

        hopsAwayFromRoot = tmpWebRec.getIndex();

        if(hopsAwayFromRoot > numberOfHops)
          hopsLimitReached = true;
        else
        {
          url           = tmpWebRec.getURL();
          System.out.println("");
          System.out.println("Processing this url: " + url);
          System.out.println("hopsAwayFromRoot: " + hopsAwayFromRoot);
          urlCon        = url.openConnection();
          urlCon.setConnectTimeout(60000);
          isr           = new InputStreamReader(urlCon.getInputStream());

          hopsAwayFromRoot++;

          tagHandler    = new WCParserCallbackTagHandler(url.toString(),histDLM,wdrLinkList,hopsAwayFromRoot);

          new ParserDelegator().parse(isr,tagHandler,true);
          System.out.println("numberOfLinksOff: " + tagHandler.numberOfLinksOff());
          histDLM.addElement(new WebDomainRecord(url,tagHandler.numberOfLinksOff()));
          System.out.println("SizeOfQueue: " + wdrLinkList.size());
          System.out.println("");
          System.out.println("Running Garbage collecter");
          System.gc();
        }
      }
      catch(MalformedURLException mue)
      {
        System.out.println("Error in startCrawler method with URL: " + url);
        mue.printStackTrace();
      }
      catch(IOException ioe)
      {
        System.out.println("startCrawlermethod:Bad Starting input from URL, might not beable to connect?");
        ioe.printStackTrace();
      }
  }
}
//***************************************************************************************************************************************************************************************************************
}//end Class
