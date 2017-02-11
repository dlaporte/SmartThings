# rollie-gauge
This is a [SmartThings](http://smartthings.com) device type handler (henceforth 'DTH') for the [Rollie WTG-BT](http://fuelminder.biz/rollie%20systems/wtg1%20remote%20tank%20gauge.html) cloud-enabled oil tank gauge. 

### Requirements
This DTH directly accesses the Rollie portal to retrieve the latest data.  You will need your serial number (printed on the label on the gauge) and a password (defaults to "rollie") to configure the device.  While you can configure the gauge through the device handler properties, full control is still possible via the [Rollie portal] (http://rollieapp.com/gauges/).

### Device View in the SmartThings Mobile Application
The DTH shows the latest data from the device (current power, change since last reading, peak reading for the day) in the top tile as well as a chart for the data of the last two days (power in blue with units on the left axis, energy in red with units on the right axis; yesterday's data is using fainter colors). Data is only available from the first installation of the DTH so yesteday's data will not be available until the second day using it.

*Note: The graph display uses a currently undocumented feature in the SmartThings mobile application which most likely will change in the future. I will try my best to keep the graph available but can't make any promises as server-side changes are beyond my control.*

The following screen shots are from the Envoy DTH using SmartThings app version 2.1.3 on iOS on iPhone 6. The SolarEdge version looks very similar but the data tiles show different time periods (today/yesterday/last month/last year/lifetime):

<img src="https://raw.githubusercontent.com/ahndee/Envoy-ST/master/devicetypes/aamann/enlighten-envoy-local.src/docs/IMG_2527.jpg" width="375px" height="667px" />
<img src="https://raw.githubusercontent.com/ahndee/Envoy-ST/master/devicetypes/aamann/enlighten-envoy-local.src/docs/IMG_2528.jpg" width="375px" height="667px" />

The tiles below the chart show the total energy production for today, yesterday, the last 7 days, and system lifetime in the middle column and the efficiency for the same time spans in the right column. Efficiency is calculated by dividing the total energy output (kWh) by the system size (kW). The efficiency measure allows the outputs of system with different sizes to be compared. The value corresponds to number of hours of full production of the system - expected values depend on location and array orientation.

<img src="https://raw.githubusercontent.com/ahndee/Envoy-ST/master/devicetypes/aamann/enlighten-envoy-local.src/docs/IMG_2529.jpg" width="375px" height="667px" />

### Installation
Please see [this FAQ in the SmartThings Community](https://community.smartthings.com/t/faq-an-overview-of-using-custom-code-in-smartthings/16772) for instructions on how to install the device handler to your ST account.

As it is not currently possible for a DTH to refresh itself, the device needs to be polled regularly in order to get data continuously for the graph display. The easiest way to achieve this is to use [Pollster](https://community.smartthings.com/t/pollster-a-smartthings-polling-daemon/3447) and set it to refresh the device every 5 minutes (the Envoy updates its data once every 5 minutes). More advanced polling (e.g. polling during daylight hours only) can be achieved using other SmartApps such as [*CoRE*](https://community.smartthings.com/t/release-candidate-core-communitys-own-rule-engine/57972).

0
