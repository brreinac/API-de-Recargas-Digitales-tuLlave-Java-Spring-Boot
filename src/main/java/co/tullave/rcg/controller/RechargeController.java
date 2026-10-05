package co.tullave.rcg.controller;

import co.tullave.rcg.dto.PageResponse;
import co.tullave.rcg.dto.RechargeRequest;
import co.tullave.rcg.dto.RechargeResponse;
import co.tullave.rcg.service.RechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Recargas", description = "Operaciones para recargas digitales tuLlave")
public class RechargeController {

    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @PostMapping("/recharges")
    @Operation(summary = "Crear una recarga")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recarga creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = co.tullave.rcg.dto.ErrorResponse.class)))
    })
    public ResponseEntity<RechargeResponse> create(@Valid @RequestBody RechargeRequest request) {
        RechargeResponse response = rechargeService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/getRecharges")
    @Operation(summary = "Listar recargas", description = "Consulta recargas con paginación y filtro opcional por número de tarjeta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = co.tullave.rcg.dto.ErrorResponse.class)))
    })
    public ResponseEntity<PageResponse<RechargeResponse>> findAll(
            @Parameter(description = "Número de página basado en cero")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be greater than or equal to 0") int page,
            @Parameter(description = "Cantidad de elementos por página")
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "size must be greater than 0") @Max(value = 100, message = "size must not exceed 100") int size,
            @Parameter(description = "Filtro exacto por número de tarjeta")
            @RequestParam(required = false) @Pattern(regexp = "\\d{16}", message = "cardNumber must contain exactly 16 numeric digits") String cardNumber) {
        return ResponseEntity.ok(rechargeService.findAll(page, size, cardNumber));
    }

    @DeleteMapping("/recharges/{id}")
    @Operation(summary = "Eliminar una recarga")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Recarga eliminada"),
            @ApiResponse(responseCode = "404", description = "Recarga no encontrada", content = @Content(schema = @Schema(implementation = co.tullave.rcg.dto.ErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rechargeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
