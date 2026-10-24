from fastapi import APIRouter
from app.api.routes import auth, sos, alerts, shelters, risk
api_router = APIRouter()
for module in (auth, sos, alerts, shelters, risk):
    api_router.include_router(module.router)
