import os

from dotenv import load_dotenv
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

load_dotenv()

DATABASE_URL = os.getenv("RESQAI_DATABASE_URL")

if not DATABASE_URL:
    raise RuntimeError("RESQAI_DATABASE_URL is missing from the .env file")

engine = create_engine(
    DATABASE_URL,
    pool_pre_ping=True,
)

SessionLocal = sessionmaker(
    bind=engine,
    autoflush=False,
    autocommit=False,
)


def test_connection():
    with engine.connect() as connection:
        return connection.execute(
            text("SELECT current_database(), current_user")
        ).one()