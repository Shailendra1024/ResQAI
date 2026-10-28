
from uuid import uuid4

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.orm import Session

from app.schemas import ShelterCreate
from app.core.dependencies import get_db
from app.db.models import Shelter

router = APIRouter(prefix="/shelters", tags=["Shelters"])


def serialize_shelter(item: Shelter) -> dict:
    return {
        "id": item.id,
        "name": item.name,
        "address": item.address,
        "latitude": item.latitude,
        "longitude": item.longitude,
        "capacity": item.capacity,
        "available_spaces": item.available_spaces,
        "contact": item.contact,
    }


@router.post("/", status_code=201)
def create_shelter(
    body: ShelterCreate,
    db: Session = Depends(get_db),
):
    if body.available_spaces > body.capacity:
        raise HTTPException(
            status_code=422,
            detail="Available spaces cannot exceed capacity",
        )

    item = Shelter(
        id=f"shelter_{uuid4().hex[:12]}",
        **body.model_dump(),
    )

    db.add(item)
    db.commit()
    db.refresh(item)

    return serialize_shelter(item)


@router.get("/")
def list_shelters(
    available_only: bool = False,
    db: Session = Depends(get_db),
):
    query = select(Shelter)

    if available_only:
        query = query.where(Shelter.available_spaces > 0)

    items = db.scalars(query).all()

    return {
        "count": len(items),
        "items": [serialize_shelter(item) for item in items],
    }


@router.get("/{shelter_id}")
def get_shelter(
    shelter_id: str,
    db: Session = Depends(get_db),
):
    item = db.get(Shelter, shelter_id)

    if not item:
        raise HTTPException(
            status_code=404,
            detail="Shelter not found",
        )

    return serialize_shelter(item)
