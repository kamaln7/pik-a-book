#!/bin/bash

things=$(jq '.[] | .photo' ../users.json  | grep -v null | sed 's/"//g')

i=0
for thing in $things
do
    wget -O "avatar_${i}.png" "$thing"
    ((i++))
done
