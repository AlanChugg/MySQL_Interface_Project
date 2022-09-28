package SystClasses;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

/**Method used specifically for associating month name and abbreviation with a specific id (ie the month number of the year).*/
public class MonthConversion
{
    public static ArrayList<MonthConversion> monthsOfYear = new ArrayList<>()
    {{
        add(new MonthConversion("January", 1, "Jan"));
        add(new MonthConversion("February",2, "Feb"));
        add(new MonthConversion("March",3, "Mar"));
        add(new MonthConversion("April",4, "Apr"));
        add(new MonthConversion("May",5, "May"));
        add(new MonthConversion("June",6, "Jun"));
        add(new MonthConversion("July",7, "Jul"));
        add(new MonthConversion("August",8, "Aug"));
        add(new MonthConversion("September",9, "Sept"));
        add(new MonthConversion("October",10,"Oct"));
        add(new MonthConversion("November",11, "Nov"));
        add(new MonthConversion("December",12, "Dec"));
    }};




    private String month;
    private int index;
    private String abbrev;

/**Constructor for the month objects.
 * @param month The month name.
 * @param index The integer associated with the month.
 * @param abbrev The abbreviation of the month name.*/
    private MonthConversion(String month, int index, String abbrev)
    {
        this.month = month;
        this.index = index;
        this.abbrev= abbrev;
    }

    /**Method that takes a month name and returns the corresponding index.
     * @param month The month name.
     * @return The index.*/
    public static int convertMonth(String month)
    {
        for (int i=0; i<monthsOfYear.size();i++)
        {
            if (monthsOfYear.get(i).getMonth().equals(month))
            {
                return monthsOfYear.get(i).getIndex();
            }
        }

        return -1;
    }

/**Takes a timestamp and then identifies the month in which that timestamp was created.
 * @param timeTemp  The timestamp.
 * @return the name of the month.*/
    public static String monthFromTimestamp(Timestamp timeTemp)
    {
        try
        {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            utcFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            java.util.Date startDate = utcFormat.parse(timeTemp.toString());
            String startDateString = startDate.toString();
            String[] startDateArray = startDateString.split(" ");
            return startDateArray[1];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

/**Takes a month index and returns the name of the month.
 * @param index The index of the month.
 * @return the name of the month.*/
    public static String convertMonth(int index)
    {
        for (int i=0; i<monthsOfYear.size();i++)
        {
            if (monthsOfYear.get(i).getIndex()==index)
            {
                return monthsOfYear.get(i).getMonth();
            }
        }

        return null;
    }

/**Getter for the index.
 * @return the index*/
    public int getIndex()
    {
        return index;
    }
    /**Getter for the month name.
     * @return the month name*/
    public String getMonth()
    {
        return month;
    }
    /**Getter for the abbreviations.
     * @return the abbreviation*/
    public String getAbbrev()
    {
        return abbrev;
    }

}
