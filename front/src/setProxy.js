const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://ec2-15-164-195-93.ap-northeast-2.compute.amazonaws.com:8080/',
            changeOrigin: true,
        })
    );
};