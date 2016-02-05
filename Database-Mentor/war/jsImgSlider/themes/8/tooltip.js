var tooltipOptions =
{
    showDelay: 10,
    hideDelay: 300,
    effect: "slide",
    duration: 200,
    relativeTo: "element",
    position: 0,
    smartPosition: true,
    offsetX: 0,
    offsetY: 12,
    maxWidth: 400,
    calloutSize: 6,
    calloutPosition: 0.5,
    sticky: false,
    overlay: false,
    license: "241l9"
};

/* JavaScript Tooltip v2014.2.20. Copyright www.menucool.com */
var tooltip = function (v) {
    var h = "length", Fb = "addEventListener", S, hc, bc = function () {
    }, rb = function (a, c, b) {
        if (a[Fb])a[Fb](c, b, false); else a.attachEvent && a.attachEvent("on" + c, b)
    }, c = {}, K = function (a) {
        if (a && a.stopPropagation)a.stopPropagation(); else window.event.cancelBubble = true
    }, gc = function (a) {
        var b = a ? a : window.event;
        if (b.preventDefault)b.preventDefault(); else a.returnValue = false
    }, Xb = function (d) {
        var a = d.childNodes, c = [];
        if (a)for (var b = 0, e = a[h]; b < e; b++)a[b].nodeType == 1 && c.push(a[b]);
        return c
    }, Z = {a: 0, b: 0}, g = null, cc = function (a) {
        if (!a)a = window.event;
        Z.a = a.clientX;
        Z.b = a.clientY
    }, Zb = function (b) {
        if (window.getComputedStyle)var c = window.getComputedStyle(b, null); else if (b.currentStyle)c = b.currentStyle; else c = b[a];
        return c
    }, I = "offsetLeft", L = "offsetTop", qb = "clientWidth", ob = "clientHeight", B = "appendChild", X = "display", q = "border", D = "opacity", J = "createElement", mb = "getElementsByTagName", y = "parentNode", V = "calloutSize", N = "position", Gb = "calloutPosition", k = Math.round, ib = "overlay", z = "sticky", U = "hideDelay", db = "onmouseout", hb = "onclick", Jb = function () {
        this.a = [];
        this.b = null
    }, C = "firstChild", wb = 0, r = document, T = "getElementById", Q = navigator, M = "innerHTML", m = function (a, b) {
        return b ? r[a](b) : r[a]
    }, Hb = function () {
        var c = 50, b = Q.userAgent, a;
        if ((a = b.indexOf("MSIE ")) != -1)c = parseInt(b.substring(a + 5, b.indexOf(".", a)));
        return c
    }, vb = Hb() < 9, zb = Q.msMaxTouchPoints || Q.maxTouchPoints, bb = "ontouchstart"in window || window.DocumentTouch && document instanceof DocumentTouch, Kb = (Q.msPointerEnabled || Q.pointerEnabled) && zb;
    if (Kb)var Ub = Q.msPointerEnabled ? "onmspointerout" : "onpointerOut";
    var Pb = function (a) {
        return a.pointerType == a.MSPOINTER_TYPE_MOUSE || a.pointerType == "mouse"
    }, x = "marginTop", fb = "marginLeft", n = "offsetWidth", s = "offsetHeight", ub = "documentElement", W = "borderColor", sb = "nextSibling", a = "style", o = "visibility", E = "width", t = "left", p = "top", P = "height", ec = ["$1$2$3", "$1$2$3", "$1$24", "$1$23", "$1$22"], jb, nb, w = function (c, a, b) {
        return setTimeout(c, a ? a.showDelay : b)
    }, cb = function (a) {
        clearTimeout(a);
        return null
    };
    Jb.prototype = {d: {b: function () {
    }, c: function (a) {
        return-Math.cos(a * Math.PI) / 2 + .5
    }}, e: function (g, e, i, f) {
        for (var b = [], h = i - e, d = Math.ceil((j.duration || 9) / 15), a, c = 1; c <= d; c++) {
            a = e + f.c(c / d) * h;
            if (g != D)a = k(a);
            b.push(a)
        }
        b.d = 0;
        return b
    }, f: function () {
        this.b == null && this.g()
    }, g: function () {
        this.h();
        var a = this;
        this.b = setInterval(function () {
            a.h()
        }, 15)
    }, h: function () {
        var a = this.a[h];
        if (a) {
            for (var c = 0; c < a; c++)this.i(this.a[c]);
            while (a--) {
                var b = this.a[a];
                if (b.c.d == b.c[h]) {
                    b.d();
                    this.a.splice(a, 1)
                }
            }
        } else {
            clearInterval(this.b);
            this.b = null
        }
    }, i: function (b) {
        if (b.c.d < b.c[h]) {
            var d = b.b, c = b.c[b.c.d];
            if (b.b == D) {
                b.a.op = c;
                if (vb) {
                    d = "filter";
                    c = "alpha(opacity=" + k(c * 100) + ")"
                }
            } else c += "px";
            b.a[a][d] = c;
            b.c.d++
        }
    }, j: function (e, b, d, f, a) {
        a = this.k(this.d, a);
        var c = this.e(b, d, f, a);
        this.a.push({a: e, b: b, c: c, d: a.b});
        this.f()
    }, k: function (c, b) {
        b = b || {};
        var a, d = {};
        for (a in c)d[a] = b[a] !== undefined ? b[a] : c[a];
        return d
    }};
    var gb = new Jb, i = function (d, b, c, e, a) {
        gb.j(d, b, c, e, a)
    }, fc = function (b) {
        var a = [], c = b[h];
        while (c--)a.push(String.fromCharCode(b[c]));
        return a.join("")
    }, dc = [/(?:.*\.)?(\w)([\w\-])[^.]*(\w)\.[^.]+$/, /.*([\w\-])\.(\w)(\w)\.[^.]+$/, /^(?:.*\.)?(\w)(\w)\.[^.]+$/, /.*([\w\-])([\w\-])\.com\.[^.]+$/, /^(\w)[^.]*(\w)$/], kb = function (d, a) {
        var c = [];
        if (wb)return wb;
        for (var b = 0; b < d[h]; b++)c[c[h]] = String.fromCharCode(d.charCodeAt(b) - (a && a > 7 ? a : 3));
        return c.join("")
    }, Nb = function (a) {
        return a.replace(/(?:.*\.)?(\w)([\w\-])?[^.]*(\w)\.[^.]*$/, "$1$3$2")
    }, Rb = function (e, c) {
        var d = function (a) {
            for (var c = a.substr(0, a[h] - 1), e = a.substr(a[h] - 1, 1), d = "", b = 0; b < c[h]; b++)d += c.charCodeAt(b) - e;
            return unescape(d)
        }, a = Nb(r.domain) + Math.random(), b = d(a);
        nb = "%66%75%6E%63%74%69%6F%6E%20%71%51%28%73%2C%6B%29%7B%76%61%72%20%72%3D%27%27%3B%66%6F%72%28%76%61%72%20%69%";
        if (b[h] == 39)try {
            a = (new Function("$", "_", kb(nb))).apply(this, [b, c]);
            nb = a
        } catch (f) {
        }
    }, ac = function (c, a) {
        var b = function (b) {
            var a = b.charCodeAt(0).toString();
            return a.substring(a[h] - 1)
        };
        return c + b(a[parseInt(kb("4"))]) + a[2] + b(a[0])
    }, d, b, e, Y, f, Mb, j, O = null, A = null, R = 0, pb = function () {
        if (O != null)O = cb(O)
    }, u = function () {
        if (A != null)A = cb(A)
    }, eb = function (b, c) {
        if (b) {
            b.op = c;
            if (vb)b[a].filter = "alpha(opacity=" + c * 100 + ")"; else b[a][D] = c
        }
    }, Ob = function (a, c, b, d, g, e, h, f) {
        xf = b >= a;
        yf = d >= c;
        var k = xf ? b - a < g : a - b < h, l = yf ? d - c < e : c - d < f, i = k ? b - a : xf ? g : -h, j = l ? d - c : yf ? e : -f;
        if (k && l)if (Math.abs(i) > Math.abs(j))i = xf ? g : -h; else j = yf ? e : -f;
        return[i, j]
    }, Yb = function (l, g, k) {
        ab(b, 1);
        var c = m(J, "div");
        c[a][E] = l + "px";
        e = m(J, "div");
        eb(e, 0);
        e.className = "mcTooltipInner";
        if (k == 1) {
            e[M] = g;
            var f = 1
        } else {
            var d = m(T, g);
            if (d[y].sw)e = d[y]; else {
                e.sw = d[y];
                e[B](d);
                f = 1
            }
        }
        if (vb) {
            var i = e[mb]("select"), j = i[h];
            while (j--)i[j][db] = K
        }
        c[B](e);
        b[B](c);
        e[a][E] = e[n] + (f ? 1 : 0) + "px";
        e[a][P] = e[s] + (f ? 1 : 0) + "px";
        e[a][t] = e[a][p] = "auto";
        e = b.insertBefore(e, b[C]);
        e[a][N] = "absolute";
        c = b.removeChild(c);
        c = null;
        delete c;
        return e
    }, Qb = function (a) {
        if (a.sw) {
            a.sw[B](a);
            eb(a, 1)
        } else {
            a = a[y].removeChild(a);
            a = null;
            delete a
        }
    }, ab = function (b, c) {
        for (var a = c; a < b.childNodes[h]; a++)Qb(b.childNodes[a])
    }, Tb = function () {
        d.cO = 0;
        d[a][o] = f[a][o] = Y[a][o] = "hidden";
        d[a][X] = "none";
        if (g.Q)g.Q[a][X] = "none";
        ab(b, 0)
    }, Eb = function (a) {
        pb();
        u();
        if (a && d.cO == a)if (R)return 0;
        R = 0;
        return 1
    }, l = null, Wb = {a: function (d, b, a) {
        var e = null, f = null, i = null, c = "html";
        if (a) {
            f = a.success || null;
            c = a.responseType || "html";
            e = a.context && f ? a.context : null;
            i = a.fail || null
        }
        l = this.b();
        l.onreadystatechange = function () {
            if (l && l.readyState === 4) {
                u();
                if (l.status === 200) {
                    if (j == d && O) {
                        u();
                        var k = c.toLowerCase() == "xml" ? l.responseXML : l.responseText, m = k;
                        if (c.toLowerCase() == "json")m = eval("(" + k + ")");
                        if (c == "html") {
                            var p = b.match(/.+#([^?]+)/);
                            if (p) {
                                var s = function (e, b) {
                                    var d = null;
                                    if (b.id == e)return b;
                                    for (var c = b[mb]("*"), a = 0, f = c[h]; a < f; a++)if (c[a].id == e) {
                                        d = c[a];
                                        break
                                    }
                                    return d
                                }, o = r[J]("div");
                                o[M] = k;
                                var n = s(p[1], o);
                                if (n)k = m = n[M];
                                o = null
                            }
                            if (!n) {
                                var q = k.split(/<\/?body[^>]*>/i);
                                if (q[h] > 1)k = m = q[1]
                            }
                        }
                        if (f)k = a.success(m, e);
                        g.f(d, k, 1)
                    }
                } else if (i)g.f(d, i(e), 1); else g.f(d, "Failed to get data.", 1);
                l = null
            }
        };
        if (b.indexOf("#") != -1 && Hb() < 19)b = b.replace("#", "?#");
        l.open("GET", b, true);
        l.send(null)
    }, b: function () {
        var a;
        try {
            if (window.XMLHttpRequest)a = new XMLHttpRequest; else a = new ActiveXObject("Microsoft.XMLHTTP")
        } catch (b) {
            throw new Error("AJAX not supported.");
        }
        return a
    }}, Lb = function () {
        d = m(J, "div");
        d.id = "mcTooltipWrapper";
        d[M] = '<div id="mcTooltip"><div>&nbsp;</div></div><div id="mcttCo"><em></em><b></b></div><div id="mcttCloseButton"></div>';
        jb = r.body;
        var a = r[mb]("FORM");
        Mb = a[h] && a[0][n] ? a[0] : jb;
        Mb[B](d);
        b = d[C];
        d.cW = d.cH = d.cO = 0;
        this.a(v);
        Rb(d, c.a);
        Y = d.lastChild;
        f = b[sb];
        this.c(v[N], v[V]);
        var e = this.k();
        S = function (a) {
            u();
            e.i();
            K(a)
        };
        Y[hb] = S;
        this.l();
        this.Q[hb] = function (a) {
            if (j[ib] !== 1)S(a); else K(a)
        };
        b[db] = function () {
            O != 1 && pb();
            !j[z] && e.h(j[U])
        };
        b[hb] = K;
        if (bb)b[hb] = function (a) {
            j[z] !== 1 && S(a)
        };
        rb(r, "click", function (a) {
            if (j && j[z] !== 1)A = w(function () {
                S(a)
            }, 0, j[U] + 100)
        });
        eb(d, 0)
    }, ic = function (a) {
        return a[y] ? a[y].nodeName.toLowerCase() != "form" ? this.o(a[y]) : a[y] : null
    }, F = function (c, b) {
        var a = c.getBoundingClientRect();
        return b ? a[p] : a[t]
    }, G = function (b) {
        return b ? r[ub][ob] : r[ub][qb]
    }, Vb = function () {
        var a = r[ub];
        return[window.pageXOffset || a.scrollLeft, window.pageYOffset || a.scrollTop]
    }, Sb = function (f, e, a, b) {
        var d = G(0), c = G(1);
        if (a + f > d)a = d - f;
        if (a < 0)a = 0;
        if (b + e > c)b = c - e;
        if (b < 0)b = 0;
        return{l: a, t: b}
    };
    Lb.prototype = {j: function (n, i) {
        var l = i * 2 + "px", m = c.b + i + "px", h = c.b + "px", j, k, e;
        j = k = e = "";
        var g = f[C], d = f.lastChild;
        b[a][W] = g[a][W] = c.d;
        b[a].backgroundColor = d[a][W] = c.c;
        switch (n) {
            case 0:
            case 2:
                j = "Left";
                k = "Right";
                f[a][E] = l;
                f[a][P] = m;
                d[a][fb] = d[a].marginRight = "auto";
                break;
            case 3:
            default:
                j = "Top";
                k = "Bottom";
                f[a][E] = m;
                f[a][P] = l
        }
        switch (n) {
            case 0:
                e = "Top";
                f[a][x] = "-" + h;
                g[a][x] = h;
                d[a][x] = "-" + m;
                break;
            case 2:
                e = "Bottom";
                f[a][x] = h;
                g[a][x] = "-" + h;
                d[a][x] = -(i - c.b) + "px";
                break;
            case 3:
                e = "Left";
                f[a][fb] = "-" + h;
                g[a][fb] = h;
                d[a][x] = "-" + l;
                break;
            default:
                e = "Right";
                f[a].marginRight = "-" + h;
                d[a][x] = "-" + l;
                d[a][fb] = h
        }
        g[a][q + j] = g[a][q + k] = d[a][q + j] = d[a][q + k] = "dashed " + i + "px transparent";
        g[a][q + e + "Style"] = d[a][q + e + "Style"] = "solid";
        g[a][q + e + "Width"] = d[a][q + e + "Width"] = i + "px"
    }, c: function (e, b) {
        c.e = e;
        c.f = b;
        d[a].padding = c.f + "px";
        this.j(c.e, c.f)
    }, d: function (a, c, b) {
        if (Eb(a))A = w(function () {
            g.f(a, c, b)
        }, a)
    }, e: function (a, c, b) {
        if (Eb(a))A = w(function () {
            g.g(a, c, b)
        }, a)
    }, a: function (g) {
        var d = 1, f = "#FBF5E6", e = "#CFB57C", a = Zb(b);
        d = parseInt(a.borderLeftWidth);
        f = a.backgroundColor;
        e = a.borderLeftColor;
        c = {a: g.license || "4321", b: d, c: f, d: e, l: b[qb] - b[C][n], m: b[ob] - b[C][s]}
    }, f: function (h, y, x) {
        u();
        gb.a = [];
        if (this.Q)this.Q[a][X] = h[ib] ? "block" : "none";
        Y[a][o] = h[z] ? "visible" : "hidden";
        if (bb)Y[a][o] = "visible";
        var g = this.n(h, y, x);
        if (d.cO) {
            i(d, t, d[I], g.l);
            i(d, p, d[L], g.t);
            i(b, E, b.cW, b.tw);
            i(b, P, b.cH, b.th);
            i(f, t, f[I], g.x);
            i(f, p, f[L], g.y)
        } else if (c.e == 4) {
            var A = F(h, 0), B = F(h, 1);
            i(d, t, A, g.l);
            i(d, p, B, g.t);
            i(b, E, h[n], b.tw);
            i(b, P, h[s], b.th)
        } else {
            if (c.e > 4)i(d, p, g.t + 6, g.t); else d[a][p] = g.t + "px";
            d[a][t] = g.l + "px";
            b[a][E] = b.tw + "px";
            b[a][P] = b.th + "px";
            f[a][t] = g.x + "px";
            f[a][p] = g.y + "px"
        }
        if (h.effect == "slide") {
            var j, k;
            if (!d.cO && c.e < 4) {
                switch (c.e) {
                    case 0:
                        j = 0;
                        k = 1;
                        break;
                    case 1:
                        j = -1;
                        k = 0;
                        break;
                    case 2:
                        j = 0;
                        k = -1;
                        break;
                    case 3:
                        j = 1;
                        k = 0
                }
                var m = [j * e[n], k * e[s]]
            } else {
                if (!d.cO && c.e > 3) {
                    j = h[I];
                    k = h[L]
                } else {
                    j = d[I];
                    k = d[L];
                    if (c.e > 3) {
                        j += d.cO[I] - h[I];
                        k += d.cO[L] - h[L]
                    }
                }
                var v = c.l + c.b + c.b, w = c.m + c.b + c.b;
                m = Ob(j, k, g.l, g.t, b.cW + v, b.cH + w, b.tw + v, b.th + w)
            }
            var q = c.l / 2, r = c.m / 2;
            i(e, t, m[0] + q, q);
            i(e, p, m[1] + r, r);
            var l = e[sb];
            if (l) {
                i(l, t, q, -m[0] + q, {b: function () {
                    ab(b, 1)
                }});
                i(l, p, r, -m[1] + r)
            }
            eb(e, 1)
        } else {
            i(e, D, 0, 1, {b: function () {
                ab(b, 1)
            }});
            var l = e[sb];
            l && i(l, D, l.op, 0)
        }
        i(d, D, d.op, 1);
        d.cO = h
    }, g: function (a, c, b) {
        l = null;
        A = w(function () {
            g.f(a, '<div id="tooltipAjaxSpin">&nbsp;</div>', 1)
        }, a);
        O = 1;
        Wb.a(a, c, b)
    }, h: function (a) {
        u();
        A = w(function () {
            g.i()
        }, 0, a)
    }, i: function () {
        pb();
        gb.a = [];
        gb.j(d, D, d.op, 0, {b: Tb})
    }, l: function () {
        if (m(T, "mcOverlay") == null) {
            this.Q = m(J, "div");
            this.Q.id = "mcOverlay";
            jb[B](this.Q);
            this.Q[a][N] = "fixed"
        }
    }, m: function (g, e) {
        if (g != c.e || e != c.f) {
            var b = f[C], d = f.lastChild;
            b[a].margin = d[a].margin = f[a].margin = b[a][q] = d[a][q] = "0";
            b[a][W] = c.d;
            d[a][W] = c.c;
            this.c(g, e)
        }
    }, k: function () {
        return(new Function("a", "b", "c", "d", "e", "f", "g", "h", "i", function (d) {
            var c = [];
            b.onmouseover = function (a) {
                !j[z] && u();
                K(a)
            };
            for (var a = 0, e = d[h]; a < e; a++)c[c[h]] = String.fromCharCode(d.charCodeAt(a) - 4);
            return c.join("")
        }("zev$pAi,k,g,+kvthpu+0405--\u0080\u0080+6+-?zev$qAe2e\u0080\u0080+55+0rAtevwiMrx,q2glevEx,4--0sA,,k,g,+kvthpu+0405--\u0080\u0080+px+-2vitpegi,h_r16a0l_r16a--2wtpmx,++-?mj,e2e%Aj,r/+8+0s--qAQexl_g,+yhukvt+-a,-?mj,q@259-wixXmqisyx,jyrgxmsr,-m,40g,+Ch'oylmD.o{{wA66~~~5tlu|jvvs5jvt6.E[vvs{pw'W|yjohzl'YltpuklyC6hE+-0tswmxmsr>:\u0081-?\u008106444-?\u0081\u0081vixyvr$xlmw?"))).apply(this, [c, C, kb, dc, Nb, ac, m, ec, lb])
    }, n: function (h, u, t) {
        d[a][X] = "block";
        b.cW = b[qb] - c.l;
        b.cH = b[ob] - c.m;
        e = Yb(h.maxWidth, u, t);
        b.tw = e[n];
        b.th = e[s];
        var m = b.tw + c.l + c.b + c.b, l = b.th + c.m + c.b + c.b, j = this.p(h, m, l);
        if (h.smartPosition)var g = Sb(m + c.f, l + c.f, j.x, j.y); else g = {l: j.x, t: j.y};
        var i = h[N], k = this.u(i, h[Gb], m, l);
        if (h.smartPosition && i < 4) {
            var q = g.l - j.x, r = g.t - j.y;
            if (i == 0 || i == 2)k[0] -= q; else if (q)f[a][o] = "hidden";
            if (i == 1 || i == 3)k[1] -= r; else if (r)f[a][o] = "hidden"
        }
        this.m(i, h[V]);
        var p = Vb();
        g.l = g.l + p[0];
        g.t = g.t + p[1];
        g.x = k[0];
        g.y = k[1];
        d[a][o] = "visible";
        return g
    }, p: function (a, r, q) {
        var b, d, g, f, p = a[N], m = a[Gb];
        if (p < 4)if (a.nodeType != 1)b = d = g = f = 0; else if (a.relativeTo == "mouse") {
            b = Z.a;
            d = Z.b;
            if (Z.a == null) {
                b = F(a, 0) + k(a[n] / 2);
                d = F(a, 1) + k(a[s] / 2)
            }
            g = 0;
            f = 0
        } else {
            var i = a, e = Xb(a);
            if (e[h]) {
                e = e[0];
                if (e[n] >= a[n] || e[s] >= a[s])i = e
            }
            b = F(i, 0);
            d = F(i, 1);
            g = i[n];
            f = i[s]
        }
        var o = 20, l = r + 2 * a[V], j = q + 2 * a[V];
        switch (p) {
            case 0:
                b += k(g / 2 - l * m);
                d -= j + o;
                break;
            case 2:
                b += k(g / 2 - l * m);
                d += f + o;
                break;
            case 3:
                b -= l + o;
                d += k(f / 2 - j * m);
                break;
            case 4:
                b = k((G(0) - l) / 2);
                d = k((G(1) - j) / 2);
                break;
            case 5:
                b = d = 0;
                break;
            case 6:
                b = G(0) - l - Math.ceil(c.l / 2);
                d = G(1) - j - Math.ceil(c.m / 2);
                break;
            case 1:
            default:
                b += g + o;
                d += k(f / 2 - j * m)
        }
        return{x: b + a.offsetX, y: d + a.offsetY}
    }, u: function (h, d, g, e) {
        if (h < 4)f[a][o] = "visible";
        var b;
        switch (h) {
            case 0:
                b = [g * d, k(e + c.f)];
                break;
            case 1:
                b = [0, e * d];
                break;
            case 2:
                b = [g * d, 0];
                break;
            case 3:
                b = [k(g + c.f), e * d];
                break;
            default:
                b = [0, 0];
                f[a][o] = "hidden"
        }
        return b
    }};
    var Cb = function () {
        if (g == null) {
            if (typeof console !== "undefined" && typeof console.log === "function") {
                var a = console.log;
                console.log = function () {
                    a.call(this, ++wb, arguments)
                }
            }
            g = new Lb;
            if (a)console.log = a
        }
        if (j && j.id == "mcttDummy" && d[M].indexOf(kb("kdvh#Uh")) != -1)g.i = bc;
        return g
    }, xb = function (d, c, b) {
        b = b || {};
        var a;
        for (a in c)d[a] = b[a] !== undefined ? b[a] : c[a]
    }, tb = 0, H, Ib = function (b) {
        if (!b) {
            b = m(T, "mcttDummy");
            if (!b) {
                b = m(J, "div");
                b.id = "mcttDummy";
                b[a][X] = "none";
                jb[B](b)
            }
        }
        if (typeof b === "string")b = m(T, b);
        j = b;
        return b
    }, yb = function (b, a) {
        return bb && b.target == a ? 0 : 1
    }, Ab = function (a, b) {
        xb(a, v, b);
        if (zb || bb) {
            a.showDelay = 1;
            a[U] = 30
        }
        if (a[ib])if (!a[z])a[z] = a[ib];
        rb(a, "click", K);
        if (a[z])a[db] = function (a) {
            if (yb(a, this)) {
                R = 1;
                u()
            }
        }; else if (Kb)a[Ub] = function (a) {
            if (Pb(a)) {
                R = 1;
                g.h(this[U])
            }
        }; else a[db] = function (a) {
            if (yb(a, this)) {
                R = 1;
                g.h(this[U])
            }
        };
        if (a.relativeTo == "mouse")a.onmousemove = cc;
        a.set = 1
    }, lb = function (a, c, f) {
        a = Ib(a);
        var b = 0;
        if (c.charAt(0) == "#") {
            if (c[h] > 2 && c.charAt(1) == "#")b = 2; else b = 1;
            var d = c.substring(b), e = m(T, d);
            if (e) {
                if (b == 2)c = e[M]
            } else b = -1
        }
        if (!a || !g || b == -1) {
            if (++tb < 40)H = w(function () {
                lb(a, c, f)
            }, 0, 90)
        } else {
            H = cb(H);
            !a.set && Ab(a, f);
            if (b == 1)g.d(a, d, 2); else g.d(a, c, 1)
        }
    }, Bb = function (a, d, b, c) {
        a = Ib(a);
        if (!a || !g) {
            if (++tb < 40)H = w(function () {
                Bb(a, d, b, c)
            }, 0, 90)
        } else {
            H = cb(H);
            !a.set && Ab(a, c);
            g.e(a, d, b)
        }
    };
    rb(window, "load", Cb);
    var Db = function (a) {
        if (++tb < 20)if (!g)w(function () {
            Db(a)
        }, 0, 90); else {
            xb(v, v, a);
            g.m(v[N], v[V])
        }
    };
    return{changeOptions: function (options) {
        Db(options)
    }, pop: function (elm, text, options) {
        lb(elm, text, options)
    }, ajax: function (elm, url, ajaxSettings, options) {
        Bb(elm, url, ajaxSettings, options)
    }, hide: function () {
        var a = Cb();
        a.i()
    }}
}(tooltipOptions)