package com.example.resqai.network

object ApiConfig {
    // Android emulator -> your computer's localhost.
    // On a real phone use your computer's LAN IP instead, e.g. "http://192.168.1.20:8000/api/v1/"
    // (and start the backend with: python -m uvicorn main:app --host 0.0.0.0 --reload)
    const val BASE_URL = "http://10.0.2.2:8000/api/v1/"

    // DEV ONLY: the app has no login screen yet, so it silently logs in as a demo user
    // (registering it first if needed). Set to false once a real login screen exists.
    const val DEV_AUTO_LOGIN = true
    const val DEV_NAME = "Demo User"
    const val DEV_EMAIL = "demo@resqai.dev"
    const val DEV_PASSWORD = "demo-pass-123"
}
