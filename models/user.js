const bcrypt = require('bcrypt');
var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

const user = new Schema({
    uuid: {
        type: String,
        required: true,
        index: {
            unique: true
        }
    },
    email: {
        type: String,
        required: true,
        index: {
            unique: true
        }
    },
    password: {
        type: String,
        required: true,
    }
});

user.methods.toJSON = function () {
    var obj = this.toObject()
    delete obj.password
    return obj
}

user.methods.authenticate = function (candidatePassword) {
    return new Promise((resolve, reject) => {
        bcrypt.compare(candidatePassword, this.password, function (err, isMatch) {
            if (err) return reject(err);
            resolve(isMatch);
        });
    });

};


mongoose.model('User', user);