# ResumePilot AI v2

## Run order

### 1. Python ML Service
```bash
cd ml-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```
Verify: http://127.0.0.1:8000/docs

### 2. Spring Boot Backend
Edit `backend/src/main/resources/application.properties` and paste your OpenRouter key:
```properties
openrouter.api.key=YOUR_KEY
```

Then run:
```bash
cd backend
mvn clean
mvn spring-boot:run
```
Verify: http://localhost:8080/

### 3. React Frontend
```bash
cd frontend
npm install
npm run dev
```
Open: http://localhost:5173

## New feature
Click **Generate New ATS Resume** to create an optimized resume from old resume + target job description.
