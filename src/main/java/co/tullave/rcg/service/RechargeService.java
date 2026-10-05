package co.tullave.rcg.service;

import co.tullave.rcg.dto.PageResponse;
import co.tullave.rcg.dto.RechargeRequest;
import co.tullave.rcg.dto.RechargeResponse;

public interface RechargeService {

    RechargeResponse create(RechargeRequest request);

    PageResponse<RechargeResponse> findAll(int page, int size, String cardNumber);

    void deleteById(Long id);
}
