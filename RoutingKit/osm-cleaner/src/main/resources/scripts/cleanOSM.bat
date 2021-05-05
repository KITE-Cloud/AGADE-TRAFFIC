cd osm-cleaner\src\main\resources\Osmosis_Programm\bin /

(
osmosis.bat  --read-xml "..\..\OSM_Files\%1.osm" --tf accept-ways highway=* cycleway=* --used-node --write-xml "..\..\OSM_Files\%1_Cleaned.osm" && (
echo Successfully finished!
exit

) || (
echo Please make sure that you correctly provided all parameters /
set /p b=Press Enter to exit
exit -1
)
)
