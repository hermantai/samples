const EMAIL_ADDRESS = "abc@gmail.com";
const COLUMN_INDEX_SYMBOL = 0;
const COLUMN_INDEX_PRICE = 1;
const COLUMN_INDEX_CHANGE = 2;
const COLUMN_INDEX_MV_20_DIFF = 4;
const COLUMN_INDEX_MV_20_PREV_DIFF = 5;
const COLUMN_INDEX_MV_50_DIFF = 6;
const COLUMN_INDEX_MV_50_PREV_DIFF = 7;
const COLUMN_INDEX_MV10 = 10;
const COLUMN_INDEX_MV10_ALERT_SENT = 11;
const GRAPH_URL_FORMAT= "https://finance.yahoo.com/chart/%s#eyJpbnRlcnZhbCI6ImRheSIsInBlcmlvZGljaXR5IjoxLCJ0aW1lVW5pdCI6bnVsbCwiY2FuZGxlV2lkdGgiOjEwLjk2Mzk2Mzk2Mzk2Mzk2NCwidm9sdW1lVW5kZXJsYXkiOnRydWUsImFkaiI6dHJ1ZSwiY3Jvc3NoYWlyIjp0cnVlLCJjaGFydFR5cGUiOiJsaW5lIiwiZXh0ZW5kZWQiOmZhbHNlLCJtYXJrZXRTZXNzaW9ucyI6e30sImFnZ3JlZ2F0aW9uVHlwZSI6Im9obGMiLCJjaGFydFNjYWxlIjoibGluZWFyIiwicGFuZWxzIjp7ImNoYXJ0Ijp7InBlcmNlbnQiOjAuNTEyLCJkaXNwbGF5IjoiREJYIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJpbmRleCI6MCwieUF4aXMiOnsibmFtZSI6ImNoYXJ0IiwicG9zaXRpb24iOm51bGx9LCJ5YXhpc0xIUyI6W10sInlheGlzUkhTIjpbImNoYXJ0Iiwidm9sIHVuZHIiXX0sIuKAjHJzaeKAjCAoMTQsQykiOnsicGVyY2VudCI6MC4xMjgsImRpc3BsYXkiOiLigIxyc2nigIwgKDE0LEMpIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJpbmRleCI6MSwieUF4aXMiOnsibmFtZSI6IuKAjHJzaeKAjCAoMTQsQykiLCJwb3NpdGlvbiI6bnVsbH0sInlheGlzTEhTIjpbXSwieWF4aXNSSFMiOlsi4oCMcnNp4oCMICgxNCxDKSJdfSwi4oCMbWFjZOKAjCAoMTIsMjYsOSkiOnsicGVyY2VudCI6MC4xNTk5OTk5OTk5OTk5OTk5OCwiZGlzcGxheSI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIiwiY2hhcnROYW1lIjoiY2hhcnQiLCJpbmRleCI6MiwieUF4aXMiOnsibmFtZSI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIiwicG9zaXRpb24iOm51bGx9LCJ5YXhpc0xIUyI6W10sInlheGlzUkhTIjpbIuKAjG1hY2TigIwgKDEyLDI2LDkpIl19LCLigIxzdG9jaGFzdGljc.KAjCAoMTQsQyx5KSI6eyJwZXJjZW50IjowLjIsImRpc3BsYXkiOiLigIxzdG9jaGFzdGljc.KAjCAoMTQsQyx5KSIsImNoYXJ0TmFtZSI6ImNoYXJ0IiwiaW5kZXgiOjMsInlBeGlzIjp7Im5hbWUiOiLigIxzdG9jaGFzdGljc.KAjCAoMTQsQyx5KSIsInBvc2l0aW9uIjpudWxsfSwieWF4aXNMSFMiOltdLCJ5YXhpc1JIUyI6WyLigIxzdG9jaGFzdGljc.KAjCAoMTQsQyx5KSJdfX0sInNldFNwYW4iOm51bGwsImxpbmVXaWR0aCI6Miwic3RyaXBlZEJhY2tncm91bmQiOnRydWUsImV2ZW50cyI6dHJ1ZSwiY29sb3IiOiIjMDA4MWYyIiwic3RyaXBlZEJhY2tncm91ZCI6dHJ1ZSwicmFuZ2UiOm51bGwsImV2ZW50TWFwIjp7ImNvcnBvcmF0ZSI6eyJkaXZzIjp0cnVlLCJzcGxpdHMiOnRydWV9LCJzaWdEZXYiOnt9fSwiY3VzdG9tUmFuZ2UiOm51bGwsInN5bWJvbHMiOlt7InN5bWJvbCI6IkRCWCIsInN5bWJvbE9iamVjdCI6eyJzeW1ib2wiOiJEQlgiLCJxdW90ZVR5cGUiOiJFUVVJVFkiLCJleGNoYW5nZVRpbWVab25lIjoiQW1lcmljYS9OZXdfWW9yayJ9LCJwZXJpb2RpY2l0eSI6MSwiaW50ZXJ2YWwiOiJkYXkiLCJ0aW1lVW5pdCI6bnVsbCwic2V0U3BhbiI6bnVsbH1dLCJzdHVkaWVzIjp7InZvbCB1bmRyIjp7InR5cGUiOiJ2b2wgdW5kciIsImlucHV0cyI6eyJpZCI6InZvbCB1bmRyIiwiZGlzcGxheSI6InZvbCB1bmRyIn0sIm91dHB1dHMiOnsiVXAgVm9sdW1lIjoiIzAwYjA2MSIsIkRvd24gVm9sdW1lIjoiI0ZGMzMzQSJ9LCJwYW5lbCI6ImNoYXJ0IiwicGFyYW1ldGVycyI6eyJ3aWR0aEZhY3RvciI6MC40NSwiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDEwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6IjEwIiwiRmllbGQiOiJDbG9zZSIsIlR5cGUiOiJzaW1wbGUiLCJPZmZzZXQiOjAsImlkIjoi4oCMbWHigIwgKDEwLEMsbWEsMCkiLCJkaXNwbGF5Ijoi4oCMbWHigIwgKDEwLEMsbWEsMCkifSwib3V0cHV0cyI6eyJNQSI6IiNhZDZlZmYifSwicGFuZWwiOiJjaGFydCIsInBhcmFtZXRlcnMiOnsiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDIwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6IjIwIiwiRmllbGQiOiJDbG9zZSIsIlR5cGUiOiJzaW1wbGUiLCJPZmZzZXQiOjAsImlkIjoi4oCMbWHigIwgKDIwLEMsbWEsMCkiLCJkaXNwbGF5Ijoi4oCMbWHigIwgKDIwLEMsbWEsMCkifSwib3V0cHV0cyI6eyJNQSI6IiNmZjgwYzUifSwicGFuZWwiOiJjaGFydCIsInBhcmFtZXRlcnMiOnsiY2hhcnROYW1lIjoiY2hhcnQiLCJwYW5lbE5hbWUiOiJjaGFydCJ9fSwi4oCMbWHigIwgKDUwLEMsbWEsMCkiOnsidHlwZSI6Im1hIiwiaW5wdXRzIjp7IlBlcmlvZCI6NTAsIkZpZWxkIjoiQ2xvc2UiLCJUeXBlIjoic2ltcGxlIiwiT2Zmc2V0IjowLCJpZCI6IuKAjG1h4oCMICg1MCxDLG1hLDApIiwiZGlzcGxheSI6IuKAjG1h4oCMICg1MCxDLG1hLDApIn0sIm91dHB1dHMiOnsiTUEiOiIjNzJkM2ZmIn0sInBhbmVsIjoiY2hhcnQiLCJwYXJhbWV0ZXJzIjp7ImNoYXJ0TmFtZSI6ImNoYXJ0IiwicGFuZWxOYW1lIjoiY2hhcnQifX0sIuKAjHJzaeKAjCAoMTQsQykiOnsidHlwZSI6InJzaSIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJGaWVsZCI6IkNsb3NlIiwiaWQiOiLigIxyc2nigIwgKDE0LEMpIiwiZGlzcGxheSI6IuKAjHJzaeKAjCAoMTQsQykifSwib3V0cHV0cyI6eyJSU0kiOiIjYWQ2ZWZmIn0sInBhbmVsIjoi4oCMcnNp4oCMICgxNCxDKSIsInBhcmFtZXRlcnMiOnsic3R1ZHlPdmVyWm9uZXNFbmFibGVkIjp0cnVlLCJzdHVkeU92ZXJCb3VnaHRWYWx1ZSI6ODAsInN0dWR5T3ZlckJvdWdodENvbG9yIjoiIzc5ZjRiZCIsInN0dWR5T3ZlclNvbGRWYWx1ZSI6MjAsInN0dWR5T3ZlclNvbGRDb2xvciI6IiNmZjgwODQiLCJjaGFydE5hbWUiOiJjaGFydCIsInBhbmVsTmFtZSI6IuKAjHJzaeKAjCAoMTQsQykifX0sIuKAjG1hY2TigIwgKDEyLDI2LDkpIjp7InR5cGUiOiJtYWNkIiwiaW5wdXRzIjp7IkZhc3QgTUEgUGVyaW9kIjoxMiwiU2xvdyBNQSBQZXJpb2QiOjI2LCJTaWduYWwgUGVyaW9kIjo5LCJpZCI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIiwiZGlzcGxheSI6IuKAjG1hY2TigIwgKDEyLDI2LDkpIn0sIm91dHB1dHMiOnsiTUFDRCI6IiNhZDZlZmYiLCJTaWduYWwiOiIjZmZhMzNmIiwiSW5jcmVhc2luZyBCYXIiOiIjNzlmNGJkIiwiRGVjcmVhc2luZyBCYXIiOiIjZmY4MDg0In0sInBhbmVsIjoi4oCMbWFjZOKAjCAoMTIsMjYsOSkiLCJwYXJhbWV0ZXJzIjp7ImNoYXJ0TmFtZSI6ImNoYXJ0IiwicGFuZWxOYW1lIjoi4oCMbWFjZOKAjCAoMTIsMjYsOSkifX0sIuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIjp7InR5cGUiOiJzdG9jaGFzdGljcyIsImlucHV0cyI6eyJQZXJpb2QiOjE0LCJGaWVsZCI6IkNsb3NlIiwiU21vb3RoIjp0cnVlLCJpZCI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIiwiZGlzcGxheSI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIn0sIm91dHB1dHMiOnsiRmFzdCI6IiMwMDAwMDAiLCJTbG93IjoiI0ZGMDAwMCJ9LCJwYW5lbCI6IuKAjHN0b2NoYXN0aWNz4oCMICgxNCxDLHkpIiwicGFyYW1ldGVycyI6eyJzdHVkeU92ZXJab25lc0VuYWJsZWQiOnRydWUsInN0dWR5T3ZlckJvdWdodFZhbHVlIjo4MCwic3R1ZHlPdmVyQm91Z2h0Q29sb3IiOiIjMDAwMDAwIiwic3R1ZHlPdmVyU29sZFZhbHVlIjoyMCwic3R1ZHlPdmVyU29sZENvbG9yIjoiIzAwMDAwMCIsImNoYXJ0TmFtZSI6ImNoYXJ0In19fX0-";

function crossoverAlerts() {
  var today = new Date();
  Logger.log("crossoverAlerts starts running at " + today.toLocaleString() + ".")
  if (today.getDay() == 6) {
    Logger.log("Not running this script on Saturdays.");
    return;
  }

  var signalMessages = "";
  var symbolsWithTriggers = [];

  var sheetNames = ["all-stocks", "all-hk-stocks"]
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

      if (isBullishSignal(row[COLUMN_INDEX_MV_20_DIFF], row[COLUMN_INDEX_MV_20_PREV_DIFF])) {
        symbolsWithTriggers.push(symbol);

        signalMessages += `${symbol} is bullish (cross mv20 from below),`;
        signalMessages += ` price: ${row[COLUMN_INDEX_PRICE]} (${row[COLUMN_INDEX_CHANGE]}),`;
        signalMessages += ` diff to mv20: ${row[COLUMN_INDEX_MV_20_DIFF].toFixed(2)},`
        signalMessages += ` prev diff to mv20: ${row[COLUMN_INDEX_MV_20_PREV_DIFF].toFixed(2)},`;
        signalMessages += " <a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>";

        signalMessages = `<p>${signalMessages}</p>\n`;
      }
      if (isBullishSignal(row[COLUMN_INDEX_MV_50_DIFF], row[COLUMN_INDEX_MV_50_PREV_DIFF])) {
        symbolsWithTriggers.push(symbol);

        signalMessages += `${symbol} is bullish (cross mv50 from below),`;
        signalMessages += ` price: ${row[COLUMN_INDEX_PRICE]} (${row[COLUMN_INDEX_CHANGE]}),`;
        signalMessages += ` diff to mv50: ${row[COLUMN_INDEX_MV_50_DIFF].toFixed(2)},`
        signalMessages += ` prev diff to m50: ${row[COLUMN_INDEX_MV_50_PREV_DIFF].toFixed(2)},`;
        signalMessages += " <a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>";

        signalMessages = `<p>${signalMessages}</p>\n`;
      }

      if (isBearishSignal(row[COLUMN_INDEX_MV_20_DIFF], row[COLUMN_INDEX_MV_20_PREV_DIFF])) {
        symbolsWithTriggers.push(symbol);

        signalMessages += `${symbol} is bearish (cross mv20 from above),`;
        signalMessages += ` price: ${row[COLUMN_INDEX_PRICE]} (${row[COLUMN_INDEX_CHANGE]}),`;
        signalMessages += ` diff to mv20: ${row[COLUMN_INDEX_MV_20_DIFF].toFixed(2)},`
        signalMessages += ` prev diff to mv20: ${row[COLUMN_INDEX_MV_20_PREV_DIFF].toFixed(2)},`;
        signalMessages += " <a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>";
        
        signalMessages = `<p>${signalMessages}</p>\n`;
      }
      if (isBearishSignal(row[COLUMN_INDEX_MV_50_DIFF], row[COLUMN_INDEX_MV_50_PREV_DIFF])) {
        symbolsWithTriggers.push(symbol);

        signalMessages += `${symbol} is bearish (cross mv50 from above),`;
        signalMessages += ` price: ${row[COLUMN_INDEX_PRICE]} (${row[COLUMN_INDEX_CHANGE]}),`;
        signalMessages += ` diff to mv50: ${row[COLUMN_INDEX_MV_50_DIFF].toFixed(2)},`
        signalMessages += ` prev diff to mv50: ${row[COLUMN_INDEX_MV_50_PREV_DIFF].toFixed(2)},`;
        signalMessages += " <a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>";
        
        signalMessages = `<p>${signalMessages}</p>\n`;
      }
    }
  }  

  if (symbolsWithTriggers.length != 0) {
    Logger.log("Trigger alerts: \n" + signalMessages);
    var subject = "crossoverAlerts: Short-term trading alerts for " + symbolsWithTriggers.join(", ");
    sendEmail(EMAIL_ADDRESS, subject, signalMessages);
  } else {
    Logger.log("No alerts");
  }
}

function priceCrossingMv10Alerts() {
  var today = new Date();
  Logger.log("priceCrossingMv10Alert starts running at " + today.toLocaleString() + ".")
  if (today.getDay() == 6) {
    Logger.log("Not running this script on Saturdays.");
    return;
  }

  var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName("all-stocks");
  var rangeData = sheet.getDataRange()
  var rows = rangeData.getValues(); 
  var signalMessages = "";
  var symbolsWithTriggers = [];
  // Start on row one (0-based) because the first row is a header row.
  for (var r = 1; r < rows.length; r++) {
    var row = rows[r];
    var symbol = row[COLUMN_INDEX_SYMBOL];

    if (symbol == '') {
      Logger.log("Exit at row " + (r + 1))
      break;
    }

    var mv10 = row[COLUMN_INDEX_MV10];
    if (row[COLUMN_INDEX_PRICE] >= mv10) {
      continue;
    }

    if (row[COLUMN_INDEX_MV10_ALERT_SENT] != "") {
      Logger.log(`Not alerting for ${symbol} because an alert has already been sent`);
      continue;
    }

    symbolsWithTriggers.push(symbol);
    signalMessages += `${symbol} crosses below 10-day moving average,`;
    signalMessages += ` price: ${row[COLUMN_INDEX_PRICE]} (${row[COLUMN_INDEX_CHANGE]}),`;
    signalMessages += ` mv10: ${mv10.toFixed(2)},`;
    signalMessages += " <a href=\"" + GRAPH_URL_FORMAT.replace("%s", symbol) + "\">graph</a>";
        
    signalMessages = `<p>${signalMessages}</p>\n`;

    // getRange is 1-based
    sheet.getRange(r+1, COLUMN_INDEX_MV10_ALERT_SENT+1).setValue(today.toLocaleString());
  }

  if (symbolsWithTriggers.length != 0) {
    Logger.log("Trigger alerts: \n" + signalMessages);

    var subject = "Price crossing 10-day moving average alerts for " + symbolsWithTriggers.join(", ");
    sendEmail(EMAIL_ADDRESS, subject, signalMessages);
  } else {
    Logger.log("No alerts");
  }

}

function sendEmail(toAddress, subject, htmlBody) {
  // // Send plain text email
  // MailApp.sendEmail(toAddress, subject, htmlBody); 
  MailApp.sendEmail({
    to: toAddress,
    subject: subject,
    htmlBody: htmlBody,
  });
}

function isBullishSignal(diff, prevDiff) {
  return diff >= 0 && prevDiff < 0;
}
function isBearishSignal(diff, prevDiff) {
  return diff < 0 && prevDiff >= 0;
}

function populateWithMaster() {
  var templateSheetName = sheetsmanagement.getChosenSheet("Which one is the template sheet?")
  if (templateSheetName == null) {
    return;
  }
  var allStocksSheetName = sheetsmanagement.getChosenSheet("Which one is the sheet for all stocks?")
  if (allStocksSheetName == null) {
    return;
  } 

  var allStocksSheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(allStocksSheetName);
  var templateSheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(templateSheetName);

  var rangeData = allStocksSheet.getDataRange()
  var rows = rangeData.getValues(); 

  var ui = SpreadsheetApp.getUi();
  // Start on row one (0-based) because the first row is a header row.
  for (var r = 1; r < rows.length; r++) {
    var row = rows[r];

    var symbol = row[COLUMN_INDEX_SYMBOL];
    Logger.log("Process stock: " + symbol);
    if (symbol == '') {
      Logger.log("Exit at row " + (r + 1))
      break;
    }
    if (symbol == templateSheetName) {
      Logger.log(`Not duplicate for ${templateSheetName} becaus it's the template sheet`)
      continue;
    }

    var activeSpreadsheet = SpreadsheetApp.getActiveSpreadsheet();
    if (activeSpreadsheet.getSheetByName(symbol)) {
      // ui.alert('Skipped', `Not populating ${symbol} because it already exists`, ui.ButtonSet.OK);
      continue;
    }
    var duplicant = templateSheet.copyTo(activeSpreadsheet);

    // /* Before cloning the sheet, delete any previous copy */
    // var old = activeSpreadsheet.getSheetByName(symbol);
    // if (old) activeSpreadsheet.deleteSheet(old);

    SpreadsheetApp.flush();
    duplicant.setName(symbol);
    var realSymbol = symbol;
    if (Number.isInteger(symbol)) {
      realSymbol = "HKG:" + symbol;
    }
    // Set the symbol name inside the sheet
    duplicant.getRange(1, 2).setValue(realSymbol);
  }
}

function onOpen(event) {
  var ui = SpreadsheetApp.getUi();
  // Or DocumentApp or FormApp.
  ui.createMenu('Custom Funcs')
      .addItem('Run moving average crossing alert', 'mvCrossingAlert')
      .addItem('Run price crossing moving average alert', 'priceCrossingMv10Alert')
      .addItem('Populate individual stock sheets', 'populateWithMaster')
      .addToUi();
  sheetsmanagement.onOpen(event);
}
