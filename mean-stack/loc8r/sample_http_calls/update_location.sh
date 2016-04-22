curl -X PUT --data-urlencode "name=Costy2" \
  --data-urlencode "address=High Street, Reading" \
  --data-urlencode "lng=-0.9630884" \
  --data-urlencode "lat=51.451041" \
  --data-urlencode "facilities=hot drink,food,restroom" \
  --data-urlencode "days1=Monday - Friday" \
  --data-urlencode "opening1=6:00am" \
  --data-urlencode "closing1=8:00pm" \
  --data-urlencode "closed1=false" \
  --data-urlencode "days2=Saturday" \
  --data-urlencode "opening2=10:00am" \
  --data-urlencode "closing2=4:00pm" \
  --data-urlencode "closed2=false" \
  localhost:3000/api/locations/put-a-valid-locationid-here
