
from uuid import uuid4

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.orm import Session

from app.schemas import AlertCreate
from app.core.dependencies import get_db
from app.db.models import Alert

router = APIRouter(prefix="/alerts", tags=["Alerts"])


def serialize_alert(item: Alert) -> dict:
    return {
        "id": item.id,
        "title": item.title,
        "description": item.description,
        "severity": item.severity,
        "location": item.location,
        "latitude": item.latitude,
        "longitude": item.longitude,
        "created_at": item.created_at.isoformat(),
        "source": item.source,
    }


@router.post("/", status_code=201)
def create_alert(body: AlertCreate, db: Session = Depends(get_db)):
    item = Alert(
        id=f"alert_{uuid4().hex[:12]}",
        **body.model_dump(),
        source="manual",
    )

    db.add(item)
    db.commit()
    db.refresh(item)

    return serialize_alert(item)


@router.get("/")
def list_alerts(
    severity: str | None = None,
    db: Session = Depends(get_db),
):
    query = select(Alert)

    if severity:
        query = query.where(Alert.severity == severity.lower())

    query = query.order_by(Alert.created_at.desc())
    items = db.scalars(query).all()

    return {
        "count": len(items),
        "items": [serialize_alert(item) for item in items],
    }


@router.get("/{alert_id}")
def get_alert(
    alert_id: str,
    db: Session = Depends(get_db),
):
    item = db.get(Alert, alert_id)

    if not item:
        raise HTTPException(status_code=404, detail="Alert not found")

    return serialize_alert(item)
