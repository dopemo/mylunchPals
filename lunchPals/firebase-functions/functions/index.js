const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

exports.matching = functions.database.ref('/newMatchRequestFor/{size}/{restaurant}/{uid}').onCreate(event => {
    const restaurant = event.data.ref.parent;
    const size = event.params.size;
    return restaurant.once('value').then(snapshot => {
        console.log("Data written at " + restaurant.key);
        console.log("Group size requested: " + size);

        if(snapshot.numChildren() >= size){
            return event.data.ref.root.child('matched').once('value').then(matchedSnap => {
                const updates = {};
                snapshot.forEach(function(update){
                    console.log("Check if key '" + update.key + "' already exists in matched: " + matchedSnap.hasChild(update.key));
                    if(!matchedSnap.hasChild(update.key)){
                        console.log("creating new update array: " + update.key);
                        const updateValue = {};
                        snapshot.forEach(function(child){
                            if(child.key != update.key && !matchedSnap.hasChild(child.key)){
                                console.log("writing to update " + update.key + ": " + child.key);
                                updateValue[child.key] = child.key;
                            }
                        });
                        console.log("writing restaurant to update " + update.key + ": " + snapshot.key);
                        updateValue["restaurant"] = snapshot.key;
                        updates[update.key] = updateValue;
                    }
                });
                const promises = [];
                //check if all users still were looking for matches
                if(Object.keys(updates).length >= size){
                    console.log("Removing search entry and adding to matched");
                    promises.push(event.data.ref.root.child('matched').update(updates));
                    promises.push(restaurant.ref.remove()); 
                    return Promise.all(promises);
                }else{
                    console.log("One or more users already matched. Users in match were: " + Object.keys(updates).length + "/" + size);
                    return true;
                }
            });
        }else{
            console.log("Not enough users to match.");
            return true;
        }
    });
});