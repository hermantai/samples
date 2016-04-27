curl -X POST \
  --data-urlencode "author=peter pan" \
  --data-urlencode "rating=5" \
  --data-urlencode 'reviewText=This is a very great place' \
  http://localhost:3000/api/locations/your-locationid-here/reviews
