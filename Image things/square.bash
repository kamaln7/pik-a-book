#!/bin/bash

for file in *.jpg
do
    convert "$file" -resize "100^>" -gravity center \
                     -crop 100x100+0+0 -strip "{$file}_sq.jpg"
done
