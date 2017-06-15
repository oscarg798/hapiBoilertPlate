var Hapi = require('hapi');
const Vision = require('vision');
const Inert = require('inert');
var config = require('./config.js');
var routes = require('../routes');
var axios = require('axios');
var moment = require('moment');
const Authenticate = require('../libs/authenticate.js');
var server = new Hapi.Server();

server.connection({
    port: config.PORT,
    routes: {
        cors: true
    }
});

var goodOptions = {
    ops: {
        interval: 1000
    },
    reporters: {
        myConsoleReporter: [{
            module: 'good-squeeze',
            name: 'Squeeze',
            args: [{
                log: '*',
                response: '*',
                error: '*'
            }]
        }, {
            module: 'good-console'
        }, 'stdout']
    }
}

if (process.env.NODE_ENV === 'test') {
    goodOptions.reporters[0].events = {}
}

server.register([Inert, Vision, {
        register: require('good'),
        options: goodOptions
    },

    {
        register: require('hapi-swagger'),
        options: {
            info: {
                title: "The Name",
                version: moment(Date.now()).format('HH:mm:ss YYYY-MM-DD'), //TODO: Fix api version and base path.
            },
            securityDefinitions: {
                'Bearer': {
                    'type': 'apiKey',
                    'name': 'Authorization',
                    'in': 'header',
                    'x-keyPrefix': 'Bearer '
                }
            },
            security: [{
                'Bearer': []
            }],
            sortEndpoints: 'path',
            sortTags: 'name'
        }
    },
    require('hapi-auth-bearer-token')
], function (err) {
    if (err) {
        console.error(err);
        throw err;
    }

    server.auth.strategy('token', 'bearer-access-token', {
        allowQueryToken: true, // optional, true by default
        allowMultipleHeaders: false, // optional, false by default
        accessTokenName: 'token', // optional, 'token' by default

        validateFunc: function (token, callback) {
            Authenticate.auth(token).then(user => {
                    callback(null, true, user)
                })
                .catch(err => {
                    callback(null, false, {
                        message: err,
                        token: token
                    });
                });
        }
    });

    server.route(routes);

    server.start(function (err) {
        if (err) {
            console.error(err);
            throw err;
        }
        console.log('Server running at:', server.info.uri);
    });
});




module.exports = server;