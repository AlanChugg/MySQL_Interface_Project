package SystClasses;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

/**A class specifically for the management of timestamps and all things time. This is an attempt to reuse code and to improve organization.*/
public class DateTimeMgmt
{

    /**Variation of the MonthCoversion program that takes timestamps and pulls out month information.
     * @param localTimestamp The timestamp.
     * @return the name of the month.*/
    public static String convertLocalTimestampToMonth(Timestamp localTimestamp)
    {
        Date tempDate = localTimestamp;
        String stringDate = String.valueOf(tempDate);
        String[] dateArray = stringDate.split(" |-");
        int numbMonth = Integer.parseInt(dateArray[1]);
        return MonthConversion.convertMonth(numbMonth);
    }

    /**Converts any timestamps created by the VM and converts them to UTC.
     * @param localTimestamp The VM created timestamp.
     * @return UTC timestamp.*/
    public static Timestamp convertLocalTimestampToUTCTimestamp(Timestamp localTimestamp)
    {
        try
        {
            LocalDateTime testZDT = localTimestamp.toLocalDateTime();

            ZonedDateTime zdt3 = testZDT.atZone(ZoneId.of("UTC"));

            Timestamp finnal = Timestamp.valueOf(zdt3.toLocalDateTime());

            return finnal;

        }catch(Exception e)
        {
            //caught
        }
        return null;

    }

/**Takes utc timestamps from the database and changes them to local time for user display.
 * @param tempDateTime Timestamp from the db.
 * @return local timestamp.*/
    public static LocalDate convertTimestampToLocalDate(Timestamp tempDateTime)
    {
        try
        {
            return tempDateTime.toLocalDateTime().toLocalDate();
        }
        catch(Exception e)
        {
            //caught
        }

        return null;
    }

    /**This recursive method takes any date and returns the date of the last Sunday (ie date for the first of the week).
     * @param comboDate A generic date.
     * @return the date of the past sunday.*/
    public static LocalDate firstDayOfWeek(LocalDate comboDate)
    {
        LocalDate theFirstOfTheWeek = comboDate;

        if(theFirstOfTheWeek.getDayOfWeek() != DayOfWeek.SUNDAY)
        {
            theFirstOfTheWeek = firstDayOfWeek(theFirstOfTheWeek.minusDays(1));
        }

        return theFirstOfTheWeek;

    }

    /**This recursive method takes any date and returns the date of the upcoming saturday (ie date for the last of the week).
     * @param comboDate A generic date.
     * @return the date for the upcoming saturday.*/
    public static LocalDate lastDayOfTheWeek(LocalDate comboDate)
    {
        LocalDate theLastOfTheWeek = comboDate;

        if(theLastOfTheWeek.getDayOfWeek() != DayOfWeek.SATURDAY)
        {
           theLastOfTheWeek = lastDayOfTheWeek(theLastOfTheWeek.plusDays(1));
        }

        return theLastOfTheWeek;

    }

    /**Due to the way the GUI is designed, it is necessary to convert a PM time into military time for timestamps.*/
    public static String convertPMToMilitaryTime(String tempDate)
    {
        try
        {
            String[] tempDateParse = tempDate.split(":");
            int hoursPM = Integer.parseInt(tempDateParse[0]);
            hoursPM = hoursPM+12;
            if (hoursPM >= 24)
            {
                hoursPM = hoursPM-12;
            }

            String hoursToString = String.valueOf(hoursPM);
            String hoursToReturn = hoursToString+":"+tempDateParse[1];
            return hoursToReturn;
        }
        catch(Exception e)
        {
            return "00:00";
        }

    }

    /**Due to the way the GUI is designed, it is necessary to convert military time to PM time.*/
    public static String convertMilitaryToPM(Timestamp tempStamp)
    {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            utcFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            java.util.Date startDate = utcFormat.parse(tempStamp.toString());
            String toParse = formatter.format(startDate.getTime());
            String[] stringAr = toParse.split(" |:");
            int hours = Integer.parseInt(stringAr[1]);
            if(hours <= 12)
            {
                return stringAr[1]+":"+stringAr[2];
            }
            else
                {
                    int hours2= hours-12;
                    String theHours = String.valueOf(hours2);
                    return theHours+":"+stringAr[2];
                }

        }catch(Exception e)
        {
            //caught
        }

        return null;

    }

    /**Checks to see if the entered time in military format
     * @param tempStamp The timestamp
     * return True or false accordingly.*/
    public static boolean timeIsInMilitary(Timestamp tempStamp)
    {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            utcFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            java.util.Date startDate = utcFormat.parse(tempStamp.toString());
            String toParse = formatter.format(startDate.getTime());
            String[] stringAr = toParse.split(" |:");
            int hours = Integer.parseInt(stringAr[1]);
            if (hours >=12)
            {
                return true;
            }
            else{return false;}
        }catch(Exception e)
        {
            //caught
        }
        return false;
    }




}
