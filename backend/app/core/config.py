import os
from dataclasses import dataclass, field


def _split_csv(value: str) -> list[str]:
    return [item.strip() for item in value.split(",") if item.strip()]


@dataclass
class Settings:
    jwt_secret: str = os.getenv("RESQAI_JWT_SECRET", "CHANGE-ME-TO-A-LONG-RANDOM-SECRET")
    jwt_expire_minutes: int = int(os.getenv("RESQAI_JWT_EXPIRE_MINUTES", "60"))
    cors_origins: list[str] = field(
        default_factory=lambda: _split_csv(
            os.getenv("RESQAI_CORS_ORIGINS", "http://localhost:3000,http://127.0.0.1:3000")
        )
    )


settings = Settings()
