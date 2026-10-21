# Account Performance Tests

Performance testing for the Spring Boot account and payment API using
Docker Compose and Grafana k6.

## Prerequisites

- Docker Engine and Docker Compose
- Python 3
- The account API Docker image built locally
- A dedicated, disposable test database

## 1. Start the API

From this project directory:

```bash
docker compose up -d api
docker compose logs -f api
```

Wait until the application is ready.

## 2. Prepare test data

Adapt the request payloads in `scripts/prepare.py` to match
the actual Java DTOs.

Then run:

```bash
python3 scripts/prepare.py
```
or

```bash
docker compose run --rm prepare
```

This creates accounts, adds members, deposits funds, and writes account
and cardholder IDs to `scripts/accounts.json`.

## 3. Run 100,000 payment requests

```bash
docker compose --profile test run --rm k6 run \
  --summary-export=/results/h2/payments-summary.json \
  /scripts/payments.js
```

Dynamically parameter of datetime
```bash
TIMESTAMP=$(date '+%Y%m%d-%H%M%S')

docker compose --profile test run --rm k6 run \
  --summary-export="/results/h2/payments-summary-${TIMESTAMP}.json" \
  /scripts/payments.js
```

The default test schedules 100,000 iterations, with one HTTP request
per iteration. Check that the test completes before interpreting its results.

## 4. Run idempotency tests
```bash
docker compose --profile test run --rm k6 run \
  --summary-export=/results/h2/retries-summary.json \
  -e RETRY_CASES=1000 \
  /scripts/retries.js
```

Dynamically parameter of datetime

```bash
TIMESTAMP=$(date '+%Y%m%d-%H%M%S')

docker compose --profile test run --rm k6 run \
  --summary-export=/results/h2/retries-summary-${TIMESTAMP}.json \
  -e RETRY_CASES=1000 \
  /scripts/retries.js
```

Each case sends the same payment twice and then reuses the key with a
different amount, which should return HTTP 409.


## 5. Make the script executable

Run this once from the project root:

```bash
chmod +x run-test.sh
```

```bash
./run-test.sh
```

# Interactive: choose between payments and retries
```bash
./run-test.sh
```

# Automatic: run payments
```bash
./run-test.sh auto
```

# Automatic: run retries
```bash
./run-test.sh auto retries.js
```


## 6. Run the scenario for POSTGRESQL DB

Run this once from the project root:

```bash
docker compose -f docker-compose-postgres.yml up -d
```

Run the 'prepare' of the main compose yaml 

```bash
docker compose -f docker-compose-postgres.yml --profile postgres run --rm prepare
```

Run the 'k6' of the main compose yaml

```bash
docker compose -f docker-compose-postgres.yml --profile postgres run --rm k6 run \
  --summary-export=/results/postgres/payments-summary.json \
  /scripts/payments.js
```
 

Stop running the docker images

```bash
docker compose -f docker-compose-postgres.yml down
```

Check the status of it

```bash
docker compose -f docker-compose-postgres.yml  ps
```
## Notes

- H2 in-memory databases are tied to the API JVM.
- Do not scale the API to multiple replicas while using this H2 setup.
- Use unique idempotency keys for independent payments.
- Run only against disposable test data, never production.
- H2 PostgreSQL compatibility mode is not a substitute for a real
  PostgreSQL performance benchmark.
- The sample scripts assume HTTP 200 for authorization decisions.
  Validate the response body as well if you need to distinguish approved
  payments from legitimate declines.
