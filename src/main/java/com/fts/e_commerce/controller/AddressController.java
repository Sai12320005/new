package com.fts.e_commerce.controller;
import com.fts.e_commerce.entity.AddressEntity;
import com.fts.e_commerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    // 1. Add a new address for a user
    @PostMapping("/{userId}")
    public ResponseEntity<AddressEntity> addAddress(@PathVariable Long userId, @RequestBody AddressEntity address) {
        AddressEntity savedAddress = addressService.saveAddress(userId, address);
        if (savedAddress == null) {
            return ResponseEntity.notFound().build();  // User not found
        }
        return ResponseEntity.ok(savedAddress);
    }

    // 2. Get all addresses for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressEntity>> getAddresses(@PathVariable Long userId) {
        List<AddressEntity> addresses = addressService.getAddressesByUserId(userId);
        if (addresses == null) {
            return ResponseEntity.notFound().build(); // User not found
        }
        return ResponseEntity.ok(addresses);
    }

    // 3. Delete a specific address
    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        boolean isDeleted = addressService.deleteAddress(addressId);
        if (isDeleted) {
            return ResponseEntity.ok("Address deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Address not found");
        }
    }
}
