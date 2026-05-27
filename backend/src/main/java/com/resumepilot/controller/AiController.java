package com.resumepilot.controller;

import com.resumepilot.ai.OpenRouterService;
import com.resumepilot.dto.AiRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class AiController {
    private final OpenRouterService openRouterService;

    public AiController(OpenRouterService openRouterService) {
        this.openRouterService = openRouterService;
    }

    @PostMapping("/cold-email")
    public Map<String, String> generateColdEmail(@RequestBody AiRequest request) {
        String prompt = """
                Write a concise recruiter cold email.

                Candidate Name: %s
                Resume: %s
                Company: %s
                Role: %s
                Job Description: %s

                Rules:
                - Keep under 140 words
                - Do not use placeholders
                - Avoid generic corporate phrases
                - Mention one relevant skill/project naturally
                - Return only the email body
                """.formatted(request.getCandidateName(), request.getResumeText(), request.getCompany(), request.getRole(), request.getJobDescription());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/cover-letter")
    public Map<String, String> generateCoverLetter(@RequestBody AiRequest request) {
        String prompt = """
                Generate a realistic cover letter.

                Candidate Name: %s
                Resume: %s
                Company: %s
                Role: %s
                Job Description: %s

                Rules:
                - Keep under 300 words
                - Do not invent experience
                - Mention only technologies present in resume
                - Return only the cover letter
                """.formatted(request.getCandidateName(), request.getResumeText(), request.getCompany(), request.getRole(), request.getJobDescription());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/linkedin-summary")
    public Map<String, String> generateLinkedInSummary(@RequestBody AiRequest request) {
        String prompt = """
                Generate LinkedIn profile content.

                Candidate Name: %s
                Resume: %s
                Target Role: %s

                Output:
                1. Headline under 220 characters
                2. About section under 180 words

                Rules:
                - Professional and recruiter-friendly
                - Do not exaggerate
                """.formatted(request.getCandidateName(), request.getResumeText(), request.getRole());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/interview-questions")
    public Map<String, String> generateInterviewQuestions(@RequestBody AiRequest request) {
        String prompt = """
                Generate 10 interview questions based on this resume and role.

                Resume: %s
                Role: %s
                Job Description: %s

                Include technical, project-based, and HR questions.
                Return numbered list only.
                """.formatted(request.getResumeText(), request.getRole(), request.getJobDescription());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/extract-skills")
    public Map<String, String> extractSkills(@RequestBody AiRequest request) {
        String prompt = """
                Extract only technical skills from this resume.

                Resume:
                %s

                Return comma-separated skills only.
                """.formatted(request.getResumeText());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/project-summary")
    public Map<String, String> projectSummary(@RequestBody AiRequest request) {
        String prompt = """
                Generate a professional project summary from this resume.
                Keep under 120 words. Do not invent fake features.

                Resume:
                %s
                """.formatted(request.getResumeText());
        return Map.of("result", openRouterService.generate(prompt));
    }

    @PostMapping("/optimized-resume")
    public Map<String, String> optimizedResume(@RequestBody AiRequest request) {
        String prompt = """
                You are an ATS resume optimization expert.

                Create a NEW optimized resume from the old resume for the target job.

                Candidate Name: %s
                Old Resume:
                %s

                Target Company: %s
                Target Role: %s
                Job Description:
                %s

                Requirements:
                - Return a complete resume in clean plain text/markdown
                - Include: Header, Professional Summary, Skills, Projects, Experience/Internship if present, Education
                - Improve wording for ATS score
                - Add relevant keywords from job description ONLY if supported by resume
                - Do not invent fake companies, fake internships, fake years, or fake metrics
                - If metrics are not present, use impact-focused wording without fake numbers
                - Make project bullets stronger for backend, APIs, microservices, ML/AI if present
                - Keep it single-page style and recruiter-friendly
                - Return only the new optimized resume
                """.formatted(request.getCandidateName(), request.getResumeText(), request.getCompany(), request.getRole(), request.getJobDescription());
        return Map.of("result", openRouterService.generate(prompt));
    }
}
