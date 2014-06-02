// Copyright MWC 1999, Generated by Together
// @Author : Ian Mayo
// @Project: Debrief 3
// @File   : FormatRNDateTime.java

package MWC.Utilities.TextFormatting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class LabelledDateFormat 
{
  static public String toString(final long theVal)
  {
    
    final java.util.Date theTime = new java.util.Date(theVal);
    String res;
    final DateFormat df = new SimpleDateFormat("dd'd HH'h mm'm ss's");
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    res = df.format(theTime);

    return res;
  }

}

