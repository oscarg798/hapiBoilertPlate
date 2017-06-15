
var _ = require("lodash");
var routes = [];
routes.push(require("./defaultRoute"));



module.exports = _.flatten(routes);