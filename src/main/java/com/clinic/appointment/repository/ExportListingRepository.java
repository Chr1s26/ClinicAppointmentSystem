package com.clinic.appointment.repository;

import com.clinic.appointment.model.ExportListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportListingRepository extends JpaRepository<ExportListing,Long>, JpaSpecificationExecutor<ExportListing> {

}
