# Confyrm Developers Demo Event Publisher application

## Build

* Build docker image: `sudo scripts/build_docker_image.sh`

## Configure

* In `src/main/resources/app.properties` change stars ('*****') with Confyrm API client `login` and `password`
* In `src/main/resources/events.cfg.json` set publisher rate for each event type
* Add user identities to config file `src/main/resources/identities.csv`. This file is multiline, each line should match format below: `user_external_ref[;user_subscription_ref]`. Part at '[]' braces is optional.

## Run

* To run event publisher: `sudo scripts/start_event_publisher.sh`

