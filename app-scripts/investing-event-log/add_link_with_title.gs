/**
 * Get URLs from the user, then add links at the cursor. Each link use the title of the destination
 * as the text and the provided url for the link.
 */
function addLinkWithTitle() {
  
  var ui = DocumentApp.getUi();
  var result = ui.prompt(
     'Please enter urls separated by commas',
      ui.ButtonSet.OK_CANCEL);
  var button = result.getSelectedButton();
  if (button != ui.Button.OK) {
    return;
  }

  var doc = DocumentApp.getActiveDocument();
  var body = doc.getBody();

  // get the cursor, so we know where to inserr the urls
  var cursor = doc.getCursor();
  var cursorPos = body.getChildIndex(cursor.getElement());

  // get the urls from user input
  var response = result.getResponseText();
  var urls = response.split(',').map(url => url.trim());
  var cursorOffset = 1;
  for (let url of urls) {
    Logger.log("Loading " + url);
    var pageResponse = UrlFetchApp.fetch(url);
    var $ = Cheerio.load(pageResponse.getContentText());
    var title = $('title').text();
    
    // Insert the title with url at the cursor, and a blank link
    var insertedParagraph = body.insertParagraph(cursorPos + cursorOffset, title)
    insertedParagraph.setLinkUrl(url);
    cursorOffset++;
    body.insertParagraph(cursorPos + cursorOffset, "");
    cursorOffset++;
    // // Insert the title with url at the beginnning of the document, and a blank link
    // var insertedParagraph = body.insertParagraph(0, title)
    // insertedParagraph.setLinkUrl(url);
    // body.insertParagraph(0, "");
  }
}

function onOpen(event) {
  var ui = DocumentApp.getUi();
  // Or DocumentApp or FormApp.
  ui.createMenu('Custom Funcs')
      .addItem('Add a link with title', 'addLinkWithTitle')
      .addToUi();
}
