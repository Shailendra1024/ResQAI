from fastapi import Depends, HTTPException
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
import jwt
from jwt import InvalidTokenError
from app.core.config import settings
from app.store import users

bearer = HTTPBearer(auto_error=False)
def current_user(credentials: HTTPAuthorizationCredentials | None = Depends(bearer)):
    if credentials is None:
        raise HTTPException(status_code=401, detail="Bearer token required")
    try:
        payload = jwt.decode(credentials.credentials, settings.jwt_secret, algorithms=["HS256"])
        email = payload.get("sub")
        if not email or email not in users:
            raise HTTPException(status_code=401, detail="Invalid or expired token")
        return users[email]
    except InvalidTokenError:
        raise HTTPException(status_code=401, detail="Invalid or expired token")
