package co.tullave.rcg.service;

import co.tullave.rcg.dto.RechargeRequest;
import co.tullave.rcg.dto.RechargeResponse;
import co.tullave.rcg.entity.PaymentMethod;
import co.tullave.rcg.entity.Recharge;
import co.tullave.rcg.exception.RechargeNotFoundException;
import co.tullave.rcg.mapper.RechargeMapper;
import co.tullave.rcg.repository.RechargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RechargeServiceImplTest {

    @Mock
    private RechargeRepository rechargeRepository;

    @Mock
    private RechargeMapper rechargeMapper;

    @InjectMocks
    private RechargeServiceImpl rechargeService;

    private Recharge recharge;
    private RechargeResponse response;

    @BeforeEach
    void setUp() {
        recharge = new Recharge();
        recharge.setCardNumber("1010000012345678");
        recharge.setAmount(new BigDecimal("50000"));
        recharge.setPaymentMethod(PaymentMethod.NEQUI);

        response = new RechargeResponse(
                1L,
                recharge.getCardNumber(),
                recharge.getAmount(),
                recharge.getPaymentMethod(),
                LocalDateTime.now());
    }

    @Test
    void shouldCreateRechargeSuccessfully() {
        RechargeRequest request = new RechargeRequest(
                "1010000012345678",
                new BigDecimal("50000"),
                PaymentMethod.NEQUI);

        when(rechargeMapper.toEntity(request)).thenReturn(recharge);
        when(rechargeRepository.save(recharge)).thenReturn(recharge);
        when(rechargeMapper.toResponse(recharge)).thenReturn(response);

        RechargeResponse result = rechargeService.create(request);

        assertThat(result).isEqualTo(response);
        verify(rechargeRepository).save(recharge);
    }

    @Test
    void shouldReturnPagedRechargesWithoutFilter() {
        Page<Recharge> page = new PageImpl<>(List.of(recharge), PageRequest.of(0, 10), 1);
        when(rechargeRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(rechargeMapper.toResponse(recharge)).thenReturn(response);

        var result = rechargeService.findAll(0, 10, null);

        assertThat(result.content()).containsExactly(response);
        assertThat(result.totalElements()).isEqualTo(1);
        verify(rechargeRepository).findAll(any(PageRequest.class));
        verify(rechargeRepository, never()).findByCardNumber(any(), any());
    }

    @Test
    void shouldReturnPagedRechargesFilteredByCardNumber() {
        Page<Recharge> page = new PageImpl<>(List.of(recharge), PageRequest.of(0, 10), 1);
        when(rechargeRepository.findByCardNumber("1010000012345678", PageRequest.of(0, 10,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"))))
                .thenReturn(page);
        when(rechargeMapper.toResponse(recharge)).thenReturn(response);

        var result = rechargeService.findAll(0, 10, "1010000012345678");

        assertThat(result.content()).containsExactly(response);
        verify(rechargeRepository).findByCardNumber(any(String.class), any(PageRequest.class));
    }

    @Test
    void shouldDeleteExistingRecharge() {
        when(rechargeRepository.existsById(1L)).thenReturn(true);

        rechargeService.deleteById(1L);

        verify(rechargeRepository).deleteById(1L);
    }

    @Test
    void shouldRejectDeletionWhenRechargeDoesNotExist() {
        when(rechargeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> rechargeService.deleteById(99L))
                .isInstanceOf(RechargeNotFoundException.class)
                .hasMessageContaining("99");

        verify(rechargeRepository, never()).deleteById(99L);
    }
}
