#!/bin/sh

wget -r -c recettes.doctissimo.fr

# we need to replace the boggy <title> ... </title> by <title />
# NekoHTM does not like it
# files will be created in backup directory

FILES="*.htm"

for FILE in $FILES
do
  echo "normalizing: $FILE"
  sed 's/<title>.*<\/title>/<title \/>/g' $FILE > backup/$FILE
done
