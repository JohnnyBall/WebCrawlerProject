import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class WebTableModel extends AbstractTableModel
                                            implements TableModel
{
  private HistoryDLM hDLM;
  WebTableModel(HistoryDLM hDLM)
  {
    int historySize;
    this.hDLM = hDLM;
    historySize = hDLM.size();
    for(int n = 0; n < historySize; n++)
    {
      setValueAt(hDLM.elementAt(n).getURL(),n,0);
      setValueAt(hDLM.elementAt(n).getNumLinks(),n,1);
    }
  }

//======================================================================================================================
  public int getRowCount()
  {
    return hDLM.size();
  }
//======================================================================================================================
  public int getColumnCount()
  {
    return 2;
  }
//======================================================================================================================
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    WebDomainRecord tmpWDR;
    tmpWDR = (WebDomainRecord) hDLM.elementAt(rowIndex);
    if(columnIndex == 0)
      return tmpWDR.getURL();
    else if(columnIndex == 1)
      return tmpWDR.getNumLinks();
    else
    {
      System.out.println("Error: There is no such Column! (you can only Request columns 0 and 1)");
      return null;
    }
  }
//======================================================================================================================
  public void setValueAt(Object value, int rowIndex, int columnIndex)
  {
    WebDomainRecord tmpWDR;
    tmpWDR = (WebDomainRecord) hDLM.elementAt(rowIndex);
    if(columnIndex == 0)
      tmpWDR.setURL((URL)value);
    else if (columnIndex == 1)
      tmpWDR.setNumLinks((int)value);
    else
      System.out.println("Error: There is no such Column! (you can only Request columns 0 and 1)");
    fireTableCellUpdated(rowIndex,columnIndex);
  }
//======================================================================================================================
}