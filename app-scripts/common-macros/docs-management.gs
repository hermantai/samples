function onOpen(event) {
  var ui = DocumentApp.getUi();
  ui.createMenu('Docs operations')
    .addSubMenu(ui.createMenu('Delete operations')
      .addItem(
        'Delete the paragraphs under a chosen heading',
        'deleteParagraphsUnderHeadingTask')
      .addItem(
        'Delete the paragraphs before a chosen heading',
        'deleteParagraphsBeforeHeadingTask')
      .addItem(
        'Delete the paragraphs after and including a chosen heading',
        'deleteParagraphsAfterAndIncludingHeadingTask')
    )
    .addToUi();
}

/**
 * Delete the portions of documet starting from a heading
 */
function deleteParagraphsUnderHeadingTask() {
  promoptForHighestRankedHeadingThenDo(
    "Delete the paragraphs under the heading",
    _deleteParagraphsUnderHeading);
}

function deleteParagraphsAfterAndIncludingHeadingTask() {
  promoptForHighestRankedHeadingThenDo(
    "Delete the paragraphs after and including the heading",
    _deleteParagraphsAfterAndIncludingHeading);
}

function deleteParagraphsBeforeHeadingTask() {
  promoptForHighestRankedHeadingThenDo(
    "Delete the paragraphs before the heading",
    _deleteParagraphsBeforeHeading);
}

/**
 * Delete the portions of documet starting from a heading
 */
function promoptForHighestRankedHeadingThenDo(opDescription, opFunc) {
  // Gather all top level headers
  var body = DocumentApp.getActiveDocument().getBody();
  var headersParagraphSearchResultWrappers = _searchHighestRankedHeaders(body);

  // Prompt for a heading
  var s = ''
  var choices = {}
  for (var i = 0; i < headersParagraphSearchResultWrappers.length; i++) {
    var headersParagraphSearchResultWrapper = headersParagraphSearchResultWrappers[i];
    s += `${i+1}: ${headersParagraphSearchResultWrapper.paragraph.getText()}\n`;
    choices[i+1] = headersParagraphSearchResultWrapper;
  }
  var ui = DocumentApp.getUi();
  var result = ui.prompt(
     'Which heading for the paragraph?',
     s + '\n\nEnter a number between 1 and ' + Object.keys(choices).length,
      ui.ButtonSet.OK_CANCEL);
  if (result.getSelectedButton() == ui.Button.CANCEL) {
    return;
  }
  
  // Handle the response and confirm if the user really wants to delete it.
  var response = result.getResponseText();
  Logger.log("Response: " + response);
  if (!(response in choices)) {
    Logger.log("not a valid choice");
    ui.alert('Wrong input', response + ' is not a valid input', ui.ButtonSet.OK);
    return;
  }

  var chosenHeadersParagraphSearchResultWrapper = choices[response];
  var resultButton = ui.alert(
     'Confirm',
     opDescription + ': "' + chosenHeadersParagraphSearchResultWrapper.paragraph.getText() + '"?',
      ui.ButtonSet.OK_CANCEL);
  if (resultButton == ui.Button.CANCEL) {
    return;
  }

  // Delete the paragraph the heading
  opFunc(chosenHeadersParagraphSearchResultWrapper.paragraph);
  return;
}

/**
 * Returns a list of ParagraphSearchResultWrapper that contains the paragraphs which are headers with
 * the highest rank in the given document body. 
 */
function _searchHighestRankedHeaders(body) {
  var highestHeadingRankSoFar = -1;
  var headersParagraphSearchResultWrappers = [];

  var searchType = DocumentApp.ElementType.PARAGRAPH;
  var searchResult = null;
  while (searchResult = body.findElement(searchType, searchResult)) {
    var paragraph = searchResult.getElement().asParagraph();
    if (paragraph.getText() == "") {
      // skip empty paragraphs
      continue;
    }

    var headingRank = _getHeadingRank(paragraph);
    if (headingRank < 0) {
      // skip paragraphs which are not headings
      continue;
    }
    if (headingRank > highestHeadingRankSoFar) {
      highestHeadingRankSoFar = headingRank;
      headersParagraphSearchResultWrappers = [new ParagraphSearchResultWrapper(searchResult, paragraph)];
    } else if (headingRank == highestHeadingRankSoFar) {
      headersParagraphSearchResultWrappers.push(new ParagraphSearchResultWrapper(searchResult, paragraph));
    }
  }

  return headersParagraphSearchResultWrappers;
}

/**
 * Deletes all elements under a heading paragraph, including the paragraph itself.
 * 
 * @param paragraph a paragraph that is a heading
 */
function _deleteParagraphsUnderHeading(paragraph) {
  var highestHeadingRank = _getHeadingRank(paragraph);

  var elementsToBeRemoved = [];

  var element = paragraph;
  while (element = element.getNextSibling()) {
    var headingRank = _getHeadingRankFromElement(element);

    if (headingRank >= highestHeadingRank) {
      // We hit a heading that is not lower than our current heading, so we are done
      break;
    }
    
    // // Deleting the element now would affect the search result, so delete it after the traversal is done.
    elementsToBeRemoved.push(element);
  }
  elementsToBeRemoved.forEach(p => p.removeFromParent());
  paragraph.removeFromParent();
}

/**
 * Deletes all paragraphs after and including a heading pargraph.
 * 
 * @param paragraph a paragraph that is a heading
 */
function _deleteParagraphsAfterAndIncludingHeading(paragraph) {
  var elementsToBeRemoved = [];

  var element = paragraph;
  while (element = element.getNextSibling()) {
    // // Deleting the element now would affect the search result, so delete it after the traversal is done.
    lastElement = element;
    elementsToBeRemoved.push(element);
  }

  // Docs does not allow deleting the last paragraph of a document, so append an empty
  // one at the end.
  DocumentApp.getActiveDocument().getBody().appendParagraph('');

  elementsToBeRemoved.forEach(p => p.removeFromParent());
  paragraph.removeFromParent();
}

/**
 * Deletes all paragraphs before a heading pargraph, not
 * including the heading paragraph.
 * 
 * @param paragraph a paragraph that is a heading
 */
function _deleteParagraphsBeforeHeading(paragraph) {
  var elementsToBeRemoved = [];

  var element = paragraph;
  while (element = element.getPreviousSibling()) {
    // // Deleting the element now would affect the search result, so delete it after the traversal is done.
    elementsToBeRemoved.push(element);
  }
  elementsToBeRemoved.forEach(p => p.removeFromParent());
}

function _getHeadingRankFromElement(element) {
  if (element.getType() != DocumentApp.ElementType.PARAGRAPH) {
    return -2;
  }
  return _getHeadingRank(element.asParagraph());
}

function _getHeadingRank(paragraph) {
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING1) {
    return 6;
  }
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING2) {
    return 5;
  }
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING3) {
    return 4;
  }
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING4) {
    return 3;
  }
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING5) {
    return 2;
  }
  if (paragraph.getHeading() == DocumentApp.ParagraphHeading.HEADING6) {
    return 1;
  }
  return -1;
}

function _debug_deleteParagraphsUnderHeadingTask() {
  deleteParagraphsUnderHeadingTask();
}

class ParagraphSearchResultWrapper {
  constructor(searchResult, paragraph) {
    this.searchResult = searchResult;
    this.paragraph = paragraph;
  }
}
