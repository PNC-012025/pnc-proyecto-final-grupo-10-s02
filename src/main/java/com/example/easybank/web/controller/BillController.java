package com.example.easybank.web.controller;

import com.example.easybank.application.dto.request.BillRequestDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.application.service.BillService;
import com.example.easybank.application.service.implementation.BillServiceImpl;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.util.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.easybank.util.Constant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API + BILL)
public class BillController {
    private final BillServiceImpl billService;

    @PostMapping(CREATE)
    public ResponseEntity<GenericResponse> createBill(@RequestBody @Valid BillRequestDTO bill) throws Exception {
        billService.save(bill);
        return GenericResponse
                .builder()
                .status(HttpStatus.CREATED)
                .message("Successfully created bill")
                .build()
                .buildResponse();
    }

    @GetMapping(FIND_OWN)
    public ResponseEntity<GenericResponse> findOwnBill() throws Exception {
        List<BillResponseDTO> bills = billService.getAllMyBills();
        return GenericResponse
                .builder()
                .status(HttpStatus.OK)
                .message("Bills found")
                .data(bills)
                .build().buildResponse();
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<GenericResponse> deleteBill(@PathVariable("id") BillRequestDTO bill) throws Exception {
        return GenericResponse.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("Successfully deleted bill")
                .build().buildResponse();
    }
}
