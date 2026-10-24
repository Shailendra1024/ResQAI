from datetime import datetime, timezone
from uuid import uuid4
from fastapi import APIRouter, HTTPException
from app.schemas import AlertCreate
from app.store import alerts
router = APIRouter(prefix="/alerts", tags=["Alerts"])

@router.post("/", status_code=201)
def create_alert(body: AlertCreate):
    aid = f"alert_{uuid4().hex[:12]}"
    item = {"id": aid, **body.model_dump(),
            "created_at": datetime.now(timezone.utc).isoformat(), "source": "manual"}
    alerts[aid] = item
    return item

@router.get("/")
def list_alerts(severity: str | None = None):
    items = list(alerts.values())
    if severity:
        items = [x for x in items if x["severity"] == severity.lower()]
    items.sort(key=lambda x: x["created_at"], reverse=True)
    return {"count": len(items), "items": items}

@router.get("/{alert_id}")
def get_alert(alert_id: str):
    item = alerts.get(alert_id)
    if not item:
        raise HTTPException(status_code=404, detail="Alert not found")
    return item
