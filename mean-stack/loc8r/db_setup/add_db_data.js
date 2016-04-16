db.locations.save({
  "name": "Starcups",
  "address": "125 High Street, Reading, RG6 1PS",
  "rating": 3.0,
  "facilities": [
    "Hot drinks",
    "Food",
    "Premium wifi"
  ],
  "coords": [
    -0.9690884,
    51.455041
  ],
  "openingTimes": [
    {
      "days": "Monday - Friday",
      "opening": "7:00am",
      "closing": "7:00pm",
      "closed": false
    },
    {
      "days": "Saturday",
      "opening": "8:00am",
      "closing": "5:00pm",
      "closed": false
    },
    {
      "days": "Sunday",
      "closed": true
    }
  ],
  "reviews": [
    {
      "author": "Simon Holmes",
      "id": ObjectId(),
      "rating": 5.0,
      "timestamp": new Date("Jul 16, 2013"),
      "reviewText": "What a great place. I can't say enough good things about it"
    }
  ]
});
db.locations.save({
  "name": "Cafe Hero",
  "address": "125 High Street, Reading, RG6 1PS",
  "rating": 4.0,
  "facilities": [
    "Hot drinks",
    "Food",
    "Premium wifi"
  ],
  "coords": [
    -0.9690884,
    51.455041
  ],
  "openingTimes": [
    {
      "days": "Monday - Friday",
      "opening": "7:00am",
      "closing": "7:00pm",
      "closed": false
    },
    {
      "days": "Saturday",
      "opening": "8:00am",
      "closing": "5:00pm",
      "closed": false
    },
    {
      "days": "Sunday",
      "closed": true
    }
  ],
  "reviews": [
    {
      "author": "Herman Tai",
      "id": ObjectId(),
      "rating": 5.0,
      "timestamp": new Date("Jan 1, 2015"),
      "reviewText": "Great one!"
    }
  ]
});
db.locations.save({
  "name": "Burger Queen",
  "address": "125 High Street, Reading, RG6 1PS",
  "rating": 4.0,
  "facilities": [
    "Food",
    "Premium wifi"
  ],
  "coords": [
    -0.9690884,
    51.455041
  ],
  "openingTimes": [
    {
      "days": "Monday - Friday",
      "opening": "7:00am",
      "closing": "7:00pm",
      "closed": false
    },
    {
      "days": "Saturday",
      "opening": "8:00am",
      "closing": "5:00pm",
      "closed": false
    },
    {
      "days": "Sunday",
      "closed": true
    }
  ],
  "reviews": [
    {
      "author": "Peter Pan",
      "id": ObjectId(),
      "rating": 1.0,
      "timestamp": new Date("Mar 1, 2015"),
      "reviewText": "Meh~~~"
    }
  ]
});
