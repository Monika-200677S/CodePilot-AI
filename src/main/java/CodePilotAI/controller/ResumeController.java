package CodePilotAI.controller;

import CodePilotAI.dto.ResumeAnalysisResponse;
import CodePilotAI.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping(value = "/analyze", consumes = "multipart/form-data")
    public ResumeAnalysisResponse analyze(@RequestParam("file") MultipartFile file) throws IOException {
        return resumeService.analyze(file);
    }
}
