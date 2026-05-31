package com.vietnam.pji.controller.medical;

import com.vietnam.pji.dto.request.SensitivityResultRequestDTO;
import com.vietnam.pji.dto.response.PaginationResultDTO;
import com.vietnam.pji.dto.response.ResponseData;
import com.vietnam.pji.model.medical.SensitivityResult;
import com.vietnam.pji.services.medical.SensitivityResultService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}")
@Validated
@Tag(name = "Sensitivity Results", description = "Antibiotic sensitivity (susceptibility) results tied to a culture")
@RequiredArgsConstructor
public class SensitivityResultController {

    private final SensitivityResultService sensitivityResultService;

    @Operation(summary = "Create sensitivity result")
    @PostMapping("/sensitivity-results")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<SensitivityResult> createSensitivityResult(
            @Valid @RequestBody SensitivityResultRequestDTO request) {
        return new ResponseData<>(HttpStatus.CREATED.value(), "Sensitivity result created successfully",
                sensitivityResultService.create(request));
    }

    @Operation(summary = "Update sensitivity result")
    @PutMapping("/sensitivity-results/{id}")
    public ResponseData<SensitivityResult> updateSensitivityResult(
            @PathVariable Long id, @Valid @RequestBody SensitivityResultRequestDTO request) {
        return new ResponseData<>(HttpStatus.OK.value(), "Sensitivity result updated successfully",
                sensitivityResultService.update(id, request));
    }

    @Operation(summary = "Get sensitivity result by id")
    @GetMapping("/sensitivity-results/{id}")
    public ResponseData<SensitivityResult> getSensitivityResult(@PathVariable Long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Fetch sensitivity result successfully",
                sensitivityResultService.getById(id));
    }

    @Operation(summary = "Delete sensitivity result")
    @DeleteMapping("/sensitivity-results/{id}")
    public ResponseData<Void> deleteSensitivityResult(@PathVariable Long id) {
        sensitivityResultService.delete(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Sensitivity result deleted successfully");
    }

    @Operation(summary = "List sensitivity results by culture", description = "Paginated sensitivity entries for the given culture result")
    @GetMapping("/culture-results/{cultureId}/sensitivity-results")
    public ResponseData<PaginationResultDTO> getSensitivityResultsByCulture(
            @PathVariable Long cultureId, Pageable pageable) {
        return new ResponseData<>(HttpStatus.OK.value(), "Fetch sensitivity results successfully",
                sensitivityResultService.getByCulture(cultureId, pageable));
    }
}
