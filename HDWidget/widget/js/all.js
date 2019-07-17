var StompOverSock = function() {
    var stompProcessor;
    return {
        getInstance: function(isDdebug) {
            if (!stompProcessor) stompProcessor = initStompOverSock(isDdebug);
            return stompProcessor;
        }
    };
}();

function initStompOverSock(debugParam) {
    var debug = debugParam;
    if (debug) console.log("################ initStompOverSock method ! ###############");
    var endPoint = "/wss";
    var socket = new SockJS(endPoint);
    var stompClient = webstomp.over(socket);
    if (!debug) stompClient.debug = (() => {});
    var unsuccessSubscribeList = [];
    var unsuccessSendList = [];
    var connected = false;
    var connectHandler = function(asa) {
        if (debug) {
            console.log("################ Connect Handler method ! ###############");
            console.log("socket.httpCode AT connectHandler: ", SockJS.httpCode);
            console.log("asa", asa);
        }
        connected = true;
        if (unsuccessSubscribeList.length > 0) {
            unsuccessSubscribeList = unsuccessSubscribeList.removeDublicateObject("path");
            unsuccessSubscribeList.forEach(function(elt, i, array) {
                stompHolder.subscribe(elt.path, elt.callback);
            });
        }
        if (unsuccessSendList.length > 0) {
            unsuccessSendList = unsuccessSendList.removeDublicateObject("path");
            unsuccessSendList.forEach(function(elt, i, array) {
                stompHolder.send(elt.path, elt.body, elt.headers);
            });
        }
        if (debug) {
            console.log("unsuccessSubscribeList: ", unsuccessSubscribeList);
            console.log("unsuccessSendList: ", unsuccessSendList);
        }
    };
    var connectErrorHandler = function(errorMessage) {
        if (debug) {
            console.log("################ Error Handler method ! ###############");
            console.log("socket.httpCode AT ErrorHandler: ", SockJS.httpCode);
        }
        if (SockJS.httpCode == 401) {
            window.location = "/";
            return;
        }
        connected = false;
        socket = new SockJS(endPoint);
        stompClient = webstomp.over(socket);
        if (!debug) stompClient.debug = (() => {});
        stompClient.connect({}, mes => {
            connectHandler(mes);
        }, erorMes => {
            setTimeout(() => {
                connectErrorHandler(erorMes);
            }, 3e3);
        });
    };
    var stompHolder = {
        subscribeList: [],
        subscribe: function(path, callbackFunction) {
            var subscribeId;
            if (connected && this.subscribeList.indexOf(path) === -1) {
                subscribeId = stompClient.subscribe(path, callbackFunction);
                this.subscribeList.push({
                    path: path,
                    id: subscribeId,
                    callback: callbackFunction
                });
            } else unsuccessSubscribeList.push({
                path: path,
                id: null,
                callback: callbackFunction
            });
        },
        send: function(path, body, headers) {
            if (connected) {
                if (debug) console.log("################ SEND to :", path, "   body: ", body, "   headers: ", headers);
                stompClient.send(path, body, headers);
            } else unsuccessSendList.push({
                path: path,
                body: body,
                headers: headers
            });
        },
        disconnect: function(callbackFunction, headers) {
            stompClient.disconnect(callbackFunction, headers);
        }
    };
    stompClient.connect({}, function(mes) {
        connectHandler(mes);
    }, function(erorMes) {
        connectErrorHandler(erorMes);
    });
    return stompHolder;
}

(function(f) {
    if (typeof exports === "object" && typeof module !== "undefined") {
        module.exports = f();
    } else if (typeof define === "function" && define.amd) {
        define([], f);
    } else {
        var g;
        if (typeof window !== "undefined") {
            g = window;
        } else if (typeof global !== "undefined") {
            g = global;
        } else if (typeof self !== "undefined") {
            g = self;
        } else {
            g = this;
        }
        g.SockJS = f();
    }
})(function() {
    var define, module, exports;
    return function() {
        function r(e, n, t) {
            function o(i, f) {
                if (!n[i]) {
                    if (!e[i]) {
                        var c = "function" == typeof require && require;
                        if (!f && c) return c(i, !0);
                        if (u) return u(i, !0);
                        var a = new Error("Cannot find module '" + i + "'");
                        throw a.code = "MODULE_NOT_FOUND", a;
                    }
                    var p = n[i] = {
                        exports: {}
                    };
                    e[i][0].call(p.exports, function(r) {
                        var n = e[i][1][r];
                        return o(n || r);
                    }, p, p.exports, r, e, n, t);
                }
                return n[i].exports;
            }
            for (var u = "function" == typeof require && require, i = 0; i < t.length; i++) o(t[i]);
            return o;
        }
        return r;
    }()({
        1: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var transportList = require("./transport-list");
                module.exports = require("./main")(transportList);
                if ("_sockjs_onload" in global) {
                    setTimeout(global._sockjs_onload, 1);
                }
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./main": 14,
            "./transport-list": 16
        } ],
        2: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), Event = require("./event");
            function CloseEvent() {
                Event.call(this);
                this.initEvent("close", false, false);
                this.wasClean = false;
                this.code = 0;
                this.reason = "";
            }
            inherits(CloseEvent, Event);
            module.exports = CloseEvent;
        }, {
            "./event": 4,
            inherits: 57
        } ],
        3: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), EventTarget = require("./eventtarget");
            function EventEmitter() {
                EventTarget.call(this);
            }
            inherits(EventEmitter, EventTarget);
            EventEmitter.prototype.removeAllListeners = function(type) {
                if (type) {
                    delete this._listeners[type];
                } else {
                    this._listeners = {};
                }
            };
            EventEmitter.prototype.once = function(type, listener) {
                var self = this, fired = false;
                function g() {
                    self.removeListener(type, g);
                    if (!fired) {
                        fired = true;
                        listener.apply(this, arguments);
                    }
                }
                this.on(type, g);
            };
            EventEmitter.prototype.emit = function() {
                var type = arguments[0];
                var listeners = this._listeners[type];
                if (!listeners) {
                    return;
                }
                var l = arguments.length;
                var args = new Array(l - 1);
                for (var ai = 1; ai < l; ai++) {
                    args[ai - 1] = arguments[ai];
                }
                for (var i = 0; i < listeners.length; i++) {
                    listeners[i].apply(this, args);
                }
            };
            EventEmitter.prototype.on = EventEmitter.prototype.addListener = EventTarget.prototype.addEventListener;
            EventEmitter.prototype.removeListener = EventTarget.prototype.removeEventListener;
            module.exports.EventEmitter = EventEmitter;
        }, {
            "./eventtarget": 5,
            inherits: 57
        } ],
        4: [ function(require, module, exports) {
            "use strict";
            function Event(eventType) {
                this.type = eventType;
            }
            Event.prototype.initEvent = function(eventType, canBubble, cancelable) {
                this.type = eventType;
                this.bubbles = canBubble;
                this.cancelable = cancelable;
                this.timeStamp = +new Date();
                return this;
            };
            Event.prototype.stopPropagation = function() {};
            Event.prototype.preventDefault = function() {};
            Event.CAPTURING_PHASE = 1;
            Event.AT_TARGET = 2;
            Event.BUBBLING_PHASE = 3;
            module.exports = Event;
        }, {} ],
        5: [ function(require, module, exports) {
            "use strict";
            function EventTarget() {
                this._listeners = {};
            }
            EventTarget.prototype.addEventListener = function(eventType, listener) {
                if (!(eventType in this._listeners)) {
                    this._listeners[eventType] = [];
                }
                var arr = this._listeners[eventType];
                if (arr.indexOf(listener) === -1) {
                    arr = arr.concat([ listener ]);
                }
                this._listeners[eventType] = arr;
            };
            EventTarget.prototype.removeEventListener = function(eventType, listener) {
                var arr = this._listeners[eventType];
                if (!arr) {
                    return;
                }
                var idx = arr.indexOf(listener);
                if (idx !== -1) {
                    if (arr.length > 1) {
                        this._listeners[eventType] = arr.slice(0, idx).concat(arr.slice(idx + 1));
                    } else {
                        delete this._listeners[eventType];
                    }
                    return;
                }
            };
            EventTarget.prototype.dispatchEvent = function() {
                var event = arguments[0];
                var t = event.type;
                var args = arguments.length === 1 ? [ event ] : Array.apply(null, arguments);
                if (this["on" + t]) {
                    this["on" + t].apply(this, args);
                }
                if (t in this._listeners) {
                    var listeners = this._listeners[t];
                    for (var i = 0; i < listeners.length; i++) {
                        listeners[i].apply(this, args);
                    }
                }
            };
            module.exports = EventTarget;
        }, {} ],
        6: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), Event = require("./event");
            function TransportMessageEvent(data) {
                Event.call(this);
                this.initEvent("message", false, false);
                this.data = data;
            }
            inherits(TransportMessageEvent, Event);
            module.exports = TransportMessageEvent;
        }, {
            "./event": 4,
            inherits: 57
        } ],
        7: [ function(require, module, exports) {
            "use strict";
            var JSON3 = require("json3"), iframeUtils = require("./utils/iframe");
            function FacadeJS(transport) {
                this._transport = transport;
                transport.on("message", this._transportMessage.bind(this));
                transport.on("close", this._transportClose.bind(this));
            }
            FacadeJS.prototype._transportClose = function(code, reason) {
                iframeUtils.postMessage("c", JSON3.stringify([ code, reason ]));
            };
            FacadeJS.prototype._transportMessage = function(frame) {
                iframeUtils.postMessage("t", frame);
            };
            FacadeJS.prototype._send = function(data) {
                this._transport.send(data);
            };
            FacadeJS.prototype._close = function() {
                this._transport.close();
                this._transport.removeAllListeners();
            };
            module.exports = FacadeJS;
        }, {
            "./utils/iframe": 47,
            json3: 58
        } ],
        8: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var urlUtils = require("./utils/url"), eventUtils = require("./utils/event"), JSON3 = require("json3"), FacadeJS = require("./facade"), InfoIframeReceiver = require("./info-iframe-receiver"), iframeUtils = require("./utils/iframe"), loc = require("./location");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:iframe-bootstrap");
                }
                module.exports = function(SockJS, availableTransports) {
                    var transportMap = {};
                    availableTransports.forEach(function(at) {
                        if (at.facadeTransport) {
                            transportMap[at.facadeTransport.transportName] = at.facadeTransport;
                        }
                    });
                    transportMap[InfoIframeReceiver.transportName] = InfoIframeReceiver;
                    var parentOrigin;
                    SockJS.bootstrap_iframe = function() {
                        var facade;
                        iframeUtils.currentWindowId = loc.hash.slice(1);
                        var onMessage = function(e) {
                            if (e.source !== parent) {
                                return;
                            }
                            if (typeof parentOrigin === "undefined") {
                                parentOrigin = e.origin;
                            }
                            if (e.origin !== parentOrigin) {
                                return;
                            }
                            var iframeMessage;
                            try {
                                iframeMessage = JSON3.parse(e.data);
                            } catch (ignored) {
                                debug("bad json", e.data);
                                return;
                            }
                            if (iframeMessage.windowId !== iframeUtils.currentWindowId) {
                                return;
                            }
                            switch (iframeMessage.type) {
                              case "s":
                                var p;
                                try {
                                    p = JSON3.parse(iframeMessage.data);
                                } catch (ignored) {
                                    debug("bad json", iframeMessage.data);
                                    break;
                                }
                                var version = p[0];
                                var transport = p[1];
                                var transUrl = p[2];
                                var baseUrl = p[3];
                                debug(version, transport, transUrl, baseUrl);
                                if (version !== SockJS.version) {
                                    throw new Error("Incompatible SockJS! Main site uses:" + ' "' + version + '", the iframe:' + ' "' + SockJS.version + '".');
                                }
                                if (!urlUtils.isOriginEqual(transUrl, loc.href) || !urlUtils.isOriginEqual(baseUrl, loc.href)) {
                                    throw new Error("Can't connect to different domain from within an " + "iframe. (" + loc.href + ", " + transUrl + ", " + baseUrl + ")");
                                }
                                facade = new FacadeJS(new transportMap[transport](transUrl, baseUrl));
                                break;

                              case "m":
                                facade._send(iframeMessage.data);
                                break;

                              case "c":
                                if (facade) {
                                    facade._close();
                                }
                                facade = null;
                                break;
                            }
                        };
                        eventUtils.attachEvent("message", onMessage);
                        iframeUtils.postMessage("s");
                    };
                };
            }).call(this, {
                env: {}
            });
        }, {
            "./facade": 7,
            "./info-iframe-receiver": 10,
            "./location": 13,
            "./utils/event": 46,
            "./utils/iframe": 47,
            "./utils/url": 52,
            debug: 55,
            json3: 58
        } ],
        9: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var EventEmitter = require("events").EventEmitter, inherits = require("inherits"), JSON3 = require("json3"), objectUtils = require("./utils/object");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:info-ajax");
                }
                function InfoAjax(url, AjaxObject) {
                    EventEmitter.call(this);
                    var self = this;
                    var t0 = +new Date();
                    this.xo = new AjaxObject("GET", url);
                    this.xo.once("finish", function(status, text) {
                        var info, rtt;
                        if (status === 200) {
                            rtt = +new Date() - t0;
                            if (text) {
                                try {
                                    info = JSON3.parse(text);
                                } catch (e) {
                                    debug("bad json", text);
                                }
                            }
                            if (!objectUtils.isObject(info)) {
                                info = {};
                            }
                        }
                        self.emit("finish", info, rtt);
                        self.removeAllListeners();
                    });
                }
                inherits(InfoAjax, EventEmitter);
                InfoAjax.prototype.close = function() {
                    this.removeAllListeners();
                    this.xo.close();
                };
                module.exports = InfoAjax;
            }).call(this, {
                env: {}
            });
        }, {
            "./utils/object": 49,
            debug: 55,
            events: 3,
            inherits: 57,
            json3: 58
        } ],
        10: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), EventEmitter = require("events").EventEmitter, JSON3 = require("json3"), XHRLocalObject = require("./transport/sender/xhr-local"), InfoAjax = require("./info-ajax");
            function InfoReceiverIframe(transUrl) {
                var self = this;
                EventEmitter.call(this);
                this.ir = new InfoAjax(transUrl, XHRLocalObject);
                this.ir.once("finish", function(info, rtt) {
                    self.ir = null;
                    self.emit("message", JSON3.stringify([ info, rtt ]));
                });
            }
            inherits(InfoReceiverIframe, EventEmitter);
            InfoReceiverIframe.transportName = "iframe-info-receiver";
            InfoReceiverIframe.prototype.close = function() {
                if (this.ir) {
                    this.ir.close();
                    this.ir = null;
                }
                this.removeAllListeners();
            };
            module.exports = InfoReceiverIframe;
        }, {
            "./info-ajax": 9,
            "./transport/sender/xhr-local": 37,
            events: 3,
            inherits: 57,
            json3: 58
        } ],
        11: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var EventEmitter = require("events").EventEmitter, inherits = require("inherits"), JSON3 = require("json3"), utils = require("./utils/event"), IframeTransport = require("./transport/iframe"), InfoReceiverIframe = require("./info-iframe-receiver");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:info-iframe");
                }
                function InfoIframe(baseUrl, url) {
                    var self = this;
                    EventEmitter.call(this);
                    var go = function() {
                        var ifr = self.ifr = new IframeTransport(InfoReceiverIframe.transportName, url, baseUrl);
                        ifr.once("message", function(msg) {
                            if (msg) {
                                var d;
                                try {
                                    d = JSON3.parse(msg);
                                } catch (e) {
                                    debug("bad json", msg);
                                    self.emit("finish");
                                    self.close();
                                    return;
                                }
                                var info = d[0], rtt = d[1];
                                self.emit("finish", info, rtt);
                            }
                            self.close();
                        });
                        ifr.once("close", function() {
                            self.emit("finish");
                            self.close();
                        });
                    };
                    if (!global.document.body) {
                        utils.attachEvent("load", go);
                    } else {
                        go();
                    }
                }
                inherits(InfoIframe, EventEmitter);
                InfoIframe.enabled = function() {
                    return IframeTransport.enabled();
                };
                InfoIframe.prototype.close = function() {
                    if (this.ifr) {
                        this.ifr.close();
                    }
                    this.removeAllListeners();
                    this.ifr = null;
                };
                module.exports = InfoIframe;
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./info-iframe-receiver": 10,
            "./transport/iframe": 22,
            "./utils/event": 46,
            debug: 55,
            events: 3,
            inherits: 57,
            json3: 58
        } ],
        12: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var EventEmitter = require("events").EventEmitter, inherits = require("inherits"), urlUtils = require("./utils/url"), XDR = require("./transport/sender/xdr"), XHRCors = require("./transport/sender/xhr-cors"), XHRLocal = require("./transport/sender/xhr-local"), XHRFake = require("./transport/sender/xhr-fake"), InfoIframe = require("./info-iframe"), InfoAjax = require("./info-ajax");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:info-receiver");
                }
                function InfoReceiver(baseUrl, urlInfo) {
                    debug(baseUrl);
                    var self = this;
                    EventEmitter.call(this);
                    setTimeout(function() {
                        self.doXhr(baseUrl, urlInfo);
                    }, 0);
                }
                inherits(InfoReceiver, EventEmitter);
                InfoReceiver._getReceiver = function(baseUrl, url, urlInfo) {
                    if (urlInfo.sameOrigin) {
                        return new InfoAjax(url, XHRLocal);
                    }
                    if (XHRCors.enabled) {
                        return new InfoAjax(url, XHRCors);
                    }
                    if (XDR.enabled && urlInfo.sameScheme) {
                        return new InfoAjax(url, XDR);
                    }
                    if (InfoIframe.enabled()) {
                        return new InfoIframe(baseUrl, url);
                    }
                    return new InfoAjax(url, XHRFake);
                };
                InfoReceiver.prototype.doXhr = function(baseUrl, urlInfo) {
                    var self = this, url = urlUtils.addPath(baseUrl, "/info");
                    debug("doXhr", url);
                    this.xo = InfoReceiver._getReceiver(baseUrl, url, urlInfo);
                    this.timeoutRef = setTimeout(function() {
                        debug("timeout");
                        self._cleanup(false);
                        self.emit("finish");
                    }, InfoReceiver.timeout);
                    this.xo.once("finish", function(info, rtt) {
                        debug("finish", info, rtt);
                        self._cleanup(true);
                        self.emit("finish", info, rtt);
                    });
                };
                InfoReceiver.prototype._cleanup = function(wasClean) {
                    debug("_cleanup");
                    clearTimeout(this.timeoutRef);
                    this.timeoutRef = null;
                    if (!wasClean && this.xo) {
                        this.xo.close();
                    }
                    this.xo = null;
                };
                InfoReceiver.prototype.close = function() {
                    debug("close");
                    this.removeAllListeners();
                    this._cleanup(false);
                };
                InfoReceiver.timeout = 8e3;
                module.exports = InfoReceiver;
            }).call(this, {
                env: {}
            });
        }, {
            "./info-ajax": 9,
            "./info-iframe": 11,
            "./transport/sender/xdr": 34,
            "./transport/sender/xhr-cors": 35,
            "./transport/sender/xhr-fake": 36,
            "./transport/sender/xhr-local": 37,
            "./utils/url": 52,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        13: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                module.exports = global.location || {
                    origin: "http://localhost:80",
                    protocol: "http:",
                    host: "localhost",
                    port: 80,
                    href: "http://localhost/",
                    hash: ""
                };
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        14: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                require("./shims");
                var URL = require("url-parse"), inherits = require("inherits"), JSON3 = require("json3"), random = require("./utils/random"), escape = require("./utils/escape"), urlUtils = require("./utils/url"), eventUtils = require("./utils/event"), transport = require("./utils/transport"), objectUtils = require("./utils/object"), browser = require("./utils/browser"), log = require("./utils/log"), Event = require("./event/event"), EventTarget = require("./event/eventtarget"), loc = require("./location"), CloseEvent = require("./event/close"), TransportMessageEvent = require("./event/trans-message"), InfoReceiver = require("./info-receiver");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:main");
                }
                var transports;
                function SockJS(url, protocols, options) {
                    if (!(this instanceof SockJS)) {
                        return new SockJS(url, protocols, options);
                    }
                    if (arguments.length < 1) {
                        throw new TypeError("Failed to construct 'SockJS: 1 argument required, but only 0 present");
                    }
                    EventTarget.call(this);
                    this.readyState = SockJS.CONNECTING;
                    this.extensions = "";
                    this.protocol = "";
                    options = options || {};
                    if (options.protocols_whitelist) {
                        log.warn("'protocols_whitelist' is DEPRECATED. Use 'transports' instead.");
                    }
                    this._transportsWhitelist = options.transports;
                    this._transportOptions = options.transportOptions || {};
                    var sessionId = options.sessionId || 8;
                    if (typeof sessionId === "function") {
                        this._generateSessionId = sessionId;
                    } else if (typeof sessionId === "number") {
                        this._generateSessionId = function() {
                            return random.string(sessionId);
                        };
                    } else {
                        throw new TypeError("If sessionId is used in the options, it needs to be a number or a function.");
                    }
                    this._server = options.server || random.numberString(1e3);
                    var parsedUrl = new URL(url);
                    if (!parsedUrl.host || !parsedUrl.protocol) {
                        throw new SyntaxError("The URL '" + url + "' is invalid");
                    } else if (parsedUrl.hash) {
                        throw new SyntaxError("The URL must not contain a fragment");
                    } else if (parsedUrl.protocol !== "http:" && parsedUrl.protocol !== "https:") {
                        throw new SyntaxError("The URL's scheme must be either 'http:' or 'https:'. '" + parsedUrl.protocol + "' is not allowed.");
                    }
                    var secure = parsedUrl.protocol === "https:";
                    if (loc.protocol === "https:" && !secure) {
                        throw new Error("SecurityError: An insecure SockJS connection may not be initiated from a page loaded over HTTPS");
                    }
                    if (!protocols) {
                        protocols = [];
                    } else if (!Array.isArray(protocols)) {
                        protocols = [ protocols ];
                    }
                    var sortedProtocols = protocols.sort();
                    sortedProtocols.forEach(function(proto, i) {
                        if (!proto) {
                            throw new SyntaxError("The protocols entry '" + proto + "' is invalid.");
                        }
                        if (i < sortedProtocols.length - 1 && proto === sortedProtocols[i + 1]) {
                            throw new SyntaxError("The protocols entry '" + proto + "' is duplicated.");
                        }
                    });
                    var o = urlUtils.getOrigin(loc.href);
                    this._origin = o ? o.toLowerCase() : null;
                    parsedUrl.set("pathname", parsedUrl.pathname.replace(/\/+$/, ""));
                    this.url = parsedUrl.href;
                    debug("using url", this.url);
                    this._urlInfo = {
                        nullOrigin: !browser.hasDomain(),
                        sameOrigin: urlUtils.isOriginEqual(this.url, loc.href),
                        sameScheme: urlUtils.isSchemeEqual(this.url, loc.href)
                    };
                    this._ir = new InfoReceiver(this.url, this._urlInfo);
                    this._ir.once("finish", this._receiveInfo.bind(this));
                }
                inherits(SockJS, EventTarget);
                function userSetCode(code) {
                    return code === 1e3 || code >= 3e3 && code <= 4999;
                }
                SockJS.prototype.close = function(code, reason) {
                    if (code && !userSetCode(code)) {
                        throw new Error("InvalidAccessError: Invalid code");
                    }
                    if (reason && reason.length > 123) {
                        throw new SyntaxError("reason argument has an invalid length");
                    }
                    if (this.readyState === SockJS.CLOSING || this.readyState === SockJS.CLOSED) {
                        return;
                    }
                    var wasClean = true;
                    this._close(code || 1e3, reason || "Normal closure", wasClean);
                };
                SockJS.prototype.send = function(data) {
                    if (typeof data !== "string") {
                        data = "" + data;
                    }
                    if (this.readyState === SockJS.CONNECTING) {
                        throw new Error("InvalidStateError: The connection has not been established yet");
                    }
                    if (this.readyState !== SockJS.OPEN) {
                        return;
                    }
                    this._transport.send(escape.quote(data));
                };
                SockJS.version = require("./version");
                SockJS.CONNECTING = 0;
                SockJS.OPEN = 1;
                SockJS.CLOSING = 2;
                SockJS.CLOSED = 3;
                SockJS.prototype._receiveInfo = function(info, rtt) {
                    debug("_receiveInfo", rtt);
                    this._ir = null;
                    if (!info) {
                        this._close(1002, "Cannot connect to server");
                        return;
                    }
                    this._rto = this.countRTO(rtt);
                    this._transUrl = info.base_url ? info.base_url : this.url;
                    info = objectUtils.extend(info, this._urlInfo);
                    debug("info", info);
                    var enabledTransports = transports.filterToEnabled(this._transportsWhitelist, info);
                    this._transports = enabledTransports.main;
                    debug(this._transports.length + " enabled transports");
                    this._connect();
                };
                SockJS.prototype._connect = function() {
                    for (var Transport = this._transports.shift(); Transport; Transport = this._transports.shift()) {
                        debug("attempt", Transport.transportName);
                        if (Transport.needBody) {
                            if (!global.document.body || typeof global.document.readyState !== "undefined" && global.document.readyState !== "complete" && global.document.readyState !== "interactive") {
                                debug("waiting for body");
                                this._transports.unshift(Transport);
                                eventUtils.attachEvent("load", this._connect.bind(this));
                                return;
                            }
                        }
                        var timeoutMs = this._rto * Transport.roundTrips || 5e3;
                        this._transportTimeoutId = setTimeout(this._transportTimeout.bind(this), timeoutMs);
                        debug("using timeout", timeoutMs);
                        var transportUrl = urlUtils.addPath(this._transUrl, "/" + this._server + "/" + this._generateSessionId());
                        var options = this._transportOptions[Transport.transportName];
                        debug("transport url", transportUrl);
                        var transportObj = new Transport(transportUrl, this._transUrl, options);
                        transportObj.on("message", this._transportMessage.bind(this));
                        transportObj.once("close", this._transportClose.bind(this));
                        transportObj.transportName = Transport.transportName;
                        this._transport = transportObj;
                        return;
                    }
                    this._close(2e3, "All transports failed", false);
                };
                SockJS.prototype._transportTimeout = function() {
                    debug("_transportTimeout");
                    if (this.readyState === SockJS.CONNECTING) {
                        if (this._transport) {
                            this._transport.close();
                        }
                        this._transportClose(2007, "Transport timed out");
                    }
                };
                SockJS.prototype._transportMessage = function(msg) {
                    debug("_transportMessage", msg);
                    var self = this, type = msg.slice(0, 1), content = msg.slice(1), payload;
                    switch (type) {
                      case "o":
                        this._open();
                        return;

                      case "h":
                        this.dispatchEvent(new Event("heartbeat"));
                        debug("heartbeat", this.transport);
                        return;
                    }
                    if (content) {
                        try {
                            payload = JSON3.parse(content);
                        } catch (e) {
                            debug("bad json", content);
                        }
                    }
                    if (typeof payload === "undefined") {
                        debug("empty payload", content);
                        return;
                    }
                    switch (type) {
                      case "a":
                        if (Array.isArray(payload)) {
                            payload.forEach(function(p) {
                                debug("message", self.transport, p);
                                self.dispatchEvent(new TransportMessageEvent(p));
                            });
                        }
                        break;

                      case "m":
                        debug("message", this.transport, payload);
                        this.dispatchEvent(new TransportMessageEvent(payload));
                        break;

                      case "c":
                        if (Array.isArray(payload) && payload.length === 2) {
                            this._close(payload[0], payload[1], true);
                        }
                        break;
                    }
                };
                SockJS.prototype._transportClose = function(code, reason) {
                    debug("_transportClose", this.transport, code, reason);
                    if (this._transport) {
                        this._transport.removeAllListeners();
                        this._transport = null;
                        this.transport = null;
                    }
                    if (!userSetCode(code) && code !== 2e3 && this.readyState === SockJS.CONNECTING) {
                        this._connect();
                        return;
                    }
                    this._close(code, reason);
                };
                SockJS.prototype._open = function() {
                    debug("_open", this._transport.transportName, this.readyState);
                    if (this.readyState === SockJS.CONNECTING) {
                        if (this._transportTimeoutId) {
                            clearTimeout(this._transportTimeoutId);
                            this._transportTimeoutId = null;
                        }
                        this.readyState = SockJS.OPEN;
                        this.transport = this._transport.transportName;
                        this.dispatchEvent(new Event("open"));
                        debug("connected", this.transport);
                    } else {
                        this._close(1006, "Server lost session");
                    }
                };
                SockJS.prototype._close = function(code, reason, wasClean) {
                    debug("_close", this.transport, code, reason, wasClean, this.readyState);
                    var forceFail = false;
                    if (this._ir) {
                        forceFail = true;
                        this._ir.close();
                        this._ir = null;
                    }
                    if (this._transport) {
                        this._transport.close();
                        this._transport = null;
                        this.transport = null;
                    }
                    if (this.readyState === SockJS.CLOSED) {
                        throw new Error("InvalidStateError: SockJS has already been closed");
                    }
                    this.readyState = SockJS.CLOSING;
                    setTimeout(function() {
                        this.readyState = SockJS.CLOSED;
                        if (forceFail) {
                            this.dispatchEvent(new Event("error"));
                        }
                        var e = new CloseEvent("close");
                        e.wasClean = wasClean || false;
                        e.code = code || 1e3;
                        e.reason = reason;
                        this.dispatchEvent(e);
                        this.onmessage = this.onclose = this.onerror = null;
                        debug("disconnected");
                    }.bind(this), 0);
                };
                SockJS.prototype.countRTO = function(rtt) {
                    if (rtt > 100) {
                        return 4 * rtt;
                    }
                    return 300 + rtt;
                };
                module.exports = function(availableTransports) {
                    transports = transport(availableTransports);
                    require("./iframe-bootstrap")(SockJS, availableTransports);
                    return SockJS;
                };
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./event/close": 2,
            "./event/event": 4,
            "./event/eventtarget": 5,
            "./event/trans-message": 6,
            "./iframe-bootstrap": 8,
            "./info-receiver": 12,
            "./location": 13,
            "./shims": 15,
            "./utils/browser": 44,
            "./utils/escape": 45,
            "./utils/event": 46,
            "./utils/log": 48,
            "./utils/object": 49,
            "./utils/random": 50,
            "./utils/transport": 51,
            "./utils/url": 52,
            "./version": 53,
            debug: 55,
            inherits: 57,
            json3: 58,
            "url-parse": 61
        } ],
        15: [ function(require, module, exports) {
            "use strict";
            var ArrayPrototype = Array.prototype;
            var ObjectPrototype = Object.prototype;
            var FunctionPrototype = Function.prototype;
            var StringPrototype = String.prototype;
            var array_slice = ArrayPrototype.slice;
            var _toString = ObjectPrototype.toString;
            var isFunction = function(val) {
                return ObjectPrototype.toString.call(val) === "[object Function]";
            };
            var isArray = function isArray(obj) {
                return _toString.call(obj) === "[object Array]";
            };
            var isString = function isString(obj) {
                return _toString.call(obj) === "[object String]";
            };
            var supportsDescriptors = Object.defineProperty && function() {
                try {
                    Object.defineProperty({}, "x", {});
                    return true;
                } catch (e) {
                    return false;
                }
            }();
            var defineProperty;
            if (supportsDescriptors) {
                defineProperty = function(object, name, method, forceAssign) {
                    if (!forceAssign && name in object) {
                        return;
                    }
                    Object.defineProperty(object, name, {
                        configurable: true,
                        enumerable: false,
                        writable: true,
                        value: method
                    });
                };
            } else {
                defineProperty = function(object, name, method, forceAssign) {
                    if (!forceAssign && name in object) {
                        return;
                    }
                    object[name] = method;
                };
            }
            var defineProperties = function(object, map, forceAssign) {
                for (var name in map) {
                    if (ObjectPrototype.hasOwnProperty.call(map, name)) {
                        defineProperty(object, name, map[name], forceAssign);
                    }
                }
            };
            var toObject = function(o) {
                if (o == null) {
                    throw new TypeError("can't convert " + o + " to object");
                }
                return Object(o);
            };
            function toInteger(num) {
                var n = +num;
                if (n !== n) {
                    n = 0;
                } else if (n !== 0 && n !== 1 / 0 && n !== -(1 / 0)) {
                    n = (n > 0 || -1) * Math.floor(Math.abs(n));
                }
                return n;
            }
            function ToUint32(x) {
                return x >>> 0;
            }
            function Empty() {}
            defineProperties(FunctionPrototype, {
                bind: function bind(that) {
                    var target = this;
                    if (!isFunction(target)) {
                        throw new TypeError("Function.prototype.bind called on incompatible " + target);
                    }
                    var args = array_slice.call(arguments, 1);
                    var binder = function() {
                        if (this instanceof bound) {
                            var result = target.apply(this, args.concat(array_slice.call(arguments)));
                            if (Object(result) === result) {
                                return result;
                            }
                            return this;
                        } else {
                            return target.apply(that, args.concat(array_slice.call(arguments)));
                        }
                    };
                    var boundLength = Math.max(0, target.length - args.length);
                    var boundArgs = [];
                    for (var i = 0; i < boundLength; i++) {
                        boundArgs.push("$" + i);
                    }
                    var bound = Function("binder", "return function (" + boundArgs.join(",") + "){ return binder.apply(this, arguments); }")(binder);
                    if (target.prototype) {
                        Empty.prototype = target.prototype;
                        bound.prototype = new Empty();
                        Empty.prototype = null;
                    }
                    return bound;
                }
            });
            defineProperties(Array, {
                isArray: isArray
            });
            var boxedString = Object("a");
            var splitString = boxedString[0] !== "a" || !(0 in boxedString);
            var properlyBoxesContext = function properlyBoxed(method) {
                var properlyBoxesNonStrict = true;
                var properlyBoxesStrict = true;
                if (method) {
                    method.call("foo", function(_, __, context) {
                        if (typeof context !== "object") {
                            properlyBoxesNonStrict = false;
                        }
                    });
                    method.call([ 1 ], function() {
                        "use strict";
                        properlyBoxesStrict = typeof this === "string";
                    }, "x");
                }
                return !!method && properlyBoxesNonStrict && properlyBoxesStrict;
            };
            defineProperties(ArrayPrototype, {
                forEach: function forEach(fun) {
                    var object = toObject(this), self = splitString && isString(this) ? this.split("") : object, thisp = arguments[1], i = -1, length = self.length >>> 0;
                    if (!isFunction(fun)) {
                        throw new TypeError();
                    }
                    while (++i < length) {
                        if (i in self) {
                            fun.call(thisp, self[i], i, object);
                        }
                    }
                }
            }, !properlyBoxesContext(ArrayPrototype.forEach));
            var hasFirefox2IndexOfBug = Array.prototype.indexOf && [ 0, 1 ].indexOf(1, 2) !== -1;
            defineProperties(ArrayPrototype, {
                indexOf: function indexOf(sought) {
                    var self = splitString && isString(this) ? this.split("") : toObject(this), length = self.length >>> 0;
                    if (!length) {
                        return -1;
                    }
                    var i = 0;
                    if (arguments.length > 1) {
                        i = toInteger(arguments[1]);
                    }
                    i = i >= 0 ? i : Math.max(0, length + i);
                    for (;i < length; i++) {
                        if (i in self && self[i] === sought) {
                            return i;
                        }
                    }
                    return -1;
                }
            }, hasFirefox2IndexOfBug);
            var string_split = StringPrototype.split;
            if ("ab".split(/(?:ab)*/).length !== 2 || ".".split(/(.?)(.?)/).length !== 4 || "tesst".split(/(s)*/)[1] === "t" || "test".split(/(?:)/, -1).length !== 4 || "".split(/.?/).length || ".".split(/()()/).length > 1) {
                (function() {
                    var compliantExecNpcg = /()??/.exec("")[1] === void 0;
                    StringPrototype.split = function(separator, limit) {
                        var string = this;
                        if (separator === void 0 && limit === 0) {
                            return [];
                        }
                        if (_toString.call(separator) !== "[object RegExp]") {
                            return string_split.call(this, separator, limit);
                        }
                        var output = [], flags = (separator.ignoreCase ? "i" : "") + (separator.multiline ? "m" : "") + (separator.extended ? "x" : "") + (separator.sticky ? "y" : ""), lastLastIndex = 0, separator2, match, lastIndex, lastLength;
                        separator = new RegExp(separator.source, flags + "g");
                        string += "";
                        if (!compliantExecNpcg) {
                            separator2 = new RegExp("^" + separator.source + "$(?!\\s)", flags);
                        }
                        limit = limit === void 0 ? -1 >>> 0 : ToUint32(limit);
                        while (match = separator.exec(string)) {
                            lastIndex = match.index + match[0].length;
                            if (lastIndex > lastLastIndex) {
                                output.push(string.slice(lastLastIndex, match.index));
                                if (!compliantExecNpcg && match.length > 1) {
                                    match[0].replace(separator2, function() {
                                        for (var i = 1; i < arguments.length - 2; i++) {
                                            if (arguments[i] === void 0) {
                                                match[i] = void 0;
                                            }
                                        }
                                    });
                                }
                                if (match.length > 1 && match.index < string.length) {
                                    ArrayPrototype.push.apply(output, match.slice(1));
                                }
                                lastLength = match[0].length;
                                lastLastIndex = lastIndex;
                                if (output.length >= limit) {
                                    break;
                                }
                            }
                            if (separator.lastIndex === match.index) {
                                separator.lastIndex++;
                            }
                        }
                        if (lastLastIndex === string.length) {
                            if (lastLength || !separator.test("")) {
                                output.push("");
                            }
                        } else {
                            output.push(string.slice(lastLastIndex));
                        }
                        return output.length > limit ? output.slice(0, limit) : output;
                    };
                })();
            } else if ("0".split(void 0, 0).length) {
                StringPrototype.split = function split(separator, limit) {
                    if (separator === void 0 && limit === 0) {
                        return [];
                    }
                    return string_split.call(this, separator, limit);
                };
            }
            var string_substr = StringPrototype.substr;
            var hasNegativeSubstrBug = "".substr && "0b".substr(-1) !== "b";
            defineProperties(StringPrototype, {
                substr: function substr(start, length) {
                    return string_substr.call(this, start < 0 ? (start = this.length + start) < 0 ? 0 : start : start, length);
                }
            }, hasNegativeSubstrBug);
        }, {} ],
        16: [ function(require, module, exports) {
            "use strict";
            module.exports = [ require("./transport/websocket"), require("./transport/xhr-streaming"), require("./transport/xdr-streaming"), require("./transport/eventsource"), require("./transport/lib/iframe-wrap")(require("./transport/eventsource")), require("./transport/htmlfile"), require("./transport/lib/iframe-wrap")(require("./transport/htmlfile")), require("./transport/xhr-polling"), require("./transport/xdr-polling"), require("./transport/lib/iframe-wrap")(require("./transport/xhr-polling")), require("./transport/jsonp-polling") ];
        }, {
            "./transport/eventsource": 20,
            "./transport/htmlfile": 21,
            "./transport/jsonp-polling": 23,
            "./transport/lib/iframe-wrap": 26,
            "./transport/websocket": 38,
            "./transport/xdr-polling": 39,
            "./transport/xdr-streaming": 40,
            "./transport/xhr-polling": 41,
            "./transport/xhr-streaming": 42
        } ],
        17: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var EventEmitter = require("events").EventEmitter, inherits = require("inherits"), utils = require("../../utils/event"), urlUtils = require("../../utils/url"), XHR = global.XMLHttpRequest;
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:browser:xhr");
                }
                function AbstractXHRObject(method, url, payload, opts) {
                    debug(method, url);
                    var self = this;
                    EventEmitter.call(this);
                    setTimeout(function() {
                        self._start(method, url, payload, opts);
                    }, 0);
                }
                inherits(AbstractXHRObject, EventEmitter);
                AbstractXHRObject.prototype._start = function(method, url, payload, opts) {
                    var self = this;
                    try {
                        this.xhr = new XHR();
                    } catch (x) {}
                    if (!this.xhr) {
                        debug("no xhr");
                        this.emit("finish", 0, "no xhr support");
                        this._cleanup();
                        return;
                    }
                    url = urlUtils.addQuery(url, "t=" + +new Date());
                    this.unloadRef = utils.unloadAdd(function() {
                        debug("unload cleanup");
                        self._cleanup(true);
                    });
                    try {
                        this.xhr.open(method, url, true);
                        if (this.timeout && "timeout" in this.xhr) {
                            this.xhr.timeout = this.timeout;
                            this.xhr.ontimeout = function() {
                                debug("xhr timeout");
                                self.emit("finish", 0, "");
                                self._cleanup(false);
                            };
                        }
                    } catch (e) {
                        debug("exception", e);
                        this.emit("finish", 0, "");
                        this._cleanup(false);
                        return;
                    }
                    if ((!opts || !opts.noCredentials) && AbstractXHRObject.supportsCORS) {
                        debug("withCredentials");
                        this.xhr.withCredentials = true;
                    }
                    if (opts && opts.headers) {
                        for (var key in opts.headers) {
                            this.xhr.setRequestHeader(key, opts.headers[key]);
                        }
                    }
                    this.xhr.onreadystatechange = function() {
                        if (self.xhr) {
                            var x = self.xhr;
                            var text, status;
                            debug("readyState", x.readyState);
                            SockJS.httpCode = x.status;
                            switch (x.readyState) {
                              case 3:
                                try {
                                    status = x.status;
                                    text = x.responseText;
                                } catch (e) {}
                                debug("status", status);
                                if (status === 1223) {
                                    status = 204;
                                }
                                if (status === 200 && text && text.length > 0) {
                                    debug("chunk");
                                    self.emit("chunk", status, text);
                                }
                                break;

                              case 4:
                                status = x.status;
                                debug("status", status);
                                if (status === 1223) {
                                    status = 204;
                                }
                                if (status === 12005 || status === 12029) {
                                    status = 0;
                                }
                                debug("finish", status, x.responseText);
                                self.emit("finish", status, x.responseText);
                                self._cleanup(false);
                                break;
                            }
                        }
                    };
                    try {
                        self.xhr.send(payload);
                    } catch (e) {
                        self.emit("finish", 0, "");
                        self._cleanup(false);
                    }
                };
                AbstractXHRObject.prototype._cleanup = function(abort) {
                    debug("cleanup");
                    if (!this.xhr) {
                        return;
                    }
                    this.removeAllListeners();
                    utils.unloadDel(this.unloadRef);
                    this.xhr.onreadystatechange = function() {};
                    if (this.xhr.ontimeout) {
                        this.xhr.ontimeout = null;
                    }
                    if (abort) {
                        try {
                            this.xhr.abort();
                        } catch (x) {}
                    }
                    this.unloadRef = this.xhr = null;
                };
                AbstractXHRObject.prototype.close = function() {
                    debug("close");
                    this._cleanup(true);
                };
                AbstractXHRObject.enabled = !!XHR;
                var axo = [ "Active" ].concat("Object").join("X");
                if (!AbstractXHRObject.enabled && axo in global) {
                    debug("overriding xmlhttprequest");
                    XHR = function() {
                        try {
                            return new global[axo]("Microsoft.XMLHTTP");
                        } catch (e) {
                            return null;
                        }
                    };
                    AbstractXHRObject.enabled = !!new XHR();
                }
                var cors = false;
                try {
                    cors = "withCredentials" in new XHR();
                } catch (ignored) {}
                AbstractXHRObject.supportsCORS = cors;
                module.exports = AbstractXHRObject;
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/event": 46,
            "../../utils/url": 52,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        18: [ function(require, module, exports) {
            (function(global) {
                module.exports = global.EventSource;
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        19: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var Driver = global.WebSocket || global.MozWebSocket;
                if (Driver) {
                    module.exports = function WebSocketBrowserDriver(url) {
                        return new Driver(url);
                    };
                } else {
                    module.exports = undefined;
                }
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        20: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), AjaxBasedTransport = require("./lib/ajax-based"), EventSourceReceiver = require("./receiver/eventsource"), XHRCorsObject = require("./sender/xhr-cors"), EventSourceDriver = require("eventsource");
            function EventSourceTransport(transUrl) {
                if (!EventSourceTransport.enabled()) {
                    throw new Error("Transport created when disabled");
                }
                AjaxBasedTransport.call(this, transUrl, "/eventsource", EventSourceReceiver, XHRCorsObject);
            }
            inherits(EventSourceTransport, AjaxBasedTransport);
            EventSourceTransport.enabled = function() {
                return !!EventSourceDriver;
            };
            EventSourceTransport.transportName = "eventsource";
            EventSourceTransport.roundTrips = 2;
            module.exports = EventSourceTransport;
        }, {
            "./lib/ajax-based": 24,
            "./receiver/eventsource": 29,
            "./sender/xhr-cors": 35,
            eventsource: 18,
            inherits: 57
        } ],
        21: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), HtmlfileReceiver = require("./receiver/htmlfile"), XHRLocalObject = require("./sender/xhr-local"), AjaxBasedTransport = require("./lib/ajax-based");
            function HtmlFileTransport(transUrl) {
                if (!HtmlfileReceiver.enabled) {
                    throw new Error("Transport created when disabled");
                }
                AjaxBasedTransport.call(this, transUrl, "/htmlfile", HtmlfileReceiver, XHRLocalObject);
            }
            inherits(HtmlFileTransport, AjaxBasedTransport);
            HtmlFileTransport.enabled = function(info) {
                return HtmlfileReceiver.enabled && info.sameOrigin;
            };
            HtmlFileTransport.transportName = "htmlfile";
            HtmlFileTransport.roundTrips = 2;
            module.exports = HtmlFileTransport;
        }, {
            "./lib/ajax-based": 24,
            "./receiver/htmlfile": 30,
            "./sender/xhr-local": 37,
            inherits: 57
        } ],
        22: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), JSON3 = require("json3"), EventEmitter = require("events").EventEmitter, version = require("../version"), urlUtils = require("../utils/url"), iframeUtils = require("../utils/iframe"), eventUtils = require("../utils/event"), random = require("../utils/random");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:transport:iframe");
                }
                function IframeTransport(transport, transUrl, baseUrl) {
                    if (!IframeTransport.enabled()) {
                        throw new Error("Transport created when disabled");
                    }
                    EventEmitter.call(this);
                    var self = this;
                    this.origin = urlUtils.getOrigin(baseUrl);
                    this.baseUrl = baseUrl;
                    this.transUrl = transUrl;
                    this.transport = transport;
                    this.windowId = random.string(8);
                    var iframeUrl = urlUtils.addPath(baseUrl, "/iframe.html") + "#" + this.windowId;
                    debug(transport, transUrl, iframeUrl);
                    this.iframeObj = iframeUtils.createIframe(iframeUrl, function(r) {
                        debug("err callback");
                        self.emit("close", 1006, "Unable to load an iframe (" + r + ")");
                        self.close();
                    });
                    this.onmessageCallback = this._message.bind(this);
                    eventUtils.attachEvent("message", this.onmessageCallback);
                }
                inherits(IframeTransport, EventEmitter);
                IframeTransport.prototype.close = function() {
                    debug("close");
                    this.removeAllListeners();
                    if (this.iframeObj) {
                        eventUtils.detachEvent("message", this.onmessageCallback);
                        try {
                            this.postMessage("c");
                        } catch (x) {}
                        this.iframeObj.cleanup();
                        this.iframeObj = null;
                        this.onmessageCallback = this.iframeObj = null;
                    }
                };
                IframeTransport.prototype._message = function(e) {
                    debug("message", e.data);
                    if (!urlUtils.isOriginEqual(e.origin, this.origin)) {
                        debug("not same origin", e.origin, this.origin);
                        return;
                    }
                    var iframeMessage;
                    try {
                        iframeMessage = JSON3.parse(e.data);
                    } catch (ignored) {
                        debug("bad json", e.data);
                        return;
                    }
                    if (iframeMessage.windowId !== this.windowId) {
                        debug("mismatched window id", iframeMessage.windowId, this.windowId);
                        return;
                    }
                    switch (iframeMessage.type) {
                      case "s":
                        this.iframeObj.loaded();
                        this.postMessage("s", JSON3.stringify([ version, this.transport, this.transUrl, this.baseUrl ]));
                        break;

                      case "t":
                        this.emit("message", iframeMessage.data);
                        break;

                      case "c":
                        var cdata;
                        try {
                            cdata = JSON3.parse(iframeMessage.data);
                        } catch (ignored) {
                            debug("bad json", iframeMessage.data);
                            return;
                        }
                        this.emit("close", cdata[0], cdata[1]);
                        this.close();
                        break;
                    }
                };
                IframeTransport.prototype.postMessage = function(type, data) {
                    debug("postMessage", type, data);
                    this.iframeObj.post(JSON3.stringify({
                        windowId: this.windowId,
                        type: type,
                        data: data || ""
                    }), this.origin);
                };
                IframeTransport.prototype.send = function(message) {
                    debug("send", message);
                    this.postMessage("m", message);
                };
                IframeTransport.enabled = function() {
                    return iframeUtils.iframeEnabled;
                };
                IframeTransport.transportName = "iframe";
                IframeTransport.roundTrips = 2;
                module.exports = IframeTransport;
            }).call(this, {
                env: {}
            });
        }, {
            "../utils/event": 46,
            "../utils/iframe": 47,
            "../utils/random": 50,
            "../utils/url": 52,
            "../version": 53,
            debug: 55,
            events: 3,
            inherits: 57,
            json3: 58
        } ],
        23: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var inherits = require("inherits"), SenderReceiver = require("./lib/sender-receiver"), JsonpReceiver = require("./receiver/jsonp"), jsonpSender = require("./sender/jsonp");
                function JsonPTransport(transUrl) {
                    if (!JsonPTransport.enabled()) {
                        throw new Error("Transport created when disabled");
                    }
                    SenderReceiver.call(this, transUrl, "/jsonp", jsonpSender, JsonpReceiver);
                }
                inherits(JsonPTransport, SenderReceiver);
                JsonPTransport.enabled = function() {
                    return !!global.document;
                };
                JsonPTransport.transportName = "jsonp-polling";
                JsonPTransport.roundTrips = 1;
                JsonPTransport.needBody = true;
                module.exports = JsonPTransport;
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./lib/sender-receiver": 28,
            "./receiver/jsonp": 31,
            "./sender/jsonp": 33,
            inherits: 57
        } ],
        24: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), urlUtils = require("../../utils/url"), SenderReceiver = require("./sender-receiver");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:ajax-based");
                }
                function createAjaxSender(AjaxObject) {
                    return function(url, payload, callback) {
                        debug("create ajax sender", url, payload);
                        var opt = {};
                        if (typeof payload === "string") {
                            opt.headers = {
                                "Content-type": "text/plain"
                            };
                        }
                        var ajaxUrl = urlUtils.addPath(url, "/xhr_send");
                        var xo = new AjaxObject("POST", ajaxUrl, payload, opt);
                        xo.once("finish", function(status) {
                            debug("finish", status);
                            xo = null;
                            if (status !== 200 && status !== 204) {
                                return callback(new Error("http status " + status));
                            }
                            callback();
                        });
                        return function() {
                            debug("abort");
                            xo.close();
                            xo = null;
                            var err = new Error("Aborted");
                            err.code = 1e3;
                            callback(err);
                        };
                    };
                }
                function AjaxBasedTransport(transUrl, urlSuffix, Receiver, AjaxObject) {
                    SenderReceiver.call(this, transUrl, urlSuffix, createAjaxSender(AjaxObject), Receiver, AjaxObject);
                }
                inherits(AjaxBasedTransport, SenderReceiver);
                module.exports = AjaxBasedTransport;
            }).call(this, {
                env: {}
            });
        }, {
            "../../utils/url": 52,
            "./sender-receiver": 28,
            debug: 55,
            inherits: 57
        } ],
        25: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), EventEmitter = require("events").EventEmitter;
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:buffered-sender");
                }
                function BufferedSender(url, sender) {
                    debug(url);
                    EventEmitter.call(this);
                    this.sendBuffer = [];
                    this.sender = sender;
                    this.url = url;
                }
                inherits(BufferedSender, EventEmitter);
                BufferedSender.prototype.send = function(message) {
                    debug("send", message);
                    this.sendBuffer.push(message);
                    if (!this.sendStop) {
                        this.sendSchedule();
                    }
                };
                BufferedSender.prototype.sendScheduleWait = function() {
                    debug("sendScheduleWait");
                    var self = this;
                    var tref;
                    this.sendStop = function() {
                        debug("sendStop");
                        self.sendStop = null;
                        clearTimeout(tref);
                    };
                    tref = setTimeout(function() {
                        debug("timeout");
                        self.sendStop = null;
                        self.sendSchedule();
                    }, 25);
                };
                BufferedSender.prototype.sendSchedule = function() {
                    debug("sendSchedule", this.sendBuffer.length);
                    var self = this;
                    if (this.sendBuffer.length > 0) {
                        var payload = "[" + this.sendBuffer.join(",") + "]";
                        this.sendStop = this.sender(this.url, payload, function(err) {
                            self.sendStop = null;
                            if (err) {
                                debug("error", err);
                                self.emit("close", err.code || 1006, "Sending error: " + err);
                                self.close();
                            } else {
                                self.sendScheduleWait();
                            }
                        });
                        this.sendBuffer = [];
                    }
                };
                BufferedSender.prototype._cleanup = function() {
                    debug("_cleanup");
                    this.removeAllListeners();
                };
                BufferedSender.prototype.close = function() {
                    debug("close");
                    this._cleanup();
                    if (this.sendStop) {
                        this.sendStop();
                        this.sendStop = null;
                    }
                };
                module.exports = BufferedSender;
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        26: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var inherits = require("inherits"), IframeTransport = require("../iframe"), objectUtils = require("../../utils/object");
                module.exports = function(transport) {
                    function IframeWrapTransport(transUrl, baseUrl) {
                        IframeTransport.call(this, transport.transportName, transUrl, baseUrl);
                    }
                    inherits(IframeWrapTransport, IframeTransport);
                    IframeWrapTransport.enabled = function(url, info) {
                        if (!global.document) {
                            return false;
                        }
                        var iframeInfo = objectUtils.extend({}, info);
                        iframeInfo.sameOrigin = true;
                        return transport.enabled(iframeInfo) && IframeTransport.enabled();
                    };
                    IframeWrapTransport.transportName = "iframe-" + transport.transportName;
                    IframeWrapTransport.needBody = true;
                    IframeWrapTransport.roundTrips = IframeTransport.roundTrips + transport.roundTrips - 1;
                    IframeWrapTransport.facadeTransport = transport;
                    return IframeWrapTransport;
                };
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/object": 49,
            "../iframe": 22,
            inherits: 57
        } ],
        27: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), EventEmitter = require("events").EventEmitter;
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:polling");
                }
                function Polling(Receiver, receiveUrl, AjaxObject) {
                    debug(receiveUrl);
                    EventEmitter.call(this);
                    this.Receiver = Receiver;
                    this.receiveUrl = receiveUrl;
                    this.AjaxObject = AjaxObject;
                    this._scheduleReceiver();
                }
                inherits(Polling, EventEmitter);
                Polling.prototype._scheduleReceiver = function() {
                    debug("_scheduleReceiver");
                    var self = this;
                    var poll = this.poll = new this.Receiver(this.receiveUrl, this.AjaxObject);
                    poll.on("message", function(msg) {
                        debug("message", msg);
                        self.emit("message", msg);
                    });
                    poll.once("close", function(code, reason) {
                        debug("close", code, reason, self.pollIsClosing);
                        self.poll = poll = null;
                        if (!self.pollIsClosing) {
                            if (reason === "network") {
                                self._scheduleReceiver();
                            } else {
                                self.emit("close", code || 1006, reason);
                                self.removeAllListeners();
                            }
                        }
                    });
                };
                Polling.prototype.abort = function() {
                    debug("abort");
                    this.removeAllListeners();
                    this.pollIsClosing = true;
                    if (this.poll) {
                        this.poll.abort();
                    }
                };
                module.exports = Polling;
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        28: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), urlUtils = require("../../utils/url"), BufferedSender = require("./buffered-sender"), Polling = require("./polling");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:sender-receiver");
                }
                function SenderReceiver(transUrl, urlSuffix, senderFunc, Receiver, AjaxObject) {
                    var pollUrl = urlUtils.addPath(transUrl, urlSuffix);
                    debug(pollUrl);
                    var self = this;
                    BufferedSender.call(this, transUrl, senderFunc);
                    this.poll = new Polling(Receiver, pollUrl, AjaxObject);
                    this.poll.on("message", function(msg) {
                        debug("poll message", msg);
                        self.emit("message", msg);
                    });
                    this.poll.once("close", function(code, reason) {
                        debug("poll close", code, reason);
                        self.poll = null;
                        self.emit("close", code, reason);
                        self.close();
                    });
                }
                inherits(SenderReceiver, BufferedSender);
                SenderReceiver.prototype.close = function() {
                    BufferedSender.prototype.close.call(this);
                    debug("close");
                    this.removeAllListeners();
                    if (this.poll) {
                        this.poll.abort();
                        this.poll = null;
                    }
                };
                module.exports = SenderReceiver;
            }).call(this, {
                env: {}
            });
        }, {
            "../../utils/url": 52,
            "./buffered-sender": 25,
            "./polling": 27,
            debug: 55,
            inherits: 57
        } ],
        29: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), EventEmitter = require("events").EventEmitter, EventSourceDriver = require("eventsource");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:receiver:eventsource");
                }
                function EventSourceReceiver(url) {
                    debug(url);
                    EventEmitter.call(this);
                    var self = this;
                    var es = this.es = new EventSourceDriver(url);
                    es.onmessage = function(e) {
                        debug("message", e.data);
                        self.emit("message", decodeURI(e.data));
                    };
                    es.onerror = function(e) {
                        debug("error", es.readyState, e);
                        var reason = es.readyState !== 2 ? "network" : "permanent";
                        self._cleanup();
                        self._close(reason);
                    };
                }
                inherits(EventSourceReceiver, EventEmitter);
                EventSourceReceiver.prototype.abort = function() {
                    debug("abort");
                    this._cleanup();
                    this._close("user");
                };
                EventSourceReceiver.prototype._cleanup = function() {
                    debug("cleanup");
                    var es = this.es;
                    if (es) {
                        es.onmessage = es.onerror = null;
                        es.close();
                        this.es = null;
                    }
                };
                EventSourceReceiver.prototype._close = function(reason) {
                    debug("close", reason);
                    var self = this;
                    setTimeout(function() {
                        self.emit("close", null, reason);
                        self.removeAllListeners();
                    }, 200);
                };
                module.exports = EventSourceReceiver;
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55,
            events: 3,
            eventsource: 18,
            inherits: 57
        } ],
        30: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var inherits = require("inherits"), iframeUtils = require("../../utils/iframe"), urlUtils = require("../../utils/url"), EventEmitter = require("events").EventEmitter, random = require("../../utils/random");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:receiver:htmlfile");
                }
                function HtmlfileReceiver(url) {
                    debug(url);
                    EventEmitter.call(this);
                    var self = this;
                    iframeUtils.polluteGlobalNamespace();
                    this.id = "a" + random.string(6);
                    url = urlUtils.addQuery(url, "c=" + decodeURIComponent(iframeUtils.WPrefix + "." + this.id));
                    debug("using htmlfile", HtmlfileReceiver.htmlfileEnabled);
                    var constructFunc = HtmlfileReceiver.htmlfileEnabled ? iframeUtils.createHtmlfile : iframeUtils.createIframe;
                    global[iframeUtils.WPrefix][this.id] = {
                        start: function() {
                            debug("start");
                            self.iframeObj.loaded();
                        },
                        message: function(data) {
                            debug("message", data);
                            self.emit("message", data);
                        },
                        stop: function() {
                            debug("stop");
                            self._cleanup();
                            self._close("network");
                        }
                    };
                    this.iframeObj = constructFunc(url, function() {
                        debug("callback");
                        self._cleanup();
                        self._close("permanent");
                    });
                }
                inherits(HtmlfileReceiver, EventEmitter);
                HtmlfileReceiver.prototype.abort = function() {
                    debug("abort");
                    this._cleanup();
                    this._close("user");
                };
                HtmlfileReceiver.prototype._cleanup = function() {
                    debug("_cleanup");
                    if (this.iframeObj) {
                        this.iframeObj.cleanup();
                        this.iframeObj = null;
                    }
                    delete global[iframeUtils.WPrefix][this.id];
                };
                HtmlfileReceiver.prototype._close = function(reason) {
                    debug("_close", reason);
                    this.emit("close", null, reason);
                    this.removeAllListeners();
                };
                HtmlfileReceiver.htmlfileEnabled = false;
                var axo = [ "Active" ].concat("Object").join("X");
                if (axo in global) {
                    try {
                        HtmlfileReceiver.htmlfileEnabled = !!new global[axo]("htmlfile");
                    } catch (x) {}
                }
                HtmlfileReceiver.enabled = HtmlfileReceiver.htmlfileEnabled || iframeUtils.iframeEnabled;
                module.exports = HtmlfileReceiver;
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/iframe": 47,
            "../../utils/random": 50,
            "../../utils/url": 52,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        31: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var utils = require("../../utils/iframe"), random = require("../../utils/random"), browser = require("../../utils/browser"), urlUtils = require("../../utils/url"), inherits = require("inherits"), EventEmitter = require("events").EventEmitter;
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:receiver:jsonp");
                }
                function JsonpReceiver(url) {
                    debug(url);
                    var self = this;
                    EventEmitter.call(this);
                    utils.polluteGlobalNamespace();
                    this.id = "a" + random.string(6);
                    var urlWithId = urlUtils.addQuery(url, "c=" + encodeURIComponent(utils.WPrefix + "." + this.id));
                    global[utils.WPrefix][this.id] = this._callback.bind(this);
                    this._createScript(urlWithId);
                    this.timeoutId = setTimeout(function() {
                        debug("timeout");
                        self._abort(new Error("JSONP script loaded abnormally (timeout)"));
                    }, JsonpReceiver.timeout);
                }
                inherits(JsonpReceiver, EventEmitter);
                JsonpReceiver.prototype.abort = function() {
                    debug("abort");
                    if (global[utils.WPrefix][this.id]) {
                        var err = new Error("JSONP user aborted read");
                        err.code = 1e3;
                        this._abort(err);
                    }
                };
                JsonpReceiver.timeout = 35e3;
                JsonpReceiver.scriptErrorTimeout = 1e3;
                JsonpReceiver.prototype._callback = function(data) {
                    debug("_callback", data);
                    this._cleanup();
                    if (this.aborting) {
                        return;
                    }
                    if (data) {
                        debug("message", data);
                        this.emit("message", data);
                    }
                    this.emit("close", null, "network");
                    this.removeAllListeners();
                };
                JsonpReceiver.prototype._abort = function(err) {
                    debug("_abort", err);
                    this._cleanup();
                    this.aborting = true;
                    this.emit("close", err.code, err.message);
                    this.removeAllListeners();
                };
                JsonpReceiver.prototype._cleanup = function() {
                    debug("_cleanup");
                    clearTimeout(this.timeoutId);
                    if (this.script2) {
                        this.script2.parentNode.removeChild(this.script2);
                        this.script2 = null;
                    }
                    if (this.script) {
                        var script = this.script;
                        script.parentNode.removeChild(script);
                        script.onreadystatechange = script.onerror = script.onload = script.onclick = null;
                        this.script = null;
                    }
                    delete global[utils.WPrefix][this.id];
                };
                JsonpReceiver.prototype._scriptError = function() {
                    debug("_scriptError");
                    var self = this;
                    if (this.errorTimer) {
                        return;
                    }
                    this.errorTimer = setTimeout(function() {
                        if (!self.loadedOkay) {
                            self._abort(new Error("JSONP script loaded abnormally (onerror)"));
                        }
                    }, JsonpReceiver.scriptErrorTimeout);
                };
                JsonpReceiver.prototype._createScript = function(url) {
                    debug("_createScript", url);
                    var self = this;
                    var script = this.script = global.document.createElement("script");
                    var script2;
                    script.id = "a" + random.string(8);
                    script.src = url;
                    script.type = "text/javascript";
                    script.charset = "UTF-8";
                    script.onerror = this._scriptError.bind(this);
                    script.onload = function() {
                        debug("onload");
                        self._abort(new Error("JSONP script loaded abnormally (onload)"));
                    };
                    script.onreadystatechange = function() {
                        debug("onreadystatechange", script.readyState);
                        if (/loaded|closed/.test(script.readyState)) {
                            if (script && script.htmlFor && script.onclick) {
                                self.loadedOkay = true;
                                try {
                                    script.onclick();
                                } catch (x) {}
                            }
                            if (script) {
                                self._abort(new Error("JSONP script loaded abnormally (onreadystatechange)"));
                            }
                        }
                    };
                    if (typeof script.async === "undefined" && global.document.attachEvent) {
                        if (!browser.isOpera()) {
                            try {
                                script.htmlFor = script.id;
                                script.event = "onclick";
                            } catch (x) {}
                            script.async = true;
                        } else {
                            script2 = this.script2 = global.document.createElement("script");
                            script2.text = "try{var a = document.getElementById('" + script.id + "'); if(a)a.onerror();}catch(x){};";
                            script.async = script2.async = false;
                        }
                    }
                    if (typeof script.async !== "undefined") {
                        script.async = true;
                    }
                    var head = global.document.getElementsByTagName("head")[0];
                    head.insertBefore(script, head.firstChild);
                    if (script2) {
                        head.insertBefore(script2, head.firstChild);
                    }
                };
                module.exports = JsonpReceiver;
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/browser": 44,
            "../../utils/iframe": 47,
            "../../utils/random": 50,
            "../../utils/url": 52,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        32: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var inherits = require("inherits"), EventEmitter = require("events").EventEmitter;
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:receiver:xhr");
                }
                function XhrReceiver(url, AjaxObject) {
                    debug(url);
                    EventEmitter.call(this);
                    var self = this;
                    this.bufferPosition = 0;
                    this.xo = new AjaxObject("POST", url, null);
                    this.xo.on("chunk", this._chunkHandler.bind(this));
                    this.xo.once("finish", function(status, text) {
                        debug("finish", status, text);
                        self._chunkHandler(status, text);
                        self.xo = null;
                        var reason = status === 200 ? "network" : "permanent";
                        debug("close", reason);
                        self.emit("close", null, reason);
                        self._cleanup();
                    });
                }
                inherits(XhrReceiver, EventEmitter);
                XhrReceiver.prototype._chunkHandler = function(status, text) {
                    debug("_chunkHandler", status);
                    if (status !== 200 || !text) {
                        return;
                    }
                    for (var idx = -1; ;this.bufferPosition += idx + 1) {
                        var buf = text.slice(this.bufferPosition);
                        idx = buf.indexOf("\n");
                        if (idx === -1) {
                            break;
                        }
                        var msg = buf.slice(0, idx);
                        if (msg) {
                            debug("message", msg);
                            this.emit("message", msg);
                        }
                    }
                };
                XhrReceiver.prototype._cleanup = function() {
                    debug("_cleanup");
                    this.removeAllListeners();
                };
                XhrReceiver.prototype.abort = function() {
                    debug("abort");
                    if (this.xo) {
                        this.xo.close();
                        debug("close");
                        this.emit("close", null, "user");
                        this.xo = null;
                    }
                    this._cleanup();
                };
                module.exports = XhrReceiver;
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        33: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var random = require("../../utils/random"), urlUtils = require("../../utils/url");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:sender:jsonp");
                }
                var form, area;
                function createIframe(id) {
                    debug("createIframe", id);
                    try {
                        return global.document.createElement('<iframe name="' + id + '">');
                    } catch (x) {
                        var iframe = global.document.createElement("iframe");
                        iframe.name = id;
                        return iframe;
                    }
                }
                function createForm() {
                    debug("createForm");
                    form = global.document.createElement("form");
                    form.style.display = "none";
                    form.style.position = "absolute";
                    form.method = "POST";
                    form.enctype = "application/x-www-form-urlencoded";
                    form.acceptCharset = "UTF-8";
                    area = global.document.createElement("textarea");
                    area.name = "d";
                    form.appendChild(area);
                    global.document.body.appendChild(form);
                }
                module.exports = function(url, payload, callback) {
                    debug(url, payload);
                    if (!form) {
                        createForm();
                    }
                    var id = "a" + random.string(8);
                    form.target = id;
                    form.action = urlUtils.addQuery(urlUtils.addPath(url, "/jsonp_send"), "i=" + id);
                    var iframe = createIframe(id);
                    iframe.id = id;
                    iframe.style.display = "none";
                    form.appendChild(iframe);
                    try {
                        area.value = payload;
                    } catch (e) {}
                    form.submit();
                    var completed = function(err) {
                        debug("completed", id, err);
                        if (!iframe.onerror) {
                            return;
                        }
                        iframe.onreadystatechange = iframe.onerror = iframe.onload = null;
                        setTimeout(function() {
                            debug("cleaning up", id);
                            iframe.parentNode.removeChild(iframe);
                            iframe = null;
                        }, 500);
                        area.value = "";
                        callback(err);
                    };
                    iframe.onerror = function() {
                        debug("onerror", id);
                        completed();
                    };
                    iframe.onload = function() {
                        debug("onload", id);
                        completed();
                    };
                    iframe.onreadystatechange = function(e) {
                        debug("onreadystatechange", id, iframe.readyState, e);
                        if (iframe.readyState === "complete") {
                            completed();
                        }
                    };
                    return function() {
                        debug("aborted", id);
                        completed(new Error("Aborted"));
                    };
                };
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/random": 50,
            "../../utils/url": 52,
            debug: 55
        } ],
        34: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var EventEmitter = require("events").EventEmitter, inherits = require("inherits"), eventUtils = require("../../utils/event"), browser = require("../../utils/browser"), urlUtils = require("../../utils/url");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:sender:xdr");
                }
                function XDRObject(method, url, payload) {
                    debug(method, url);
                    var self = this;
                    EventEmitter.call(this);
                    setTimeout(function() {
                        self._start(method, url, payload);
                    }, 0);
                }
                inherits(XDRObject, EventEmitter);
                XDRObject.prototype._start = function(method, url, payload) {
                    debug("_start");
                    var self = this;
                    var xdr = new global.XDomainRequest();
                    url = urlUtils.addQuery(url, "t=" + +new Date());
                    xdr.onerror = function() {
                        debug("onerror");
                        self._error();
                    };
                    xdr.ontimeout = function() {
                        debug("ontimeout");
                        self._error();
                    };
                    xdr.onprogress = function() {
                        debug("progress", xdr.responseText);
                        self.emit("chunk", 200, xdr.responseText);
                    };
                    xdr.onload = function() {
                        debug("load");
                        self.emit("finish", 200, xdr.responseText);
                        self._cleanup(false);
                    };
                    this.xdr = xdr;
                    this.unloadRef = eventUtils.unloadAdd(function() {
                        self._cleanup(true);
                    });
                    try {
                        this.xdr.open(method, url);
                        if (this.timeout) {
                            this.xdr.timeout = this.timeout;
                        }
                        this.xdr.send(payload);
                    } catch (x) {
                        this._error();
                    }
                };
                XDRObject.prototype._error = function() {
                    this.emit("finish", 0, "");
                    this._cleanup(false);
                };
                XDRObject.prototype._cleanup = function(abort) {
                    debug("cleanup", abort);
                    if (!this.xdr) {
                        return;
                    }
                    this.removeAllListeners();
                    eventUtils.unloadDel(this.unloadRef);
                    this.xdr.ontimeout = this.xdr.onerror = this.xdr.onprogress = this.xdr.onload = null;
                    if (abort) {
                        try {
                            this.xdr.abort();
                        } catch (x) {}
                    }
                    this.unloadRef = this.xdr = null;
                };
                XDRObject.prototype.close = function() {
                    debug("close");
                    this._cleanup(true);
                };
                XDRObject.enabled = !!(global.XDomainRequest && browser.hasDomain());
                module.exports = XDRObject;
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../../utils/browser": 44,
            "../../utils/event": 46,
            "../../utils/url": 52,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        35: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), XhrDriver = require("../driver/xhr");
            function XHRCorsObject(method, url, payload, opts) {
                XhrDriver.call(this, method, url, payload, opts);
            }
            inherits(XHRCorsObject, XhrDriver);
            XHRCorsObject.enabled = XhrDriver.enabled && XhrDriver.supportsCORS;
            module.exports = XHRCorsObject;
        }, {
            "../driver/xhr": 17,
            inherits: 57
        } ],
        36: [ function(require, module, exports) {
            "use strict";
            var EventEmitter = require("events").EventEmitter, inherits = require("inherits");
            function XHRFake() {
                var self = this;
                EventEmitter.call(this);
                this.to = setTimeout(function() {
                    self.emit("finish", 200, "{}");
                }, XHRFake.timeout);
            }
            inherits(XHRFake, EventEmitter);
            XHRFake.prototype.close = function() {
                clearTimeout(this.to);
            };
            XHRFake.timeout = 2e3;
            module.exports = XHRFake;
        }, {
            events: 3,
            inherits: 57
        } ],
        37: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), XhrDriver = require("../driver/xhr");
            function XHRLocalObject(method, url, payload) {
                XhrDriver.call(this, method, url, payload, {
                    noCredentials: true
                });
            }
            inherits(XHRLocalObject, XhrDriver);
            XHRLocalObject.enabled = XhrDriver.enabled;
            module.exports = XHRLocalObject;
        }, {
            "../driver/xhr": 17,
            inherits: 57
        } ],
        38: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var utils = require("../utils/event"), urlUtils = require("../utils/url"), inherits = require("inherits"), EventEmitter = require("events").EventEmitter, WebsocketDriver = require("./driver/websocket");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:websocket");
                }
                function WebSocketTransport(transUrl, ignore, options) {
                    if (!WebSocketTransport.enabled()) {
                        throw new Error("Transport created when disabled");
                    }
                    EventEmitter.call(this);
                    debug("constructor", transUrl);
                    var self = this;
                    var url = urlUtils.addPath(transUrl, "/websocket");
                    if (url.slice(0, 5) === "https") {
                        url = "wss" + url.slice(5);
                    } else {
                        url = "ws" + url.slice(4);
                    }
                    this.url = url;
                    this.ws = new WebsocketDriver(this.url, [], options);
                    this.ws.onmessage = function(e) {
                        debug("message event", e.data);
                        self.emit("message", e.data);
                    };
                    this.unloadRef = utils.unloadAdd(function() {
                        debug("unload");
                        self.ws.close();
                    });
                    this.ws.onclose = function(e) {
                        debug("close event", e.code, e.reason);
                        self.emit("close", e.code, e.reason);
                        self._cleanup();
                    };
                    this.ws.onerror = function(e) {
                        debug("error event", e);
                        self.emit("close", 1006, "WebSocket connection broken");
                        self._cleanup();
                    };
                }
                inherits(WebSocketTransport, EventEmitter);
                WebSocketTransport.prototype.send = function(data) {
                    var msg = "[" + data + "]";
                    debug("send", msg);
                    this.ws.send(msg);
                };
                WebSocketTransport.prototype.close = function() {
                    debug("close");
                    var ws = this.ws;
                    this._cleanup();
                    if (ws) {
                        ws.close();
                    }
                };
                WebSocketTransport.prototype._cleanup = function() {
                    debug("_cleanup");
                    var ws = this.ws;
                    if (ws) {
                        ws.onmessage = ws.onclose = ws.onerror = null;
                    }
                    utils.unloadDel(this.unloadRef);
                    this.unloadRef = this.ws = null;
                    this.removeAllListeners();
                };
                WebSocketTransport.enabled = function() {
                    debug("enabled");
                    return !!WebsocketDriver;
                };
                WebSocketTransport.transportName = "websocket";
                WebSocketTransport.roundTrips = 2;
                module.exports = WebSocketTransport;
            }).call(this, {
                env: {}
            });
        }, {
            "../utils/event": 46,
            "../utils/url": 52,
            "./driver/websocket": 19,
            debug: 55,
            events: 3,
            inherits: 57
        } ],
        39: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), AjaxBasedTransport = require("./lib/ajax-based"), XdrStreamingTransport = require("./xdr-streaming"), XhrReceiver = require("./receiver/xhr"), XDRObject = require("./sender/xdr");
            function XdrPollingTransport(transUrl) {
                if (!XDRObject.enabled) {
                    throw new Error("Transport created when disabled");
                }
                AjaxBasedTransport.call(this, transUrl, "/xhr", XhrReceiver, XDRObject);
            }
            inherits(XdrPollingTransport, AjaxBasedTransport);
            XdrPollingTransport.enabled = XdrStreamingTransport.enabled;
            XdrPollingTransport.transportName = "xdr-polling";
            XdrPollingTransport.roundTrips = 2;
            module.exports = XdrPollingTransport;
        }, {
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xdr": 34,
            "./xdr-streaming": 40,
            inherits: 57
        } ],
        40: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), AjaxBasedTransport = require("./lib/ajax-based"), XhrReceiver = require("./receiver/xhr"), XDRObject = require("./sender/xdr");
            function XdrStreamingTransport(transUrl) {
                if (!XDRObject.enabled) {
                    throw new Error("Transport created when disabled");
                }
                AjaxBasedTransport.call(this, transUrl, "/xhr_streaming", XhrReceiver, XDRObject);
            }
            inherits(XdrStreamingTransport, AjaxBasedTransport);
            XdrStreamingTransport.enabled = function(info) {
                if (info.cookie_needed || info.nullOrigin) {
                    return false;
                }
                return XDRObject.enabled && info.sameScheme;
            };
            XdrStreamingTransport.transportName = "xdr-streaming";
            XdrStreamingTransport.roundTrips = 2;
            module.exports = XdrStreamingTransport;
        }, {
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xdr": 34,
            inherits: 57
        } ],
        41: [ function(require, module, exports) {
            "use strict";
            var inherits = require("inherits"), AjaxBasedTransport = require("./lib/ajax-based"), XhrReceiver = require("./receiver/xhr"), XHRCorsObject = require("./sender/xhr-cors"), XHRLocalObject = require("./sender/xhr-local");
            function XhrPollingTransport(transUrl) {
                if (!XHRLocalObject.enabled && !XHRCorsObject.enabled) {
                    throw new Error("Transport created when disabled");
                }
                AjaxBasedTransport.call(this, transUrl, "/xhr", XhrReceiver, XHRCorsObject);
            }
            inherits(XhrPollingTransport, AjaxBasedTransport);
            XhrPollingTransport.enabled = function(info) {
                if (info.nullOrigin) {
                    return false;
                }
                if (XHRLocalObject.enabled && info.sameOrigin) {
                    return true;
                }
                return XHRCorsObject.enabled;
            };
            XhrPollingTransport.transportName = "xhr-polling";
            XhrPollingTransport.roundTrips = 2;
            module.exports = XhrPollingTransport;
        }, {
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xhr-cors": 35,
            "./sender/xhr-local": 37,
            inherits: 57
        } ],
        42: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var inherits = require("inherits"), AjaxBasedTransport = require("./lib/ajax-based"), XhrReceiver = require("./receiver/xhr"), XHRCorsObject = require("./sender/xhr-cors"), XHRLocalObject = require("./sender/xhr-local"), browser = require("../utils/browser");
                function XhrStreamingTransport(transUrl) {
                    if (!XHRLocalObject.enabled && !XHRCorsObject.enabled) {
                        throw new Error("Transport created when disabled");
                    }
                    AjaxBasedTransport.call(this, transUrl, "/xhr_streaming", XhrReceiver, XHRCorsObject);
                }
                inherits(XhrStreamingTransport, AjaxBasedTransport);
                XhrStreamingTransport.enabled = function(info) {
                    if (info.nullOrigin) {
                        return false;
                    }
                    if (browser.isOpera()) {
                        return false;
                    }
                    return XHRCorsObject.enabled;
                };
                XhrStreamingTransport.transportName = "xhr-streaming";
                XhrStreamingTransport.roundTrips = 2;
                XhrStreamingTransport.needBody = !!global.document;
                module.exports = XhrStreamingTransport;
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "../utils/browser": 44,
            "./lib/ajax-based": 24,
            "./receiver/xhr": 32,
            "./sender/xhr-cors": 35,
            "./sender/xhr-local": 37,
            inherits: 57
        } ],
        43: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                if (global.crypto && global.crypto.getRandomValues) {
                    module.exports.randomBytes = function(length) {
                        var bytes = new Uint8Array(length);
                        global.crypto.getRandomValues(bytes);
                        return bytes;
                    };
                } else {
                    module.exports.randomBytes = function(length) {
                        var bytes = new Array(length);
                        for (var i = 0; i < length; i++) {
                            bytes[i] = Math.floor(Math.random() * 256);
                        }
                        return bytes;
                    };
                }
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        44: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                module.exports = {
                    isOpera: function() {
                        return global.navigator && /opera/i.test(global.navigator.userAgent);
                    },
                    isKonqueror: function() {
                        return global.navigator && /konqueror/i.test(global.navigator.userAgent);
                    },
                    hasDomain: function() {
                        if (!global.document) {
                            return true;
                        }
                        try {
                            return !!global.document.domain;
                        } catch (e) {
                            return false;
                        }
                    }
                };
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        45: [ function(require, module, exports) {
            "use strict";
            var JSON3 = require("json3");
            var extraEscapable = /[\x00-\x1f\ud800-\udfff\ufffe\uffff\u0300-\u0333\u033d-\u0346\u034a-\u034c\u0350-\u0352\u0357-\u0358\u035c-\u0362\u0374\u037e\u0387\u0591-\u05af\u05c4\u0610-\u0617\u0653-\u0654\u0657-\u065b\u065d-\u065e\u06df-\u06e2\u06eb-\u06ec\u0730\u0732-\u0733\u0735-\u0736\u073a\u073d\u073f-\u0741\u0743\u0745\u0747\u07eb-\u07f1\u0951\u0958-\u095f\u09dc-\u09dd\u09df\u0a33\u0a36\u0a59-\u0a5b\u0a5e\u0b5c-\u0b5d\u0e38-\u0e39\u0f43\u0f4d\u0f52\u0f57\u0f5c\u0f69\u0f72-\u0f76\u0f78\u0f80-\u0f83\u0f93\u0f9d\u0fa2\u0fa7\u0fac\u0fb9\u1939-\u193a\u1a17\u1b6b\u1cda-\u1cdb\u1dc0-\u1dcf\u1dfc\u1dfe\u1f71\u1f73\u1f75\u1f77\u1f79\u1f7b\u1f7d\u1fbb\u1fbe\u1fc9\u1fcb\u1fd3\u1fdb\u1fe3\u1feb\u1fee-\u1fef\u1ff9\u1ffb\u1ffd\u2000-\u2001\u20d0-\u20d1\u20d4-\u20d7\u20e7-\u20e9\u2126\u212a-\u212b\u2329-\u232a\u2adc\u302b-\u302c\uaab2-\uaab3\uf900-\ufa0d\ufa10\ufa12\ufa15-\ufa1e\ufa20\ufa22\ufa25-\ufa26\ufa2a-\ufa2d\ufa30-\ufa6d\ufa70-\ufad9\ufb1d\ufb1f\ufb2a-\ufb36\ufb38-\ufb3c\ufb3e\ufb40-\ufb41\ufb43-\ufb44\ufb46-\ufb4e\ufff0-\uffff]/g, extraLookup;
            var unrollLookup = function(escapable) {
                var i;
                var unrolled = {};
                var c = [];
                for (i = 0; i < 65536; i++) {
                    c.push(String.fromCharCode(i));
                }
                escapable.lastIndex = 0;
                c.join("").replace(escapable, function(a) {
                    unrolled[a] = "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4);
                    return "";
                });
                escapable.lastIndex = 0;
                return unrolled;
            };
            module.exports = {
                quote: function(string) {
                    var quoted = JSON3.stringify(string);
                    extraEscapable.lastIndex = 0;
                    if (!extraEscapable.test(quoted)) {
                        return quoted;
                    }
                    if (!extraLookup) {
                        extraLookup = unrollLookup(extraEscapable);
                    }
                    return quoted.replace(extraEscapable, function(a) {
                        return extraLookup[a];
                    });
                }
            };
        }, {
            json3: 58
        } ],
        46: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var random = require("./random");
                var onUnload = {}, afterUnload = false, isChromePackagedApp = global.chrome && global.chrome.app && global.chrome.app.runtime;
                module.exports = {
                    attachEvent: function(event, listener) {
                        if (typeof global.addEventListener !== "undefined") {
                            global.addEventListener(event, listener, false);
                        } else if (global.document && global.attachEvent) {
                            global.document.attachEvent("on" + event, listener);
                            global.attachEvent("on" + event, listener);
                        }
                    },
                    detachEvent: function(event, listener) {
                        if (typeof global.addEventListener !== "undefined") {
                            global.removeEventListener(event, listener, false);
                        } else if (global.document && global.detachEvent) {
                            global.document.detachEvent("on" + event, listener);
                            global.detachEvent("on" + event, listener);
                        }
                    },
                    unloadAdd: function(listener) {
                        if (isChromePackagedApp) {
                            return null;
                        }
                        var ref = random.string(8);
                        onUnload[ref] = listener;
                        if (afterUnload) {
                            setTimeout(this.triggerUnloadCallbacks, 0);
                        }
                        return ref;
                    },
                    unloadDel: function(ref) {
                        if (ref in onUnload) {
                            delete onUnload[ref];
                        }
                    },
                    triggerUnloadCallbacks: function() {
                        for (var ref in onUnload) {
                            onUnload[ref]();
                            delete onUnload[ref];
                        }
                    }
                };
                var unloadTriggered = function() {
                    if (afterUnload) {
                        return;
                    }
                    afterUnload = true;
                    module.exports.triggerUnloadCallbacks();
                };
                if (!isChromePackagedApp) {
                    module.exports.attachEvent("unload", unloadTriggered);
                }
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./random": 50
        } ],
        47: [ function(require, module, exports) {
            (function(process, global) {
                "use strict";
                var eventUtils = require("./event"), JSON3 = require("json3"), browser = require("./browser");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:utils:iframe");
                }
                module.exports = {
                    WPrefix: "_jp",
                    currentWindowId: null,
                    polluteGlobalNamespace: function() {
                        if (!(module.exports.WPrefix in global)) {
                            global[module.exports.WPrefix] = {};
                        }
                    },
                    postMessage: function(type, data) {
                        if (global.parent !== global) {
                            global.parent.postMessage(JSON3.stringify({
                                windowId: module.exports.currentWindowId,
                                type: type,
                                data: data || ""
                            }), "*");
                        } else {
                            debug("Cannot postMessage, no parent window.", type, data);
                        }
                    },
                    createIframe: function(iframeUrl, errorCallback) {
                        var iframe = global.document.createElement("iframe");
                        var tref, unloadRef;
                        var unattach = function() {
                            debug("unattach");
                            clearTimeout(tref);
                            try {
                                iframe.onload = null;
                            } catch (x) {}
                            iframe.onerror = null;
                        };
                        var cleanup = function() {
                            debug("cleanup");
                            if (iframe) {
                                unattach();
                                setTimeout(function() {
                                    if (iframe) {
                                        iframe.parentNode.removeChild(iframe);
                                    }
                                    iframe = null;
                                }, 0);
                                eventUtils.unloadDel(unloadRef);
                            }
                        };
                        var onerror = function(err) {
                            debug("onerror", err);
                            if (iframe) {
                                cleanup();
                                errorCallback(err);
                            }
                        };
                        var post = function(msg, origin) {
                            debug("post", msg, origin);
                            setTimeout(function() {
                                try {
                                    if (iframe && iframe.contentWindow) {
                                        iframe.contentWindow.postMessage(msg, origin);
                                    }
                                } catch (x) {}
                            }, 0);
                        };
                        iframe.src = iframeUrl;
                        iframe.style.display = "none";
                        iframe.style.position = "absolute";
                        iframe.onerror = function() {
                            onerror("onerror");
                        };
                        iframe.onload = function() {
                            debug("onload");
                            clearTimeout(tref);
                            tref = setTimeout(function() {
                                onerror("onload timeout");
                            }, 2e3);
                        };
                        global.document.body.appendChild(iframe);
                        tref = setTimeout(function() {
                            onerror("timeout");
                        }, 15e3);
                        unloadRef = eventUtils.unloadAdd(cleanup);
                        return {
                            post: post,
                            cleanup: cleanup,
                            loaded: unattach
                        };
                    },
                    createHtmlfile: function(iframeUrl, errorCallback) {
                        var axo = [ "Active" ].concat("Object").join("X");
                        var doc = new global[axo]("htmlfile");
                        var tref, unloadRef;
                        var iframe;
                        var unattach = function() {
                            clearTimeout(tref);
                            iframe.onerror = null;
                        };
                        var cleanup = function() {
                            if (doc) {
                                unattach();
                                eventUtils.unloadDel(unloadRef);
                                iframe.parentNode.removeChild(iframe);
                                iframe = doc = null;
                                CollectGarbage();
                            }
                        };
                        var onerror = function(r) {
                            debug("onerror", r);
                            if (doc) {
                                cleanup();
                                errorCallback(r);
                            }
                        };
                        var post = function(msg, origin) {
                            try {
                                setTimeout(function() {
                                    if (iframe && iframe.contentWindow) {
                                        iframe.contentWindow.postMessage(msg, origin);
                                    }
                                }, 0);
                            } catch (x) {}
                        };
                        doc.open();
                        doc.write("<html><s" + "cript>" + 'document.domain="' + global.document.domain + '";' + "</s" + "cript></html>");
                        doc.close();
                        doc.parentWindow[module.exports.WPrefix] = global[module.exports.WPrefix];
                        var c = doc.createElement("div");
                        doc.body.appendChild(c);
                        iframe = doc.createElement("iframe");
                        c.appendChild(iframe);
                        iframe.src = iframeUrl;
                        iframe.onerror = function() {
                            onerror("onerror");
                        };
                        tref = setTimeout(function() {
                            onerror("timeout");
                        }, 15e3);
                        unloadRef = eventUtils.unloadAdd(cleanup);
                        return {
                            post: post,
                            cleanup: cleanup,
                            loaded: unattach
                        };
                    }
                };
                module.exports.iframeEnabled = false;
                if (global.document) {
                    module.exports.iframeEnabled = (typeof global.postMessage === "function" || typeof global.postMessage === "object") && !browser.isKonqueror();
                }
            }).call(this, {
                env: {}
            }, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            "./browser": 44,
            "./event": 46,
            debug: 55,
            json3: 58
        } ],
        48: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var logObject = {};
                [ "log", "debug", "warn" ].forEach(function(level) {
                    var levelExists;
                    try {
                        levelExists = global.console && global.console[level] && global.console[level].apply;
                    } catch (e) {}
                    logObject[level] = levelExists ? function() {
                        return global.console[level].apply(global.console, arguments);
                    } : level === "log" ? function() {} : logObject.log;
                });
                module.exports = logObject;
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        49: [ function(require, module, exports) {
            "use strict";
            module.exports = {
                isObject: function(obj) {
                    var type = typeof obj;
                    return type === "function" || type === "object" && !!obj;
                },
                extend: function(obj) {
                    if (!this.isObject(obj)) {
                        return obj;
                    }
                    var source, prop;
                    for (var i = 1, length = arguments.length; i < length; i++) {
                        source = arguments[i];
                        for (prop in source) {
                            if (Object.prototype.hasOwnProperty.call(source, prop)) {
                                obj[prop] = source[prop];
                            }
                        }
                    }
                    return obj;
                }
            };
        }, {} ],
        50: [ function(require, module, exports) {
            "use strict";
            var crypto = require("crypto");
            var _randomStringChars = "abcdefghijklmnopqrstuvwxyz012345";
            module.exports = {
                string: function(length) {
                    var max = _randomStringChars.length;
                    var bytes = crypto.randomBytes(length);
                    var ret = [];
                    for (var i = 0; i < length; i++) {
                        ret.push(_randomStringChars.substr(bytes[i] % max, 1));
                    }
                    return ret.join("");
                },
                number: function(max) {
                    return Math.floor(Math.random() * max);
                },
                numberString: function(max) {
                    var t = ("" + (max - 1)).length;
                    var p = new Array(t + 1).join("0");
                    return (p + this.number(max)).slice(-t);
                }
            };
        }, {
            crypto: 43
        } ],
        51: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:utils:transport");
                }
                module.exports = function(availableTransports) {
                    return {
                        filterToEnabled: function(transportsWhitelist, info) {
                            var transports = {
                                main: [],
                                facade: []
                            };
                            if (!transportsWhitelist) {
                                transportsWhitelist = [];
                            } else if (typeof transportsWhitelist === "string") {
                                transportsWhitelist = [ transportsWhitelist ];
                            }
                            availableTransports.forEach(function(trans) {
                                if (!trans) {
                                    return;
                                }
                                if (trans.transportName === "websocket" && info.websocket === false) {
                                    debug("disabled from server", "websocket");
                                    return;
                                }
                                if (transportsWhitelist.length && transportsWhitelist.indexOf(trans.transportName) === -1) {
                                    debug("not in whitelist", trans.transportName);
                                    return;
                                }
                                if (trans.enabled(info)) {
                                    debug("enabled", trans.transportName);
                                    transports.main.push(trans);
                                    if (trans.facadeTransport) {
                                        transports.facade.push(trans.facadeTransport);
                                    }
                                } else {
                                    debug("disabled", trans.transportName);
                                }
                            });
                            return transports;
                        }
                    };
                };
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55
        } ],
        52: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                var URL = require("url-parse");
                var debug = function() {};
                if (process.env.NODE_ENV !== "production") {
                    debug = require("debug")("sockjs-client:utils:url");
                }
                module.exports = {
                    getOrigin: function(url) {
                        if (!url) {
                            return null;
                        }
                        var p = new URL(url);
                        if (p.protocol === "file:") {
                            return null;
                        }
                        var port = p.port;
                        if (!port) {
                            port = p.protocol === "https:" ? "443" : "80";
                        }
                        return p.protocol + "//" + p.hostname + ":" + port;
                    },
                    isOriginEqual: function(a, b) {
                        var res = this.getOrigin(a) === this.getOrigin(b);
                        debug("same", a, b, res);
                        return res;
                    },
                    isSchemeEqual: function(a, b) {
                        return a.split(":")[0] === b.split(":")[0];
                    },
                    addPath: function(url, path) {
                        var qs = url.split("?");
                        return qs[0] + path + (qs[1] ? "?" + qs[1] : "");
                    },
                    addQuery: function(url, q) {
                        return url + (url.indexOf("?") === -1 ? "?" + q : "&" + q);
                    }
                };
            }).call(this, {
                env: {}
            });
        }, {
            debug: 55,
            "url-parse": 61
        } ],
        53: [ function(require, module, exports) {
            module.exports = "1.3.0";
        }, {} ],
        54: [ function(require, module, exports) {
            var s = 1e3;
            var m = s * 60;
            var h = m * 60;
            var d = h * 24;
            var w = d * 7;
            var y = d * 365.25;
            module.exports = function(val, options) {
                options = options || {};
                var type = typeof val;
                if (type === "string" && val.length > 0) {
                    return parse(val);
                } else if (type === "number" && isNaN(val) === false) {
                    return options.long ? fmtLong(val) : fmtShort(val);
                }
                throw new Error("val is not a non-empty string or a valid number. val=" + JSON.stringify(val));
            };
            function parse(str) {
                str = String(str);
                if (str.length > 100) {
                    return;
                }
                var match = /^((?:\d+)?\-?\d?\.?\d+) *(milliseconds?|msecs?|ms|seconds?|secs?|s|minutes?|mins?|m|hours?|hrs?|h|days?|d|weeks?|w|years?|yrs?|y)?$/i.exec(str);
                if (!match) {
                    return;
                }
                var n = parseFloat(match[1]);
                var type = (match[2] || "ms").toLowerCase();
                switch (type) {
                  case "years":
                  case "year":
                  case "yrs":
                  case "yr":
                  case "y":
                    return n * y;

                  case "weeks":
                  case "week":
                  case "w":
                    return n * w;

                  case "days":
                  case "day":
                  case "d":
                    return n * d;

                  case "hours":
                  case "hour":
                  case "hrs":
                  case "hr":
                  case "h":
                    return n * h;

                  case "minutes":
                  case "minute":
                  case "mins":
                  case "min":
                  case "m":
                    return n * m;

                  case "seconds":
                  case "second":
                  case "secs":
                  case "sec":
                  case "s":
                    return n * s;

                  case "milliseconds":
                  case "millisecond":
                  case "msecs":
                  case "msec":
                  case "ms":
                    return n;

                  default:
                    return undefined;
                }
            }
            function fmtShort(ms) {
                var msAbs = Math.abs(ms);
                if (msAbs >= d) {
                    return Math.round(ms / d) + "d";
                }
                if (msAbs >= h) {
                    return Math.round(ms / h) + "h";
                }
                if (msAbs >= m) {
                    return Math.round(ms / m) + "m";
                }
                if (msAbs >= s) {
                    return Math.round(ms / s) + "s";
                }
                return ms + "ms";
            }
            function fmtLong(ms) {
                var msAbs = Math.abs(ms);
                if (msAbs >= d) {
                    return plural(ms, msAbs, d, "day");
                }
                if (msAbs >= h) {
                    return plural(ms, msAbs, h, "hour");
                }
                if (msAbs >= m) {
                    return plural(ms, msAbs, m, "minute");
                }
                if (msAbs >= s) {
                    return plural(ms, msAbs, s, "second");
                }
                return ms + " ms";
            }
            function plural(ms, msAbs, n, name) {
                var isPlural = msAbs >= n * 1.5;
                return Math.round(ms / n) + " " + name + (isPlural ? "s" : "");
            }
        }, {} ],
        55: [ function(require, module, exports) {
            (function(process) {
                "use strict";
                function _typeof(obj) {
                    if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") {
                        _typeof = function _typeof(obj) {
                            return typeof obj;
                        };
                    } else {
                        _typeof = function _typeof(obj) {
                            return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
                        };
                    }
                    return _typeof(obj);
                }
                exports.log = log;
                exports.formatArgs = formatArgs;
                exports.save = save;
                exports.load = load;
                exports.useColors = useColors;
                exports.storage = localstorage();
                exports.colors = [ "#0000CC", "#0000FF", "#0033CC", "#0033FF", "#0066CC", "#0066FF", "#0099CC", "#0099FF", "#00CC00", "#00CC33", "#00CC66", "#00CC99", "#00CCCC", "#00CCFF", "#3300CC", "#3300FF", "#3333CC", "#3333FF", "#3366CC", "#3366FF", "#3399CC", "#3399FF", "#33CC00", "#33CC33", "#33CC66", "#33CC99", "#33CCCC", "#33CCFF", "#6600CC", "#6600FF", "#6633CC", "#6633FF", "#66CC00", "#66CC33", "#9900CC", "#9900FF", "#9933CC", "#9933FF", "#99CC00", "#99CC33", "#CC0000", "#CC0033", "#CC0066", "#CC0099", "#CC00CC", "#CC00FF", "#CC3300", "#CC3333", "#CC3366", "#CC3399", "#CC33CC", "#CC33FF", "#CC6600", "#CC6633", "#CC9900", "#CC9933", "#CCCC00", "#CCCC33", "#FF0000", "#FF0033", "#FF0066", "#FF0099", "#FF00CC", "#FF00FF", "#FF3300", "#FF3333", "#FF3366", "#FF3399", "#FF33CC", "#FF33FF", "#FF6600", "#FF6633", "#FF9900", "#FF9933", "#FFCC00", "#FFCC33" ];
                function useColors() {
                    if (typeof window !== "undefined" && window.process && (window.process.type === "renderer" || window.process.__nwjs)) {
                        return true;
                    }
                    if (typeof navigator !== "undefined" && navigator.userAgent && navigator.userAgent.toLowerCase().match(/(edge|trident)\/(\d+)/)) {
                        return false;
                    }
                    return typeof document !== "undefined" && document.documentElement && document.documentElement.style && document.documentElement.style.WebkitAppearance || typeof window !== "undefined" && window.console && (window.console.firebug || window.console.exception && window.console.table) || typeof navigator !== "undefined" && navigator.userAgent && navigator.userAgent.toLowerCase().match(/firefox\/(\d+)/) && parseInt(RegExp.$1, 10) >= 31 || typeof navigator !== "undefined" && navigator.userAgent && navigator.userAgent.toLowerCase().match(/applewebkit\/(\d+)/);
                }
                function formatArgs(args) {
                    args[0] = (this.useColors ? "%c" : "") + this.namespace + (this.useColors ? " %c" : " ") + args[0] + (this.useColors ? "%c " : " ") + "+" + module.exports.humanize(this.diff);
                    if (!this.useColors) {
                        return;
                    }
                    var c = "color: " + this.color;
                    args.splice(1, 0, c, "color: inherit");
                    var index = 0;
                    var lastC = 0;
                    args[0].replace(/%[a-zA-Z%]/g, function(match) {
                        if (match === "%%") {
                            return;
                        }
                        index++;
                        if (match === "%c") {
                            lastC = index;
                        }
                    });
                    args.splice(lastC, 0, c);
                }
                function log() {
                    var _console;
                    return (typeof console === "undefined" ? "undefined" : _typeof(console)) === "object" && console.log && (_console = console).log.apply(_console, arguments);
                }
                function save(namespaces) {
                    try {
                        if (namespaces) {
                            exports.storage.setItem("debug", namespaces);
                        } else {
                            exports.storage.removeItem("debug");
                        }
                    } catch (error) {}
                }
                function load() {
                    var r;
                    try {
                        r = exports.storage.getItem("debug");
                    } catch (error) {}
                    if (!r && typeof process !== "undefined" && "env" in process) {
                        r = process.env.DEBUG;
                    }
                    return r;
                }
                function localstorage() {
                    try {
                        return localStorage;
                    } catch (error) {}
                }
                module.exports = require("./common")(exports);
                var formatters = module.exports.formatters;
                formatters.j = function(v) {
                    try {
                        return JSON.stringify(v);
                    } catch (error) {
                        return "[UnexpectedJSONParseError]: " + error.message;
                    }
                };
            }).call(this, {
                env: {}
            });
        }, {
            "./common": 56
        } ],
        56: [ function(require, module, exports) {
            "use strict";
            function setup(env) {
                createDebug.debug = createDebug;
                createDebug.default = createDebug;
                createDebug.coerce = coerce;
                createDebug.disable = disable;
                createDebug.enable = enable;
                createDebug.enabled = enabled;
                createDebug.humanize = require("ms");
                Object.keys(env).forEach(function(key) {
                    createDebug[key] = env[key];
                });
                createDebug.instances = [];
                createDebug.names = [];
                createDebug.skips = [];
                createDebug.formatters = {};
                function selectColor(namespace) {
                    var hash = 0;
                    for (var i = 0; i < namespace.length; i++) {
                        hash = (hash << 5) - hash + namespace.charCodeAt(i);
                        hash |= 0;
                    }
                    return createDebug.colors[Math.abs(hash) % createDebug.colors.length];
                }
                createDebug.selectColor = selectColor;
                function createDebug(namespace) {
                    var prevTime;
                    function debug() {
                        for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
                            args[_key] = arguments[_key];
                        }
                        if (!debug.enabled) {
                            return;
                        }
                        var self = debug;
                        var curr = Number(new Date());
                        var ms = curr - (prevTime || curr);
                        self.diff = ms;
                        self.prev = prevTime;
                        self.curr = curr;
                        prevTime = curr;
                        args[0] = createDebug.coerce(args[0]);
                        if (typeof args[0] !== "string") {
                            args.unshift("%O");
                        }
                        var index = 0;
                        args[0] = args[0].replace(/%([a-zA-Z%])/g, function(match, format) {
                            if (match === "%%") {
                                return match;
                            }
                            index++;
                            var formatter = createDebug.formatters[format];
                            if (typeof formatter === "function") {
                                var val = args[index];
                                match = formatter.call(self, val);
                                args.splice(index, 1);
                                index--;
                            }
                            return match;
                        });
                        createDebug.formatArgs.call(self, args);
                        var logFn = self.log || createDebug.log;
                        logFn.apply(self, args);
                    }
                    debug.namespace = namespace;
                    debug.enabled = createDebug.enabled(namespace);
                    debug.useColors = createDebug.useColors();
                    debug.color = selectColor(namespace);
                    debug.destroy = destroy;
                    debug.extend = extend;
                    if (typeof createDebug.init === "function") {
                        createDebug.init(debug);
                    }
                    createDebug.instances.push(debug);
                    return debug;
                }
                function destroy() {
                    var index = createDebug.instances.indexOf(this);
                    if (index !== -1) {
                        createDebug.instances.splice(index, 1);
                        return true;
                    }
                    return false;
                }
                function extend(namespace, delimiter) {
                    return createDebug(this.namespace + (typeof delimiter === "undefined" ? ":" : delimiter) + namespace);
                }
                function enable(namespaces) {
                    createDebug.save(namespaces);
                    createDebug.names = [];
                    createDebug.skips = [];
                    var i;
                    var split = (typeof namespaces === "string" ? namespaces : "").split(/[\s,]+/);
                    var len = split.length;
                    for (i = 0; i < len; i++) {
                        if (!split[i]) {
                            continue;
                        }
                        namespaces = split[i].replace(/\*/g, ".*?");
                        if (namespaces[0] === "-") {
                            createDebug.skips.push(new RegExp("^" + namespaces.substr(1) + "$"));
                        } else {
                            createDebug.names.push(new RegExp("^" + namespaces + "$"));
                        }
                    }
                    for (i = 0; i < createDebug.instances.length; i++) {
                        var instance = createDebug.instances[i];
                        instance.enabled = createDebug.enabled(instance.namespace);
                    }
                }
                function disable() {
                    createDebug.enable("");
                }
                function enabled(name) {
                    if (name[name.length - 1] === "*") {
                        return true;
                    }
                    var i;
                    var len;
                    for (i = 0, len = createDebug.skips.length; i < len; i++) {
                        if (createDebug.skips[i].test(name)) {
                            return false;
                        }
                    }
                    for (i = 0, len = createDebug.names.length; i < len; i++) {
                        if (createDebug.names[i].test(name)) {
                            return true;
                        }
                    }
                    return false;
                }
                function coerce(val) {
                    if (val instanceof Error) {
                        return val.stack || val.message;
                    }
                    return val;
                }
                createDebug.enable(createDebug.load());
                return createDebug;
            }
            module.exports = setup;
        }, {
            ms: 54
        } ],
        57: [ function(require, module, exports) {
            if (typeof Object.create === "function") {
                module.exports = function inherits(ctor, superCtor) {
                    ctor.super_ = superCtor;
                    ctor.prototype = Object.create(superCtor.prototype, {
                        constructor: {
                            value: ctor,
                            enumerable: false,
                            writable: true,
                            configurable: true
                        }
                    });
                };
            } else {
                module.exports = function inherits(ctor, superCtor) {
                    ctor.super_ = superCtor;
                    var TempCtor = function() {};
                    TempCtor.prototype = superCtor.prototype;
                    ctor.prototype = new TempCtor();
                    ctor.prototype.constructor = ctor;
                };
            }
        }, {} ],
        58: [ function(require, module, exports) {
            (function(global) {
                (function() {
                    var isLoader = typeof define === "function" && define.amd;
                    var objectTypes = {
                        function: true,
                        object: true
                    };
                    var freeExports = objectTypes[typeof exports] && exports && !exports.nodeType && exports;
                    var root = objectTypes[typeof window] && window || this, freeGlobal = freeExports && objectTypes[typeof module] && module && !module.nodeType && typeof global == "object" && global;
                    if (freeGlobal && (freeGlobal["global"] === freeGlobal || freeGlobal["window"] === freeGlobal || freeGlobal["self"] === freeGlobal)) {
                        root = freeGlobal;
                    }
                    function runInContext(context, exports) {
                        context || (context = root["Object"]());
                        exports || (exports = root["Object"]());
                        var Number = context["Number"] || root["Number"], String = context["String"] || root["String"], Object = context["Object"] || root["Object"], Date = context["Date"] || root["Date"], SyntaxError = context["SyntaxError"] || root["SyntaxError"], TypeError = context["TypeError"] || root["TypeError"], Math = context["Math"] || root["Math"], nativeJSON = context["JSON"] || root["JSON"];
                        if (typeof nativeJSON == "object" && nativeJSON) {
                            exports.stringify = nativeJSON.stringify;
                            exports.parse = nativeJSON.parse;
                        }
                        var objectProto = Object.prototype, getClass = objectProto.toString, isProperty, forEach, undef;
                        var isExtended = new Date(-0xc782b5b800cec);
                        try {
                            isExtended = isExtended.getUTCFullYear() == -109252 && isExtended.getUTCMonth() === 0 && isExtended.getUTCDate() === 1 && isExtended.getUTCHours() == 10 && isExtended.getUTCMinutes() == 37 && isExtended.getUTCSeconds() == 6 && isExtended.getUTCMilliseconds() == 708;
                        } catch (exception) {}
                        function has(name) {
                            if (has[name] !== undef) {
                                return has[name];
                            }
                            var isSupported;
                            if (name == "bug-string-char-index") {
                                isSupported = "a"[0] != "a";
                            } else if (name == "json") {
                                isSupported = has("json-stringify") && has("json-parse");
                            } else {
                                var value, serialized = '{"a":[1,true,false,null,"\\u0000\\b\\n\\f\\r\\t"]}';
                                if (name == "json-stringify") {
                                    var stringify = exports.stringify, stringifySupported = typeof stringify == "function" && isExtended;
                                    if (stringifySupported) {
                                        (value = function() {
                                            return 1;
                                        }).toJSON = value;
                                        try {
                                            stringifySupported = stringify(0) === "0" && stringify(new Number()) === "0" && stringify(new String()) == '""' && stringify(getClass) === undef && stringify(undef) === undef && stringify() === undef && stringify(value) === "1" && stringify([ value ]) == "[1]" && stringify([ undef ]) == "[null]" && stringify(null) == "null" && stringify([ undef, getClass, null ]) == "[null,null,null]" && stringify({
                                                a: [ value, true, false, null, "\0\b\n\f\r\t" ]
                                            }) == serialized && stringify(null, value) === "1" && stringify([ 1, 2 ], null, 1) == "[\n 1,\n 2\n]" && stringify(new Date(-864e13)) == '"-271821-04-20T00:00:00.000Z"' && stringify(new Date(864e13)) == '"+275760-09-13T00:00:00.000Z"' && stringify(new Date(-621987552e5)) == '"-000001-01-01T00:00:00.000Z"' && stringify(new Date(-1)) == '"1969-12-31T23:59:59.999Z"';
                                        } catch (exception) {
                                            stringifySupported = false;
                                        }
                                    }
                                    isSupported = stringifySupported;
                                }
                                if (name == "json-parse") {
                                    var parse = exports.parse;
                                    if (typeof parse == "function") {
                                        try {
                                            if (parse("0") === 0 && !parse(false)) {
                                                value = parse(serialized);
                                                var parseSupported = value["a"].length == 5 && value["a"][0] === 1;
                                                if (parseSupported) {
                                                    try {
                                                        parseSupported = !parse('"\t"');
                                                    } catch (exception) {}
                                                    if (parseSupported) {
                                                        try {
                                                            parseSupported = parse("01") !== 1;
                                                        } catch (exception) {}
                                                    }
                                                    if (parseSupported) {
                                                        try {
                                                            parseSupported = parse("1.") !== 1;
                                                        } catch (exception) {}
                                                    }
                                                }
                                            }
                                        } catch (exception) {
                                            parseSupported = false;
                                        }
                                    }
                                    isSupported = parseSupported;
                                }
                            }
                            return has[name] = !!isSupported;
                        }
                        if (!has("json")) {
                            var functionClass = "[object Function]", dateClass = "[object Date]", numberClass = "[object Number]", stringClass = "[object String]", arrayClass = "[object Array]", booleanClass = "[object Boolean]";
                            var charIndexBuggy = has("bug-string-char-index");
                            if (!isExtended) {
                                var floor = Math.floor;
                                var Months = [ 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 ];
                                var getDay = function(year, month) {
                                    return Months[month] + 365 * (year - 1970) + floor((year - 1969 + (month = +(month > 1))) / 4) - floor((year - 1901 + month) / 100) + floor((year - 1601 + month) / 400);
                                };
                            }
                            if (!(isProperty = objectProto.hasOwnProperty)) {
                                isProperty = function(property) {
                                    var members = {}, constructor;
                                    if ((members.__proto__ = null, members.__proto__ = {
                                        toString: 1
                                    }, members).toString != getClass) {
                                        isProperty = function(property) {
                                            var original = this.__proto__, result = property in (this.__proto__ = null, this);
                                            this.__proto__ = original;
                                            return result;
                                        };
                                    } else {
                                        constructor = members.constructor;
                                        isProperty = function(property) {
                                            var parent = (this.constructor || constructor).prototype;
                                            return property in this && !(property in parent && this[property] === parent[property]);
                                        };
                                    }
                                    members = null;
                                    return isProperty.call(this, property);
                                };
                            }
                            forEach = function(object, callback) {
                                var size = 0, Properties, members, property;
                                (Properties = function() {
                                    this.valueOf = 0;
                                }).prototype.valueOf = 0;
                                members = new Properties();
                                for (property in members) {
                                    if (isProperty.call(members, property)) {
                                        size++;
                                    }
                                }
                                Properties = members = null;
                                if (!size) {
                                    members = [ "valueOf", "toString", "toLocaleString", "propertyIsEnumerable", "isPrototypeOf", "hasOwnProperty", "constructor" ];
                                    forEach = function(object, callback) {
                                        var isFunction = getClass.call(object) == functionClass, property, length;
                                        var hasProperty = !isFunction && typeof object.constructor != "function" && objectTypes[typeof object.hasOwnProperty] && object.hasOwnProperty || isProperty;
                                        for (property in object) {
                                            if (!(isFunction && property == "prototype") && hasProperty.call(object, property)) {
                                                callback(property);
                                            }
                                        }
                                        for (length = members.length; property = members[--length]; hasProperty.call(object, property) && callback(property)) ;
                                    };
                                } else if (size == 2) {
                                    forEach = function(object, callback) {
                                        var members = {}, isFunction = getClass.call(object) == functionClass, property;
                                        for (property in object) {
                                            if (!(isFunction && property == "prototype") && !isProperty.call(members, property) && (members[property] = 1) && isProperty.call(object, property)) {
                                                callback(property);
                                            }
                                        }
                                    };
                                } else {
                                    forEach = function(object, callback) {
                                        var isFunction = getClass.call(object) == functionClass, property, isConstructor;
                                        for (property in object) {
                                            if (!(isFunction && property == "prototype") && isProperty.call(object, property) && !(isConstructor = property === "constructor")) {
                                                callback(property);
                                            }
                                        }
                                        if (isConstructor || isProperty.call(object, property = "constructor")) {
                                            callback(property);
                                        }
                                    };
                                }
                                return forEach(object, callback);
                            };
                            if (!has("json-stringify")) {
                                var Escapes = {
                                    92: "\\\\",
                                    34: '\\"',
                                    8: "\\b",
                                    12: "\\f",
                                    10: "\\n",
                                    13: "\\r",
                                    9: "\\t"
                                };
                                var leadingZeroes = "000000";
                                var toPaddedString = function(width, value) {
                                    return (leadingZeroes + (value || 0)).slice(-width);
                                };
                                var unicodePrefix = "\\u00";
                                var quote = function(value) {
                                    var result = '"', index = 0, length = value.length, useCharIndex = !charIndexBuggy || length > 10;
                                    var symbols = useCharIndex && (charIndexBuggy ? value.split("") : value);
                                    for (;index < length; index++) {
                                        var charCode = value.charCodeAt(index);
                                        switch (charCode) {
                                          case 8:
                                          case 9:
                                          case 10:
                                          case 12:
                                          case 13:
                                          case 34:
                                          case 92:
                                            result += Escapes[charCode];
                                            break;

                                          default:
                                            if (charCode < 32) {
                                                result += unicodePrefix + toPaddedString(2, charCode.toString(16));
                                                break;
                                            }
                                            result += useCharIndex ? symbols[index] : value.charAt(index);
                                        }
                                    }
                                    return result + '"';
                                };
                                var serialize = function(property, object, callback, properties, whitespace, indentation, stack) {
                                    var value, className, year, month, date, time, hours, minutes, seconds, milliseconds, results, element, index, length, prefix, result;
                                    try {
                                        value = object[property];
                                    } catch (exception) {}
                                    if (typeof value == "object" && value) {
                                        className = getClass.call(value);
                                        if (className == dateClass && !isProperty.call(value, "toJSON")) {
                                            if (value > -1 / 0 && value < 1 / 0) {
                                                if (getDay) {
                                                    date = floor(value / 864e5);
                                                    for (year = floor(date / 365.2425) + 1970 - 1; getDay(year + 1, 0) <= date; year++) ;
                                                    for (month = floor((date - getDay(year, 0)) / 30.42); getDay(year, month + 1) <= date; month++) ;
                                                    date = 1 + date - getDay(year, month);
                                                    time = (value % 864e5 + 864e5) % 864e5;
                                                    hours = floor(time / 36e5) % 24;
                                                    minutes = floor(time / 6e4) % 60;
                                                    seconds = floor(time / 1e3) % 60;
                                                    milliseconds = time % 1e3;
                                                } else {
                                                    year = value.getUTCFullYear();
                                                    month = value.getUTCMonth();
                                                    date = value.getUTCDate();
                                                    hours = value.getUTCHours();
                                                    minutes = value.getUTCMinutes();
                                                    seconds = value.getUTCSeconds();
                                                    milliseconds = value.getUTCMilliseconds();
                                                }
                                                value = (year <= 0 || year >= 1e4 ? (year < 0 ? "-" : "+") + toPaddedString(6, year < 0 ? -year : year) : toPaddedString(4, year)) + "-" + toPaddedString(2, month + 1) + "-" + toPaddedString(2, date) + "T" + toPaddedString(2, hours) + ":" + toPaddedString(2, minutes) + ":" + toPaddedString(2, seconds) + "." + toPaddedString(3, milliseconds) + "Z";
                                            } else {
                                                value = null;
                                            }
                                        } else if (typeof value.toJSON == "function" && (className != numberClass && className != stringClass && className != arrayClass || isProperty.call(value, "toJSON"))) {
                                            value = value.toJSON(property);
                                        }
                                    }
                                    if (callback) {
                                        value = callback.call(object, property, value);
                                    }
                                    if (value === null) {
                                        return "null";
                                    }
                                    className = getClass.call(value);
                                    if (className == booleanClass) {
                                        return "" + value;
                                    } else if (className == numberClass) {
                                        return value > -1 / 0 && value < 1 / 0 ? "" + value : "null";
                                    } else if (className == stringClass) {
                                        return quote("" + value);
                                    }
                                    if (typeof value == "object") {
                                        for (length = stack.length; length--; ) {
                                            if (stack[length] === value) {
                                                throw TypeError();
                                            }
                                        }
                                        stack.push(value);
                                        results = [];
                                        prefix = indentation;
                                        indentation += whitespace;
                                        if (className == arrayClass) {
                                            for (index = 0, length = value.length; index < length; index++) {
                                                element = serialize(index, value, callback, properties, whitespace, indentation, stack);
                                                results.push(element === undef ? "null" : element);
                                            }
                                            result = results.length ? whitespace ? "[\n" + indentation + results.join(",\n" + indentation) + "\n" + prefix + "]" : "[" + results.join(",") + "]" : "[]";
                                        } else {
                                            forEach(properties || value, function(property) {
                                                var element = serialize(property, value, callback, properties, whitespace, indentation, stack);
                                                if (element !== undef) {
                                                    results.push(quote(property) + ":" + (whitespace ? " " : "") + element);
                                                }
                                            });
                                            result = results.length ? whitespace ? "{\n" + indentation + results.join(",\n" + indentation) + "\n" + prefix + "}" : "{" + results.join(",") + "}" : "{}";
                                        }
                                        stack.pop();
                                        return result;
                                    }
                                };
                                exports.stringify = function(source, filter, width) {
                                    var whitespace, callback, properties, className;
                                    if (objectTypes[typeof filter] && filter) {
                                        if ((className = getClass.call(filter)) == functionClass) {
                                            callback = filter;
                                        } else if (className == arrayClass) {
                                            properties = {};
                                            for (var index = 0, length = filter.length, value; index < length; value = filter[index++], 
                                            (className = getClass.call(value), className == stringClass || className == numberClass) && (properties[value] = 1)) ;
                                        }
                                    }
                                    if (width) {
                                        if ((className = getClass.call(width)) == numberClass) {
                                            if ((width -= width % 1) > 0) {
                                                for (whitespace = "", width > 10 && (width = 10); whitespace.length < width; whitespace += " ") ;
                                            }
                                        } else if (className == stringClass) {
                                            whitespace = width.length <= 10 ? width : width.slice(0, 10);
                                        }
                                    }
                                    return serialize("", (value = {}, value[""] = source, value), callback, properties, whitespace, "", []);
                                };
                            }
                            if (!has("json-parse")) {
                                var fromCharCode = String.fromCharCode;
                                var Unescapes = {
                                    92: "\\",
                                    34: '"',
                                    47: "/",
                                    98: "\b",
                                    116: "\t",
                                    110: "\n",
                                    102: "\f",
                                    114: "\r"
                                };
                                var Index, Source;
                                var abort = function() {
                                    Index = Source = null;
                                    throw SyntaxError();
                                };
                                var lex = function() {
                                    var source = Source, length = source.length, value, begin, position, isSigned, charCode;
                                    while (Index < length) {
                                        charCode = source.charCodeAt(Index);
                                        switch (charCode) {
                                          case 9:
                                          case 10:
                                          case 13:
                                          case 32:
                                            Index++;
                                            break;

                                          case 123:
                                          case 125:
                                          case 91:
                                          case 93:
                                          case 58:
                                          case 44:
                                            value = charIndexBuggy ? source.charAt(Index) : source[Index];
                                            Index++;
                                            return value;

                                          case 34:
                                            for (value = "@", Index++; Index < length; ) {
                                                charCode = source.charCodeAt(Index);
                                                if (charCode < 32) {
                                                    abort();
                                                } else if (charCode == 92) {
                                                    charCode = source.charCodeAt(++Index);
                                                    switch (charCode) {
                                                      case 92:
                                                      case 34:
                                                      case 47:
                                                      case 98:
                                                      case 116:
                                                      case 110:
                                                      case 102:
                                                      case 114:
                                                        value += Unescapes[charCode];
                                                        Index++;
                                                        break;

                                                      case 117:
                                                        begin = ++Index;
                                                        for (position = Index + 4; Index < position; Index++) {
                                                            charCode = source.charCodeAt(Index);
                                                            if (!(charCode >= 48 && charCode <= 57 || charCode >= 97 && charCode <= 102 || charCode >= 65 && charCode <= 70)) {
                                                                abort();
                                                            }
                                                        }
                                                        value += fromCharCode("0x" + source.slice(begin, Index));
                                                        break;

                                                      default:
                                                        abort();
                                                    }
                                                } else {
                                                    if (charCode == 34) {
                                                        break;
                                                    }
                                                    charCode = source.charCodeAt(Index);
                                                    begin = Index;
                                                    while (charCode >= 32 && charCode != 92 && charCode != 34) {
                                                        charCode = source.charCodeAt(++Index);
                                                    }
                                                    value += source.slice(begin, Index);
                                                }
                                            }
                                            if (source.charCodeAt(Index) == 34) {
                                                Index++;
                                                return value;
                                            }
                                            abort();

                                          default:
                                            begin = Index;
                                            if (charCode == 45) {
                                                isSigned = true;
                                                charCode = source.charCodeAt(++Index);
                                            }
                                            if (charCode >= 48 && charCode <= 57) {
                                                if (charCode == 48 && (charCode = source.charCodeAt(Index + 1), charCode >= 48 && charCode <= 57)) {
                                                    abort();
                                                }
                                                isSigned = false;
                                                for (;Index < length && (charCode = source.charCodeAt(Index), charCode >= 48 && charCode <= 57); Index++) ;
                                                if (source.charCodeAt(Index) == 46) {
                                                    position = ++Index;
                                                    for (;position < length && (charCode = source.charCodeAt(position), charCode >= 48 && charCode <= 57); position++) ;
                                                    if (position == Index) {
                                                        abort();
                                                    }
                                                    Index = position;
                                                }
                                                charCode = source.charCodeAt(Index);
                                                if (charCode == 101 || charCode == 69) {
                                                    charCode = source.charCodeAt(++Index);
                                                    if (charCode == 43 || charCode == 45) {
                                                        Index++;
                                                    }
                                                    for (position = Index; position < length && (charCode = source.charCodeAt(position), 
                                                    charCode >= 48 && charCode <= 57); position++) ;
                                                    if (position == Index) {
                                                        abort();
                                                    }
                                                    Index = position;
                                                }
                                                return +source.slice(begin, Index);
                                            }
                                            if (isSigned) {
                                                abort();
                                            }
                                            if (source.slice(Index, Index + 4) == "true") {
                                                Index += 4;
                                                return true;
                                            } else if (source.slice(Index, Index + 5) == "false") {
                                                Index += 5;
                                                return false;
                                            } else if (source.slice(Index, Index + 4) == "null") {
                                                Index += 4;
                                                return null;
                                            }
                                            abort();
                                        }
                                    }
                                    return "$";
                                };
                                var get = function(value) {
                                    var results, hasMembers;
                                    if (value == "$") {
                                        abort();
                                    }
                                    if (typeof value == "string") {
                                        if ((charIndexBuggy ? value.charAt(0) : value[0]) == "@") {
                                            return value.slice(1);
                                        }
                                        if (value == "[") {
                                            results = [];
                                            for (;;hasMembers || (hasMembers = true)) {
                                                value = lex();
                                                if (value == "]") {
                                                    break;
                                                }
                                                if (hasMembers) {
                                                    if (value == ",") {
                                                        value = lex();
                                                        if (value == "]") {
                                                            abort();
                                                        }
                                                    } else {
                                                        abort();
                                                    }
                                                }
                                                if (value == ",") {
                                                    abort();
                                                }
                                                results.push(get(value));
                                            }
                                            return results;
                                        } else if (value == "{") {
                                            results = {};
                                            for (;;hasMembers || (hasMembers = true)) {
                                                value = lex();
                                                if (value == "}") {
                                                    break;
                                                }
                                                if (hasMembers) {
                                                    if (value == ",") {
                                                        value = lex();
                                                        if (value == "}") {
                                                            abort();
                                                        }
                                                    } else {
                                                        abort();
                                                    }
                                                }
                                                if (value == "," || typeof value != "string" || (charIndexBuggy ? value.charAt(0) : value[0]) != "@" || lex() != ":") {
                                                    abort();
                                                }
                                                results[value.slice(1)] = get(lex());
                                            }
                                            return results;
                                        }
                                        abort();
                                    }
                                    return value;
                                };
                                var update = function(source, property, callback) {
                                    var element = walk(source, property, callback);
                                    if (element === undef) {
                                        delete source[property];
                                    } else {
                                        source[property] = element;
                                    }
                                };
                                var walk = function(source, property, callback) {
                                    var value = source[property], length;
                                    if (typeof value == "object" && value) {
                                        if (getClass.call(value) == arrayClass) {
                                            for (length = value.length; length--; ) {
                                                update(value, length, callback);
                                            }
                                        } else {
                                            forEach(value, function(property) {
                                                update(value, property, callback);
                                            });
                                        }
                                    }
                                    return callback.call(source, property, value);
                                };
                                exports.parse = function(source, callback) {
                                    var result, value;
                                    Index = 0;
                                    Source = "" + source;
                                    result = get(lex());
                                    if (lex() != "$") {
                                        abort();
                                    }
                                    Index = Source = null;
                                    return callback && getClass.call(callback) == functionClass ? walk((value = {}, 
                                    value[""] = result, value), "", callback) : result;
                                };
                            }
                        }
                        exports["runInContext"] = runInContext;
                        return exports;
                    }
                    if (freeExports && !isLoader) {
                        runInContext(root, freeExports);
                    } else {
                        var nativeJSON = root.JSON, previousJSON = root["JSON3"], isRestored = false;
                        var JSON3 = runInContext(root, root["JSON3"] = {
                            noConflict: function() {
                                if (!isRestored) {
                                    isRestored = true;
                                    root.JSON = nativeJSON;
                                    root["JSON3"] = previousJSON;
                                    nativeJSON = previousJSON = null;
                                }
                                return JSON3;
                            }
                        });
                        root.JSON = {
                            parse: JSON3.parse,
                            stringify: JSON3.stringify
                        };
                    }
                    if (isLoader) {
                        define(function() {
                            return JSON3;
                        });
                    }
                }).call(this);
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {} ],
        59: [ function(require, module, exports) {
            "use strict";
            var has = Object.prototype.hasOwnProperty;
            function decode(input) {
                return decodeURIComponent(input.replace(/\+/g, " "));
            }
            function querystring(query) {
                var parser = /([^=?&]+)=?([^&]*)/g, result = {}, part;
                while (part = parser.exec(query)) {
                    var key = decode(part[1]), value = decode(part[2]);
                    if (key in result) continue;
                    result[key] = value;
                }
                return result;
            }
            function querystringify(obj, prefix) {
                prefix = prefix || "";
                var pairs = [];
                if ("string" !== typeof prefix) prefix = "?";
                for (var key in obj) {
                    if (has.call(obj, key)) {
                        pairs.push(encodeURIComponent(key) + "=" + encodeURIComponent(obj[key]));
                    }
                }
                return pairs.length ? prefix + pairs.join("&") : "";
            }
            exports.stringify = querystringify;
            exports.parse = querystring;
        }, {} ],
        60: [ function(require, module, exports) {
            "use strict";
            module.exports = function required(port, protocol) {
                protocol = protocol.split(":")[0];
                port = +port;
                if (!port) return false;
                switch (protocol) {
                  case "http":
                  case "ws":
                    return port !== 80;

                  case "https":
                  case "wss":
                    return port !== 443;

                  case "ftp":
                    return port !== 21;

                  case "gopher":
                    return port !== 70;

                  case "file":
                    return false;
                }
                return port !== 0;
            };
        }, {} ],
        61: [ function(require, module, exports) {
            (function(global) {
                "use strict";
                var required = require("requires-port"), qs = require("querystringify"), protocolre = /^([a-z][a-z0-9.+-]*:)?(\/\/)?([\S\s]*)/i, slashes = /^[A-Za-z][A-Za-z0-9+-.]*:\/\//;
                var rules = [ [ "#", "hash" ], [ "?", "query" ], function sanitize(address) {
                    return address.replace("\\", "/");
                }, [ "/", "pathname" ], [ "@", "auth", 1 ], [ NaN, "host", undefined, 1, 1 ], [ /:(\d+)$/, "port", undefined, 1 ], [ NaN, "hostname", undefined, 1, 1 ] ];
                var ignore = {
                    hash: 1,
                    query: 1
                };
                function lolcation(loc) {
                    var location = global && global.location || {};
                    loc = loc || location;
                    var finaldestination = {}, type = typeof loc, key;
                    if ("blob:" === loc.protocol) {
                        finaldestination = new Url(unescape(loc.pathname), {});
                    } else if ("string" === type) {
                        finaldestination = new Url(loc, {});
                        for (key in ignore) delete finaldestination[key];
                    } else if ("object" === type) {
                        for (key in loc) {
                            if (key in ignore) continue;
                            finaldestination[key] = loc[key];
                        }
                        if (finaldestination.slashes === undefined) {
                            finaldestination.slashes = slashes.test(loc.href);
                        }
                    }
                    return finaldestination;
                }
                function extractProtocol(address) {
                    var match = protocolre.exec(address);
                    return {
                        protocol: match[1] ? match[1].toLowerCase() : "",
                        slashes: !!match[2],
                        rest: match[3]
                    };
                }
                function resolve(relative, base) {
                    var path = (base || "/").split("/").slice(0, -1).concat(relative.split("/")), i = path.length, last = path[i - 1], unshift = false, up = 0;
                    while (i--) {
                        if (path[i] === ".") {
                            path.splice(i, 1);
                        } else if (path[i] === "..") {
                            path.splice(i, 1);
                            up++;
                        } else if (up) {
                            if (i === 0) unshift = true;
                            path.splice(i, 1);
                            up--;
                        }
                    }
                    if (unshift) path.unshift("");
                    if (last === "." || last === "..") path.push("");
                    return path.join("/");
                }
                function Url(address, location, parser) {
                    if (!(this instanceof Url)) {
                        return new Url(address, location, parser);
                    }
                    var relative, extracted, parse, instruction, index, key, instructions = rules.slice(), type = typeof location, url = this, i = 0;
                    if ("object" !== type && "string" !== type) {
                        parser = location;
                        location = null;
                    }
                    if (parser && "function" !== typeof parser) parser = qs.parse;
                    location = lolcation(location);
                    extracted = extractProtocol(address || "");
                    relative = !extracted.protocol && !extracted.slashes;
                    url.slashes = extracted.slashes || relative && location.slashes;
                    url.protocol = extracted.protocol || location.protocol || "";
                    address = extracted.rest;
                    if (!extracted.slashes) instructions[3] = [ /(.*)/, "pathname" ];
                    for (;i < instructions.length; i++) {
                        instruction = instructions[i];
                        if (typeof instruction === "function") {
                            address = instruction(address);
                            continue;
                        }
                        parse = instruction[0];
                        key = instruction[1];
                        if (parse !== parse) {
                            url[key] = address;
                        } else if ("string" === typeof parse) {
                            if (~(index = address.indexOf(parse))) {
                                if ("number" === typeof instruction[2]) {
                                    url[key] = address.slice(0, index);
                                    address = address.slice(index + instruction[2]);
                                } else {
                                    url[key] = address.slice(index);
                                    address = address.slice(0, index);
                                }
                            }
                        } else if (index = parse.exec(address)) {
                            url[key] = index[1];
                            address = address.slice(0, index.index);
                        }
                        url[key] = url[key] || (relative && instruction[3] ? location[key] || "" : "");
                        if (instruction[4]) url[key] = url[key].toLowerCase();
                    }
                    if (parser) url.query = parser(url.query);
                    if (relative && location.slashes && url.pathname.charAt(0) !== "/" && (url.pathname !== "" || location.pathname !== "")) {
                        url.pathname = resolve(url.pathname, location.pathname);
                    }
                    if (!required(url.port, url.protocol)) {
                        url.host = url.hostname;
                        url.port = "";
                    }
                    url.username = url.password = "";
                    if (url.auth) {
                        instruction = url.auth.split(":");
                        url.username = instruction[0] || "";
                        url.password = instruction[1] || "";
                    }
                    url.origin = url.protocol && url.host && url.protocol !== "file:" ? url.protocol + "//" + url.host : "null";
                    url.href = url.toString();
                }
                function set(part, value, fn) {
                    var url = this;
                    switch (part) {
                      case "query":
                        if ("string" === typeof value && value.length) {
                            value = (fn || qs.parse)(value);
                        }
                        url[part] = value;
                        break;

                      case "port":
                        url[part] = value;
                        if (!required(value, url.protocol)) {
                            url.host = url.hostname;
                            url[part] = "";
                        } else if (value) {
                            url.host = url.hostname + ":" + value;
                        }
                        break;

                      case "hostname":
                        url[part] = value;
                        if (url.port) value += ":" + url.port;
                        url.host = value;
                        break;

                      case "host":
                        url[part] = value;
                        if (/:\d+$/.test(value)) {
                            value = value.split(":");
                            url.port = value.pop();
                            url.hostname = value.join(":");
                        } else {
                            url.hostname = value;
                            url.port = "";
                        }
                        break;

                      case "protocol":
                        url.protocol = value.toLowerCase();
                        url.slashes = !fn;
                        break;

                      case "pathname":
                      case "hash":
                        if (value) {
                            var char = part === "pathname" ? "/" : "#";
                            url[part] = value.charAt(0) !== char ? char + value : value;
                        } else {
                            url[part] = value;
                        }
                        break;

                      default:
                        url[part] = value;
                    }
                    for (var i = 0; i < rules.length; i++) {
                        var ins = rules[i];
                        if (ins[4]) url[ins[1]] = url[ins[1]].toLowerCase();
                    }
                    url.origin = url.protocol && url.host && url.protocol !== "file:" ? url.protocol + "//" + url.host : "null";
                    url.href = url.toString();
                    return url;
                }
                function toString(stringify) {
                    if (!stringify || "function" !== typeof stringify) stringify = qs.stringify;
                    var query, url = this, protocol = url.protocol;
                    if (protocol && protocol.charAt(protocol.length - 1) !== ":") protocol += ":";
                    var result = protocol + (url.slashes ? "//" : "");
                    if (url.username) {
                        result += url.username;
                        if (url.password) result += ":" + url.password;
                        result += "@";
                    }
                    result += url.host + url.pathname;
                    query = "object" === typeof url.query ? stringify(url.query) : url.query;
                    if (query) result += "?" !== query.charAt(0) ? "?" + query : query;
                    if (url.hash) result += url.hash;
                    return result;
                }
                Url.prototype = {
                    set: set,
                    toString: toString
                };
                Url.extractProtocol = extractProtocol;
                Url.location = lolcation;
                Url.qs = qs;
                module.exports = Url;
            }).call(this, typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {});
        }, {
            querystringify: 59,
            "requires-port": 60
        } ]
    }, {}, [ 1 ])(1);
});

client = {
    registerUser: function() {
        ajax("/api/user/register", function(result) {
            var result = JSON.parse(result);
            if (result.ok) {
                rmClass(byId("call-oper-button"), "hide");
            } else {
                alert(result.message);
            }
        }, null, function(result) {
            var result = JSON.parse(result);
            alert("Во время регистрации произошла ошибка!");
            console.log("Во время регистрации произошла ошибка!" + result);
        }, "POST", "application/json");
    },
    sendMessage: function(message) {
        console.log("message to send : " + message);
        wssConnector.send("/method/fromUser", JSON.stringify({
            message: message,
            sessionId: getCookie("sessionIdUser")
        }), {});
        client.pushMessage(message, getCookie("operId"));
    },
    pullMessage: function(message, roomId) {
        if (!message) {
            message = "";
        }
        chatHistory = localStorage.getItem("chatHistory_" + roomId) == null ? [] : localStorage.getItem("chatHistory_" + roomId).split(endMarker);
        if (message != "") {
            chatHistory.push(getDateLable() + "   " + message);
        }
        localStorage.setItem("chatHistory_" + roomId, chatHistory.join(endMarker));
        byClass(byId("firstPage"), "chat")[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
    },
    callOper: function() {
        if (!getCookie("operId") && !getCookie("sessionIdOper")) {
            ajax("/api/oper/call", function(result) {
                result = JSON.parse(result);
                if (!result.ok) {
                    alert(result.message);
                } else {
                    addClass(byId("call-oper-button"), "hide");
                    rmClass(byId("firstPage"), "hide");
                    wssConnector = StompOverSock.getInstance(true);
                    window.setTimeout(function() {
                        wssConnector.send("/method/createUserAccount", JSON.stringify({
                            message: "",
                            sessionId: getCookie("sessionIdUser")
                        }), {});
                        wssConnector.subscribe("/user/queue/input/requests", Handlers.inputRequestsHandlerUser);
                        client.pushMessage(result.message, getCookie("operId"));
                    }, 1e3);
                }
            }, null, function(result) {
                var result = JSON.parse(result);
                alert("Во время вызова оператора произошлда ошибка!");
                console.log("Во время вызова оператора произошлда ошибка!" + result);
            }, "GET");
        } else {
            alert("Вы уже подключены к оператору");
        }
    },
    pushMessage: function(message, roomId) {
        if (!message) {
            message = "";
        }
        chatHistory = localStorage.getItem("chatHistory_" + roomId) == null ? [] : localStorage.getItem("chatHistory_" + roomId).split(endMarker);
        if (message != "") {
            chatHistory.push(getDateLable() + "   " + message);
        }
        localStorage.setItem("chatHistory_" + roomId, chatHistory.join(endMarker));
        byClass(byId("firstPage"), "chat")[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
        byClass(byId("firstPage"), "message")[0].value = "";
    }
};

var wssConnector;

addEvent(window, "load", function() {
    if (!getCookie("sessionIdUser")) {
        client.registerUser();
    } else {
        rmClass(byId("call-oper-button"), "hide");
    }
});

var WebSock = function(fullUrl, listener, closeCallback) {
    "use strict";
    if (!fullUrl) {
        throw new Error("Url must be specified");
    }
    if (!listener) {
        throw new Error("No listener for opened WebSocket");
    }
    this.closed = false;
    this.queue = [];
    this.failCount = 0;
    this.onMessage = function(e) {
        var raw = e.data, parsed = JSON.parse(raw);
        listener(parsed);
    };
    this.openWebSocket = function() {
        if (this.failCount > 3) {
            this.close("1000", "could not connect");
            return;
        }
        try {
            this.ws = new WebSocket(fullUrl);
            this.ws.onmessage = this.onMessage.bind(this);
            this.ws.onclose = this.onClose.bind(this);
            return true;
        } catch (e) {
            ++this.failCount;
            console.error("Can't open WebSocket connection");
            return false;
        }
    };
    this.onClose = function() {
        if (closeCallback) {
            closeCallback();
        } else if (!this.closed) {
            window.setTimeout(this.openWebSocket.bind(this), 1e3);
        }
    };
    this.send = function(obj, doNotTouchMyData) {
        if (!obj) {
            throw new Error("no arguments provided");
        }
        obj = doNotTouchMyData ? obj : JSON.stringify(obj);
        this.queue.push(obj);
    }.bind(this);
    this.close = function(code, reason) {
        if (this.ws && this.ws.close) {
            this.ws.close(code, reason);
        }
        if (closeCallback) {
            closeCallback();
        }
        this.closed = true;
    };
    this.processQueue = function() {
        if (this.closed || this.failCount > 3) {
            this.closed = true;
            window.clearInterval(this.interval);
            return;
        }
        var temp;
        while (temp = this.queue.shift()) {
            try {
                this.ws.send(temp);
            } catch (e) {
                console.warn("Websockets send error", arguments);
                ++this.failCount;
                this.queue.unshift(temp);
                window.setTimeout(this.openWebSocket.bind(this), 1e3);
                break;
            }
        }
    };
    if (this.openWebSocket()) {
        this.ws.onopen = this.onOpen.bind(this);
    }
    this.interval = window.setInterval(this.processQueue.bind(this), 100);
    return this;
};

WebSock.prototype = {
    pingTimer: 3e5,
    ping: function() {
        this.send("ping");
    },
    onOpen: function() {
        if (!this.closed) {
            this.ping();
            setTimeout(this.onOpen.bind(this), this.pingTimer);
        }
    }
};

(function(global, factory) {
    typeof exports === "object" && typeof module !== "undefined" ? module.exports = factory() : typeof define === "function" && define.amd ? define(factory) : global.webstomp = factory();
})(this, function() {
    "use strict";
    var classCallCheck = function(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    };
    var createClass = function() {
        function defineProperties(target, props) {
            for (var i = 0; i < props.length; i++) {
                var descriptor = props[i];
                descriptor.enumerable = descriptor.enumerable || false;
                descriptor.configurable = true;
                if ("value" in descriptor) descriptor.writable = true;
                Object.defineProperty(target, descriptor.key, descriptor);
            }
        }
        return function(Constructor, protoProps, staticProps) {
            if (protoProps) defineProperties(Constructor.prototype, protoProps);
            if (staticProps) defineProperties(Constructor, staticProps);
            return Constructor;
        };
    }();
    var slicedToArray = function() {
        function sliceIterator(arr, i) {
            var _arr = [];
            var _n = true;
            var _d = false;
            var _e = undefined;
            try {
                for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {
                    _arr.push(_s.value);
                    if (i && _arr.length === i) break;
                }
            } catch (err) {
                _d = true;
                _e = err;
            } finally {
                try {
                    if (!_n && _i["return"]) _i["return"]();
                } finally {
                    if (_d) throw _e;
                }
            }
            return _arr;
        }
        return function(arr, i) {
            if (Array.isArray(arr)) {
                return arr;
            } else if (Symbol.iterator in Object(arr)) {
                return sliceIterator(arr, i);
            } else {
                throw new TypeError("Invalid attempt to destructure non-iterable instance");
            }
        };
    }();
    var toConsumableArray = function(arr) {
        if (Array.isArray(arr)) {
            for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) arr2[i] = arr[i];
            return arr2;
        } else {
            return Array.from(arr);
        }
    };
    var VERSIONS = {
        V1_0: "1.0",
        V1_1: "1.1",
        V1_2: "1.2",
        supportedVersions: function supportedVersions() {
            return "1.2,1.1,1.0";
        },
        supportedProtocols: function supportedProtocols() {
            return [ "v10.stomp", "v11.stomp", "v12.stomp" ];
        }
    };
    var PROTOCOLS_VERSIONS = {
        "v10.stomp": VERSIONS.V1_0,
        "v11.stomp": VERSIONS.V1_1,
        "v12.stomp": VERSIONS.V1_2
    };
    function getSupportedVersion(protocol, debug) {
        var knownVersion = PROTOCOLS_VERSIONS[protocol];
        if (!knownVersion && debug) {
            debug("DEPRECATED: " + protocol + " is not a recognized STOMP version. In next major client version, this will close the connection.");
        }
        return knownVersion || VERSIONS.V1_2;
    }
    var BYTES = {
        LF: "\n",
        NULL: "\0"
    };
    var trim = function trim(str) {
        return str.replace(/^\s+|\s+$/g, "");
    };
    function unicodeStringToTypedArray(s) {
        var escstr = encodeURIComponent(s);
        var binstr = escstr.replace(/%([0-9A-F]{2})/g, function(match, p1) {
            return String.fromCharCode("0x" + p1);
        });
        var arr = Array.prototype.map.call(binstr, function(c) {
            return c.charCodeAt(0);
        });
        return new Uint8Array(arr);
    }
    function typedArrayToUnicodeString(ua) {
        var binstr = String.fromCharCode.apply(String, toConsumableArray(ua));
        var escstr = binstr.replace(/(.)/g, function(m, p) {
            var code = p.charCodeAt(0).toString(16).toUpperCase();
            if (code.length < 2) {
                code = "0" + code;
            }
            return "%" + code;
        });
        return decodeURIComponent(escstr);
    }
    function sizeOfUTF8(s) {
        if (!s) return 0;
        return encodeURIComponent(s).match(/%..|./g).length;
    }
    function createId() {
        var ts = new Date().getTime();
        var rand = Math.floor(Math.random() * 1e3);
        return ts + "-" + rand;
    }
    var Frame = function() {
        function Frame(command) {
            var headers = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
            var body = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : "";
            classCallCheck(this, Frame);
            this.command = command;
            this.headers = headers;
            this.body = body;
        }
        createClass(Frame, [ {
            key: "toString",
            value: function toString() {
                var _this = this;
                var lines = [ this.command ], skipContentLength = this.headers["content-length"] === false;
                if (skipContentLength) delete this.headers["content-length"];
                Object.keys(this.headers).forEach(function(name) {
                    var value = _this.headers[name];
                    lines.push(name + ":" + value);
                });
                if (this.body && !skipContentLength) {
                    lines.push("content-length:" + sizeOfUTF8(this.body));
                }
                lines.push(BYTES.LF + this.body);
                return lines.join(BYTES.LF);
            }
        } ], [ {
            key: "unmarshallSingle",
            value: function unmarshallSingle(data) {
                var divider = data.search(new RegExp(BYTES.LF + BYTES.LF)), headerLines = data.substring(0, divider).split(BYTES.LF), command = headerLines.shift(), headers = {}, body = "", bodyIndex = divider + 2;
                var _iteratorNormalCompletion = true;
                var _didIteratorError = false;
                var _iteratorError = undefined;
                try {
                    for (var _iterator = headerLines.reverse()[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                        var line = _step.value;
                        var idx = line.indexOf(":");
                        headers[trim(line.substring(0, idx))] = trim(line.substring(idx + 1));
                    }
                } catch (err) {
                    _didIteratorError = true;
                    _iteratorError = err;
                } finally {
                    try {
                        if (!_iteratorNormalCompletion && _iterator.return) {
                            _iterator.return();
                        }
                    } finally {
                        if (_didIteratorError) {
                            throw _iteratorError;
                        }
                    }
                }
                if (headers["content-length"]) {
                    var len = parseInt(headers["content-length"], 10);
                    body = ("" + data).substring(bodyIndex, bodyIndex + len);
                } else {
                    var chr = null;
                    for (var i = bodyIndex; i < data.length; i++) {
                        chr = data.charAt(i);
                        if (chr === BYTES.NULL) break;
                        body += chr;
                    }
                }
                return new Frame(command, headers, body);
            }
        }, {
            key: "unmarshall",
            value: function unmarshall(datas) {
                var frames = datas.split(new RegExp(BYTES.NULL + BYTES.LF + "*")), firstFrames = frames.slice(0, -1), lastFrame = frames.slice(-1)[0], r = {
                    frames: firstFrames.map(function(f) {
                        return Frame.unmarshallSingle(f);
                    }),
                    partial: ""
                };
                if (lastFrame === BYTES.LF || lastFrame.search(RegExp(BYTES.NULL + BYTES.LF + "*$")) !== -1) {
                    r.frames.push(Frame.unmarshallSingle(lastFrame));
                } else {
                    r.partial = lastFrame;
                }
                return r;
            }
        }, {
            key: "marshall",
            value: function marshall(command, headers, body) {
                var frame = new Frame(command, headers, body);
                return frame.toString() + BYTES.NULL;
            }
        } ]);
        return Frame;
    }();
    var Client = function() {
        function Client(ws) {
            var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
            classCallCheck(this, Client);
            var _options$binary = options.binary, binary = _options$binary === undefined ? false : _options$binary, _options$heartbeat = options.heartbeat, heartbeat = _options$heartbeat === undefined ? {
                outgoing: 1e4,
                incoming: 1e4
            } : _options$heartbeat, _options$debug = options.debug, debug = _options$debug === undefined ? true : _options$debug, _options$protocols = options.protocols, protocols = _options$protocols === undefined ? [] : _options$protocols;
            this.ws = ws;
            this.ws.binaryType = "arraybuffer";
            this.isBinary = !!binary;
            this.hasDebug = !!debug;
            this.connected = false;
            this.heartbeat = heartbeat || {
                outgoing: 0,
                incoming: 0
            };
            this.maxWebSocketFrameSize = 16 * 1024;
            this.subscriptions = {};
            this.partialData = "";
            this.protocols = protocols;
        }
        createClass(Client, [ {
            key: "debug",
            value: function debug() {
                var _console;
                if (this.hasDebug) (_console = console).log.apply(_console, arguments);
            }
        }, {
            key: "connect",
            value: function connect() {
                var _this = this;
                var _parseConnect2 = this._parseConnect.apply(this, arguments), _parseConnect3 = slicedToArray(_parseConnect2, 3), headers = _parseConnect3[0], connectCallback = _parseConnect3[1], errorCallback = _parseConnect3[2];
                this.connectCallback = connectCallback;
                this.debug("Opening Web Socket...");
                this.ws.onmessage = function(evt) {
                    var data = evt.data;
                    if (evt.data instanceof ArrayBuffer) {
                        data = typedArrayToUnicodeString(new Uint8Array(evt.data));
                    }
                    _this.serverActivity = Date.now();
                    if (data === BYTES.LF) {
                        _this.debug("<<< PONG");
                        return;
                    }
                    _this.debug("<<< " + data);
                    var unmarshalledData = Frame.unmarshall(_this.partialData + data);
                    _this.partialData = unmarshalledData.partial;
                    unmarshalledData.frames.forEach(function(frame) {
                        switch (frame.command) {
                          case "CONNECTED":
                            _this.debug("connected to server " + frame.headers.server);
                            _this.connected = true;
                            _this.version = frame.headers.version;
                            _this._setupHeartbeat(frame.headers);
                            if (connectCallback) connectCallback(frame);
                            break;

                          case "MESSAGE":
                            var subscription = frame.headers.subscription;
                            var onreceive = _this.subscriptions[subscription] || _this.onreceive;
                            if (onreceive) {
                                var messageID = _this.version === VERSIONS.V1_2 && frame.headers.ack || frame.headers["message-id"];
                                frame.ack = _this.ack.bind(_this, messageID, subscription);
                                frame.nack = _this.nack.bind(_this, messageID, subscription);
                                onreceive(frame);
                            } else {
                                _this.debug("Unhandled received MESSAGE: " + frame);
                            }
                            break;

                          case "RECEIPT":
                            if (_this.onreceipt) _this.onreceipt(frame);
                            break;

                          case "ERROR":
                            if (errorCallback) errorCallback(frame);
                            break;

                          default:
                            _this.debug("Unhandled frame: " + frame);
                        }
                    });
                };
                this.ws.onclose = function(event) {
                    _this.debug("Whoops! Lost connection to " + _this.ws.url + ":", {
                        event: event
                    });
                    _this._cleanUp();
                    if (errorCallback) errorCallback(event);
                };
                this.ws.onopen = function() {
                    _this.debug("Web Socket Opened...");
                    headers["accept-version"] = getSupportedVersion(_this.ws.protocol || _this.protocols[0], _this.debug.bind(_this));
                    if (!headers["heart-beat"]) {
                        headers["heart-beat"] = [ _this.heartbeat.outgoing, _this.heartbeat.incoming ].join(",");
                    }
                    _this._transmit("CONNECT", headers);
                };
                if (this.ws.readyState === this.ws.OPEN) {
                    this.ws.onopen();
                }
            }
        }, {
            key: "disconnect",
            value: function disconnect(disconnectCallback) {
                var headers = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
                this._transmit("DISCONNECT", headers);
                this.ws.onclose = null;
                this.ws.close();
                this._cleanUp();
                if (disconnectCallback) disconnectCallback();
            }
        }, {
            key: "send",
            value: function send(destination) {
                var body = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : "";
                var headers = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
                var hdrs = Object.assign({}, headers);
                hdrs.destination = destination;
                this._transmit("SEND", hdrs, body);
            }
        }, {
            key: "begin",
            value: function begin() {
                var transaction = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : "tx-" + createId();
                this._transmit("BEGIN", {
                    transaction: transaction
                });
                return {
                    id: transaction,
                    commit: this.commit.bind(this, transaction),
                    abort: this.abort.bind(this, transaction)
                };
            }
        }, {
            key: "commit",
            value: function commit(transaction) {
                this._transmit("COMMIT", {
                    transaction: transaction
                });
            }
        }, {
            key: "abort",
            value: function abort(transaction) {
                this._transmit("ABORT", {
                    transaction: transaction
                });
            }
        }, {
            key: "ack",
            value: function ack(messageID, subscription) {
                var headers = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
                var hdrs = Object.assign({}, headers);
                var idAttr = this.version === VERSIONS.V1_2 ? "id" : "message-id";
                hdrs[idAttr] = messageID;
                hdrs.subscription = subscription;
                this._transmit("ACK", hdrs);
            }
        }, {
            key: "nack",
            value: function nack(messageID, subscription) {
                var headers = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
                var hdrs = Object.assign({}, headers);
                var idAttr = this.version === VERSIONS.V1_2 ? "id" : "message-id";
                hdrs[idAttr] = messageID;
                hdrs.subscription = subscription;
                this._transmit("NACK", hdrs);
            }
        }, {
            key: "subscribe",
            value: function subscribe(destination, callback) {
                var headers = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
                var hdrs = Object.assign({}, headers);
                if (!hdrs.id) hdrs.id = "sub-" + createId();
                hdrs.destination = destination;
                this.subscriptions[hdrs.id] = callback;
                this._transmit("SUBSCRIBE", hdrs);
                return {
                    id: hdrs.id,
                    unsubscribe: this.unsubscribe.bind(this, hdrs.id)
                };
            }
        }, {
            key: "unsubscribe",
            value: function unsubscribe(id) {
                var headers = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
                var hdrs = Object.assign({}, headers);
                delete this.subscriptions[id];
                hdrs.id = id;
                this._transmit("UNSUBSCRIBE", hdrs);
            }
        }, {
            key: "_cleanUp",
            value: function _cleanUp() {
                this.connected = false;
                clearInterval(this.pinger);
                clearInterval(this.ponger);
            }
        }, {
            key: "_transmit",
            value: function _transmit(command, headers, body) {
                var out = Frame.marshall(command, headers, body);
                this.debug(">>> " + out, {
                    frame: {
                        command: command,
                        headers: headers,
                        body: body
                    }
                });
                this._wsSend(out);
            }
        }, {
            key: "_wsSend",
            value: function _wsSend(data) {
                if (this.isBinary) data = unicodeStringToTypedArray(data);
                this.debug(">>> length " + data.length);
                while (true) {
                    if (data.length > this.maxWebSocketFrameSize) {
                        this.ws.send(data.slice(0, this.maxWebSocketFrameSize));
                        data = data.slice(this.maxWebSocketFrameSize);
                        this.debug("remaining = " + data.length);
                    } else {
                        return this.ws.send(data);
                    }
                }
            }
        }, {
            key: "_setupHeartbeat",
            value: function _setupHeartbeat(headers) {
                var _this2 = this;
                if (this.version !== VERSIONS.V1_1 && this.version !== VERSIONS.V1_2) return;
                var _split$map = (headers["heart-beat"] || "0,0").split(",").map(function(v) {
                    return parseInt(v, 10);
                }), _split$map2 = slicedToArray(_split$map, 2), serverOutgoing = _split$map2[0], serverIncoming = _split$map2[1];
                if (!(this.heartbeat.outgoing === 0 || serverIncoming === 0)) {
                    var ttl = Math.max(this.heartbeat.outgoing, serverIncoming);
                    this.debug("send PING every " + ttl + "ms");
                    this.pinger = setInterval(function() {
                        _this2._wsSend(BYTES.LF);
                        _this2.debug(">>> PING");
                    }, ttl);
                }
                if (!(this.heartbeat.incoming === 0 || serverOutgoing === 0)) {
                    var _ttl = Math.max(this.heartbeat.incoming, serverOutgoing);
                    this.debug("check PONG every " + _ttl + "ms");
                    this.ponger = setInterval(function() {
                        var delta = Date.now() - _this2.serverActivity;
                        if (delta > _ttl * 2) {
                            _this2.debug("did not receive server activity for the last " + delta + "ms");
                            _this2.ws.close();
                        }
                    }, _ttl);
                }
            }
        }, {
            key: "_parseConnect",
            value: function _parseConnect() {
                var headers = {}, connectCallback = void 0, errorCallback = void 0;
                for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
                    args[_key] = arguments[_key];
                }
                switch (args.length) {
                  case 2:
                    headers = args[0];
                    connectCallback = args[1];
                    break;

                  case 3:
                    if (args[1] instanceof Function) {
                        headers = args[0];
                        connectCallback = args[1];
                        errorCallback = args[2];
                    } else {
                        headers.login = args[0];
                        headers.passcode = args[1];
                        connectCallback = args[2];
                    }
                    break;

                  case 4:
                    headers.login = args[0];
                    headers.passcode = args[1];
                    connectCallback = args[2];
                    errorCallback = args[3];
                    break;

                  default:
                    headers.login = args[0];
                    headers.passcode = args[1];
                    connectCallback = args[2];
                    errorCallback = args[3];
                    headers.host = args[4];
                }
                return [ headers, connectCallback, errorCallback ];
            }
        } ]);
        return Client;
    }();
    var webstomp = {
        Frame: Frame,
        VERSIONS: VERSIONS,
        client: function client(url) {
            var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
            var ws = new WebSocket(url, options.protocols || VERSIONS.supportedProtocols());
            return new Client(ws, options);
        },
        over: function over() {
            for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
                args[_key] = arguments[_key];
            }
            return new (Function.prototype.bind.apply(Client, [ null ].concat(args)))();
        }
    };
    return webstomp;
});

var emailRegexp = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

var urlRegexp = /^(ftp|http|https):\/\/[^ "]+$/;

var endMarker = "--endMesMark";

function ajax(request, func, params, errorFunction, mode, contentType) {
    var ajaxRequest = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
    if (typeof mode === "string" && mode != "RAW") {
        ajaxRequest.open(mode, request, true);
    } else {
        ajaxRequest.open(typeof params === "undefined" || params == null ? "GET" : "POST", request, true);
    }
    ajaxRequest.timeout = 2e4;
    ajaxRequest.onload = ajaxRequest.onerror = function() {
        if (this.status >= 200 && ajaxRequest.status <= 206 && typeof func === "function") {
            if (mode == "RAW") {
                func(this.response);
            } else {
                func(this.responseText);
            }
        } else {
            if (typeof errorFunction === "function") {
                errorFunction(this.status);
            }
        }
    };
    if (mode == "RAW") ajaxRequest.responseType = "arraybuffer";
    if (contentType) ajaxRequest.setRequestHeader("Content-Type", contentType); else {
        if (mode == "RAW") ajaxRequest.setRequestHeader("Content-Type", "application/bytes"); else ajaxRequest.setRequestHeader("Content-Type", "text/plain; charset=utf-8");
    }
    if (mode == "DELETE") {
        params = "";
    }
    if (typeof version !== "undefined") {
        ajaxRequest.setRequestHeader("v", version);
    }
    if (typeof params === "object" && mode != "RAW") {
        params = JSON.stringify(params);
    }
    ajaxRequest.send(params);
    return ajaxRequest;
}

function getDateLable() {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, "0");
    var mm = String(today.getMonth() + 1).padStart(2, "0");
    var hh = String(today.getHours()).padStart(2, "0");
    var MM = String(today.getMinutes()).padStart(2, "0");
    today = mm + "." + dd + " " + hh + ":" + MM;
    return today;
}

function pushMessage(message, roomId) {
    if (!message) {
        message = "";
    }
    chatHistory = localStorage.getItem("chatHistory") == null ? [] : localStorage.getItem("chatHistory").split(endMarker);
    if (message != "") {
        chatHistory.push(getDateLable() + "   " + message);
    }
    localStorage.setItem("chatHistory", chatHistory.join(endMarker));
    byClass(byId("firstPage"), "chat")[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
    byClass(byId("firstPage"), "message")[0].value = "";
}

function pullMessage(message, roomId) {
    if (!message) {
        message = "";
    }
    chatHistory = localStorage.getItem("chatHistory") == null ? [] : localStorage.getItem("chatHistory").split(endMarker);
    if (message != "") {
        chatHistory.push(getDateLable() + "   " + message);
    }
    localStorage.setItem("chatHistory", chatHistory.join(endMarker));
    byClass(byId(roomId), "chat")[0].innerHTML = chatHistory.join("\n").replace(/--endMesMark/g, "");
}

function objectToMap(obj, key, value) {
    let result = {};
    obj.filter(obj => {
        result[obj[key]] = obj[value];
    });
    return result;
}

function getCookie(name) {
    var matches = document.cookie.match(new RegExp("(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, "\\$1") + "=([^;]*)"));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function ajaxRaw(url, callback, error) {
    ajax(url, function() {
        var ff = callback;
        return function(result) {
            ff(_arrayBufferToBase64(result));
        };
    }(), null, error, "RAW");
}

function ajaxRawRePost(url, file, callback, error) {
    ajax(url, function() {
        var ff = callback;
        return function(result) {
            ff(result);
        };
    }(), file, error, "RAW");
}

function _arrayBufferToBase64(buffer) {
    var binary = "";
    var bytes = new Uint8Array(buffer);
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i]);
    }
    return btoa(binary);
}

function b64toBlob(b64Data, contentType, sliceSize) {
    if (typeof b64Data == "undefined" || b64Data == null) {
        return new Blob([], {
            type: contentType
        });
    }
    contentType = contentType || "application/octet-stream";
    sliceSize = sliceSize || 512;
    var byteCharacters = atob(b64Data.replace(/[ \r\n]+$/, ""));
    var byteArrays = [];
    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);
        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, {
        type: contentType
    });
}

function byId(id) {
    return document.getElementById(id);
}

function byTag(domNode, tagName) {
    if (domNode == null || typeof domNode != "object" || typeof tagName === "undefined") {
        return null;
    }
    return domNode.getElementsByTagName(tagName);
}

function byClass(domNode, searchClass, tagName) {
    var tags;
    var el = [];
    if (typeof domNode == "string") {
        domNode = document.getElementById(domNode);
    }
    if (domNode == null) {
        domNode = document;
    }
    if (domNode.getElementsByClassName) {
        tags = domNode.getElementsByClassName(searchClass);
        var t;
        if (tagName) {
            for (t in tags) {
                if (tags.hasOwnProperty(t) && tags[t].tagName == tagName) {
                    el.push(tags[t]);
                }
            }
        } else {
            for (t in tags) {
                if (tags.hasOwnProperty(t)) {
                    el.push(tags[t]);
                }
            }
        }
        return el;
    }
    if (tagName == null) tagName = "*";
    tags = domNode.getElementsByTagName(tagName);
    var tcl = " " + searchClass + " ";
    for (var i = 0, j = 0; i < tags.length; i++) {
        var test = " " + tags[i].className + " ";
        if (test.indexOf(tcl) != -1) el[j++] = tags[i];
    }
    return el;
}

function hasClass(el, name) {
    if (typeof el == "undefined" || el == null) {
        return false;
    }
    return new RegExp("(\\s|^)" + name + "(\\s|$)").test(el.className);
}

function addClass(el, name) {
    if (typeof el == "undefined" || el == null) {
        return false;
    }
    if (!hasClass(el, name)) {
        el.className += (el.className ? " " : "") + name;
    }
}

function rmClass(el, name) {
    if (el === void 0 || el == null) {
        return false;
    }
    if (hasClass(el, name)) {
        el.className = el.className.replace(name, "").trim();
    }
}

function addEvent(el, type, eventHandle) {
    if (el === void 0 || el == null) {
        return;
    }
    if (el.addEventListener) {
        el.addEventListener(type, eventHandle, false);
    } else if (el.attachEvent) {
        el.attachEvent("on" + type, eventHandle);
    } else {
        el["on" + type] = eventHandle;
    }
}

Array.prototype.removeDublicateObject = function() {
    var arg = arguments;
    return this.filter(function(outerElt, outerIndex, outerArray) {
        return outerIndex == outerArray.findIndex(function(innerElt, innerIndex, innerArray) {
            var result = true;
            for (var i = 0; i < arg.length; ++i) {
                result = result && outerElt[arg[i]] === innerElt[arg[i]];
            }
            return result;
        });
    });
};

Date.prototype.toTimeString = function() {
    var d = this;
    var h = d.getHours() < 10 ? "0" + d.getHours() : d.getHours();
    var m = d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes();
    return h + ":" + m;
};

Date.prototype.toTimestampString = function() {
    var d = this;
    var ds = d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
    var ms = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
    var h = d.getHours() < 10 ? "0" + d.getHours() : d.getHours();
    var m = d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes();
    return h + ":" + m + " " + ds + "." + ms + "." + d.getFullYear();
};

Date.prototype.toRevertTimestampString = function() {
    var d = this;
    var ds = d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
    var ms = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
    var h = d.getHours() < 10 ? "0" + d.getHours() : d.getHours();
    var m = d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes();
    return d.getFullYear() + "." + ms + "." + ds + " " + h + ":" + m;
};

function today(td) {
    var d = new Date();
    return td.getDate() == d.getDate() && td.getMonth() == d.getMonth() && td.getFullYear() == d.getFullYear();
}

Date.prototype.toDatastampString = function() {
    var d = this;
    var ds = d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
    var ms = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
    return ds + "." + ms + "." + d.getFullYear();
};

if (!Date.prototype.toISOString) {
    (function() {
        function pad(number) {
            if (number < 10) {
                return "0" + number;
            }
            return number;
        }
        Date.prototype.toISOString = function() {
            return this.getUTCFullYear() + "-" + pad(this.getUTCMonth() + 1) + "-" + pad(this.getUTCDate()) + "T" + pad(this.getUTCHours()) + ":" + pad(this.getUTCMinutes()) + ":" + pad(this.getUTCSeconds()) + "." + (this.getUTCMilliseconds() / 1e3).toFixed(3).slice(2, 5) + "Z";
        };
    })();
}

function stopBubble(event) {
    if (event) {
        if (event.stopPropagation) {
            event.stopPropagation();
        } else {
            event.cancelBubble = true;
        }
    }
}

Date.prototype.toTimestampString = function() {
    var d = this;
    var ds = d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
    var ms = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
    var h = d.getHours() < 10 ? "0" + d.getHours() : d.getHours();
    var m = d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes();
    return h + ":" + m + " " + ds + "." + ms + "." + d.getFullYear();
};

Date.prototype.toSimpleString = function() {
    var d = this;
    var ds = d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
    var ms = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1;
    return ds + "." + ms + "." + d.getFullYear();
};

function foreach(items, callback, parentNode) {
    if (typeof items === void 0 || items == null) {
        return void 0;
    }
    if (parentNode !== void 0) {
        callback = function(value, index, collection) {
            return callback.call(parentNode, value, index, collection);
        };
    }
    var i, length = items.length;
    if (typeof length == "number" && length >= 0) {
        for (i = 0; i < length; i++) {
            callback(items[i], i, items);
        }
    } else {
        var keys = [];
        for (var key in items) if (items.hasOwnProperty(key)) keys.push(key);
        for (i = 0, length = keys.length; i < length; i++) {
            callback(items[keys[i]], keys[i], items);
        }
    }
    return items;
}

var buildCache = [];

function buildNode(nodeName, attributes, content, events) {
    var element;
    if (!(nodeName in buildCache)) {
        buildCache[nodeName] = document.createElement(nodeName);
    }
    element = buildCache[nodeName].cloneNode(false);
    if (attributes != null) {
        for (var attribute in attributes) {
            if (attributes.hasOwnProperty(attribute)) {
                if (attribute == "style" && typeof attributes["style"] !== "string") {
                    var sts = attributes["style"];
                    for (var s in sts) {
                        if (sts.hasOwnProperty(s)) {
                            try {
                                element.style[s] = sts[s];
                            } catch (e) {}
                        }
                    }
                } else {
                    element[attribute] = attributes[attribute];
                }
            }
        }
    }
    if (content != null) {
        if (typeof content == "object") {
            if (content.constructor == Array) {
                for (var c in content) {
                    if (content.hasOwnProperty(c) && content[c]) {
                        if (typeof content[c] == "object") {
                            element.appendChild(content[c]);
                        } else {
                            element.appendChild(document.createTextNode(content[c]));
                        }
                    }
                }
            } else {
                element.appendChild(content);
            }
        } else {
            element.innerHTML = content;
        }
    }
    if (events != null) {
        for (var e in events) {
            if (events.hasOwnProperty(e) && typeof events[e] === "function") {
                addEvent(element, e, events[e]);
            }
        }
    }
    return element;
}

function getOffset(el) {
    var _x = 0, _y = 0;
    while (el && el.tagName.toLowerCase() != "body" && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {
        _x += el.offsetLeft - el.scrollLeft;
        _y += el.offsetTop - el.scrollTop;
        el = el.offsetParent;
    }
    return {
        top: _y,
        left: _x
    };
}

function cursorOnBlock(el, x, y) {
    if (!el) {
        return false;
    }
    var offset = getOffset(el);
    return !(x < offset.left || y < offset.top || x > offset.left + el.offsetWidth || y > offset.top + el.offsetHeight);
}

function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

String.prototype.insertAt = function(str, position) {
    return [ this.slice(0, position), str, this.slice(position) ].join("");
};

function setCaretPosition(element, start, end) {
    if (!end) {
        end = start;
    }
    if (element.setSelectionRange) element.setSelectionRange(start, end); else if (element.createTextRange) {
        var range = element.createTextRange();
        range.collapse(true);
        range.moveStart("character", start);
        range.moveEnd("character", end);
        range.select();
    }
}

function loadJs(url, callback) {
    var node = document.createElement("script");
    node.setAttribute("type", "text/javascript");
    node.setAttribute("src", url);
    if (typeof callback == "function") {
        setTimeout(function() {
            callback();
        }, 2e3);
    }
    if (typeof node != "undefined") {
        document.getElementsByTagName("head")[0].appendChild(node);
    }
}

function stripHTMLTags(html, maxSize) {
    if (typeof maxSize === "number") {
        return html.replace(/</gi, "&lt;").replace(/>/gi, "&gt;").substr(0, maxSize);
    } else {
        return html.replace(/</gi, "&lt;").replace(/>/gi, "&gt;");
    }
}

var Base64 = {
    _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
    encode: function(e) {
        var t = "";
        var n, r, i, s, o, u, a;
        var f = 0;
        e = Base64._utf8_encode(e);
        while (f < e.length) {
            n = e.charCodeAt(f++);
            r = e.charCodeAt(f++);
            i = e.charCodeAt(f++);
            s = n >> 2;
            o = (n & 3) << 4 | r >> 4;
            u = (r & 15) << 2 | i >> 6;
            a = i & 63;
            if (isNaN(r)) {
                u = a = 64;
            } else if (isNaN(i)) {
                a = 64;
            }
            t = t + this._keyStr.charAt(s) + this._keyStr.charAt(o) + this._keyStr.charAt(u) + this._keyStr.charAt(a);
        }
        return t;
    },
    decode: function(e) {
        var t = "";
        var n, r, i;
        var s, o, u, a;
        var f = 0;
        e = e.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (f < e.length) {
            s = this._keyStr.indexOf(e.charAt(f++));
            o = this._keyStr.indexOf(e.charAt(f++));
            u = this._keyStr.indexOf(e.charAt(f++));
            a = this._keyStr.indexOf(e.charAt(f++));
            n = s << 2 | o >> 4;
            r = (o & 15) << 4 | u >> 2;
            i = (u & 3) << 6 | a;
            t = t + String.fromCharCode(n);
            if (u != 64) {
                t = t + String.fromCharCode(r);
            }
            if (a != 64) {
                t = t + String.fromCharCode(i);
            }
        }
        t = Base64._utf8_decode(t);
        return t;
    },
    _utf8_encode: function(e) {
        e = e.replace(/\r\n/g, "\n");
        var t = "";
        for (var n = 0; n < e.length; n++) {
            var r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r);
            } else if (r > 127 && r < 2048) {
                t += String.fromCharCode(r >> 6 | 192);
                t += String.fromCharCode(r & 63 | 128);
            } else {
                t += String.fromCharCode(r >> 12 | 224);
                t += String.fromCharCode(r >> 6 & 63 | 128);
                t += String.fromCharCode(r & 63 | 128);
            }
        }
        return t;
    },
    _utf8_decode: function(e) {
        var t = "";
        var n = 0;
        var r = c1 = c2 = 0;
        while (n < e.length) {
            r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r);
                n++;
            } else if (r > 191 && r < 224) {
                c2 = e.charCodeAt(n + 1);
                t += String.fromCharCode((r & 31) << 6 | c2 & 63);
                n += 2;
            } else {
                c2 = e.charCodeAt(n + 1);
                c3 = e.charCodeAt(n + 2);
                t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
                n += 3;
            }
        }
        return t;
    }
};

function hexToBase64(str) {
    return btoa(String.fromCharCode.apply(null, str.replace(/\r|\n/g, "").replace(/([\da-fA-F]{2}) ?/g, "0x$1 ").replace(/ +$/, "").split(" ")));
}

function base64ToHex(str, joinChar) {
    str = str.replace(/-/g, "+").replace(/_/g, "/");
    for (var i = 0, bin = atob(str.replace(/[ \r\n]+$/, "")), hex = []; i < bin.length; ++i) {
        var tmp = bin.charCodeAt(i).toString(16);
        if (tmp.length === 1) tmp = "0" + tmp;
        hex[hex.length] = tmp;
    }
    if (typeof joinChar == "undefined") {
        joinChar = "";
    }
    return hex.join(joinChar);
}

function b64revert(str) {
    var data = base64ToHex(str);
    var newData = "";
    for (var i = data.length - 1; i > 0; i -= 2) {
        newData += data[i - 1] + data[i];
    }
    return hexToBase64(newData);
}

CanvasRenderingContext2D.prototype.roundRect = function(x, y, w, h, r) {
    if (w < 2 * r) r = w / 2;
    if (h < 2 * r) r = h / 2;
    this.beginPath();
    this.moveTo(x + r, y);
    this.arcTo(x + w, y, x + w, y + h, r);
    this.arcTo(x + w, y + h, x, y + h, r);
    this.arcTo(x, y + h, x, y, r);
    this.arcTo(x, y, x + w, y, r);
    this.closePath();
    return this;
};

CanvasRenderingContext2D.prototype.wrapText = function(text, x, y, maxWidth, lineHeight) {
    var cars = text.split("\n");
    for (var ii = 0; ii < cars.length; ii++) {
        var line = "";
        var words = cars[ii].split(" ");
        for (var n = 0; n < words.length; n++) {
            var testLine = line + words[n] + " ";
            var metrics = this.measureText(testLine);
            var testWidth = metrics.width;
            if (testWidth > maxWidth) {
                this.fillText(line, x + (maxWidth - this.measureText(line).width) / 2, y);
                line = words[n] + " ";
                y += lineHeight;
            } else {
                line = testLine;
            }
        }
        this.fillText(line, x + (maxWidth - this.measureText(line).width) / 2, y);
        y += lineHeight;
    }
};

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

window.isArray = function() {
    return window.Array.isArray || function(obj) {
        return window.Object.toString.call(obj) === "[object Array]";
    };
}();

var _ = {
    template: function(text, settings) {
        var escaper = /\\|'|\r|\n|\u2028|\u2029/g;
        var noMatch = /(.)^/;
        settings = settings || {
            evaluate: /<%([\s\S]+?)%>/g,
            interpolate: /<%=([\s\S]+?)%>/g,
            escape: /<%-([\s\S]+?)%>/g
        };
        var matcher = RegExp([ (settings.escape || noMatch).source, (settings.interpolate || noMatch).source, (settings.evaluate || noMatch).source ].join("|") + "|$", "g");
        var index = 0;
        var source = "__p+='";
        text.replace(matcher, function(match, escape, interpolate, evaluate, offset) {
            source += text.slice(index, offset).replace(escaper, function(match) {
                return "\\" + match;
            });
            index = offset + match.length;
            if (escape) {
                source += "'+\n((__t=(" + escape + "))==null?'':_.escape(__t))+\n'";
            } else if (interpolate) {
                source += "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'";
            } else if (evaluate) {
                source += "';\n" + evaluate + "\n__p+='";
            }
            return match;
        });
        source += "';\n";
        if (!settings.variable) source = "with(obj||{}){\n" + source + "}\n";
        source = "var __t,__p='',__j=Array.prototype.join," + "print=function(){__p+=__j.call(arguments,'');};\n" + source + "return __p;\n";
        try {
            var render = new Function(settings.variable || "obj", "_", source);
        } catch (e) {
            e.source = source;
            throw e;
        }
        var template = function(data) {
            return render.call(this, data, _);
        };
        var argument = settings.variable || "obj";
        template.source = "function(" + argument + "){\n" + source + "}";
        return template;
    }
};

Handlers = {
    inputRequestsHandler: function(input) {
        console.log(input);
        input = JSON.parse(input.body);
        if (input.requestType == "CALL_OPER") {
            dialogs.appendChild(buildNode("DIV", {
                id: input.clientId,
                className: "pure-form"
            }, [ buildNode("DIV", {
                className: "pure-form"
            }, "Вы подключены к клиенту " + input.clientId), buildNode("TEXTAREA", {
                className: "chat"
            }), buildNode("BR"), buildNode("BR"), buildNode("INPUT", {
                className: "message",
                type: "text",
                placeholder: "Введите сообщение"
            }), buildNode("BR"), buildNode("BR"), buildNode("BUTTON", {
                className: "pure-u-1 pure-button card-0 primary"
            }, "Отправить", {
                click: function() {
                    admin.sendMessage(input.clientId, byClass(byId(input.clientId), "message")[0].value);
                }
            }) ]));
            rmClass(byId("dialogs"), "hide");
        } else {
            admin.pullMessage(input.message, input.sessionId);
        }
    },
    inputRequestsHandlerUser: function(input) {
        console.log(input);
        input = JSON.parse(input.body);
        client.pullMessage(input.message, getCookie("operId"));
    },
    resultHandler: function(message) {
        var response = JSON.parse(message.body);
        if (!response.ok) {
            alert(response.message);
        } else {
            chatHistory.push(response.message);
            localStorage.setItem("chatHistory", chatHistory);
            byClass(byId("firstPage"), "chat")[0].innerHTML = chatHistory.join("\n");
        }
    }
};