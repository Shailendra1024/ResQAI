from datetime import datetime, timezone
from uuid import uuid4
from fastapi import APIRouter, Depends, HTTPException
from app.schemas import SOSCreate, SOSStageUpdate
from app.store import sos_requests
from app.core.dependencies import current_user
router = APIRouter(prefix="/sos", tags=["SOS"])

@router.post("/", status_code=201)
def create_sos(body: SOSCreate, user=Depends(current_user)):
    sid = f"sos_{uuid4().hex[:12]}"
    item = {"id": sid, "user_id": user["id"], "latitude": body.latitude,
            "longitude": body.longitude, "message": body.message, "stage": "SENT",
            "created_at": datetime.now(timezone.utc).isoformat(),
            "responder_name": None, "responder_eta": None,
            "dispatch_status": "NOT_CONNECTED_TO_DISPATCH_SERVICE"}
    sos_requests[sid] = item
    return item

@router.get("/")
def list_my_sos(user=Depends(current_user)):
    items = [x for x in sos_requests.values() if x["user_id"] == user["id"]]
    return {"count": len(items), "items": items}

@router.get("/{sos_id}")
def get_sos(sos_id: str, user=Depends(current_user)):
    item = sos_requests.get(sos_id)
    if not item or item["user_id"] != user["id"]:
        raise HTTPException(status_code=404, detail="SOS request not found")
    return item

@router.patch("/{sos_id}/stage")
def update_sos_stage(sos_id: str, body: SOSStageUpdate, user=Depends(current_user)):
    item = sos_requests.get(sos_id)
    if not item or item["user_id"] != user["id"]:
        raise HTTPException(status_code=404, detail="SOS request not found")
    item["stage"] = body.stage
    item["updated_at"] = datetime.now(timezone.utc).isoformat()
    return item
