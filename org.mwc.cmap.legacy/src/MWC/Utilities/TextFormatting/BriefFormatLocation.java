/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
// @Author : Ian Mayo
// @Project: Debrief 3
// @File   : BriefFormatLocation.java

package MWC.Utilities.TextFormatting;

import java.text.DecimalFormat;

import MWC.GenericData.WorldLocation;

public class BriefFormatLocation implements PlainFormatLocation
{
	/** degree symbol, obviously.  Taken from helpful website at http://www.fileformat.info/info/unicode/char/00b0/index.htm
	 * 
	 */
  public static final String DEGREE_SYMBOL = "\u00B0";
	public static DecimalFormat df = new DecimalFormat("00.00", new java.text.DecimalFormatSymbols(java.util.Locale.UK));
  public static DecimalFormat df2 = new DecimalFormat("00", new java.text.DecimalFormatSymbols(java.util.Locale.UK));
  public static DecimalFormat df3 = new DecimalFormat("000", new java.text.DecimalFormatSymbols(java.util.Locale.UK));

  /** reduce object creation - have working copy of object used for breaking down decimal value into components
   * 
   */
  private static brokenDown _workingHolder = new brokenDown(0, true);


  /**
   * break this double degrees down to a string
   *
   * @param val            the latitude in degrees
   * @param includeDecimal whether to plot decimal seconds
   */
  static public String toStringLat(final double val, final boolean includeDecimal)
  {
    _workingHolder.setData(val, true);

    java.text.DecimalFormat secFormat = null;
    if (includeDecimal)
      secFormat = df;
    else
      secFormat = df2;

    String res = " " + df2.format(_workingHolder.deg) + DEGREE_SYMBOL +
      df2.format(_workingHolder.min) + "\'" +
      secFormat.format(_workingHolder.sec) + "\"";

    // just check we're not at origin
    if(val != 0)
    	 res += _workingHolder.hem + "";

    // hack: when the degs symbol appears in the third character of the string, when
    // we write to Metafile the following (4th) character is swapped for a y.

    // when the degs symbol appears as the 4th character, however, the string
    // writes perfectly to metafile

    // consequently we insert a space at the front of the string to artificially
    // put the deg symbol in the 4th character.

    return res;
  }

  /**
   * break this double degrees down to a string
   *
   * @param val            the longitude in degrees
   * @param includeDecimal whether to plot decimal seconds
   */
  static public String toStringLong(final double val, final boolean includeDecimal)
  {
    _workingHolder.setData(val, false);

    java.text.DecimalFormat secFormat = null;
    if (includeDecimal)
      secFormat = df;
    else
      secFormat = df2;

    String res = df3.format(_workingHolder.deg) + DEGREE_SYMBOL +
      df2.format(_workingHolder.min) + "\'" +
      secFormat.format(_workingHolder.sec) + "\"";

    // just check we're not at origin
    if(val != 0)
    	 res += _workingHolder.hem + "";

    return res;
  }

  /**
   * return the supplied location as a string
   */
  static public String toString(final WorldLocation loc)
  {
    final String res = toStringLat(loc.getLat(), true) + " " + toStringLong(loc.getLong(), true);

    return res;
  }

  static public class brokenDown
  {
    // the resolution we are going to use when 'rounding' the product of the subtraction
    // below, since 12.2 - 12 is resulting in 1.999999999 ==> MAD!
    private static final long SCALE = 1000000;
    public int deg;
    public int min;
    public double sec;
    public char hem;

    public brokenDown(final double val, final boolean isLat)
    {
      setData(val, isLat);
    }

    public void setData(final double val, final boolean isLat)
    {
      hem = doHem(val, isLat);
      final double theVal = Math.abs(val);
      deg = (int) (theVal);
      double degVal = Math.rint((theVal - deg) * SCALE) / SCALE;

      // special case - if we're saying that there are 60 minutes, we really
      // mean 1 degree and 59 minutes. trap it.
      if (degVal == 1.0)
      {
        degVal = 0;
        deg++;
        min = 0;
      }
      else
      {
        min = (int) (degVal * 60.0);
      }

      // ok, on with the seconds
      sec = ((degVal) - ((double) min / 60.0)) * 3600d;

      // there's a wierd circumstance where we get 60 minutes calculated, look out for it
      if (sec >= 59.99)
      {
        // also check if the minutes are also at 59
        if (min == 59)
        {
          // increment the degrees
          deg++;
          // zero the minutes
          min = 0;
        }
        else
        {
          // just increment the minutes
          min++;
        }

        // yes, it says that there are 60 seconds here.  set to zero and increment the minutes
        sec = 0;
      }
    }

    protected char doHem(final double val, final boolean isLat)
    {
      char res;
      if (val > 0)
      {
        if (isLat)
          res = 'N';
        else
          res = 'E';
      }
      else
      {
        if (isLat)
          res = 'S';
        else
          res = 'W';
      }

      return res;
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // testing for this class
  //////////////////////////////////////////////////////////////////////////////////////////////////
  static public class FormatLocationTest extends junit.framework.TestCase
  {
    static public final String TEST_ALL_TEST_TYPE = "UNIT";

    public FormatLocationTest(final String val)
    {
      super(val);
    }

    public void testFormatting()
    {
    	// try to check we've successfully created our degrees symbol
    	assertNotNull("failed to create degree symbol", DEGREE_SYMBOL);
    	assertEquals("degree symbol wrong length", 1, DEGREE_SYMBOL.length());
    	
      final WorldLocation la = new WorldLocation(0d, 0d, 0d);
      String res1 = " 00" + DEGREE_SYMBOL + "00\'00.00\" 000" + DEGREE_SYMBOL + "00\'00.00\"";
      super.assertEquals("first test", res1, BriefFormatLocation.toString(la));
      la.setLat(-12.345);
      la.setLong(22.432);
      res1 = " 12" + DEGREE_SYMBOL + "20\'42.00\"S 022" + DEGREE_SYMBOL + "25\'55.20\"E";
      super.assertEquals("second test", res1, BriefFormatLocation.toString(la));
      la.setLat(12.34533);
      la.setLong(-22.43222);
      res1 = " 12" + DEGREE_SYMBOL + "20\'43.19\"N 022" + DEGREE_SYMBOL + "25\'55.99\"W";
      super.assertEquals("third test", res1, BriefFormatLocation.toString(la));
      la.setLat(82.345);
      la.setLong(172.432);
      res1 = " 82" + DEGREE_SYMBOL + "20\'42.00\"N 172" + DEGREE_SYMBOL + "25\'55.20\"E";
      super.assertEquals("fourth test", res1, BriefFormatLocation.toString(la));

    }
  }

  public static void main(final String[] args)
  {
    final WorldLocation la = new WorldLocation(0, 0, 0);
    System.out.println("first:" + BriefFormatLocation.toString(la));
    la.setLat(-12.345);
    la.setLong(22.432);
    System.out.println("second:" + BriefFormatLocation.toString(la));
    la.setLat(12.34533);
    la.setLong(-22.43222);
    System.out.println("third:" + BriefFormatLocation.toString(la));
    la.setLat(82.345);
    la.setLong(172.432);
    System.out.println("fourth:" + BriefFormatLocation.toString(la));

  }

	public String convertToString(final WorldLocation theLocation)
	{
		return toString(theLocation);
	}

	public String getExampleString()
	{
		return "short location";
	}

}
