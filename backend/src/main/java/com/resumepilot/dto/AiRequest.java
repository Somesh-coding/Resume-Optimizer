package com.resumepilot.dto;

public class AiRequest {
    private String candidateName;
    private String resumeText;
    private String company;
    private String role;
    private String jobDescription;
    private String bullet;

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public String getBullet() { return bullet; }
    public void setBullet(String bullet) { this.bullet = bullet; }
}
