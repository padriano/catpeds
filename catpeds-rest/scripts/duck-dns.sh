#!/bin/sh

echo url="https://www.duckdns.org/update?domains=${dns.domain}&token=${dns.token}&ip=" | curl -k -o ~/duck.log -K -
