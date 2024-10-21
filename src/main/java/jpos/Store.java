package jpos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Store {
    List<Tool> tools;

    public Store() {
        tools = new ArrayList<>();
        // Initialize tools
        tools.add(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        tools.add(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        tools.add(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        tools.add(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

    public RentalAgreement checkoutTool(String toolCode, int rentalDays, int discountPercent, Date checkoutDate) throws IllegalArgumentException {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = tools.stream().filter(t -> t.getToolCode().equals(toolCode)).findFirst().orElse(null);
        if (tool == null) {
            throw new IllegalArgumentException("Tool not found.");
        }

        return new RentalAgreement(tool, rentalDays, checkoutDate, discountPercent);
    }
}
