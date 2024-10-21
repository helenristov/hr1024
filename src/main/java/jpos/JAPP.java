/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jpos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author helen
 */

public class JAPP {
    
    public static void main(String[] args) throws ParseException {
        String str_date = "10-October-24";
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = formatter.parse(str_date);
        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
        RentalAgreement ra = new RentalAgreement(tool, 30, date, 0);
        ra.printAgreement();
        
        System.out.println("Hello World!");
    }
}
