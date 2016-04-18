var mongoose = require('mongoose');

var openingTimeSchema = new mongoose.Schema(
  {
    days: {type: String, required: true},
    // opening and closing could be models as number of minutes from midnight
    // for easier computations
    opening: String,
    closing: String,
    closed: {type: Boolean, required: true}
  }
);

var reviewSchema = new mongoose.Schema(
  {
    author: String,
    rating: {type: Number, 'default': 0, min: 0, max: 5},
    reviewText: String,
    createdOn: {type: Date, "default": Date.now}
  }
);

var locationSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true
    },
    address: String,
    rating: {
      type: Number,
      'default': 0,
      min: 0,
      max: 5
    },
    facilities: [String],
    coords: {
      // Longtitude then latitude
      type: [Number],
      index: "2dsphere"
    },
    openiningTimes: [openingTimeSchema],
    reviews: [reviewSchema]
  }
);

// Compiling the models
// mongoose.model(name of the model, schema to use, optional mongo collection
// name)
mongoose.model('Location', locationSchema);
