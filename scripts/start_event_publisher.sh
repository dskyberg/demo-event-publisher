#!/usr/bin/env bash

[ `whoami` = root ] || { sudo "$0" "$@"; exit $?; }  # Ensure that user uses 'sudo' or runs this script under root

BASEDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

docker run -it --rm --net=host --name EventPublisher -v "$BASEDIR/../src/main/resources":/data confyrm-event-publisher