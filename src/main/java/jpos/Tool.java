package jpos;

public class Tool {
    String toolCode;
    String toolType;
    String brand;
    double dailyCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;

    public Tool(String toolCode, String toolType, String brand, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public String getToolInfo() {
        return String.format("Tool code: %s\nTool type: %s\nBrand: %s\nDaily charge: $%.2f\n", toolCode, toolType, brand, dailyCharge);
    }

    // Getters and setters

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

    public String getToolCode() {
        return toolCode;
    }

    public double getDailyCharge() {
        return dailyCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }
}