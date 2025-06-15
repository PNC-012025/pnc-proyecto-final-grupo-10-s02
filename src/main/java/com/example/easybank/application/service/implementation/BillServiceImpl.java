package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.request.BillRequestDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.application.mapper.BillMapper;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.BillService;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.BillRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    @Override
    public void save(BillRequestDTO billRequestDTO) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Bill bill = BillMapper.toEntity(billRequestDTO);
        bill.setUser(user);

        billRepository.save(bill);
    }

    @Override
    public List<BillResponseDTO> getAllMyBills() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        return user.getBills()
                .stream()
                .map(BillMapper::toDTO)
                .toList();
    }
}
