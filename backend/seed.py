"""Fill the running backend with the sample alerts and shelters the app used to hard-code.

Usage (backend must be running):  python seed.py
The store is in-memory, so run this again after every backend restart.
"""
import sys
import httpx

BASE = "http://127.0.0.1:8000/api/v1"
LAT, LNG = 26.8467, 80.9462  # same placeholder location as the app's LocationProvider

ALERTS = [
    ("Flood Warning", "high", "Riverside District",
     "Continuous heavy rainfall has caused the river level to rise sharply. Flash flooding is expected in low-lying areas within the next 6 hours. Residents are advised to move to higher ground immediately."),
    ("Heavy Rainfall", "medium", "Downtown Area",
     "Sustained heavy rainfall is forecast over the next 12 hours. Localized waterlogging possible in low-drainage zones."),
    ("Heat Wave Advisory", "low", "City Wide",
     "Temperatures are expected to remain 4-6 C above the seasonal average for the next 3 days."),
    ("Earthquake Tremor", "medium", "North Hills Region",
     "A magnitude 4.2 tremor was recorded. No major structural damage reported."),
    ("Cyclone Watch", "high", "Coastal Belt",
     "A tropical cyclone system is forming off the coast and may make landfall within 48 hours."),
]

# (name, distance_km, capacity, occupancy, address)
SHELTERS = [
    ("Community Hall Shelter", 1.2, 200, 120, "42 Riverside Ave"),
    ("St. Mary's School Shelter", 2.5, 150, 150, "18 Church Road"),
    ("Riverside Sports Complex", 3.8, 300, 90, "7 Stadium Lane"),
    ("North Hills Community Center", 4.6, 180, 60, "101 Hillcrest Blvd"),
    ("Central Library Shelter", 5.1, 100, 100, "5 Main Street"),
    ("Downtown Fire Station Annex", 6.3, 80, 25, "220 5th Avenue"),
]


def main() -> int:
    try:
        with httpx.Client(base_url=BASE, timeout=10) as c:
            for title, severity, location, description in ALERTS:
                c.post("/alerts/", json={
                    "title": title, "description": description, "severity": severity,
                    "location": location, "latitude": LAT, "longitude": LNG,
                }).raise_for_status()
            for i, (name, dist, cap, occ, addr) in enumerate(SHELTERS):
                c.post("/shelters/", json={
                    "name": name, "address": addr,
                    "latitude": LAT + dist / 111.0,      # roughly dist km north of the user
                    "longitude": LNG + i * 0.003,
                    "capacity": cap, "available_spaces": cap - occ,
                }).raise_for_status()
    except httpx.HTTPError as e:
        print(f"Seeding failed: {e}\nIs the backend running on {BASE}?")
        return 1
    print(f"Seeded {len(ALERTS)} alerts and {len(SHELTERS)} shelters.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
