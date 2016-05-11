import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

class WCParserCallbackTagHandler extends HTMLEditorKit.ParserCallback
{
  HistoryDLM                    hDLM;
  LinkedList<HopsWebRecord> queueList;
  StringBuffer                  parentURLString;
  int                           hopIndex;
  int                           numberLinks;

  WCParserCallbackTagHandler(String parentURLString,HistoryDLM hDLM,LinkedList<HopsWebRecord>  queueList,int hopIndex)
  {
    this.hDLM            = hDLM;
    this.queueList       = queueList;
    this.parentURLString = new StringBuffer(parentURLString);
    this.hopIndex        = hopIndex;
    numberLinks          = 0;
  }
//===========================================================================================================
  public void handleStartTag(HTML.Tag tag, MutableAttributeSet attSet,int pos)
  {
    Object attribute;
    StringBuffer attributeStr;
    StringBuffer httpStrbuff = new StringBuffer("http://");
    StringBuffer httpsecStrbuff = new StringBuffer("https://");

    if(tag==HTML.Tag.A)
    {
      attribute = attSet.getAttribute(HTML.Attribute.HREF);
      if(attribute != null)
      {
         attributeStr = new StringBuffer(attribute.toString());

         if(attributeStr.indexOf("http://") != -1)
           checkForinLists(httpStrbuff,attributeStr);

         else if(attributeStr.indexOf("https://") != -1)
           checkForinLists(httpsecStrbuff,attributeStr);
      }
    }
  }//end of handleStartTag
//===========================================================================================================
  void checkForinLists(StringBuffer protocolStr,StringBuffer attributeStr)
  {
    try
    {
      int listSize;
      int index;
      String[] split = attributeStr.toString().split("/");// SPLITS THE STRING BY / FOR PARSING OUT THE DOMAINS
      StringBuffer urlStr = new StringBuffer();
      Boolean foundURL;
      HopsWebRecord tmpWR;

      foundURL = false;

      urlStr.append(protocolStr.append(split[2]));//URL STRING SET

      Arrays.fill(split, null);//NULLS STRING ARRAY AFTER DONE USING IT
      listSize = queueList.size();//Queue LIST SIZE
      index    = 0;

      if(urlStr.toString().equals(parentURLString.toString()))//Checks to see if the current URL is the same as the one parsed out.
        foundURL = true;

      while(index != listSize && !foundURL)//Iterates through the linked list checking for the current URL(Going to be bonkers slow and take up all the memory)
      {
        tmpWR = (HopsWebRecord)queueList.get(index);
        if(urlStr.toString().equals(tmpWR.getURL().toString()))
          foundURL = true;
        else
          index++;
      }

      listSize = hDLM.size();//history LIST SIZE
      index    = 0;//resets Index
      while(index != listSize && !foundURL)//Iterates through the history list
      {
        if(urlStr.toString().equals(hDLM.get(index).getURL().toString()))
          foundURL = true;
        else
          index++;
      }

      if(!foundURL)
      {
        queueList.add(new HopsWebRecord(new URL(urlStr.toString()),hopIndex));
        System.out.println(urlStr + " Added to Queue!");
        numberLinks++;
      }
    }//end of try
    catch(MalformedURLException mURL)
    {
      System.out.println("MalformedURL found in String queue");
      mURL.printStackTrace();
    }
  }
//===========================================================================================================
  int numberOfLinksOff()
  {
    return numberLinks;
  }
}//endofparse