package com.witbank.carwash.controller;

import com.witbank.carwash.service.BookingService;
import com.witbank.carwash.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("totalRevenue", bookingService.getTotalRevenue());
        model.addAttribute("totalBookings", (long) bookingService.getAllBookings().size());
        
        // Count specific services
        model.addAttribute("eliteCount", bookingService.getCountByService("Elite Detail"));
        model.addAttribute("platinumCount", bookingService.getCountByService("Platinum Ceramic"));
        model.addAttribute("expressCount", bookingService.getCountByService("Express Shine"));

        // New data for expanded features
        model.addAttribute("inventory", bookingService.getInventory());
        model.addAttribute("services", bookingService.getServices());
        model.addAttribute("customerStats", bookingService.getCustomerVisitCount());
        model.addAttribute("notifications", notificationService.getAllDispatchedLogs());

        return "admin";
    }

    @PostMapping("/inventory/add")
    public String addInventory(@RequestParam String itemName,
                               @RequestParam int quantity,
                               @RequestParam int threshold) {
        bookingService.addInventoryItem(itemName, quantity, threshold);
        return "redirect:/admin/dashboard#inventory-section";
    }

    @PostMapping("/inventory/update")
    public String updateInventory(@RequestParam String itemName,
                                  @RequestParam int quantity,
                                  @RequestParam int threshold) {
        bookingService.updateInventory(itemName, quantity, threshold);
        return "redirect:/admin/dashboard#inventory-section";
    }

    @PostMapping("/inventory/delete")
    public String deleteInventory(@RequestParam String itemName) {
        bookingService.deleteInventory(itemName);
        return "redirect:/admin/dashboard#inventory-section";
    }

    @PostMapping("/bookings/update-status")
    public String updateStatus(@RequestParam Long id, @RequestParam String status) {
        bookingService.updateBookingStatus(id, status);
        return "redirect:/admin/dashboard#bookings-section";
    }

    @PostMapping("/bookings/delete")
    public String deleteBooking(@RequestParam Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/admin/dashboard#bookings-section";
    }

    @PostMapping("/notifications/dispatch")
    public String dispatchManualNotification(@RequestParam String recipient,
                                             @RequestParam String type,
                                             @RequestParam String messageContent) {
        if ("SMS".equalsIgnoreCase(type)) {
            notificationService.dispatchSms(recipient, messageContent);
        } else {
            notificationService.dispatchEmail(recipient, "Admin Notice - Witbank Elite", messageContent);
        }
        return "redirect:/admin/dashboard#notifications-section";
    }

    @GetMapping("/export")
    public String exportReport(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("totalRevenue", bookingService.getTotalRevenue());
        model.addAttribute("totalBookings", (long) bookingService.getAllBookings().size());
        model.addAttribute("date", java.time.LocalDate.now());
        
        return "report_print";
    }
}
