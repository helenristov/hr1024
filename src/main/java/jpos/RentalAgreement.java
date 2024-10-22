package jpos;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class RentalAgreement {
    Tool tool;
    int rentalDays;
    Date checkoutDate;
    private Date dueDate;
    int discountPercent;
    private int chargeDays;
    private double preDiscountCharge;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement(Tool tool, int rentalDays, Date checkoutDate, int discountPercent) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.discountPercent = discountPercent;
        calculateCharges();
    }

    private void calculateCharges() {
        // Calculate due date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DAY_OF_YEAR, rentalDays);
        dueDate = calendar.getTime();

        // Calculate charge days and charges
        chargeDays = calculateChargeDays();
        preDiscountCharge = chargeDays * tool.getDailyCharge();
        discountAmount = preDiscountCharge * discountPercent / 100;
        finalCharge = preDiscountCharge - discountAmount;
    }

    private int calculateChargeDays() {
        // Implement logic to calculate chargeable days based on tool type and holidays
        LocalDate startDate = LocalDate.ofInstant(checkoutDate.toInstant(), ZoneId.systemDefault());
        LocalDate endDate = LocalDate.ofInstant(dueDate.toInstant(), ZoneId.systemDefault());
        int chargeableDays = 0;
        for (LocalDate date = startDate.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
            if ((tool.weekdayCharge && isWeekday(date)) || (tool.weekendCharge && isWeekend(date)) || (tool.holidayCharge && isHoliday(date))) {
                chargeableDays++;
            }
        }
        return chargeableDays; // Placeholder
    }

    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 6;
    }

    public static boolean isWeekday(LocalDate date) {
        return date.getDayOfWeek().getValue() < 6;
    }

//Independence Day (July 4th) is observed on the closest weekday if it falls on a weekend.
//Labor Day is defined as the first Monday of September.

    public static boolean isHoliday(LocalDate date) {
        // Check for Independence Day
        if (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4) {
            return true;
        }

        // Calculate Independence Day observed date
        LocalDate independenceDayObserved = LocalDate.of(date.getYear(), Month.JULY, 4);
        if (independenceDayObserved.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDayObserved = independenceDayObserved.minusDays(1); // Friday
        } else if (independenceDayObserved.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDayObserved = independenceDayObserved.plusDays(1); // Monday
        }

        // Check if the date is the observed Independence Day
        if (date.equals(independenceDayObserved)) {
            return true;
        }
        // Check for Labor Day (First Monday in September)
        LocalDate laborDay = getLaborDay(date.getYear());
        return date.equals(laborDay); // Labor Day check
    }

    private static LocalDate getLaborDay(int year) {
        LocalDate septemberFirst = LocalDate.of(year, Month.SEPTEMBER, 1);
        // Find the first Monday of September
        while (septemberFirst.getDayOfWeek() != DayOfWeek.MONDAY) {
            septemberFirst = septemberFirst.plusDays(1);
        }
        return septemberFirst; // This is Labor Day
    }
    

    public void printAgreement() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        System.out.println("Tool code: " + tool.getToolCode());
        System.out.println("Tool type: " + tool.getToolType());
        System.out.println("Tool brand: " + tool.getBrand());
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Check out date: " + dateFormat.format(checkoutDate));
        System.out.println("Due date: " + dateFormat.format(dueDate));
        System.out.println("Daily rental charge: $" + String.format("%.2f", tool.getDailyCharge()));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: $" + String.format("%.2f", preDiscountCharge));
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: $" + String.format("%.2f", discountAmount));
        System.out.println("Final charge: $" + String.format("%.2f", finalCharge));
    }

    // Getters and setters
}
