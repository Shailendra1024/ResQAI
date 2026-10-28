
from datetime import datetime, timezone
from uuid import uuid4

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.orm import Session

from app.schemas import SOSCreate, SOSStageUpdate
from app.core.dependencies import current_user, get_db
from app.db.models import SOSRequest

router = APIRouter(prefix="/sos", tags=["SOS"])


def serialize_sos(item: SOSRequest) -> dict:
    return {
        "id": item.id,
        "user_id": item.user_id,
        "latitude": item.latitude,
        "longitude": item.longitude,
        "message": item.message,
        "stage": item.stage,
        "created_at": item.created_at.isoformat(),
        "responder_name": item.responder_name,
        "responder_eta": item.responder_eta,
        "dispatch_status": item.dispatch_status,
        "updated_at": (
            item.updated_at.isoformat() if item.updated_at else None
        ),
    }


@router.post("/", status_code=201)
def create_sos(
    body: SOSCreate,
    user=Depends(current_user),
    db: Session = Depends(get_db),
):
    item = SOSRequest(
        id=f"sos_{uuid4().hex[:12]}",
        user_id=user["id"],
        latitude=body.latitude,
        longitude=body.longitude,
        message=body.message,
        stage="SENT",
        dispatch_status="NOT_CONNECTED_TO_DISPATCH_SERVICE",
    )

    db.add(item)
    db.commit()
    db.refresh(item)
    return serialize_sos(item)


@router.get("/")
def list_my_sos(
    user=Depends(current_user),
    db: Session = Depends(get_db),
):
    items = db.scalars(
        select(SOSRequest)
        .where(SOSRequest.user_id == user["id"])
        .order_by(SOSRequest.created_at.desc())
    ).all()

    return {
        "count": len(items),
        "items": [serialize_sos(item) for item in items],
    }


@router.get("/{sos_id}")
def get_sos(
    sos_id: str,
    user=Depends(current_user),
    db: Session = Depends(get_db),
):
    item = db.scalar(
        select(SOSRequest).where(
            SOSRequest.id == sos_id,
            SOSRequest.user_id == user["id"],
        )
    )

    if not item:
        raise HTTPException(
            status_code=404, detail="SOS request not found"
        )

    return serialize_sos(item)


@router.patch("/{sos_id}/stage")
def update_sos_stage(
    sos_id: str,
    body: SOSStageUpdate,
    user=Depends(current_user),
    db: Session = Depends(get_db),
):
    item = db.scalar(
        select(SOSRequest).where(
            SOSRequest.id == sos_id,
            SOSRequest.user_id == user["id"],
        )
    )

    if not item:
        raise HTTPException(
            status_code=404, detail="SOS request not found"
        )

    item.stage = body.stage
    item.updated_at = datetime.now(timezone.utc)

    db.commit()
    db.refresh(item)
    return serialize_sos(item)
