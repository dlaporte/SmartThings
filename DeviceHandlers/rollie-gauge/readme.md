# rollie-gauge
This is a [SmartThings](http://smartthings.com) device handler ("DH") for the [Rollie WTG-BT](http://fuelminder.biz/rollie%20systems/wtg1%20remote%20tank%20gauge.html) cloud-enabled oil tank gauge. 

### Requirements
This DTH directly accesses the Rollie portal to retrieve the latest data.  You will need your serial number (printed on the label on the gauge) and a password (defaults to "rollie") to configure the device.  While you can configure the gauge through the device handler properties, full control is still possible via the [Rollie portal] (http://rollieapp.com/gauges/).

### Device View in the SmartThings Mobile Application
The DH shows the latest data from the device (gallons/inches remaining) in the top tile as well as a usage chart for the last eight days (gallons remaining in blue with units on the left axis, inches remaining in red with units on the right axis; previous day's data is using fainter colors).

*Note: The graph display uses a currently undocumented feature in the SmartThings mobile application which most likely will change in the future. I will try my best to keep the graph available but can't make any promises as server-side changes are beyond my control.*

### Installation
Please see [this FAQ in the SmartThings Community](https://community.smartthings.com/t/faq-an-overview-of-using-custom-code-in-smartthings/16772) for instructions on how to install the device handler to your ST account.

As it is not currently possible for a DTH to refresh itself, the device needs to be polled regularly in order to get data continuously for the graph display. The easiest way to achieve this is to use [Pollster](https://community.smartthings.com/t/pollster-a-smartthings-polling-daemon/3447) and set it to refresh the device every 30 minutes (the Envoy updates its data approximately every 30 minutes). More advanced polling can be achieved using other SmartApps such as [*CoRE*](https://community.smartthings.com/t/release-candidate-core-communitys-own-rule-engine/57972).

0
