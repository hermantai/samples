curl -X PUT \
  --data-urlencode "author=peter pan" \
  --data-urlencode "rating=1" \
  --data-urlencode 'reviewText=This is a very awful place' \
  http://localhost:3000/api/locations/your-locationid-here/reviews/your-reviewid-here
