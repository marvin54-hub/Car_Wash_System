package com.witbank.carwash.service;

import com.witbank.carwash.model.Booking;
import com.witbank.carwash.model.InventoryItem;
import com.witbank.carwash.model.ServicePackage;
import com.witbank.carwash.repository.BookingRepository;
import com.witbank.carwash.repository.InventoryItemRepository;
import com.witbank.carwash.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private NotificationService notificationService;

    @PostConstruct
    public void init() {
        // Initialize Services if table is empty
        if (servicePackageRepository.count() == 0) {
            servicePackageRepository.save(new ServicePackage("express", "Express Shine",
                    "Quick exterior wash, wheel cleaning, and hand dry.", 150.0, "🚿"));
            servicePackageRepository.save(new ServicePackage("elite", "Elite Detail",
                    "Full interior & exterior, wax protection, and engine bay clean.", 450.0, "✨"));
            servicePackageRepository.save(new ServicePackage("platinum", "Platinum Ceramic",
                    "Ultra-deep clean plus ceramic coating for lasting shine.", 850.0, "💎"));
        }

        // Initialize Inventory if table is empty
        if (inventoryItemRepository.count() == 0) {
            inventoryItemRepository.save(new InventoryItem("Car Shampoo", 50, 10));
            inventoryItemRepository.save(new InventoryItem("Wax Polish", 20, 5));
            inventoryItemRepository.save(new InventoryItem("Microfiber Cloths", 100, 20));
            inventoryItemRepository.save(new InventoryItem("Tire Shine", 5, 8)); // Trigger low stock
        }
    }

    public void addBooking(String name, String cellphone, String email, String serviceName, String time, double price) {
        Booking booking = new Booking(
                null,
                name,
                cellphone,
                email,
                serviceName,
                LocalDateTime.parse(time),
                price);
        bookingRepository.save(booking);

        // Deduct inventory (simulated)
        inventoryItemRepository.findById("Car Shampoo").ifPresent(i -> {
            i.setQuantity(i.getQuantity() - 1);
            inventoryItemRepository.save(i);
        });

        // Fire internal dispatches automatically!
        String formattedTime = time.replace("T", " ");
        String smsMsg = "Witbank Elite Car Wash: Hello " + name + ", your " + serviceName + " booking on " + formattedTime + " is CONFIRMED. Thank you!";
        notificationService.dispatchSms(cellphone, smsMsg);

        String emailMsg = "Hello " + name + ",\n\nWe are pleased to confirm your reservation for the " + serviceName + " package.\nScheduled Time: " + formattedTime + "\nTotal Price: R" + price + "\n\nThank you for choosing Witbank Elite Car Wash!";
        notificationService.dispatchEmail(email, "Booking Confirmation - " + serviceName, emailMsg);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void updateBookingStatus(Long id, String status) {
        bookingRepository.findById(id).ifPresent(b -> {
            b.setStatus(status);
            bookingRepository.save(b);
            if ("Completed".equalsIgnoreCase(status)) {
                String smsMsg = "Witbank Elite Car Wash: Hi " + b.getCustomerName() + ", your vehicle is ready for collection! Service (" + b.getServiceType() + ") is COMPLETED.";
                notificationService.dispatchSms(b.getCellphone(), smsMsg);

                String emailMsg = "Hello " + b.getCustomerName() + ",\n\nFantastic news! Your vehicle has completed its " + b.getServiceType() + " treatment and is shining perfectly. It is now ready for collection at our Witbank depot.\n\nDrive safely!";
                notificationService.dispatchEmail(b.getEmail(), "Service Completed! - Witbank Elite", emailMsg);
            }
        });
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<InventoryItem> getInventory() {
        return inventoryItemRepository.findAll();
    }

    public void addInventoryItem(String name, int quantity, int threshold) {
        inventoryItemRepository.save(new InventoryItem(name, quantity, threshold));
    }

    public void updateInventory(String name, int quantity, int threshold) {
        inventoryItemRepository.findById(name).ifPresent(i -> {
            i.setQuantity(quantity);
            i.setThreshold(threshold);
            inventoryItemRepository.save(i);
        });
    }

    public void deleteInventory(String name) {
        inventoryItemRepository.deleteById(name);
    }

    public List<ServicePackage> getServices() {
        return servicePackageRepository.findAll();
    }

    public double getTotalRevenue() {
        return bookingRepository.findAll().stream().mapToDouble(Booking::getPrice).sum();
    }

    public long getCountByService(String serviceType) {
        return bookingRepository.countByServiceType(serviceType);
    }

    public Map<String, Long> getCustomerVisitCount() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(Booking::getCustomerName, Collectors.counting()));
    }

    public boolean isVipCustomer(String customerName) {
        return getCustomerVisitCount().getOrDefault(customerName, 0L) >= 3;
    }
}
