# ResQ AI FastAPI backend starter

## Run in PowerShell from the backend folder
```powershell
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
Copy-Item .env.example .env
python -m uvicorn main:app --reload
```
Docs: http://127.0.0.1:8000/docs

## API
- GET /, GET /health
- POST /api/v1/auth/register, POST /api/v1/auth/login
- POST /api/v1/sos/, GET /api/v1/sos/, GET /api/v1/sos/{sos_id}, PATCH /api/v1/sos/{sos_id}/stage
- POST /api/v1/alerts/, GET /api/v1/alerts/, GET /api/v1/alerts/{alert_id}
- POST /api/v1/shelters/, GET /api/v1/shelters/, GET /api/v1/shelters/{shelter_id}
- POST /api/v1/risk/estimate

## Limitations
In-memory development storage resets on restart and is not production-ready. SOS is authenticated but does not dispatch real responders. Alerts are manually created, not connected to an official live feed. Risk scoring is a transparent rule-based baseline, not a trained/validated ML model. Weather provider, push notifications, official disaster feeds, and real dispatch are not connected. Set a strong JWT secret before use beyond local development.
