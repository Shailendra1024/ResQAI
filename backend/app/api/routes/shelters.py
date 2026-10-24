from uuid import uuid4
from fastapi import APIRouter, HTTPException
from app.schemas import ShelterCreate
from app.store import shelters
router = APIRouter(prefix="/shelters", tags=["Shelters"])

@router.post("/", status_code=201)
def create_shelter(body: ShelterCreate):
    if body.available_spaces > body.capacity:
        raise HTTPException(status_code=422, detail="Available spaces cannot exceed capacity")
    sid = f"shelter_{uuid4().hex[:12]}"
    item = {"id": sid, **body.model_dump()}
    shelters[sid] = item
    return item

@router.get("/")
def list_shelters(available_only: bool = False):
    items = list(shelters.values())
    if available_only:
        items = [x for x in items if x["available_spaces"] > 0]
    return {"count": len(items), "items": items}

@router.get("/{shelter_id}")
def get_shelter(shelter_id: str):
    item = shelters.get(shelter_id)
    if not item:
        raise HTTPException(status_code=404, detail="Shelter not found")
    return item
