
import json
import os
import urllib.error
import urllib.request
import uuid
from pathlib import Path

BASE_URL = os.getenv(
    "BASE_URL",
    "http://127.0.0.1:8080/payment-v1",
)
API_PREFIX = os.getenv("API_PREFIX", "/payment/accounts")

COUNT = int(os.getenv("ACCOUNTS_COUNT", "10000"))

PROJECT_ROOT = Path(__file__).resolve().parent.parent

OUTPUT = Path(os.getenv("OUTPUT", "../scripts/accounts.json"))
OUTPUT = Path(os.getenv("OUTPUT", str(PROJECT_ROOT / "scripts" / "accounts.json"),)).resolve()


CREATE_PAYLOAD = os.getenv(
    "CREATE_ACCOUNT_PAYLOAD",
    '{"ownerId":"{{user_id}}"}',
)
MEMBER_PAYLOAD = os.getenv(
    "ADD_MEMBER_PAYLOAD",
    '{"userId":"{{cardholder_id}}","role":"MEMBER"}',
)
DEPOSIT_PAYLOAD = os.getenv(
    "DEPOSIT_PAYLOAD",
    '{"amount":10000.00,"idempotencyKey":"deposit-{{index}}-{{account_id}}"}',
)


def call(method, path, payload=None):
    url = BASE_URL.rstrip("/") + "/" + path.lstrip("/")
    body = None if payload is None else json.dumps(payload).encode("utf-8")

    request = urllib.request.Request(
        url,
        data=body,
        method=method,
        headers={
            "Content-Type": "application/json",
            "Accept": "application/json",
        },
    )

    try:
        with urllib.request.urlopen(request, timeout=30) as response:
            raw = response.read().decode("utf-8")
            return json.loads(raw) if raw.strip() else {}
    except urllib.error.HTTPError as exc:
        error_body = exc.read().decode("utf-8", errors="replace")
        print(f"\nRequest failed: {method} {url}")
        print(f"HTTP status: {exc.code}")
        print(f"Response: {error_body}")
        raise


def render(template, values):
    for key, value in values.items():
        template = template.replace("{{" + key + "}}", str(value))
    return json.loads(template)


def main():
    if COUNT < 1:
        raise ValueError("ACCOUNTS_COUNT must be at least 1")

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    accounts = []

    for index in range(COUNT):
        # The account owner and payment cardholder must be different users.
        owner_id = str(uuid.uuid4())
        cardholder_id = str(uuid.uuid4())

        values = {
            "user_id": owner_id,
            "owner_id": owner_id,
            "cardholder_id": cardholder_id,
            "index": index,
        }

        # 1. Create the account. The owner is associated automatically.
        created = call(
            "POST",
            API_PREFIX,
            render(CREATE_PAYLOAD, values),
        )

        account_id = created.get("id")
        if not account_id:
            raise RuntimeError(
                f"Cannot find 'id' in create-account response: {created}"
            )

        values["account_id"] = account_id

        # 2. Add a different user as an active member.
        call(
            "PUT",
            f"{API_PREFIX}/{account_id}/members",
            render(MEMBER_PAYLOAD, values),
        )

        # 3. Fund the account for authorization tests.
        call(
            "PUT",
            f"{API_PREFIX}/{account_id}/deposits",
            render(DEPOSIT_PAYLOAD, values),
        )

        accounts.append({
            "accountId": str(account_id),
            "cardholderId": cardholder_id,
        })

        if (index + 1) % 100 == 0 or index + 1 == COUNT:
            print(f"Prepared {index + 1}/{COUNT}")

    OUTPUT.write_text(json.dumps(accounts, indent=2) + "\n", encoding="utf-8")
    print(f"Saved {len(accounts)} accounts to {OUTPUT}")


if __name__ == "__main__":
    main()