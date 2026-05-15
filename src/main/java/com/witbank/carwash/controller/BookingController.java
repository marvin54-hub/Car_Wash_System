package com.witbank.carwash.controller;

import com.witbank.carwash.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("services", bookingService.getServices());
        return "index";
    }

    @PostMapping("/book")
    public String book(@RequestParam String name, 
                       @RequestParam(required = false, defaultValue = "") String cellphone,
                       @RequestParam(required = false, defaultValue = "") String email,
                       @RequestParam String service, 
                       @RequestParam String time, 
                       Model model) {
        
        // Use dynamic price from service list
        double price = bookingService.getServices().stream()
            .filter(s -> s.getName().equals(service))
            .findFirst()
            .map(s -> s.getPrice())
            .orElse(0.0);

        bookingService.addBooking(name, cellphone, email, service, time, price);

        model.addAttribute("message", "Thank you, " + name + "! Your " + service + " booking for " + time + " has been received.");
        model.addAttribute("bookedCellphone", cellphone);
        model.addAttribute("bookedEmail", email);
        model.addAttribute("bookedName", name);
        model.addAttribute("bookedService", service);
        model.addAttribute("bookedTime", time);
        model.addAttribute("services", bookingService.getServices()); // Re-add for re-render
        return "index";
    }
}
