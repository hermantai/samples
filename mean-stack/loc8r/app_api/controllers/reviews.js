var mongoose = require('mongoose');
var Loc = mongoose.model('Location');
// https://www.npmjs.com/package/http-status-codes
var httpStatusCodes = require('http-status-codes');
var _ = require('underscore');

var sendJsonResponse = function(res, status, content) {
  res.status(status);
  res.json(content);
};

module.exports.reviewsCreate = function (req, res) {
  var locationid = req.params.locationid;
  if (!locationid) {
    sendJsonResponse(
      res,
      httpStatusCodes.BAD_REQUEST,
      {"message": "locationid required"}
    );
    return;
  }

  Loc.findById(locationid)
    .select("reviews")
    .exec(
      function(err, location) {
        if (err) {
          sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        }
        doAddReview(req, res, location);
      }
    );
};

var doAddReview = function(req, res, location) {
  if (!location) {
    sendJsonResponse(
      res,
      httpStatusCodes.NOT_FOUND,
      {"message": "location not found: " + req.params.locationid}
    );
    return;
  }

  location.reviews.push(
    {
      author: req.body.author,
      rating: req.body.rating,
      reviewText: req.body.reviewText,
    }
  );

  location.save(function(err, location) {
    var thisReview;
    if (err) {
      sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
      return;
    }

    updateAverageRating(location._id);
    thisReview = location.reviews[location.reviews.length - 1];
    sendJsonResponse(res, httpStatusCodes.CREATED, thisReview);
  });
}

var updateAverageRating = function(locationid) {
  Loc
    .findById(locationid)
    .select('rating reviews')
    .exec(
      function(err, location) {
        if (!err) {
          doSetAverageRating(location);
        }
      }
    );
};

var doSetAverageRating = function(location) {
  var i, reviewCount, ratingAverage, ratingTotal;
  if (location.reviews && location.reviews.length > 0) {
    reviewCount = location.reviews.length;
    ratingTotal = _.reduce(
      location.reviews,
      function(memo, review) {
        return memo + review.rating;
      },
      0
    );
    ratingAverage = parseInt(ratingTotal / reviewCount, 10);
    location.rating = ratingAverage;
    location.save(function(err) {
      if (err) {
        console.log(
          `Error when updating rating average`
          + ` for ${location._id} to ${ratingAverage}: ${err}`
        );
      } else {
        console.log(
          `Average rating for location ${location._id} updated to ${ratingAverage}`);
      }
    });
  }
}

module.exports.reviewsReadOne = function (req, res) {
  if (req.params && req.params.locationid && req.params.reviewid) {
    Loc.findById(req.params.locationid)
      .select("name reviews")
      .exec(function(err, location) {
        var response, review;
        if (err) {
          sendJsonResponse(res, 400, err);
          return;
        } else if (!location) {
          sendJsonResponse(
            res,
            404,
            {'message': "location not found for " + req.params.locationid}
          );
          return;
        }
        if (location.reviews && location.reviews.length > 0) {
          review = location.reviews.id(req.params.reviewid);
          if (!review) {
            sendJsonResponse(
              res,
              404,
              {'message': "review not found for " + req.params.reviewid}
            );
          } else {
            response = {
              location : {
                name : location.name,
                id : req.params.locationid
              },
              review : review
            };
            sendJsonResponse(res, 200, response);
          }
        } else {
          sendJsonResponse(res, 404, {
            'message': "No reviews found for location: " + location._id
          });
        }
      });
  } else {
    sendJsonResponse(
      res,
      404,
      {
        'message': "Not found, locationid and reviewid are both required"
      }
    );
  }
};
module.exports.reviewsUpdateOne = function (req, res) {
  sendJsonResponse(res, 200, {"status": "success"});
};
module.exports.reviewsDeleteOne = function (req, res) {
  sendJsonResponse(res, 200, {"status": "success"});
};
