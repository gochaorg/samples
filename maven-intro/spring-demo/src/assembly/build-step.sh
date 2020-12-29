#!/bin/bash

echo "running build step"

if [ -d $1 ] ;
then
  git status 1>${1}/git-status
fi