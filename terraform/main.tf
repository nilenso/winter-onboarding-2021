terraform {
    backend "s3" {
        bucket = "winter-onboarding-2021"
        key = "staging.tfstate"
        region = "ap-south-1"
    }

    required_providers {
        heroku = {
            source  = "heroku/heroku"
            version = "~> 4.0"
        }
    }
}

provider "heroku" {
  email   = var.heroku_email
  api_key = var.heroku_api_key
}


resource "heroku_app" "winter_onboarding" {
    name = "winter-onboarding-staging"
    region = "us"
}

resource "heroku_addon" "winter_onboarding_db" {
    app = heroku_app.winter_onboarding.name
    plan = "heroku-postgresql:hobby-dev"
}

resource "null_resource" "test" {

}
