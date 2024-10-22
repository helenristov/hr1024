<h1 align="center">Design an application for rental tools</h1>
<h3 align="center">Let's design a java application!</h3>

**We'll cover the following:**

* [Functional Requirements](#functional-requirements)
* [Class Diagram](#class-diagram)
* [Full Scale Architecture](#full-scale-architecture)
* [Code](#code)
* [Testing](#junit-test-cases)

In this exercise we will design and demo some code for a java application which can be used to check-out tools

### Functional Requirements

We will focus on the following set of functional requirements provided. The demonstration included below is to code and test a simple tool rental application. 

1. The application is a point-of-sale tool for a store, like Home Depot, that rents big tools.
2. Customers rent a tool for a specified number of days.
3. When a customer checks out a tool, a Rental Agreement is produced.
4. The store charges a daily rental fee, whose amount is different for each tool type.
5. Some tools are free of charge on weekends or holidays.
6. Clerks may give customers a discount that is applied to the total daily charges to reduce the final charge.

Each tool instance has the following attributes:
Tool Code - Unique identifier for a tool instance
Tool Type - The type of tool. The type also specifies the daily rental charge, and the days for which the
daily rental charge applies.
Brand - The brand of the ladder, chain saw or jackhammer.

Additional Requirements
Holidays
There are only two (2) holidays in the calendar:
1. Independence Day, July 4th - If falls on weekend, it is observed on the closest weekday (if Sat,then Friday before, if Sunday, then Monday after)
2. Labor Day - First Monday in September

Checkout
Checkout requires the following information to be provided:
1. Tool code - See tool table above
2. Rental day count - The number of days for which the customer wants to rent the tool. (e.g. 4 days)
3. Discount percent - As a whole number, 0-100 (e.g. 20 = 20%)
4. Check out date checkout should throw an exception with an instructive, user-friendly message if
    a. Rental day count is not 1 or greater
    b. Discount percent is not in the range 0-100
Checkout generates a Rental Agreement instance with the following values.
1. Tool code - Specified at checkout
2. Tool type - From tool info
3. Tool brand - From tool info
4. Rental days - Specified at checkout
5. Check out date - Specified at checkout
6. Due date - Calculated from checkout date and rental days.
7. Daily rental charge - Amount per day, specified by the tool type.
8. Charge days - Count of chargeable days, from day after checkout through and including due date, excluding “no charge” days as specified by the tool type.
9. Pre-discount charge - Calculated as charge days X daily charge. Resulting total rounded half up to cents.
10. Discount percent - Specified at checkout.
11. Discount amount - calculated from discount % and pre-discount charge. Resulting amount rounded half up to cents.
12. Final charge - Calculated as pre-discount charge - discount amount.


Rental Agreement should include a method that can print the above values as text to the console like this:
Tool code: LADW
Tool type: Ladder
...
Final charge: $9.99
with formatting as follows:
● Date mm/dd/yy
● Currency $9,999.99
● Percent 99%


### Class Diagram

Here are the main classes of the system:

* **Customer:**  Customers will be able to rent and checkout a tool. 
* **Tool:**  Tools are available for checkout and rental each with their own set of conditions on when rental fees are charged on holidays and weekdays.
* **Rental Agreement:** Rental Agreements are generated for each tool.
* **Store:** POS main application where a customer can rent tools.
* **Clerk** Clerks can add discounts to reduce the final price.

<p align="center">
    <img src="/media-files/Screenshot 2024-10-09 215630.png" alt="Class Diagram">
    <br />
    UML Class Diagram for Java Application
</p>


### Full Scale Architecture

The code below will feature the demo application to meet the given functional requirements, but a full scale architecture is provided below which can expand on the demo and support a large scale java application. 

1. Presentation Layer
•	Components:	Web Interface: HTML, CSS, JavaScript (React or Angular)
                API Gateway: Handles requests from the web interface
2. Application Layer
•	Components: Controller: Manages incoming requests and routes them to appropriate services
                Services: Business logic for tool rental operations            
                ToolService: Manages tool inventory and availability
                RentalService: Handles rental transactions and agreements
                PricingService: Calculates rental fees, including discounts and free days
                DTOs (Data Transfer Objects): Transfer data between layers
3. Persistence Layer
•	Components: Repositories: Interface with the database (e.g., JPA/Hibernate for ORM)
                Entities: Represent database tables (e.g., Tool, Customer, Rental, RentalAgreement)
4. Database Layer
•	Components: Database: Relational Database (e.g., MySQL, PostgreSQL)
                Cache: In-memory data store for frequently accessed data (e.g., Redis)
5. Integration Layer
•	Components: External Services: Payment gateways, holiday APIs for determining free days
                Messaging: Message brokers for asynchronous communication (e.g., RabbitMQ, Kafka)
6. Security Layer
•	Components: Authentication: User login and registration (e.g., Spring Security, OAuth2)
                Authorization: Role-based access control
7. Infrastructure Layer
•	Components: Deployment: Containerization (e.g., Docker), Orchestration (e.g., Kubernetes)
                Monitoring: Application performance monitoring (e.g., Prometheus, Grafana)
                Logging: Centralized logging (e.g., ELK Stack - Elasticsearch, Logstash, Kibana)


<p align="center">
    <img src="/media-files/Screenshot 2024-10-10 001100.png" alt="Full Architecture Diagram">
    <br />
    Extended Architecture
</p>

### Code

Here is the high-level definition for the classes described above.


```java
public class Tool {
    private String toolCode;
    private String toolType;
    private String brand;
    private double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

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
}

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RentalAgreement {
    private Tool tool;
    private int rentalDays;
    private Date checkoutDate;
    private Date dueDate;
    private int discountPercent;
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
        return rentalDays; // Placeholder
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Store {
    private List<Tool> tools;

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


```
### Testing
In order to provide a robust application, a junit testing harness can be developed and integrated into the build. As the build becomes larger, this will play an important role in regression testing development changes. To start, a few cases will be integrated to test the logic and calculations around tool rentals and the fields in the rental agreement.
