
import { useState } from "react";
import { Wand2, Upload, FileText, Sparkles, Target, Copy, RefreshCw } from "lucide-react";
import API from "./api/api";
import "./style.css";

const SAMPLE_RESUME = `Somesh Kumar Singh
B.Tech Information Technology
Skills: Java, Spring Boot, React, Kafka, PostgreSQL, JWT, REST APIs, Microservices, Docker
Projects:
AI Resume Automation Platform - Built with React, Spring Boot, FastAPI and OpenRouter.
Microservices Payment System - Implemented JWT authentication, wallet APIs, and transaction services.
Experience:
Software Engineering Internship simulation involving REST APIs, databases, testing and code quality.`;

const SAMPLE_JOB = `Software Engineering Intern role requiring Java, Spring Boot, REST APIs, microservices, Kafka, PostgreSQL, Docker, clean coding, and distributed systems understanding.`;

export default function App() {
  const [candidateName, setCandidateName] = useState("Somesh Kumar Singh");
  const [resumeText, setResumeText] = useState(SAMPLE_RESUME);
  const [jobDescription, setJobDescription] = useState(SAMPLE_JOB);
  const [company, setCompany] = useState("Google");
  const [role, setRole] = useState("Software Engineering Intern");

  const [matchResult, setMatchResult] = useState(null);
  const [aiResult, setAiResult] = useState("");
  const [resumeOutput, setResumeOutput] = useState("");
  const [activeTool, setActiveTool] = useState("");
  const [loadingMatch, setLoadingMatch] = useState(false);
  const [loadingAi, setLoadingAi] = useState(false);
  const [error, setError] = useState("");

  const payload = () => ({
    candidateName,
    resumeText,
    company,
    role,
    jobDescription,
    bullet: "Built backend using Spring Boot"
  });

  const analyzeMatch = async () => {
    try {
      setError("");
      setLoadingMatch(true);
      const res = await API.post("/match/analyze", {
        resume_text: resumeText,
        job_description: jobDescription
      });
      setMatchResult(res.data);
    } catch (err) {
      console.error(err);
      setError("Match analysis failed. Run Python ML service on port 8000 and Spring Boot on port 8080.");
    } finally {
      setLoadingMatch(false);
    }
  };

  const generateAI = async (type) => {
    try {
      setError("");
      setLoadingAi(true);
      setAiResult("");
      setResumeOutput("");
      setActiveTool(type);

      const res = await API.post(`/ai/${type}`, payload());
      setAiResult(res.data.result || "No result returned.");
    } catch (err) {
      console.error("AI frontend error:", err);
      console.error("Backend response:", err.response?.data);
      setError(err.response?.data?.result || err.response?.data?.error || "AI generation failed. Check backend and OpenRouter key.");
    } finally {
      setLoadingAi(false);
    }
  };

  const generateOptimizedResume = async () => {
    try {
      setError("");
      setLoadingAi(true);
      setAiResult("");
      setResumeOutput("");
      setActiveTool("optimized-resume");

      const res = await API.post("/ai/optimized-resume", payload());
      setResumeOutput(res.data.result || "No resume returned.");
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.result || "Optimized resume generation failed.");
    } finally {
      setLoadingAi(false);
    }
  };

  const uploadResume = async (e) => {
    try {
      setError("");
      const file = e.target.files[0];
      if (!file) return;
      const formData = new FormData();
      formData.append("file", file);
      const res = await API.post("/resume/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setResumeText(res.data);
    } catch (err) {
      console.error(err);
      setError("Resume upload failed. Make sure PDFBox backend endpoint is running.");
    }
  };

  const copyOutput = async () => {
    await navigator.clipboard.writeText(resumeOutput || aiResult || "");
  };

  return (
    <main className="shell">
      <section className="hero">
        <div className="stamp">RESUMEPILOT AI // ATS OPS</div>
        <h1>One screen. Fewer questions. Better applications.</h1>
        <p>
          Paste or upload your resume once, add one target job, then generate match analysis,
          recruiter emails, LinkedIn copy, interview questions, and a new ATS-focused resume.
        </p>
      </section>

      {error && <div className="error">{error}</div>}

      <section className="workbench">
        <aside className="panel input-panel">
          <div className="panel-title">
            <FileText size={20} />
            <span>Input Dossier</span>
          </div>

          <div className="mini-grid">
            <label>
              Name
              <input value={candidateName} onChange={(e) => setCandidateName(e.target.value)} />
            </label>
            <label>
              Company
              <input value={company} onChange={(e) => setCompany(e.target.value)} />
            </label>
          </div>

          <label>
            Role
            <input value={role} onChange={(e) => setRole(e.target.value)} />
          </label>

          <label className="upload-box">
            <Upload size={18} />
            Upload Resume PDF
            <input className="hidden-file" type="file" accept=".pdf" onChange={uploadResume} />
          </label>

          <label>
            Resume Text
            <textarea value={resumeText} onChange={(e) => setResumeText(e.target.value)} />
          </label>

          <label>
            Job Description
            <textarea value={jobDescription} onChange={(e) => setJobDescription(e.target.value)} />
          </label>
        </aside>

        <section className="panel control-panel">
          <div className="panel-title">
            <Wand2 size={20} />
            <span>Automation Deck</span>
          </div>

          <button className="mega-btn" onClick={analyzeMatch}>
            <Target size={18} />
            {loadingMatch ? "Analyzing Resume..." : "Analyze Resume Match"}
          </button>

          <button className="resume-btn" onClick={generateOptimizedResume}>
            <Sparkles size={18} />
            Generate New ATS Resume
          </button>

          <div className="tool-grid">
            <button onClick={() => generateAI("cold-email")}>Cold Email</button>
            <button onClick={() => generateAI("cover-letter")}>Cover Letter</button>
            <button onClick={() => generateAI("linkedin-summary")}>LinkedIn Summary</button>
            <button onClick={() => generateAI("interview-questions")}>Interview Questions</button>
            <button onClick={() => generateAI("extract-skills")}>Extract Skills</button>
            <button onClick={() => generateAI("project-summary")}>Project Summary</button>
          </div>

          {matchResult && (
            <div className="scan result">
              <div className="result-kicker">Python ML Output</div>
              <div className="score">{matchResult.matchScore}%</div>
              <div className="cols">
                <div>
                  <h4>Matched</h4>
                  <p>{matchResult.matchedSkills?.join(", ") || "None"}</p>
                </div>
                <div>
                  <h4>Missing</h4>
                  <p>{matchResult.missingSkills?.join(", ") || "None"}</p>
                </div>
              </div>
            </div>
          )}

          {(loadingAi || aiResult || resumeOutput) && (
            <div className="result output">
              <div className="output-head">
                <div>
                  <div className="result-kicker">OpenRouter AI Output</div>
                  <h3>{activeTool}</h3>
                </div>
                <button className="copy" onClick={copyOutput}><Copy size={16}/> Copy</button>
              </div>
              {loadingAi ? (
                <div className="loading"><RefreshCw size={18}/> Generating...</div>
              ) : (
                <pre>{resumeOutput || aiResult}</pre>
              )}
            </div>
          )}
        </section>
      </section>
    </main>
  );
}
