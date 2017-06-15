const Joi = require("joi");

module.exports = {
    method: "GET",
    path: "/{name}",
    config: {
        tags: ['api'],
        notes: [],
        validate: {
            params: {
                name: Joi.string().optional()
            }
        },
        handler: function(request,response){

            response('Hello: '+ request.params.name);
        }
    }
};