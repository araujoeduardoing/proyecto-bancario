const {
  shareAll,
  withModuleFederationPlugin,
} = require("@angular-architects/module-federation/webpack");

module.exports = withModuleFederationPlugin({
  name: "shell",

  remotes: {
    "clientes-mf": "clientes-mf@http://localhost:4201/remoteEntry.js",
    "reportes-mf": "reportes-mf@http://localhost:4204/remoteEntry.js",
  },

  shared: {
    ...shareAll({
      singleton: true,
      strictVersion: false,
      requiredVersion: "auto",
    }),
  },
});
