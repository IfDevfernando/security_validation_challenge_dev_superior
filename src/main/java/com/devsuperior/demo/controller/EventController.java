package com.devsuperior.demo.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.service.EventService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService service;

    @RequestMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
        Page<EventDTO> list = service.findAll(pageable);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO eventDTO) {
        eventDTO = service.insert(eventDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(eventDTO);
    }
}
