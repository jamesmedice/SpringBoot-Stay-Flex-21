
import http from 'k6/http';
import { check } from 'k6';
import exec from 'k6/execution';
import { SharedArray } from 'k6/data';

const BASE_URL = (__ENV.BASE_URL || 'http://localhost:8080/payment-v1').replace(/\/$/, '');
const API_PREFIX = (__ENV.API_PREFIX || '/payment/accounts').replace(/\/$/, ''); 
const ACCOUNTS_JSON = __ENV.ACCOUNTS_JSON || '../scripts/accounts.json';

const accounts = new SharedArray('accounts', function () {
  return JSON.parse(open(ACCOUNTS_JSON));
});

export const options = {
    scenarios: {
        payments: {
            executor: 'shared-iterations',
            vus: Number(__ENV.VUS || 50),
            iterations: Number(__ENV.TOTAL_REQUESTS || 100000),
            maxDuration: __ENV.MAX_DURATION || '30m',
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500', 'p(99)<1000'],
    },
};

if (!Array.isArray(accounts) || accounts.length === 0) {
    throw new Error('accounts.json must contain prepared accounts');
}

export default function () {
    const iteration = exec.scenario.iterationInTest;
    const account = accounts[iteration % accounts.length];

    const payload = {
        cardholderId: account.cardholderId,
        idempotencyKey: `payment-${account.cardholderId || 'test'}-${iteration}`,
        amount: Number(__ENV.PAYMENT_AMOUNT || 1.00),
    };

    const response = http.put(
        `${BASE_URL}${API_PREFIX}/${account.accountId}/authorizations`,
        JSON.stringify(payload),
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { endpoint: 'authorization' },
        },
    );

    check(response, {
        'authorization returned HTTP 200': (r) => r.status === 200,
    });
}
