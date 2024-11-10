#!/bin/bash

# Takes the input KML file as the first argument
inputFile="$1"

# Create a new CSV file as the second argument
outputFile="$2"

# Will only continue if both arguments are provided, showing proper usage.
if [ $# -ne 2 ]; then
	echo "Incorrect usage of the script. Usage: $0 inputFile outputFile"
	exit 1
fi

# Will rewrite an existing output file with the same name
rm -f "$outputFile"

echo "Converting $inputFile -> $outputFile..."

echo "Timestamp,Latitude,Longitude,MinSeaLevelPressure,MaxIntensity" >> "$outputFile"

# Opens the .kml file
exec 3< "$inputFile"

# Read each line of the file, not treating \ as escape characters
# Finds the information inside each tag, and removes the tags
# Then for each line, it will append to the file until the end of file is reached
# IF statements sequential to the layout of the .kml file (truncation / formatting errors otherwise)
while read -r line <&3; do
	if [[ $line == *"<lat>"* ]]; then
		lat=$(echo $line | sed -n 's/.*<lat>\(.*\)<\/lat>.*/\1/p')
	elif [[ $line == *"<lon>"* ]]; then
		lon=$(echo $line | sed -n 's/.*<lon>\(.*\)<\/lon>.*/\1/p')
	elif [[ $line == *"<intensity>"* ]]; then
		intensity=$(echo $line | sed -n 's/.*<intensity>\(.*\)<\/intensity>.*/\1/p')
	elif [[ $line == *"<minSeaLevelPres>"* ]]; then
		minSeaLevelPres=$(echo $line | sed -n 's/.*<minSeaLevelPres>\(.*\)<\/minSeaLevelPres>.*/\1/p')
	elif [[ $line == *"<dtg>"* ]]; then
    		dtg=$(echo $line | sed -n 's/.*<dtg>\(.*\)<\/dtg>.*/\1/p')
	echo "$dtg,$lat N,$lon W,$minSeaLevelPres mb,$intensity knots" >> "$outputFile"
	fi
done

# Closes the .kml file
exec 3<&-

echo 'Done!'
