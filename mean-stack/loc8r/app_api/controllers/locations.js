var mongoose = require('mongoose');
var util = require('util');  // util.inspect
// https://www.npmjs.com/package/http-status-codes
var httpStatusCodes = require('http-status-codes');

var responseutil = require('./responseutil');
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

module.exports.locationsListByDistance = function (req, res) {
  var lng = parseFloat(req.query.lng);
  var lat = parseFloat(req.query.lat);
  if (isNaN(lng) || isNaN(lat)) {
    responseutil.sendJsonResponse(
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
        responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
        return;
      }
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
      responseutil.sendJsonResponse(res, httpStatusCodes.OK, locations);
    }
  );
};

module.exports.locationsCreate = function (req, res) {
  Loc.create(
    {
      name: req.body.name,
      address: req.body.address,
      facilities: req.body.facilities
          ?  req.body.facilities.split(",")
          : [],
      coords: [parseFloat(req.body.lng), parseFloat(req.body.lat)],
      openingTimes: [
        {
          days: req.body.days1,
          opening: req.body.opening1,
          closing: req.body.closing1,
          closed: false //req.body.closed1
        },
        {
          days: req.body.days2,
          opening: req.body.opening2,
          closing: req.body.closing2,
          closed: false //req.body.closed2
        }
      ]
    },
    function(err, location) {
      if (err) {
        responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
      } else {
        responseutil.sendJsonResponse(res, httpStatusCodes.CREATED, location);
      }
    }
  );
};
module.exports.locationsReadOne = function (req, res) {
  if (req.params && req.params.locationid) {
    Loc.findById(req.params.locationid)
      .exec(function(err, location) {
        if (err) {
          responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        } else if (!location) {
          responseutil.sendLocationNotFound(res, req.params.locationid);
          return;
        }
        responseutil.sendJsonResponse(res, httpStatusCodes.OK, location);
      });
  } else {
    responseutil.sendNoLocationId(res);
  }
};
module.exports.locationsUpdateOne = function (req, res) {
  var locationid = req.params.locationid;
  if (!locationid) {
    responseutil.sendNoLocationId(res);
    return;
  }

  Loc.findById(locationid)
    .select("-reviews -rating")
    .exec(
      function(err, location) {
        if (err) {
          responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
          return;
        }
        if (!location) {
          responseutil.sendLocationNotFound(res, locationid);
          return;
        }
        location.name = req.body.name;
        location.address = req.body.address;
        location.facilities = req.body.facilities
            ?  req.body.facilities.split(",")
            : [];
        coords = [
          parseFloat(req.body.lng), parseFloat(req.body.lat)
        ];
        location.openingTimes = [
          {
            days: req.body.days1,
            opening: req.body.opening1,
            closing: req.body.closing1,
            closed: false //req.body.closed1
          },
          {
            days: req.body.days2,
            opening: req.body.opening2,
            closing: req.body.closing2,
            closed: false //req.body.closed2
          }
        ];
        location.save(
          function(err, location) {
            if (err) {
              responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
              return;
            }
            responseutil.sendJsonResponse(res, httpStatusCodes.OK, location);
          }
        );
      }
    );
};

module.exports.locationsDeleteOne = function (req, res) {
  var locationid = req.params.locationid;
  if (!locationid) {
    responseutil.sendNoLocationId(res);
    return;
  }

  Loc.findById(locationid)
    .exec(function(err, location) {
      if (err) {
        responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
        return;
      }

      if (!location) {
        responseutil.sendLocationNotFound(res, locationid);
        return;
      }

      location.remove(function(err, location) {
        responseutil.sendJsonResponse(res, httpStatusCodes.NO_CONTENT, null);
      });
    });
};

/* // a simpler way for delete with less flexibility

module.exports.locationsDeleteOne = function (req, res) {
  var locationid = req.params.locationid;
  if (!locationid) {
    responseutil.sendNoLocationId(res);
    return;
  }

  Loc.findByIdAndRemove(locationid)
    .exec(function(err, location) {
      if (err) {
        responseutil.sendJsonResponse(res, httpStatusCodes.BAD_REQUEST, err);
        return;
      }

      responseutil.sendJsonResponse(res, httpStatusCodes.NO_CONTENT, null);
    });
};
*/
