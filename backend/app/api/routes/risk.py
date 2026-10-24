from fastapi import APIRouter
from app.schemas import RiskRequest
router = APIRouter(prefix="/risk", tags=["Weather & Risk"])

@router.post("/estimate")
def estimate_risk(body: RiskRequest):
    # Baseline rules only; this is not a validated ML model or official warning.
    score, reasons = 0, []
    p, w, t = body.precipitation_mm, body.wind_kph, body.temperature_c
    if p is not None:
        if p >= 100: score += 50; reasons.append("Very heavy precipitation reported")
        elif p >= 50: score += 30; reasons.append("Heavy precipitation reported")
        elif p >= 20: score += 15; reasons.append("Moderate precipitation reported")
    if w is not None:
        if w >= 90: score += 35; reasons.append("Very high wind speed reported")
        elif w >= 60: score += 20; reasons.append("High wind speed reported")
    if t is not None:
        if t >= 45: score += 25; reasons.append("Extreme heat reported")
        elif t <= -10: score += 25; reasons.append("Extreme cold reported")
    score = min(score, 100)
    level = "low" if score < 25 else "moderate" if score < 50 else "high" if score < 75 else "critical"
    return {"latitude": body.latitude, "longitude": body.longitude, "risk_score": score,
            "risk_level": level, "reasons": reasons, "model": "baseline_rules_v1",
            "warning": "Estimate only; not an official warning or validated predictive model."}
