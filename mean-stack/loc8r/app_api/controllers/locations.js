var mongoose = require('mongoose');
var util = require('util');
// https://www.npmjs.com/package/http-status-codes
var httpStatusCodes = require('http-status-codes');

var Loc = mongoose.model('Location');

var theEarth = (function(){
  var earthRadius = 6371; // km, miles is 3959
  var getDistanceFromRads = function(rads) {
    return parseFloat(rads * earthRadius);
  };
  var getRadsFromDistance = function(distance) {
    return parseFloat(distance / earthRadius);
  }

  return {
    getDistanceFromRads: getDistanceFromRads,
    getRadsFromDistance: getRadsFromDistance
  };
})();

var sendJsonResponse = function(res, status, content) {
  res.status(status);
  res.json(content);
};

module.exports.locationsListByDistance = function (req, res) {
  var lng = parseFloat(req.query.lng);
  var lat = parseFloat(req.query.lat);
  if (isNaN(lng) || isNaN(lat)) {
    sendJsonResponse(
      res,
      httpStatusCodes.BAD_REQUEST,
      {
        message: "lng and lat query parameters are required and have to be numbers"
      }
    );
    return;
  }
  var point = {
    type: "Point",
    coordinates: [lng, lat]
  };
  var geoOptions = {
    spherical: true,
    maxDistance: 20, // theEarth.getRadsFromDistance(2000),
    num: 10
  };

  Loc.geoNear(
      point,
      geoOptions,
      function (err, results, stats) {
        var locations = [];
        if (err) {
          sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        } else {
          results.forEach(function(doc) {
          locations.push(
            {
              // the distance is in meters
              distance: doc.dis,  // theEarth.getDistanceFromRads(doc.dis)
              name: doc.obj.name,
              address: doc.obj.address,
              rating: doc.obj.rating,
              facilities: doc.obj.facilities,
              coords: doc.obj.coords,
              _id: doc.obj._id
            }
          );
        });
        sendJsonResponse(res, httpStatusCodes.OK, locations);
      }
    }
  );
};
module.exports.locationsCreate = function (req, res) {
  sendJsonResponse(res, httpStatusCodes.OK, {"status": "success"});
};
module.exports.locationsReadOne = function (req, res) {
  if (req.params && req.params.locationid) {
    Loc.findById(req.params.locationid)
      .exec(function(err, location) {
        if (err) {
          sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        } else if (!location) {
          sendJsonResponse(
            res,
            httpStatusCodes.NOT_FOUND,
            {'message': "location not found for " + req.params.locationid}
          );
          return;
        }
        sendJsonResponse(res, httpStatusCodes.OK, location);
      });
  } else {
    sendJsonResponse(
      res,
      httpStatusCodes.BAD_REQUEST,
      {
        'message': "No locationid in request"
      }
    );
  }
};
module.exports.locationsUpdateOne = function (req, res) {
  sendJsonResponse(res, httpStatusCodes.OK, {"status": "success"});
};
module.exports.locationsDeleteOne = function (req, res) {
  sendJsonResponse(res, httpStatusCodes.OK, {"status": "success"});
};

