curl -X POST --data-urlencode "name=Costy" \
  --data-urlencode "address=High Street, Reading" \
  --data-urlencode "lng=-0.9630884" \
  --data-urlencode "lat=51.451041" \
  --data-urlencode "facilities=hot drink, food" \
  --data-urlencode "days1=Monday - Friday" \
  --data-urlencode "opening1=8:00am" \
  --data-urlencode "closing1=5:00pm" \
  --data-urlencode "closed1=false" \
  --data-urlencode "days2=Saturday" \
  --data-urlencode "opening2=9:00am" \
  --data-urlencode "closing2=4:00pm" \
  --data-urlencode "closed2=false" \
  localhost:3000/api/locations
