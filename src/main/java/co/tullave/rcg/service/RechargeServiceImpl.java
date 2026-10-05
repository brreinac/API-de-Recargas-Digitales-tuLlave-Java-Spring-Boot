package co.tullave.rcg.service;

import co.tullave.rcg.dto.PageResponse;
import co.tullave.rcg.dto.RechargeRequest;
import co.tullave.rcg.dto.RechargeResponse;
import co.tullave.rcg.entity.Recharge;
import co.tullave.rcg.exception.RechargeNotFoundException;
import co.tullave.rcg.mapper.RechargeMapper;
import co.tullave.rcg.repository.RechargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class RechargeServiceImpl implements RechargeService {

    private static final Logger log = LoggerFactory.getLogger(RechargeServiceImpl.class);

    private final RechargeRepository rechargeRepository;
    private final RechargeMapper rechargeMapper;

    public RechargeServiceImpl(RechargeRepository rechargeRepository, RechargeMapper rechargeMapper) {
        this.rechargeRepository = rechargeRepository;
        this.rechargeMapper = rechargeMapper;
    }

    @Override
    @Transactional
    public RechargeResponse create(RechargeRequest request) {
        Recharge recharge = rechargeMapper.toEntity(request);
        Recharge saved = rechargeRepository.save(recharge);
        log.info("Recharge created successfully with id {}", saved.getId());
        return rechargeMapper.toResponse(saved);
    }

    @Override
    public PageResponse<RechargeResponse> findAll(int page, int size, String cardNumber) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Recharge> recharges = StringUtils.hasText(cardNumber)
                ? rechargeRepository.findByCardNumber(cardNumber, pageable)
                : rechargeRepository.findAll(pageable);

        log.info("Recharge query completed: page={}, size={}, cardNumberFilterApplied={}",
                page, size, StringUtils.hasText(cardNumber));

        Page<RechargeResponse> responsePage = recharges.map(rechargeMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!rechargeRepository.existsById(id)) {
            throw new RechargeNotFoundException(id);
        }

        rechargeRepository.deleteById(id);
        log.info("Recharge deleted successfully with id {}", id);
    }
}
