import http from 'k6/http';
import {check, group, sleep} from 'k6';

// for local or docker execution
const SERVICE_URL = 'http://localhost:9001';
const BASE_URL = `${SERVICE_URL}/20250525`;

// for k8s with NodePort execution
//const SERVICE_URL = 'http://localhost:30020';
//const BASE_URL=`${SERVICE_URL}/20250525`;

// for local or docker execution through Edge API gateway
//const SERVICE_URL = 'http://localhost:8080';
//const BASE_URL=`${SERVICE_URL}/api/catalog/20250525`;

export const options = {
    thresholds: {
        http_req_failed: ['rate < 0.01'], // http errors should be less than 1%
        http_req_duration: ['p(99) < 250'], // 99% of requests should be below 250ms
    },
};

export default function () {

    let projectId;

    // group('CREATE new book', function () {
    //     projectId = createRandomBook();
    // });

    group('LIST books', listBooks);

    // group('DELETE book', function () {
    //     deleteBook(projectId);
    // });

    sleep(1);
}

function createRandomBook() {
    const randomNumber = Math.floor(Math.random() * 1000000) + 1;

    const data = JSON.stringify({
        "name": `project-${randomNumber}`,
        "displayName": `Project with ${randomNumber}`,
        "description": 'Project with pipelines for Omics',
        "customerCompartmentId": 'ocid1.compartment.oc1..aaaaaaaaripodj35cwvd43z6rbgaa424mlh6uzihqdv6afafswhe3u2j2gbq',
        "tenantId": 'ocid1.tenancy.oc1..aaaaaaaa5husazs2ytt4vmvztlaifts4zraa26vz6p7i76vgugoic7a5ddga'
    });

    const headers = {
        'Content-Type': 'application/json'
    };

    const resp = http.post(`${BASE_URL}/projects`, data, {headers: headers});

    check(resp, {
        'CREATE is status 201': (r) => r.status === 201,
    });

    return JSON.parse(resp.body).ocid;
}

function deleteBook(projectId) {
    const resp = http.del(`${BASE_URL}/projects/${projectId}`);

    check(resp, {
        'DELETE is status 204': (r) => r.status === 204,
    });
}

function listBooks() {
    const resp = http.get(`${BASE_URL}/books`);
    check(resp, {
        'LIST is status 200': (r) => r.status === 200,
    });
}
