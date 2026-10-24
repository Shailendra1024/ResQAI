from pydantic import BaseModel, EmailStr, Field
from typing import Literal

class RegisterRequest(BaseModel):
    name: str = Field(min_length=2, max_length=100)
    email: EmailStr
    password: str = Field(min_length=8, max_length=128)
class LoginRequest(BaseModel):
    email: EmailStr
    password: str = Field(min_length=8, max_length=128)
class SOSCreate(BaseModel):
    latitude: float = Field(ge=-90, le=90)
    longitude: float = Field(ge=-180, le=180)
    message: str = Field(default="Emergency assistance required", max_length=500)
class SOSStageUpdate(BaseModel):
    stage: Literal["SENT", "RESPONDER_ASSIGNED", "RESPONDER_EN_ROUTE", "RESCUE_COMPLETED"]
class AlertCreate(BaseModel):
    title: str = Field(min_length=3, max_length=150)
    description: str = Field(min_length=3, max_length=1000)
    severity: Literal["low", "medium", "high", "critical"]
    location: str = Field(min_length=2, max_length=200)
    latitude: float | None = Field(default=None, ge=-90, le=90)
    longitude: float | None = Field(default=None, ge=-180, le=180)
class ShelterCreate(BaseModel):
    name: str = Field(min_length=2, max_length=150)
    address: str = Field(min_length=3, max_length=300)
    latitude: float = Field(ge=-90, le=90)
    longitude: float = Field(ge=-180, le=180)
    capacity: int = Field(ge=1, le=100000)
    available_spaces: int = Field(ge=0, le=100000)
    contact: str | None = Field(default=None, max_length=40)
class RiskRequest(BaseModel):
    latitude: float = Field(ge=-90, le=90)
    longitude: float = Field(ge=-180, le=180)
    temperature_c: float | None = Field(default=None, ge=-80, le=65)
    precipitation_mm: float | None = Field(default=None, ge=0, le=2000)
    wind_kph: float | None = Field(default=None, ge=0, le=500)
