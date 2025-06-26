package com.example.easybank.service;

import com.example.easybank.domain.dto.request.BillRequestDTO;
import com.example.easybank.domain.dto.response.BillResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BillService {
    public void save(BillRequestDTO billRequestDTO) throws Exception;
    public List<BillResponseDTO> getAllMyBills() throws Exception;
    public void delete(UUID id) throws Exception;
    public void payBill(UUID id) throws Exception;
}
