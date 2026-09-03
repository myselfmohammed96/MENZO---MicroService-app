package com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Repository;

import com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Entity.InventoryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryPreferenceRepository extends JpaRepository<InventoryPreference, UUID> {
}
