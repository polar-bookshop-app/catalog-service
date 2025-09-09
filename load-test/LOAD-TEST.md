# Load Testing

For load testing we will use [k6](https://k6.io/).

You need to install `k6` locally, this is one time effort for details
check [k6-installation](https://k6.io/docs/get-started/installation/)

For windows `choco install k6`

## Run Load Scrip

* Run warm up session using 10 virtual users and 30 seconds

```
k6 run --vus 10 --duration 30s --summary-trend-stats="med,p(95),p(99)" load.js
```

* Run load testing session at least FEW times with 100 virtual users for 180 seconds (3 minutes):

```bash
k6 run --vus 100 --duration 180s --summary-trend-stats="med,p(95),p(99)" load.js
```

The SLOs we are testing against (with 100 concurrent users):

|    SLO name     | Threshold |
|:---------------:|:---------:|
| HTTP error rate |    1%     |
| P99 percentile  |  250 ms   |

## Real-time memory and CPU tracking

To track memory and CPU usage in real time use `docker stats` if you are running service in docker container or
`watch kubectl top pod` if you are running in k8s.

## Observed Results

* Direct calls

```
    http_req_duration..............: med=3.81ms p(95)=9.17ms p(99)=20.17ms
      { expected_response:true }...: med=3.81ms p(95)=9.17ms p(99)=20.17ms
    http_req_failed................: 0.00%  0 out of 17936
    http_reqs......................: 17936  99.094978/s
    
    http_req_duration..............: med=3.85ms p(95)=9.77ms p(99)=15.85ms
      { expected_response:true }...: med=3.85ms p(95)=9.77ms p(99)=15.85ms
    http_req_failed................: 0.00%  0 out of 18000
    http_reqs......................: 18000  99.474707/s
    
```

* Call through API gateway

```
TODO:
   
```


