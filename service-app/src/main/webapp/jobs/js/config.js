steal.config({
    map: {
        "can/util/util": "can/util/jquery/jquery",
        "jquery/jquery": "jquery"
    },
    paths: {
        "jquery": "jquery/jquery.min.js",
        "can/*" : "can/*.js",
        "rxjs": "rxjs/rxjs.all.min.js"
    },
    shim : {
        jquery: {
            exports: "jQuery"
        }
    },
    ext: {
        js: "js",
        css: "css",
        mustache: "can/view/mustache/system"
    }
});