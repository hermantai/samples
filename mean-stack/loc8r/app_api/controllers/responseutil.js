// https://www.npmjs.com/package/http-status-codes
var httpStatusCodes = require('http-status-codes');

var sendNoLocationId = function(res) {
  sendJsonResponse(
    res,
    httpStatusCodes.BAD_REQUEST,
    {'message': "locationid is required"}
  );
};

var sendNoReviewId = function(res) {
  sendJsonResponse(
    res,
    httpStatusCodes.BAD_REQUEST,
    {'message': "reviewid is required"}
  );
};

var sendLocationNotFound = function(res, locationid) {
  sendJsonResponse(
    res,
    httpStatusCodes.NOT_FOUND,
    {'message': "location not found for " + locationid}
  );
}

var sendReviewNotFound = function(res, locationid, reviewid) {
  sendJsonResponse(
    res,
    httpStatusCodes.NOT_FOUND,
    {
      'message':
        `review not found for review id ${reviewid} in location ${locationid}`
    }
  );
}

var sendJsonResponse = function(res, status, content) {
  res.status(status);
  res.json(content);
};

module.exports.sendLocationNotFound = sendLocationNotFound;
module.exports.sendJsonResponse = sendJsonResponse;
module.exports.sendReviewNotFound = sendReviewNotFound;
