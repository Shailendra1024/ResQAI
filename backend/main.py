from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import api_router
from app.core.config import settings

app = FastAPI(title="ResQ AI Backend", version="1.0.0",
              description="SOS, alerts, shelters, authentication and risk estimate APIs")
app.add_middleware(CORSMiddleware, allow_origins=settings.cors_origins,
                   allow_credentials=True, allow_methods=["*"], allow_headers=["*"])
app.include_router(api_router, prefix="/api/v1")

@app.get("/", tags=["System"])
def home():
    return {"project": "ResQ AI", "status": "running", "docs": "/docs"}

@app.get("/health", tags=["System"])
def health():
    return {"status": "healthy"}
