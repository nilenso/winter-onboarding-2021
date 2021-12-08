# Fleet Management Service

## Installation

1. Clone this repo `git clone https://github.com/nilenso/winter-onboarding-2021.git`
2. `cd` into `winter-onboarding-2021`
3. Install dependencies `lein deps`
4. Create the databases: `fleet_management`, and `fleet_management_test`
5. Run the migrations
```
lein migrations migrate
ENV=test lein migrations migrate
```
6. Run the tests `lein test`
7. Run the server `lein run server`

The server will be available at `http://localhost:3000/`
