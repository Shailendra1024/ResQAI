from fastapi import APIRouter, HTTPException
from app.schemas import RegisterRequest, LoginRequest
from app.core.security import hash_password, verify_password, create_access_token
from app.store import users
router = APIRouter(prefix="/auth", tags=["Authentication"])

@router.post("/register", status_code=201)
def register(body: RegisterRequest):
    email = str(body.email).lower()
    if email in users:
        raise HTTPException(status_code=409, detail="Email already registered")
    user = {"id": f"user_{len(users)+1}", "name": body.name, "email": email,
            "password_hash": hash_password(body.password)}
    users[email] = user
    return {"id": user["id"], "name": user["name"], "email": email}

@router.post("/login")
def login(body: LoginRequest):
    email = str(body.email).lower()
    user = users.get(email)
    if not user or not verify_password(body.password, user["password_hash"]):
        raise HTTPException(status_code=401, detail="Invalid email or password")
    return {"access_token": create_access_token(email), "token_type": "bearer",
            "expires_in_minutes": 60,
            "user": {"id": user["id"], "name": user["name"], "email": email}}
