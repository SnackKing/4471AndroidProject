//const functions = require('firebase-functions');
//const admin = require('firebase-admin');
const twilio = require('twilio');
//admin.initializeApp();

var accountSid = 'ACe02f8826f7281a4a3bce8f052e152749'; // Your Account SID from www.twilio.com/console
var authToken = '354a64572f59a6bc1270ec11e429cb6e';   // Your Auth Token from www.twilio.com/console
var client = new twilio(accountSid, authToken);



// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.sendMessages = functions.database.ref('/contacts').onCreate((snapshot,context) => {
// const data = snapshot.val();
//  //response.send("Hello from Firebase!");
// });

client.messages
  .create({
     body: 'This is the ship that made the Kessel Run in fourteen parsecs?',
     from: '+12348134373',
     to: '+14408362515'
   })
  .then(message => console.log(message.sid));
