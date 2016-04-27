var mongoose = require('mongoose');
var util = require('util');  // util.inspect
// https://www.npmjs.com/package/http-status-codes
var httpStatusCodes = require('http-status-codes');
var _ = require('underscore');

var Loc = mongoose.model('Location');
var responseutil = require('./responseutil');

module.exports.reviewsCreate = function (req, res) {
  var locationid = req.params.locationid;
  if (!locationid) {
    responseutil.sendJsonResponse(
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
          responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        }
        doAddReview(req, res, location);
      }
    );
};

var doAddReview = function(req, res, location) {
  if (!location) {
    responseutil.sendLocationNotFound(
      res,
      req.params.locationid
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
      responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
      return;
    }

    updateAverageRating(location._id);
    thisReview = location.reviews[location.reviews.length - 1];
    responseutil.sendJsonResponse(res, httpStatusCodes.CREATED, thisReview);
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
          responseutil.sendJsonResponse(res, 400, err);
          return;
        } else if (!location) {
          responseutil.sendJsonResponse(
            res,
            404,
            {'message': "location not found for " + req.params.locationid}
          );
          return;
        }
        if (location.reviews && location.reviews.length > 0) {
          review = location.reviews.id(req.params.reviewid);
          if (!review) {
            responseutil.sendJsonResponse(
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
            responseutil.sendJsonResponse(res, 200, response);
          }
        } else {
          responseutil.sendJsonResponse(res, 404, {
            'message': "No reviews found for location: " + location._id
          });
        }
      });
  } else {
    responseutil.sendJsonResponse(
      res,
      404,
      {
        'message': "Not found, locationid and reviewid are both required"
      }
    );
  }
};

module.exports.reviewsUpdateOne = function (req, res) {
  var locationid = req.params.locationid;
  var reviewid = req.params.reviewid;
  if (!locationid) {
    responseutil.sendJsonResponse(
        res,
        httpStatusCodes.BAD_REQUEST,
        "locationid is required"
    );
    return;
  }
  if (!reviewid) {
    responseutil.sendJsonResponse(
        res,
        httpStatusCodes.BAD_REQUEST,
        "reviewid is required"
    );
    return;
  }

  Loc.findById(locationid)
    .select("reviews")
    .exec(function (err, location) {
      var thisReview;
      if (err) {
        responseutil.sendJsonResponse(
          res,
          httpStatusCodes.BAD_REQUEST,
          err
        );
        return;
      }

      if (!location) {
        responseutil.sendLocationNotFound(res, locationid);
        return;
      }

      if (!location.reviews) {
        responseutil.sendReviewNotFound(res, locationid, reviewid);
        return;
      }

      thisReview = location.reviews.id(reviewid);
      if (!thisReview) {
        responseutil.sendReviewNotFound(
            res,
            locationid,
            reviewid
        );
        return;
      }

      thisReview.author = req.body.author;
      thisReview.rating = req.body.rating;
      thisReview.reviewText = req.body.reviewText;
      location.save(function(err, location) {
        if (err) {
          responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        }
        updateAverageRating(location._id);
      });
      responseutil.sendJsonResponse(res, httpStatusCodes.OK, thisReview);
  });
};

module.exports.reviewsDeleteOne = function (req, res) {
  responseutil.sendJsonResponse(res, 200, {"status": "success"});
};
