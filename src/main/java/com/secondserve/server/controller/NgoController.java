package com.secondserve.server.controller;

import com.secondserve.server.dto.NgoDto;
import com.secondserve.server.service.NgoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ngos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NgoController {

    @Autowired
    private NgoService ngoService;

    @GetMapping
    public ResponseEntity<List<NgoDto>> getAllNgos() {
        List<NgoDto> ngos = ngoService.getAllActiveNgos();
        return ResponseEntity.ok(ngos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgoDto> getNgoById(@PathVariable Long id) {
        try {
            NgoDto ngo = ngoService.getNgoById(id);
            return ResponseEntity.ok(ngo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgoDto> updateNgo(@PathVariable Long id, @Valid @RequestBody NgoDto ngoDto) {
        try {
            NgoDto updatedNgo = ngoService.updateNgo(id, ngoDto);
            return ResponseEntity.ok(updatedNgo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNgo(@PathVariable Long id) {
        try {
            ngoService.deleteNgo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}