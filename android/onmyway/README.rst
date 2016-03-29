#########
On My Way
#########

**On My Way** is an android app that allows a user to mock locations of a route
on the android device. It is mostly for the purpose of developing location
aware applications.

************
User Stories
************

* User can locate the current location by pressing the "locate me" button on
  the map.

****************
Pending features
****************
* User can search for a location and it will be shown on the map.
* User can choose multiple locations to indicate a route.
* The route is indicated by polylines and markers on the map.
* User can remove any locations of a route.
* If there are at least two locations selected, User can press a button "Ready"
  to open a screen to select properties for the route being mocked. Finally,
  User can press the button "Go" to start mocking the locations of the route on
  the device.

  * User can choose the time to complete the route.
  * User can see the distance between immediate points on the route.
  * User can see the speed given the time to complete the route.

* User can see the current locations being mocked while the mock is in progress.
* User can press the "Stop" button (which replaces the "Ready" button) to stop
  the mock.
* A sticky notification is shown when the mocking is in progress. User can
  press the notification to go back to the app.
* User can close the app even the location mocking is in progress. However, the
  sticky notification always stays.
* Any route mocked is saved in "History".
* User can press the "History" button in the main screen to look at the History
  screen.
* User can choose a historical route, which replaces the current route in the
  main screen.
* User can delete a historical route.

**************************
Open-source libraries used
**************************

*******
License
*******

    Copyright 2016 Heung Ming Tai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
