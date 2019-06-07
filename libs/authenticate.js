
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const config = require('../config/config.js');
const moment = require('moment');
const authenticate = {
  auth: auth,
  create: create
};
module.exports = authenticate;

function auth(token) {
  return new Promise((resolve, reject) => {
    if (!token) {
      return reject('Authentication token is required');
    }
    let payload = jwt.verify(token, config.secret);
    if (payload) {
      return resolve({
        id: payload._id,
        email: payload.email,
        uuid: payload.uuid,
        token: token
      });
    }

    return reject('not user found');
  });
}

function create(user) {
  return new Promise((resolve, reject) => {
    if (!user) {
      return reject('must provide an user');
    }
    return resolve(jwt.sign(JSON.stringify(user), config.secret));
  });
}