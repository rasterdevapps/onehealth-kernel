package com.onehealth.emr.controller;

import com.onehealth.erp.common.dto.EncounterDTO;
import com.onehealth.emr.service.EncounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/encounters")
public class EncounterController {

    private final EncounterService encounterService;

    public EncounterController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @PostMapping
    public ResponseEntity<EncounterDTO> create(@RequestBody EncounterDTO dto) {
        EncounterDTO created = encounterService.createEncounter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<EncounterDTO>> listByTenant() {
        return ResponseEntity.ok(encounterService.getEncountersByTenant());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EncounterDTO> updateStatus(@PathVariable UUID id,
                                                     @RequestParam String status) {
        return ResponseEntity.ok(encounterService.updateStatus(id, status));
    }
}
