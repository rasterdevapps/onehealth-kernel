package com.onehealth.lis.controller;

import com.onehealth.erp.common.dto.LabOrderDTO;
import com.onehealth.lis.service.LabOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lab-orders")
public class LabOrderController {

    private final LabOrderService labOrderService;

    public LabOrderController(LabOrderService labOrderService) {
        this.labOrderService = labOrderService;
    }

    @PostMapping
    public ResponseEntity<LabOrderDTO> create(@RequestBody LabOrderDTO dto) {
        LabOrderDTO created = labOrderService.createLabOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/execute")
    public ResponseEntity<LabOrderDTO> execute(@PathVariable UUID id) {
        return ResponseEntity.ok(labOrderService.executeLabTest(id));
    }

    @GetMapping
    public ResponseEntity<List<LabOrderDTO>> listByTenant() {
        return ResponseEntity.ok(labOrderService.getOrdersByTenant());
    }
}
