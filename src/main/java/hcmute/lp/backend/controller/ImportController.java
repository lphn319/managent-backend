package hcmute.lp.backend.controller;

import hcmute.lp.backend.model.common.ApiResponse;
import hcmute.lp.backend.model.dto.import_.ImportDto;
import hcmute.lp.backend.model.dto.import_.ImportRequest;
import hcmute.lp.backend.service.ImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/imports")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Import API")
public class ImportController {
    @Autowired
    private ImportService importService;

}