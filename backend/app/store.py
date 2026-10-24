# Development-only in-memory store; data resets when the server restarts.
users: dict[str, dict] = {}
sos_requests: dict[str, dict] = {}
alerts: dict[str, dict] = {}
shelters: dict[str, dict] = {}
