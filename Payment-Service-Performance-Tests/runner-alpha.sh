#!/usr/bin/env bash
set -Eeuo pipefail

# Run from the directory containing this script.
cd "$(dirname "$0")"

# API settings used by prepare.py.
export BASE_URL="http://127.0.0.1:8080/payment-v1"
export API_PREFIX="/payment/accounts"

# Choose execution mode:
#   ./run-test.sh          -> interactive mode
#   ./run-test.sh auto     -> automatic mode (payments.js by default)
#   ./run-test.sh auto retries.js -> automatic mode with retries.js
MODE="${1:-interactive}"

case "$MODE" in
  auto)
    K6_SCRIPT="${2:-payments.js}"
    ;;
  interactive)
    echo ""
    echo "Select a performance test:"
    echo "1) Payments test (payments.js)"
    echo "2) Idempotency / retries test (retries.js)"
    echo ""

    read -rp "Enter your choice [1-2]: " choice

    case "$choice" in
      1) K6_SCRIPT="payments.js" ;;
      2) K6_SCRIPT="retries.js" ;;
      *)
        echo "Invalid choice. Please select 1 or 2." >&2
        exit 1
        ;;
    esac
    ;;
  *)
    echo "Usage:"
    echo "  ./run-test.sh"
    echo "  ./run-test.sh auto [payments.js|retries.js]"
    exit 1
    ;;
esac

# Validate the selected test script.
case "$K6_SCRIPT" in
  payments.js|retries.js) ;;
  *)
    echo "Unsupported test script: $K6_SCRIPT" >&2
    echo "Allowed scripts: payments.js, retries.js" >&2
    exit 1
    ;;
esac

# Generate a unique timestamp for the summary filename.
TIMESTAMP="$(date +%s%3N)"
SUMMARY_FILE="results/h2/${K6_SCRIPT%.js}-summary-${TIMESTAMP}.json"

mkdir -p results

echo "======================================"
echo "Performance test"
echo "Mode: ${MODE}"
echo "k6 script: ${K6_SCRIPT}"
echo "Summary: ${SUMMARY_FILE}"
echo "======================================"

# Step 1: Prepare test data.
echo "Step 1/2: Preparing test data..."
python3 scripts/prepare.py

# Step 2: Run k6 against the prepared accounts.
echo "Step 2/2: Running k6..."

K6_ARGS=()

if [[ "$K6_SCRIPT" == "retries.js" ]]; then
  K6_ARGS=(-e RETRY_CASES=1000)
fi

docker compose --profile test run --rm k6 run \
  "${K6_ARGS[@]}" \
  --summary-export="/results/${SUMMARY_FILE#results/}" \
  "/scripts/${K6_SCRIPT}"

echo "======================================"
echo "Test completed."
echo "Summary: ${SUMMARY_FILE}"
echo "======================================"