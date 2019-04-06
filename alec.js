const twilio = require('twilio');
admin.initializeApp();

var accountSid = 'ACe02f8826f7281a4a3bce8f052e152749'; // Your Account SID from www.twilio.com/console
var authToken = '354a64572f59a6bc1270ec11e429cb6e';   // Your Auth Token from www.twilio.com/console
var client = new twilio(accountSid, authToken);

const SECS_TO_RESEND = 60;

var ref = /* GET FIREBASE REFERENCE */

ref.once('value').then(function(snapshot) {
	snapshot.forEach((child) => {
		var currentPhone = child.key;
  		if (Date.now() > child.val() + (SECS_TO_RESEND * 1000)) {
			client.messages.create({
		     	body: 'This is the ship that made the Kessel Run in fourteen parsecs?',
		     	from: '+12348134373',
		     	to: currentPhone
		  	});

		  	// update value
		  	// change child.value to current time
		  	db.ref("Desired/" + currentPhone).set(Date.now());
  		}

  	});

  	res.redirect(200);  
});
