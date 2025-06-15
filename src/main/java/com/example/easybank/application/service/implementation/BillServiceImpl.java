package com.example.easybank.application.service.implementation;

import com.example.easybank.application.dto.request.BillRequestDTO;
import com.example.easybank.application.dto.response.BillResponseDTO;
import com.example.easybank.application.mapper.BillMapper;
import com.example.easybank.application.mapper.UserMapper;
import com.example.easybank.application.service.BillService;
import com.example.easybank.domain.entity.Account;
import com.example.easybank.domain.entity.Bill;
import com.example.easybank.domain.entity.UserData;
import com.example.easybank.domain.exception.AccessDeniedException;
import com.example.easybank.domain.exception.BillAlreadyPaidException;
import com.example.easybank.domain.exception.ModelNotFoundException;
import com.example.easybank.infrastructure.repository.AccountRepository;
import com.example.easybank.infrastructure.repository.BillRepository;
import com.example.easybank.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public void save(BillRequestDTO billRequestDTO) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("User not found"));

        Bill bill = BillMapper.toEntity(billRequestDTO);
        bill.setUser(user);
        bill.setState("PENDING");

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

    @Override
    public void delete(UUID id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Bill not found"));

        // TODO: CREAR EXCEPCION PARA ESTO
        if (!bill.getUser().getUsername().equals(username)) {
            throw new ModelNotFoundException("User not logged in");
        }

        billRepository.delete(bill);
    }

    @Override
    public void payBill(UUID id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Bill not found"));

        if (!bill.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("User is not the owner of this bill");
        }

        if (bill.getState().equals("PAID")) {
            throw new BillAlreadyPaidException("Bill is already paid");
        }

        Account account = bill.getUser().getAccounts().getFirst();
        account.setBalance(account.getBalance().subtract(bill.getAmount()));
        bill.setState("PAID");

        billRepository.save(bill);
        accountRepository.save(account);
    }
}
