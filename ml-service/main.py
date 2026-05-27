from fastapi import FastAPI
from pydantic import BaseModel
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = FastAPI(title="ResumePilot ML Service")

class MatchRequest(BaseModel):
    resume_text: str
    job_description: str

SKILLS = [
    "java", "spring boot", "react", "kafka", "docker", "kubernetes", "aws",
    "redis", "mongodb", "postgresql", "microservices", "jwt", "rest api",
    "python", "fastapi", "sql", "git", "github", "supabase"
]

@app.get("/")
def home():
    return {"status": "ML Service Running"}

@app.post("/match")
def match_resume(req: MatchRequest):
    texts = [req.resume_text or "", req.job_description or ""]
    vectorizer = TfidfVectorizer()
    vectors = vectorizer.fit_transform(texts)
    score = cosine_similarity(vectors[0], vectors[1])[0][0]
    match_score = round(score * 100, 2)

    resume_lower = req.resume_text.lower()
    job_lower = req.job_description.lower()
    matched, missing = [], []

    for skill in SKILLS:
        if skill in job_lower:
            if skill in resume_lower:
                matched.append(skill)
            else:
                missing.append(skill)

    return {
        "matchScore": match_score,
        "matchedSkills": matched,
        "missingSkills": missing
    }
