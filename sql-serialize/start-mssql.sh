#!/bin/bash

echo startup mssql
PSWD=qA%124zA_e!
docker run \
  --name ms1 \
  -e "ACCEPT_EULA=Y" \
  -e "SA_PASSWORD=$PSWD" \
  -p 1433:1433 \
  -d mcr.microsoft.com/mssql/server:latest
