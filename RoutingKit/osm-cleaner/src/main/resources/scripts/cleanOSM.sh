#!/bin/bash

# in case you need to make it executable:

# in terminal: navigate to location of this script,

# then execute "chmod 700 cleanOSM.sh"

# after that, run script with ./cleanOSM.sh <filename>

echo cleaning process started

cd ./osm-cleaner/src/main/resources/Osmosis_Programm/bin

# make osmosis executable

chmod 700 osmosis

if ./osmosis --read-xml "../../OSM_Files/$1.osm" --tf accept-ways highway=motorway,trunk,trunk_link,primary,secondary,tertiary,unclassified,residential,living_street --used-node --write-xml "../../OSM_Files/$1_Cleaned.osm" ; then

    echo "success"

else

    echo "fail"

fi
