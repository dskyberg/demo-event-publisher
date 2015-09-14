#!/usr/bin/env bash

[ `whoami` = root ] || { sudo "$0" "$@"; exit $?; }  # Ensure that user uses 'sudo' or runs this script under root

BASEDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

docker build -t confyrm-event-publisher "$BASEDIR"/..
