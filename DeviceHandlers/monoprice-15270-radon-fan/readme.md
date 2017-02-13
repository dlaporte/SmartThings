# monoprice-15270-radon-fan
This is a [SmartThings](http://smartthings.com) device handler for the Monoprice 15270/WADWAZ-1 (aka GoControl) door/window sensor.  These sensors have internal terminals that are well suited to pairing them with switches, relays, and other montoring devices.  This device handler includes minor cosmetic customizations to adapt it to monitoring a Radon Fan.

The switch is paired with a pressure differential switch to ensure air flow through the vent pipe in a radon mediation system.

### Requirements
For a non-destructive installation (ie. to not drill an additional hole in your pipe), you will need the following:

* Monoprice 15270, Linear/GoControl WADWAZ-1, Schlage RS100, or equivalent
*	Dwyer 1910-00 Pressure Switch
*	1/8" barbed 3-way fitting
*	1/8" Barb x 1/8" NPT Male Pipe fitting
*	1/8" ID plastic tubing

### Device View in the SmartThings Mobile App
The device handler shows fan state in the Things list and a simple color-coded display in the Detailed Device view - pretty simple stuff...

#### Things View
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/monoprice-15270-radon-fan/docs/IMG_2564.png">

#### Detailed Device View
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/monoprice-15270-radon-fan/docs/IMG_2565.png">
<img width="250" src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/monoprice-15270-radon-fan/docs/IMG_2566.png">

### Installation
Please see [this FAQ in the SmartThings Community](https://community.smartthings.com/t/faq-an-overview-of-using-custom-code-in-smartthings/16772) for instructions on how to install the device handler to your ST account.  Here's the quick version:

* For US, visit: https://graph.api.smartthings.com
* For UK, visit: https://graph-eu01-euwest1.api.smartthings.com
* Click "My Device Handlers"
* Click "New Device Handler" in the top right
* Click the "From Code" tab
* Paste in the code in "monoprice-15270-radon-fan.groovy"
* Click "Create"
* Click "Publish -> For Me"
* Click "My Devices", select your sensor, and change the type to "WADWAZ-1/Monoprice 15270 as Radon Fan Sensor"

Here are some quick instructions on how to rig up your existing system.  I assume you have a [RadonAway Easy Manometer](https://www.radonaway.com/manometer-blue.php) that uses 1/8" tubing.

* Use a dremel or other tool to cut away sensor case to expose terminals
* Connect terminals on pressure switch to terminals on sensor
* Install 1/8" Barb x 1/8" NPT fitting on pressure switch low pressure connection  
* Test - attach tubing to adapter and gently inhale
* Remove 1/8" manometer tubing from hole installed in vent pipe
* Attach small segment of tubing to 3-way fitting
* Attach manometer tubing to 3-way fitting
* Connect tubing from adapter to 3-way fitting
* Insert short tube into vent pipe hole
* Test - shut off power to the fan to make sure there's not an updraft in the pipe that false positives
* Setup a CoRE piston to alert you 
* Sleep soundly knowing your house can no longer silently kill you if the fan dies!

When you are done, you should have something that looks like this:

<img width=600 src="https://github.com/dlaporte/SmartThings/blob/master/DeviceHandlers/monoprice-15270-radon-fan/docs/IMG_2550.jpg">
