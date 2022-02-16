// Scheduled to run every 10 minutes
// Trigger: https://pasteboard.co/TkmbHRWei6TV.png

const EMAIL_ADDRESS = "abc@gmail.com";
const COLUMN_INDEX_SYMBOL = 0;
const COLUMN_INDEX_PRICE = 1;
const COLUMN_INDEX_CHANGE = 2;
const COLUMN_INDEX_ALERT_PRICE = 8;
const COLUMN_INDEX_ALERT_PRICE_SENT = 9;
const COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_DROP = 10;
const COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_DROP_SENT = 11;
const COLUMN_INDEX_ALERT_PRICE_ABOVE = 12;
const COLUMN_INDEX_ALERT_PRICE_ABOVE_SENT = 13;
const COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_INCREASE = 14;
const COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_INCREASE_SENT = 15;

const GRAPH_URL_FORMAT= "https://finance.yahoo.com/chart/%s#eyJpbnRlcnZhbCI6ImRheSIsInBlcmlvZGljaXR5IjoxLCJ0aW1lVW5pdCI6bnVsbCwiY2FuZGxlV2lkdGgiOjEwLjk2Mzk2Mzk2Mzk2Mzk2NCwidm9sdW1lVW5kZXJsYXkiOnRydWUsImFkaiI6dHJ1ZSwiY3Jvc3NoYWlyIjp0cnVlLCJjaGFydFR5cGUiOiJsaW5lIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuNTEyLCJkaXNwbGF5IjoiQkFCQSIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwiaW5kZXgiOjAsInlBeGlzIjp7Im5hbWUiOiJjaGFydCIsInBvc2l0aW9uIjpudWxsfSwieWF4aXNMSFMiOltdLCJ5YXhpc1JIUyI6WyJjaGFydCIsInZvbCB1bmRyIl19LCLigIxyc2nigIwgKDE0LEMpIjp7InBlcmNlbnQiOjAuMTI4LCJkaXNwbGF5Ijoi4oCMcnNp4oCMICgxNCxDKSIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwiaW5kZXgiOjEsInlBeGlzIjp7Im5hbWUiOiLigIxyc2nigIwgKDE0LEMpIiwicG9zaXRpb24iOm51bGx9LCJ5YXhpc0xIUyI6W10sInlheGlzUkhTIjpbIuKAjHJzaeKAjCAoMTQsQykiXX0sIuKAjG1hY2TigIwgKDEyLDI2LDkpIjp7InBlcmNlbnQiOjAuMTU5OTk5OTk5OTk5OTk5OTgsImRpc3BsYXkiOiLigIxtYWNk4oCMICgxMiwyNiw5KSIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwiaW5kZXgiOjIsInlBeGlzIjp7Im5hbWUiOiLigIxtYWNk4oCMICgxMiwyNiw5KSIsInBvc2l0aW9uIjpudWxsfSwieWF4aXNMSFMiOltdLCJ5YXhpc1JIUyI6WyLigIxtYWNk4oCMICgxMiwyNiw5KSJdfSwi4oCMc3RvY2hhc3RpY3PigIwgKDE0LEMseSkiOnsicGVyY2VudCI6MC4yLCJkaXNwbGF5Ijoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LEMseSkiLCJjaGFydE5hbWUiOiJjaGFydCIsImluZGV4IjozLCJ5QXhpcyI6eyJuYW1lIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LEMseSkiLCJwb3NpdGlvbiI6bnVsbH0sInlheGlzTEhTIjpbXSwieWF4aXNSSFMiOlsi4oCMc3RvY2hhc3RpY3PigIwgKDE0LEMseSkiXX19LCJzZXRTcGFuIjpudWxsLCJsaW5lV2lkdGgiOjIsInN0cmlwZWRCYWNrZ3JvdW5kIjp0cnVlLCJldmVudHMiOnRydWUsImNvbG9yIjoiIzAwODFmMiIsInN0cmlwZWRCYWNrZ3JvdWQiOnRydWUsInJhbmdlIjpudWxsLCJldmVudE1hcCI6eyJjb3Jwb3JhdGUiOnsiZGl2cyI6dHJ1ZSwic3BsaXRzIjp0cnVlfSwic2lnRGV2Ijp7fX0sImN1c3RvbVJhbmdlIjpudWxsLCJzeW1ib2xzIjpbeyJzeW1ib2wiOiJCQUJBIiwic3ltYm9sT2JqZWN0Ijp7InN5bWJvbCI6IkJBQkEiLCJxdW90ZVR5cGUiOiJFUVVJVFkiLCJleGNoYW5nZVRpbWVab25lIjoiQW1lcmljYS9OZXdfWW9yayJ9LCJwZXJpb2RpY2l0eSI6MSwiaW50ZXJ2YWwiOiJkYXkiLCJ0aW1lVW5pdCI6bnVsbCwic2V0U3BhbiI6bnVsbH1dLCJzdHVkaWVzIjp7InZvbCB1bmRyIjp7InR5cGUiOiJ2b2wgdW5kciIsImlucHV0cyI6eyJpZCI6InZvbCB1bmRyIiwiZGlzcGxheSI6InZvbCB1bmRyIn0sIm91dHB1dHMiOnsiVXAgVm9sdW1lIjoiIzAwYjA2MSIsIkRvd24gVm9sdW1lIjoiI0ZGMzMzQSJ9LCJwYW5lbCI6ImNoYXJ0IiwicGFyYW1ldGVycyI6eyJ3aWR0aEZhY3RvciI6MC40NSwiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDEwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6IjEwIiwiRmllbGQiOiJDbG9zZSIsIlR5cGUiOiJzaW1wbGUiLCJPZmZzZXQiOjAsImlkIjoi4oCMbWHigIwgKDEwLEMsbWEsMCkiLCJkaXNwbGF5Ijoi4oCMbWHigIwgKDEwLEMsbWEsMCkifSwib3V0cHV0cyI6eyJNQSI6IiNmZjMzM2EifSwicGFuZWwiOiJjaGFydCIsInBhcmFtZXRlcnMiOnsiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDIwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6IjIwIiwiRmllbGQiOiJDbG9zZSIsIlR5cGUiOiJzaW1wbGUiLCJPZmZzZXQiOjAsImlkIjoi4oCMbWHigIwgKDIwLEMsbWEsMCkiLCJkaXNwbGF5Ijoi4oCMbWHigIwgKDIwLEMsbWEsMCkifSwib3V0cHV0cyI6eyJNQSI6IiNmZmEzM2YifSwicGFuZWwiOiJjaGFydCIsInBhcmFtZXRlcnMiOnsiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDUwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6NTAsIkZpZWxkIjoiQ2xvc2UiLCJUeXBlIjoic2ltcGxlIiwiT2Zmc2V0IjowLCJpZCI6IuKAjG1h4oCMICg1MCxDLG1hLDApIiwiZGlzcGxheSI6IuKAjG1h4oCMICg1MCxDLG1hLDApIn0sIm91dHB1dHMiOnsiTUEiOiIjZmZkYjQ4In0sInBhbmVsIjoiY2hhcnQiLCJwYXJhbWV0ZXJzIjp7ImNoYXJ0TmFtZSI6ImNoYXJ0IiwicGFuZWxOYW1lIjoiY2hhcnQifX0sIuKAjHJzaeKAjCAoMTQsQykiOnsidHlwZSI6InJzaSIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJGaWVsZCI6IkNsb3NlIiwiaWQiOiLigIxyc2nigIwgKDE0LEMpIiwiZGlzcGxheSI6IuKAjHJzaeKAjCAoMTQsQykifSwib3V0cHV0cyI6eyJSU0kiOiIjYWQ2ZWZmIn0sInBhbmVsIjoi4oCMcnNp4oCMICgxNCxDKSIsInBhcmFtZXRlcnMiOnsic3R1ZHlPdmVyWm9uZXNFbmFibGVkIjp0cnVlLCJzdHVkeU92ZXJCb3VnaHRWYWx1ZSI6ODAsInN0dWR5T3ZlckJvdWdodENvbG9yIjoiIzc5ZjRiZCIsInN0dWR5T3ZlclNvbGRWYWx1ZSI6MjAsInN0dWR5T3ZlclNvbGRDb2xvciI6IiNmZjgwODQiLCJjaGFydE5hbWUiOiJjaGFydCIsInBhbmVsTmFtZSI6IuKAjHJzaeKAjCAoMTQsQykifX0sIuKAjG1hY2TigIwgKDEyLDI2LDkpIjp7InR5cGUiOiJtYWNkIiwiaW5wdXRzIjp7IkZhc3QgTUEgUGVyaW9kIjoxMiwiU2xvdyBNQSBQZXJpb2QiOjI2LCJTaWduYWwgUGVyaW9kIjo5LCJpZCI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIiwiZGlzcGxheSI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIn0sIm91dHB1dHMiOnsiTUFDRCI6IiNhZDZlZmYiLCJTaWduYWwiOiIjZmZhMzNmIiwiSW5jcmVhc2luZyBCYXIiOiIjNzlmNGJkIiwiRGVjcmVhc2luZyBCYXIiOiIjZmY4MDg0In0sInBhbmVsIjoi4oCMbWFjZOKAjCAoMTIsMjYsOSkiLCJwYXJhbWV0ZXJzIjp7ImNoYXJ0TmFtZSI6ImNoYXJ0IiwicGFuZWxOYW1lIjoi4oCMbWFjZOKAjCAoMTIsMjYsOSkifX0sIuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIjp7InR5cGUiOiJzdG9jaGFzdGljcyIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJGaWVsZCI6IkNsb3NlIiwiU21vb3RoIjp0cnVlLCJpZCI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIiwiZGlzcGxheSI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIn0sIm91dHB1dHMiOnsiRmFzdCI6IiMwMDAwMDAiLCJTbG93IjoiI0ZGMDAwMCJ9LCJwYW5lbCI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIiwicGFyYW1ldGVycyI6eyJzdHVkeU92ZXJab25lc0VuYWJsZWQiOnRydWUsInN0dWR5T3ZlckJvdWdodFZhbHVlIjo4MCwic3R1ZHlPdmVyQm91Z2h0Q29sb3IiOiIjMDAwMDAwIiwic3R1ZHlPdmVyU29sZFZhbHVlIjoyMCwic3R1ZHlPdmVyU29sZENvbG9yIjoiIzAwMDAwMCIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwicGFuZWxOYW1lIjoi4oCMc3RvY2hhc3RpY3PigIwgKDE0LEMseSkifX0sIuKAjG1h4oCMICgyMDAsQyxtYSwwKSI6eyJ0eXBlIjoibWEiLCJpbnB1dHMiOnsiUGVyaW9kIjoiMjAwIiwiRmllbGQiOiJDbG9zZSIsIlR5cGUiOiJzaW1wbGUiLCJPZmZzZXQiOjAsImlkIjoi4oCMbWHigIwgKDIwMCxDLG1hLDApIiwiZGlzcGxheSI6IuKAjG1h4oCMICgyMDAsQyxtYSwwKSJ9LCJvdXRwdXRzIjp7Ik1BIjoiIzAwYjA2MSJ9LCJwYW5lbCI6ImNoYXJ0IiwicGFyYW1ldGVycyI6eyJjaGFydE5hbWUiOiJjaGFydCJ9fX19";
const ORIGINAL_SPREADSHEET_URL = 'https://docs.google.com/spreadsheets/d/abcdefg';
const STOCK_INFO_URL_TEMPLATE = "https://finance.yahoo.com/quote/%s";

function priceAlerts() {
  var today = new Date();
  Logger.log("crossoverAlerts starts running at " + today.toLocaleString() + ".")
  if (today.getDay() == 6) {
    Logger.log("Not running this script on Saturdays.");
    return;
  }

  var signalMessages = "";
  var symbolsWithTriggers = [];

  var sheetNames = ["stocks"]
  for (var si = 0; si < sheetNames.length; si++) {
    var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetNames[si]);
    var rangeData = sheet.getDataRange()
    var rows = rangeData.getValues(); 
  
    // Start on row one (0-based) because the first row is a header row.
    for (var r = 1; r < rows.length; r++) {
      var row = rows[r];

      var symbol = row[COLUMN_INDEX_SYMBOL];
      Logger.log("Process stock: " + symbol);
      if (symbol == '') {
        Logger.log("Exit at row " + (r + 1))
        break;
      }

      var price = row[COLUMN_INDEX_PRICE];
      var triggerSent = false;

      var alertPrice = row[COLUMN_INDEX_ALERT_PRICE];
      if (alertPrice != '' && price < alertPrice) {
        if (row[COLUMN_INDEX_ALERT_PRICE_SENT] != "") {
          Logger.log(`No price below alert for ${symbol} because an alert has already been sent`);
        } else {
          var signalMessage = `${symbol}'s price is ${price} and is below ${alertPrice}`;
          signalMessage += " " + getAdditionalMoreInfo(symbol);
          signalMessage = `<p>${signalMessage}</p>\n`;
          signalMessages += signalMessage;

          symbolsWithTriggers.push(symbol);
          triggerSent = true;

          // getRange is 1-based
          sheet.getRange(r+1, COLUMN_INDEX_ALERT_PRICE_SENT+1).setValue(today.toLocaleString());
        }
      }
      
      var priceChange = parseFloat(row[COLUMN_INDEX_CHANGE]);
      var priceChangeRatio = (-1*priceChange / price).toFixed(4);
      var priceChangeRatioThreshold = row[COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_DROP];

      if (priceChangeRatioThreshold != '' && priceChange < 0 && priceChangeRatio > priceChangeRatioThreshold) {
        if (row[COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_DROP_SENT] != "") {
          Logger.log(`No price drop alert for ${symbol} because an alert has already been sent`);
        } else {
          var priceChangePercentage = (priceChangeRatio * 100).toFixed(4) + '%';
          var threshold = priceChangeRatioThreshold * 100 + '%';
          var signalMessage = `${symbol}'s price is ${price} and dropped ${priceChangePercentage}, more than ${threshold}`;
          signalMessage += " " + getAdditionalMoreInfo(symbol);
          signalMessage = `<p>${signalMessage}</p>\n`;
          signalMessages += signalMessage;

          if (!triggerSent) {
            symbolsWithTriggers.push(symbol);
            triggerSent = true;
          }

          // getRange is 1-based
          sheet.getRange(r+1, COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_DROP_SENT+1).setValue(today.toLocaleString());
        }
      }

      var alertPriceAbove = row[COLUMN_INDEX_ALERT_PRICE_ABOVE];
      if (alertPriceAbove != '' && price > alertPriceAbove) {
        if (row[COLUMN_INDEX_ALERT_PRICE_ABOVE_SENT] != "") {
          Logger.log(`No price above alert for ${symbol} because an alert has already been sent`);
        } else {
          var signalMessage = `${symbol}'s price is ${price} and is above ${alertPriceAbove}`;
          signalMessage += " " + getAdditionalMoreInfo(symbol);
          signalMessage = `<p>${signalMessage}</p>\n`;
          signalMessages += signalMessage;

          symbolsWithTriggers.push(symbol);
          triggerSent = true;

          // getRange is 1-based
          sheet.getRange(r+1, COLUMN_INDEX_ALERT_PRICE_ABOVE_SENT+1).setValue(today.toLocaleString());
        }
      }
      
      var priceChangeIncreaseRatio = (priceChange / price).toFixed(4);
      var priceChangeIncreaseRatioThreshold = row[COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_INCREASE];

      if (priceChangeIncreaseRatioThreshold != '' && priceChange > 0 && priceChangeIncreaseRatio > priceChangeIncreaseRatioThreshold) {
        if (row[COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_INCREASE_SENT] != "") {
          Logger.log(`No price increase alert for ${symbol} because an alert has already been sent`);
        } else {
          var priceChangePercentage = priceChangeIncreaseRatio * 100 + '%';
          var threshold = priceChangeIncreaseRatioThreshold * 100 + '%';
          var signalMessage = `${symbol}'s price is ${price} and increased ${priceChangePercentage}, more than ${threshold}`;
          signalMessage += " " + getAdditionalMoreInfo(symbol);
          signalMessage = `<p>${signalMessage}</p>\n`;
          signalMessages += signalMessage;

          if (!triggerSent) {
            symbolsWithTriggers.push(symbol);
            triggerSent = true;
          }

          // getRange is 1-based
          sheet.getRange(r+1, COLUMN_INDEX_ALERT_PRICE_PERCENTAGE_INCREASE_SENT+1).setValue(today.toLocaleString());
        }
      }
    }
  }  

  if (symbolsWithTriggers.length != 0) {
    signalMessages += "\n<p>Check out the original <a href=\"%s\">Spreadsheet</a></p>\n".replace('%s', ORIGINAL_SPREADSHEET_URL);
    Logger.log("Trigger alerts: \n" + signalMessages);
    var subject = "Price alerts for " + symbolsWithTriggers.join(", ");
    sendEmail(EMAIL_ADDRESS, subject, signalMessages);
  } else {
    Logger.log("No alerts");
  }
}

function getAdditionalMoreInfo(symbol) {
  var signalMessage = "[<a href=\"" + STOCK_INFO_URL_TEMPLATE.replace("%s", symbol) + "\">info</a>]";
  signalMessage += " [<a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>]";
  return signalMessage;
}

function sendEmail(toAddress, subject, htmlBody) {
  // // Send plain text email
  MailApp.sendEmail({
    to: toAddress,
    subject: subject,
    htmlBody: htmlBody,
  });
}

function onOpen(event) {
  var ui = SpreadsheetApp.getUi();
  // Or DocumentApp or FormApp.
  ui.createMenu('Custom Funcs')
      .addItem('Run price alerts', 'priceAlerts')
      .addToUi();
}
