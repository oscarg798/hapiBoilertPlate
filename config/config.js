const config = {
    env: process.env.NODE_ENV || 'local',
    PORT: 3001,
    secret:'someScrect',
    dbName:'dbName'

};
module.exports = config;