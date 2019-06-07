var Hapi = require('hapi');
const Vision = require('vision');
const Inert = require('inert');
var config = require('./config.js');
var axios = require('axios');
var moment = require('moment');
const Authenticate = require('../libs/authenticate.js');
const routes = require('../routes/index')

module.exports = (async () => {
    const server = await new Hapi.Server({
        host: 'localhost',
        port: 3000,
    });

    await server.register([
        Inert,
        Vision,
        {
            plugin: require('hapi-swagger'),
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
                }
            }
        },
        require('hapi-auth-bearer-token')
    ]);

    server.auth.strategy('token', 'bearer-access-token', {
        allowQueryToken: true,
        allowMultipleHeaders: false,
        accessTokenName: 'token',
        validate: async (req, token, h) => {
            try {
                const user = await Authenticate.auth(token)
                const isValid = user != null;
                const credentials = {
                    token
                };
                const artifacts = {
                    user
                }

                return {
                    isValid,
                    credentials,
                    artifacts
                }
            } catch (error) {
                return error
            }
        }
    });

    try {
        await server.start();
        console.log('Server running at:', server.info.uri);
    } catch (err) {
        console.log(err);
    }

    server.route(routes)
})();