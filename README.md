# winter-onboarding-2021

![example workflow](https://github.com/nilenso/winter-onboarding-2021/actions/workflows/deploy-staging.yml/badge.svg)

Contains the code written by onboardees of Winter 2021:
1. Alisha
2. Yogi
3. Shivam

## Usage

Assumes you have Lein setup on your system. If not, follow [Install Lein](https://leiningen.org/#install).

Steps:
1. Clone this repo
2. `cd` into the repo and run `lein deps`
3. Run `lein test` to run the tests


## What will handlers return?

All handlers which render some HTML, should return a structure like below:
```clojure
{:title "Whatever Page title"
 :content [:div "Whatever content of div"]}
```

Other endpoints can return a simple `ring.util.response/response` for returning something.
