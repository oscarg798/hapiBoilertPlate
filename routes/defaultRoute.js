const Joi = require("joi");

module.exports = {
    method: "GET",
    path: "/hello",
    options: {
        tags: ['api'],
        handler: function (req, res){
            return { message: "Hello from happi"}
        }
    }
}