# Fleet Management Service

## Installation

1. Clone this repo `git clone https://github.com/nilenso/winter-onboarding-2021.git`
2. `cd` into `winter-onboarding-2021`
3. Install dependencies `lein deps`
4. Run the migrations
```
lein migrations migrate
ENV=test lein migrations migrate
```
5. Run the tests `lein test`
6. Run the server `lein run`

The server will be available at `http://localhost:3000/`