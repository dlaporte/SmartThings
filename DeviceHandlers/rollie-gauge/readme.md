# rollie-gauge
This is a [SmartThings](http://smartthings.com) device handler for the [Rollie WTG-BT](http://fuelminder.biz/rollie%20systems/wtg1%20remote%20tank%20gauge.html) cloud-enabled oil tank gauge. 

### Requirements
This DTH directly accesses the Rollie portal to retrieve the latest data.  You will need your serial number (printed on the label on the gauge) and a password (defaults to "rollie") to configure the device.  While you can configure the gauge through the device handler properties, full control is still possible via the [Rollie portal] (http://rollieapp.com/gauges/).

### Device View in the SmartThings Mobile App
The device handler shows the latest data from the device (gallons/inches remaining) in the top tile as well as approximate usage data for current day, yesterday, and the past week.  Previous days data will remain blank if there is not enough data or if the usage data doesn't make sense (eg. you received an oil delivery sometime that week).

These screenshots show the device in the Things view, the detailed device view, and the configuration screen.

#### Things View
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2558.png">

#### Detailed Device View
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2559.png">
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2563.png">

#### Setting View
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2560.png">
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2561.png">
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/rollie-gauge/docs/IMG_2562.png">

### Installation
Please see [this FAQ in the SmartThings Community](https://community.smartthings.com/t/faq-an-overview-of-using-custom-code-in-smartthings/16772) for instructions on how to install the device handler to your ST account.

Once created, you can configure your account setting, your tank type, and alerting options via the settings icon in the device view.  The dimensions (length, width, height, diameter) are optional and only required for vertical/horizontal cylinders and square takes.  Your settings will be configured in the Rollie portal when you you click "Configure" in the device view.

As it is not currently possible for a device handler to refresh itself, the device needs to be polled regularly in order to get data continuously for the graph display. The easiest way to achieve this is to use [Pollster](https://community.smartthings.com/t/pollster-a-smartthings-polling-daemon/3447) and set it to refresh the device every 30 minutes (the Rollie updates its data approximately every 30 minutes). More advanced polling can be achieved using other SmartApps such as [*CoRE*](https://community.smartthings.com/t/release-candidate-core-communitys-own-rule-engine/57972).
