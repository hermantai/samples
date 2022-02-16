// Delete all the sheets on the right of a chosen sheet.
function deleteSheetsRightOf() {
  var ui = SpreadsheetApp.getUi();
  var sheets = SpreadsheetApp.getActiveSpreadsheet().getSheets();
  var s = ''
  var choices = {}
  for (var i = 0; i < sheets.length; i++) {
    var sheet = sheets[i];
    s += `${i+1}: ${sheet.getName()}\n`;
    choices[i+1] = sheet.getName();
  }
  var result = ui.prompt(
     'On the right of which sheet you want the sheets to be deleted?',
     s + '\n\nEnter a number between 1 and ' + Object.keys(choices).length,
      ui.ButtonSet.OK_CANCEL);
  var button = result.getSelectedButton();
  var response = result.getResponseText();
  if (button == ui.Button.OK) {
    Logger.log("Response: " + response);
    if (!(response in choices)) {
      Logger.log("not a valid choice");
      ui.alert('Wrong input', response + ' is not a valid input', ui.ButtonSet.OK);

      return;
    }

    var chosenSheetName = choices[response];
    Logger.log("Right of this sheet will be deleted: " + chosenSheetName);
    var sheetsToBeDeleted = [];
    var started = false;
    for (var i = 0; i < sheets.length; i++) {
      var sheet = sheets[i];
      if (started) {
        sheetsToBeDeleted.push(sheet);
        continue;
      }

      if (sheet.getName() == chosenSheetName) {
        started = true;
      }
    }

    sheetsToBeDeleted.forEach(
      (sheet) => {
        Logger.log("Delete sheet: " + sheet.getName());
        SpreadsheetApp.getActiveSpreadsheet().deleteSheet(sheet);
      }
    );

  } else if (button == ui.Button.CANCEL) {
    ui.alert('Cancelled', 'Nothing is deleted', ui.ButtonSet.OK);
  } else if (button == ui.Button.CLOSE) {
    // do nothing
  }
}

// Jump to a chosen sheet.
function jumpToSheet() {
  var sheetName = getChosenSheet("Jump to ...?")
  if (sheetName == null) {
    return;
  }

  var sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName(sheetName);
  SpreadsheetApp.getActiveSpreadsheet().setActiveSheet(sheet);
}

// Returns the name of the choosen sheet. Returns null if cancelled or invalid input.
function getChosenSheet(question) {
  var ui = SpreadsheetApp.getUi();
  var sheets = SpreadsheetApp.getActiveSpreadsheet().getSheets();
  var s = ''
  var choices = {}
  for (var i = 0; i < sheets.length; i++) {
    var sheet = sheets[i];
    s += `${i+1}: ${sheet.getName()}\n`;
    choices[i+1] = sheet.getName();
  }
  var result = ui.prompt(
     question,
     s + '\n\nEnter a number between 1 and ' + Object.keys(choices).length,
      ui.ButtonSet.OK_CANCEL);
  var button = result.getSelectedButton();
  var response = result.getResponseText();
  if (button == ui.Button.OK) {
    Logger.log("getChosenSheet Response: " + response);
    if (!(response in choices)) {
      Logger.log("not a valid choice");
      ui.alert('Wrong input', response + ' is not a valid input', ui.ButtonSet.OK);

      return;
    }

    var chosenSheetName = choices[response];
    return chosenSheetName;
  } else if (button == ui.Button.CANCEL) {
    ui.alert('Cancelled', 'Operation cancelled', ui.ButtonSet.OK);
  } else if (button == ui.Button.CLOSE) {
    // do nothing
  }
  return null;
}

function onOpen(event) {
  var ui = SpreadsheetApp.getUi();
  // Or DocumentApp or FormApp.
  ui.createMenu('Sheets operations')
      .addItem('Delete sheets right of...', 'sheetsmanagement.deleteSheetsRightOf')
      .addItem('Jump to ...', 'sheetsmanagement.jumpToSheet')
      // .addSeparator()
      // .addSubMenu(ui.createMenu('Sheets operations')
      //    .addItem('Delete sheets right of...', 'deleteSheetsRightOf')
      //    .addItem('Populate individual stock sheets', 'populateWithMaster')
      //    .addItem('Jump to ...', 'jumpToSheet')
      //   )
      .addToUi();
}
