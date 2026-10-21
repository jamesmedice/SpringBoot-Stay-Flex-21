
import http from 'k6/http';
import { check } from 'k6';
import exec from 'k6/execution';

const BASE_URL = (__ENV.BASE_URL || 'http://localhost:8080/payment-v1').replace(/\/$/, '');
const API_PREFIX = (__ENV.API_PREFIX || '/payment/accounts').replace(/\/$/, '');
const accounts = JSON.parse(open('../scripts/accounts.json'));

export const options = {
    scenarios: {
        retries: {
            executor: 'shared-iterations',
            vus: Number(__ENV.VUS || 10),
            iterations: Number(__ENV.RETRY_CASES || 1000),
            maxDuration: '10m',
        },
    },
};

if (!Array.isArray(accounts) || accounts.length === 0) {
    throw new Error('accounts.json must contain prepared accounts');
}

export default function () {
    const iteration = exec.scenario.iterationInTest;
    const account = accounts[iteration % accounts.length];
    const key = `retry-${account.cardholderId || 'test'}-${iteration}`;

    const url =
        `${BASE_URL}${API_PREFIX}/${account.accountId}/authorizations`;

    const headers = {
        headers: { 'Content-Type': 'application/json' },
        tags: { endpoint: 'authorization-retry' },
    };

    const payload = {
        cardholderId: account.cardholderId,
        idempotencyKey: key,
        amount: Number(__ENV.PAYMENT_AMOUNT || 1.00),
    };

    const first = http.put(url, JSON.stringify(payload), headers);
    check(first, {
        'first authorization returned 200': (r) => r.status === 200,
    });

    const retry = http.put(url, JSON.stringify(payload), headers);
    check(retry, {
        'same request retry returned 200': (r) => r.status === 200,
    });

    const conflictPayload = {
        ...payload,
        amount: payload.amount + 1,
    };

    const conflict = http.put(
        url,
        JSON.stringify(conflictPayload),
        headers,
    );

    check(conflict, {
        'same key with different amount returned 409': (r) => r.status === 409,
    });
}
